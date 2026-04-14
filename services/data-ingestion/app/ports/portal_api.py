from abc import ABC, abstractmethod

class PortalApiPort(ABC):
    @abstractmethod
    def buscar_despesas(self,
                        mes_ano: str,
                        codigo_orgao: str,
                        max_paginas: int) -> list[dict]:
        pass