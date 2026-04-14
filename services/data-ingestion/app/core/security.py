from fastapi import Header, HTTPException
from app.core.config import settings


def verify_api_key(x_api_key: str = Header(...)):
    if not settings.ingestion_api_key:
        raise HTTPException(status_code=500, detail="INGESTION_API_KEY não configurada no servidor")
    if x_api_key != settings.ingestion_api_key:
        raise HTTPException(status_code=401, detail="API key inválida")
