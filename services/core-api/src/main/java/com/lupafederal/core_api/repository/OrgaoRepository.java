package com.lupafederal.core_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lupafederal.core_api.model.Orgao;

public interface OrgaoRepository extends JpaRepository<Orgao, Integer> {

    List<Orgao> findAllByOrderByNomeAsc();

    boolean existsByCodigoSiafi(String codigoSiafi);

    boolean existsByIdOrgaoApi(Long idOrgaoApi);
}
