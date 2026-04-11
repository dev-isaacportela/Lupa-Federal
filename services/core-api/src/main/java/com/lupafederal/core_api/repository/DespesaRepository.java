package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {

    boolean existsByIdDespesaApi(Long idDespesaApi);
}
