from abc import ABC, abstractmethod

class CoreApiPort(ABC):
    @abstractmethod
    def ingerir_despesas(self, itens: list[dict]) -> dict:
        pass