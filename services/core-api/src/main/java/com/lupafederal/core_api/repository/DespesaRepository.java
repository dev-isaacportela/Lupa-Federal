package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Despesa;
import com.lupafederal.core_api.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Integer>, JpaSpecificationExecutor<Despesa> {
    Optional<Despesa> findByIdDespesaApi(Long idDespesaApi);

    boolean existsByIdDespesaApi(Long idDespesaApi);
}
