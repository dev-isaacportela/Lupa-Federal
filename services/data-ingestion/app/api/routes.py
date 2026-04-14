from fastapi import Depends, FastAPI
from pydantic import BaseModel
from app.core.security import verify_api_key
from app.core.config import settings
from app.core.logging import logger
from app.adapters.portal_http import PortalHttpAdapter
from app.adapters.core_http import CoreHttpAdapter
from app.domain.use_cases.ingerir_despesas import IngerirDespesasUseCase
from app.domain.use_cases.ingerir_despesas_por_ano import IngerirDespesasPorAnoUseCase
from app.domain.use_cases.ingerir_despesas_por_mes import IngerirDespesasPorMesUseCase

app = FastAPI(
    title="Lupa Federal — Data Ingestion",
    version="1.0.0",
    description="Serviço de ingestão de despesas do Portal da Transparência"
)


class IngestRequest(BaseModel):
    data_emissao: str
    gestao: str
    fase: int = 1
    max_paginas: int = 5


class IngestAnoRequest(BaseModel):
    ano: int
    gestao: str
    fase: int = 3
    max_paginas: int = 10


class IngestMesRequest(BaseModel):
    ano: int
    mes: int
    gestao: str
    fase: int = 3
    max_paginas: int = 10


def _build_adapters():
    portal = PortalHttpAdapter(api_key=settings.portal_api_key, base_url=settings.portal_api_url)
    core = CoreHttpAdapter(base_url=settings.core_api_url)
    return portal, core


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/ingest/despesas", dependencies=[Depends(verify_api_key)])
def ingest(request: IngestRequest):
    logger.info(f"Ingestão iniciada: data_emissao={request.data_emissao}, gestao={request.gestao}, fase={request.fase}")
    portal, core = _build_adapters()
    resultado = IngerirDespesasUseCase(portal_port=portal, core_port=core).executar(
        data_emissao=request.data_emissao,
        gestao=request.gestao,
        fase=request.fase,
        max_paginas=request.max_paginas
    )
    return resultado


@app.post("/ingest/despesas/mes", dependencies=[Depends(verify_api_key)])
def ingest_mes(request: IngestMesRequest):
    logger.info(f"Ingestão mensal iniciada: {request.mes:02d}/{request.ano}, gestao={request.gestao}, fase={request.fase}")
    portal, core = _build_adapters()
    resultado = IngerirDespesasPorMesUseCase(portal_port=portal, core_port=core).executar(
        ano=request.ano,
        mes=request.mes,
        gestao=request.gestao,
        fase=request.fase,
        max_paginas=request.max_paginas
    )
    return resultado


@app.post("/ingest/despesas/ano", dependencies=[Depends(verify_api_key)])
def ingest_ano(request: IngestAnoRequest):
    logger.info(f"Ingestão anual iniciada: ano={request.ano}, gestao={request.gestao}, fase={request.fase}")
    portal, core = _build_adapters()
    resultado = IngerirDespesasPorAnoUseCase(portal_port=portal, core_port=core).executar(
        ano=request.ano,
        gestao=request.gestao,
        fase=request.fase,
        max_paginas=request.max_paginas
    )
    return resultado
