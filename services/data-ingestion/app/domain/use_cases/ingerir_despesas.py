import logging

from app.domain.models.resultado_ingestao import ResultadoIngestao

logger = logging.getLogger(__name__)


class IngerirDespesasUseCase:
    def __init__(self, portal_port, core_port):
        self.portal_port = portal_port
        self.core_port = core_port

    def executar(self,
                 data_emissao: str,
                 gestao: str,
                 fase: int = 3,
                 max_paginas: int = 5) -> ResultadoIngestao:
        despesas = self.portal_port.buscar_despesas(
            data_emissao, gestao, fase, max_paginas
        )
        if despesas:
            logger.info(f"Primeiro item do Portal: {despesas[0]}")
        resultado_core = self.core_port.ingerir_despesas(despesas)
        return ResultadoIngestao(
            criadas=resultado_core['criadosComSucesso'],
            duplicadas=0,  # TODO: pegar do core-api
            erros=len(resultado_core.get('falhas', [])),
            detalhes_erros=[f['mensagemErro'] for f in resultado_core.get('falhas', [])]
        )