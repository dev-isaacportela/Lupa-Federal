package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateCategoriaDespesaRequest;
import com.lupafederal.core_api.dto.response.CategoriaDespesaResponse;
import com.lupafederal.core_api.model.CategoriaDespesa;
import com.lupafederal.core_api.repository.CategoriaDespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaDespesaService {
    private final CategoriaDespesaRepository categoriaDespesaRepository;

    public CategoriaDespesaService(CategoriaDespesaRepository categoriaDespesaRepository) {
        this.categoriaDespesaRepository = categoriaDespesaRepository;
    }

    public List<CategoriaDespesaResponse> listar() {
        return categoriaDespesaRepository.findByOrderByDescricaoAsc()
                .stream()
                .map(categoriaDespesa -> new CategoriaDespesaResponse(
                        categoriaDespesa.getId(),
                        categoriaDespesa.getDescricao()
                ))
                .toList();
    }

    public CategoriaDespesaResponse criar(CreateCategoriaDespesaRequest request) {
        if(categoriaDespesaRepository.existsByDescricaoIgnoreCase(request.descricao())) {
            throw new RuntimeException("Despesa já cadastrada!");
        }

        CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
        categoriaDespesa.setDescricao(request.descricao());

        CategoriaDespesa salvo = categoriaDespesaRepository.save(categoriaDespesa);

        return new CategoriaDespesaResponse(
                salvo.getId(),
                salvo.getDescricao());
    }
}
