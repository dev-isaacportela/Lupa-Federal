package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Agente;
import com.lupafederal.core_api.model.Fornecedor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Integer> {
    List<Agente> findByOrderByNomeAsc();

    Optional<Agente> findByIdAgenteApi(Long idAgenteApi);

    boolean existsAgentesByidAgenteApi(Long idAgenteApi);
}
