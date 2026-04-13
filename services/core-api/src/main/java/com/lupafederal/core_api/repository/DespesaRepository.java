package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DespesaRepository extends JpaRepository<Despesa, Integer>, JpaSpecificationExecutor<Despesa> {

    boolean existsByIdDespesaApi(Long idDespesaApi);
}
