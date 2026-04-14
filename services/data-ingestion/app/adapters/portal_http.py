import requests
from app.ports.portal_api import PortalApiPort
from app.core.config import settings
from app.core.logging import logger


class PortalHttpAdapter(PortalApiPort):
    def __init__(self, api_key: str, base_url: str):
        self.api_key = api_key
        self.base_url = base_url

    def buscar_despesas(self, data_emissao: str, gestao: str, fase: int, max_paginas: int) -> list[dict]:
        todas_despesas = []
        url = f"{self.base_url}/despesas/documentos"

        for pagina in range(1, max_paginas + 1):
            try:
                headers = {
                    "chave-api-dados": self.api_key,
                    "accept": "*/*"
                }

                params = {
                    "dataEmissao": data_emissao,
                    "gestao": gestao,
                    "fase": fase,
                    "pagina": pagina
                }

                logger.info(f"Buscando página {pagina}...")
                response = requests.get(url, headers=headers, params=params, timeout=30)

                if response.status_code == 204:
                    logger.info("Fim da paginação")
                    break

                response.raise_for_status()

                items = response.json()
                if not items:
                    logger.info("Página vazia — fim da paginação")
                    break

                todas_despesas.extend(items)
                logger.info(f"Página {pagina}: {len(items)} registros")

                if len(items) < 100:
                    break

            except Exception as e:
                logger.error(f"Erro na página {pagina}: {e}")
                raise

        logger.info(f"Total obtido: {len(todas_despesas)}")
        return todas_despesas
