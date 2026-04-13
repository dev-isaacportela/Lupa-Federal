package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

    List<Fornecedor> findAllByOrderByRazaoSocialAsc();

    Optional<Fornecedor> findByIdFornecedorApi(Long idFornecedorApi);

    boolean existsByCnpjCpf(String cnpjCpf);

    boolean existsByIdFornecedorApi(Long idFornecedorApi);
}
