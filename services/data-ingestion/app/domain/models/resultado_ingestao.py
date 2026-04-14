from dataclasses import dataclass, field

@dataclass
class ResultadoIngestao:
    criadas: int
    duplicadas: int
    erros: int
    detalhes_erros: list[str] = field(default_factory=list)