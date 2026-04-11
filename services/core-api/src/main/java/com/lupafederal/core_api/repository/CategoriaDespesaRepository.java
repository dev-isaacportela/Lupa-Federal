package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.CategoriaDespesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa, Integer> {
    List<CategoriaDespesa> findByOrderByDescricaoAsc();

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByIdCategoriaDespesaApi(Long idCategoriaDespesaApi);
}
