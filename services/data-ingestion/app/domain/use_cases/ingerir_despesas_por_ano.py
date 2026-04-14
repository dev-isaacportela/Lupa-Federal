import logging
from datetime import date, timedelta
from dataclasses import dataclass, field

logger = logging.getLogger(__name__)


@dataclass
class ResultadoIngestaoAnual:
    ano: int
    dias_processados: int
    dias_sem_dados: int
    dias_com_erro: int
    total_criadas: int
    total_erros: int
    erros_por_dia: dict[str, list[str]] = field(default_factory=dict)


class IngerirDespesasPorAnoUseCase:
    def __init__(self, portal_port, core_port):
        self.portal_port = portal_port
        self.core_port = core_port

    def executar(self, ano: int, gestao: str, fase: int = 3, max_paginas: int = 10) -> ResultadoIngestaoAnual:
        from app.domain.use_cases.ingerir_despesas import IngerirDespesasUseCase

        use_case_dia = IngerirDespesasUseCase(self.portal_port, self.core_port)
        resultado = ResultadoIngestaoAnual(ano=ano, dias_processados=0, dias_sem_dados=0, dias_com_erro=0, total_criadas=0, total_erros=0)

        dia_atual = date(ano, 1, 1)
        fim = date(ano, 12, 31)

        while dia_atual <= fim:
            data_str = dia_atual.strftime('%d/%m/%Y')
            try:
                res = use_case_dia.executar(data_str, gestao, fase, max_paginas)
                if res.criadas == 0 and res.erros == 0:
                    resultado.dias_sem_dados += 1
                else:
                    resultado.dias_processados += 1
                    resultado.total_criadas += res.criadas
                    resultado.total_erros += res.erros
                    if res.detalhes_erros:
                        resultado.erros_por_dia[data_str] = res.detalhes_erros
            except Exception as e:
                logger.error(f"Erro ao processar {data_str}: {e}")
                resultado.dias_com_erro += 1

            dia_atual += timedelta(days=1)

        logger.info(
            f"Ano {ano} concluído: {resultado.dias_processados} dias com dados, "
            f"{resultado.dias_sem_dados} sem dados, {resultado.dias_com_erro} com erro, "
            f"{resultado.total_criadas} despesas criadas"
        )
        return resultado
