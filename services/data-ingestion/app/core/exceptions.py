class DomainException(Exception):
    """Exceção base do domínio."""
    pass

class DespesaDuplicadaException(DomainException):
    """Despesa já existe no sistema."""
    pass

class EntidadeNaoEncontradaException(DomainException):
    """Entidade não encontrada no core-api."""
    pass