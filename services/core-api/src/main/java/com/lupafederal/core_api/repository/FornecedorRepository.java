package com.lupafederal.core_api.repository;

import com.lupafederal.core_api.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

    List<Fornecedor> findAllByOrderByRazaoSocialAsc();

    boolean existsByCnpjCpf(String cnpjCpf);
}
