package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateFornecedorRequest;
import com.lupafederal.core_api.dto.response.FornecedorResponse;
import com.lupafederal.core_api.model.Fornecedor;
import com.lupafederal.core_api.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<FornecedorResponse> listar() {
        return fornecedorRepository.findAllByOrderByRazaoSocialAsc()
                .stream()
                .map(fornecedor -> new FornecedorResponse(
                        fornecedor.getId(),
                        fornecedor.getCnpjCpf(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getTipoPessoa()
                ))
                .toList();

    }

    public FornecedorResponse criar(CreateFornecedorRequest request){
        if(fornecedorRepository.existsByCnpjCpf(request.cnpjCpf())) {
            throw new RuntimeException("Número de CNPJ já cadastrado!");
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpjCpf(request.cnpjCpf());
        fornecedor.setRazaoSocial(request.razaoSocial());
        fornecedor.setTipoPessoa(request.tipoPessoa());

        Fornecedor salvo = fornecedorRepository.save(fornecedor);

        return new FornecedorResponse(
                salvo.getId(),
                salvo.getCnpjCpf(),
                salvo.getRazaoSocial(),
                salvo.getTipoPessoa());
    }
}
