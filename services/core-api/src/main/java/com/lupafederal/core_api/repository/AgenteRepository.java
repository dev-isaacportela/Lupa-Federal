package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Agente;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenteRepository extends JpaRepository<Agente, Integer> {
    List<Agente> findByOrderByNomeAsc();

    boolean existsAgentesByIdPortalApi(Long idPortalApi);
}
