import re
import hashlib
import logging
import requests
from abc import ABC, abstractmethod
from datetime import datetime

logger = logging.getLogger(__name__)


def _only_digits(value: str) -> str:
    return re.sub(r'\D', '', value)


def _parse_valor(valor_str: str) -> float:
    return float(valor_str.replace('.', '').replace(',', '.'))


def _parse_data(data_str: str) -> str:
    return datetime.strptime(data_str, '%d/%m/%Y').strftime('%Y-%m-%d')


def _extract_codigo_elemento(elemento: str) -> int:
    match = re.match(r'^(\d+)', elemento or '')
    return int(match.group(1)) if match else 0


def _strip_codigo_elemento(elemento: str) -> str:
    """'39 - Outros Serviços...' → 'Outros Serviços...'"""
    return re.sub(r'^\d+\s*-\s*', '', elemento or '').strip()


def _despesa_id(documento: str, cnpj_cpf: str) -> int:
    key = f"{documento}:{cnpj_cpf}"
    return int(hashlib.sha256(key.encode()).hexdigest(), 16) % (10 ** 15)


class _EntidadeRegistrar(ABC):
    def __init__(self, session: requests.Session, base_url: str):
        self._session = session
        self._base_url = base_url
        self._cache: dict[int, int] = {}

    def registrar(self, id_externo: int, **dados) -> None:
        if id_externo in self._cache:
            return
        payload = self._build_payload(id_externo, **dados)
        resp = self._session.post(
            f"{self._base_url}/{self._endpoint()}",
            json=payload,
            timeout=15
        )
        if resp.status_code == 201:
            self._cache[id_externo] = resp.json()['id']
        elif resp.status_code == 409:
            self._cache[id_externo] = -1
        else:
            logger.warning(
                f"Falha ao registrar {self.__class__.__name__} "
                f"{id_externo}: {resp.status_code} {resp.text[:200]}"
            )

    @abstractmethod
    def _endpoint(self) -> str: ...

    @abstractmethod
    def _build_payload(self, id_externo: int, **dados) -> dict: ...


class _OrgaoRegistrar(_EntidadeRegistrar):
    def _endpoint(self): return "api/v1/orgaos"

    def _build_payload(self, id_externo, **d):
        return {
            "idOrgaoApi": id_externo,
            "codigoSiafi": str(id_externo),
            "nome": d["item"].get("orgao", "")[:255],
            "sigla": d["item"].get("orgao", "")[:10],
        }


class _FornecedorRegistrar(_EntidadeRegistrar):
    def _endpoint(self): return "api/v1/fornecedores"

    def _build_payload(self, id_externo, **d):
        cnpj_cpf = d["cnpj_cpf"]
        return {
            "idFornecedorApi": id_externo,
            "cnpjCpf": cnpj_cpf[:14],
            "razaoSocial": d["item"].get("nomeFavorecido", "")[:255],
            "tipoPessoa": "J" if len(cnpj_cpf) == 14 else "F",
        }


class _CategoriaRegistrar(_EntidadeRegistrar):
    def _endpoint(self): return "api/v1/categorias-despesas"

    def _build_payload(self, id_externo, **d):
        return {
            "idCategoriaDespesaApi": id_externo,
            "descricao": _strip_codigo_elemento(d["descricao"])[:100],
        }


class _AgenteRegistrar(_EntidadeRegistrar):
    def _endpoint(self): return "api/v1/agentes"

    def _build_payload(self, id_externo, **d):
        return {
            "idAgenteApi": id_externo,
            "nome": f"[UG] {d['item'].get('ug', d['codigo_ug'])}"[:255],
            "cpfMascarado": "***.***.***-**",
            "idOrgao": d["id_orgao_interno"],
        }


class PortalToCoreMappingService:
    def __init__(self, core_base_url: str):
        self.base_url = core_base_url
        self.session = requests.Session()
        self.session.headers.update({"Content-Type": "application/json"})

        self._orgaos       = _OrgaoRegistrar(self.session, core_base_url)
        self._fornecedores = _FornecedorRegistrar(self.session, core_base_url)
        self._categorias   = _CategoriaRegistrar(self.session, core_base_url)
        self._agentes      = _AgenteRegistrar(self.session, core_base_url)

    def transformar(self, itens_portal: list[dict]) -> list[dict]:
        resultado = []
        for item in itens_portal:
            try:
                transformado = self._transformar_item(item)
                if transformado:
                    resultado.append(transformado)
            except Exception as e:
                logger.error(f"Erro ao transformar {item.get('documento')}: {e}")
        return resultado

    def _transformar_item(self, item: dict) -> dict | None:
        codigo_orgao = item.get('codigoOrgao', '')
        if not codigo_orgao:
            return None
        id_orgao_api = int(codigo_orgao)
        self._orgaos.registrar(id_orgao_api, item=item)

        cnpj_cpf = _only_digits(item.get('codigoFavorecido', ''))
        if not cnpj_cpf:
            return None
        id_fornecedor_api = int(cnpj_cpf[:14])
        self._fornecedores.registrar(id_fornecedor_api, item=item, cnpj_cpf=cnpj_cpf)

        elemento = item.get('elemento', 'Sem categoria')
        id_categoria_api = _extract_codigo_elemento(elemento)
        self._categorias.registrar(id_categoria_api, descricao=elemento)

        codigo_ug = item.get('codigoUg', '')
        id_agente_api = int(codigo_ug) if codigo_ug.isdigit() else abs(hash(codigo_ug)) % (2 ** 31)
        id_orgao_interno = self._buscar_id_interno_orgao(id_orgao_api)
        if id_orgao_interno:
            self._agentes.registrar(id_agente_api, item=item, codigo_ug=codigo_ug, id_orgao_interno=id_orgao_interno)

        return {
            "idDespesaApi": _despesa_id(item.get('documento', ''), cnpj_cpf),
            "idOrgaoApi": id_orgao_api,
            "idFornecedorApi": id_fornecedor_api,
            "idCategoriaDespesaApi": id_categoria_api,
            "idAgenteApi": id_agente_api,
            "dataEmissao": _parse_data(item.get('data', '')),
            "valor": _parse_valor(item.get('valor', '0')),
            "numeroDocumento": item.get('documentoResumido', item.get('documento', ''))[:50],
        }

    def _buscar_id_interno_orgao(self, id_orgao_api: int) -> int | None:
        orgaos = self.session.get(f"{self.base_url}/api/v1/orgaos", timeout=15).json()
        orgao = next((o for o in orgaos if o.get('codigoSiafi') == str(id_orgao_api)), None)
        if not orgao:
            logger.warning(f"Orgao {id_orgao_api} não encontrado")
            return None
        return orgao['id']
