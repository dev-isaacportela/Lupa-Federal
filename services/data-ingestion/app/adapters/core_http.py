import requests
from app.ports.core_api import CoreApiPort
from app.core.logging import logger
from app.adapters.transformer import PortalToCoreMappingService


class CoreHttpAdapter(CoreApiPort):
    def __init__(self, base_url: str):
        self.base_url = base_url
        self._mapper = PortalToCoreMappingService(core_base_url=base_url)

    def ingerir_despesas(self, itens: list[dict]) -> dict:
        url = f"{self.base_url}/api/v1/ingest/despesas"
        headers = {"Content-Type": "application/json"}

        try:
            logger.info(f"Transformando {len(itens)} despesas...")
            transformados = self._mapper.transformar(itens)
            logger.info(f"Enviando {len(transformados)} despesas para o core-api...")

            response = requests.post(url, json=transformados, headers=headers, timeout=60)

            logger.info(f"core-api status={response.status_code} body={response.text[:500]}")

            response.raise_for_status()

            resultado = response.json()
            logger.info(f"Resultado: {resultado['criadosComSucesso']} criadas, "
                        f"{len(resultado.get('falhas', []))} com erro")

            return resultado

        except requests.exceptions.RequestException as e:
            logger.error(f"Erro ao enviar despesas: {e}")
            raise
