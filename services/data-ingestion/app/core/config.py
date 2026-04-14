from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    portal_api_url: str = "https://api.portaldatransparencia.gov.br/api-de-dados"
    core_api_url: str = "http://127.0.0.1:8080"
    portal_api_key: str = ""
    ingestion_api_key: str = ""

    class Config:
        env_file = ".env"
settings = Settings()