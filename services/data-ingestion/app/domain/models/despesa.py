from dataclasses import dataclass
from datetime import date
from decimal import Decimal

@dataclass
class Despesa:
    id_despesa_api: int
    id_agente_api: int
    id_fornecedor_api: int
    id_categoria_api: int
    data_emissao: date
    valor: Decimal
    numero_documento: str