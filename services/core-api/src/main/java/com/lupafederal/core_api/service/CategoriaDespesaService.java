package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateCategoriaDespesaRequest;
import com.lupafederal.core_api.dto.response.CategoriaDespesaResponse;
import com.lupafederal.core_api.model.CategoriaDespesa;
import com.lupafederal.core_api.repository.CategoriaDespesaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if(categoriaDespesaRepository.existsByIdCategoriaDespesaApi(request.idCategoriaDespesaApi())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ID da API para esta Categoria de Despesa já cadastrado!");
        }
        if(categoriaDespesaRepository.existsByDescricaoIgnoreCase(request.descricao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoria de Despesa já cadastrada!");
        }

        CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
        categoriaDespesa.setIdCategoriaDespesaApi(request.idCategoriaDespesaApi());
        categoriaDespesa.setDescricao(request.descricao());

        CategoriaDespesa salvo = categoriaDespesaRepository.save(categoriaDespesa);

        return new CategoriaDespesaResponse(
                salvo.getId(),
                salvo.getDescricao());
    }
}
