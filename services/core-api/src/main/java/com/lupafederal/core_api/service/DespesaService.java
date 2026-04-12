package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateDespesaRequest;
import com.lupafederal.core_api.dto.response.DespesaResponse;
import com.lupafederal.core_api.model.Agente;
import com.lupafederal.core_api.model.CategoriaDespesa;
import com.lupafederal.core_api.model.Despesa;
import com.lupafederal.core_api.model.Fornecedor;
import com.lupafederal.core_api.repository.AgenteRepository;
import com.lupafederal.core_api.repository.CategoriaDespesaRepository;
import com.lupafederal.core_api.repository.DespesaRepository;
import com.lupafederal.core_api.repository.FornecedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final AgenteRepository agenteRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaDespesaRepository categoriaDespesaRepository;

    public DespesaService(DespesaRepository despesaRepository,
                          AgenteRepository agenteRepository,
                          FornecedorRepository fornecedorRepository,
                          CategoriaDespesaRepository categoriaDespesaRepository) {
        this.despesaRepository = despesaRepository;
        this.agenteRepository = agenteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.categoriaDespesaRepository = categoriaDespesaRepository;
    }

    public DespesaResponse criar(CreateDespesaRequest request) {
        if(despesaRepository.existsByIdDespesaApi(request.idDespesaApi())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ID da API para esta despesa já cadastrado!");
        }
        Despesa despesa = new Despesa();
        despesa.setIdDespesaApi(request.idDespesaApi());
        despesa.setDataEmissao(request.dataEmissao());
        despesa.setValor(request.valor());
        despesa.setNumeroDocumento(request.numeroDocumento());

        Agente agente = agenteRepository.findById(request.idAgente())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente não encontrado com o ID: " + request.idAgente())
                );
        despesa.setAgente(agente);

        Fornecedor fornecedor = fornecedorRepository.findById(request.idFornecedor())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forncedor não encontrado com o ID: " + request.idFornecedor())
                );
        despesa.setFornecedor(fornecedor);

        CategoriaDespesa categoriaDespesa = categoriaDespesaRepository.findById(request.idCategoriaDespesa())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria de despesa não encontrada com o ID: " + request.idCategoriaDespesa())
                );
        despesa.setCategoriaDespesa(categoriaDespesa);

        Despesa save = despesaRepository.save(despesa);

        return new DespesaResponse(
                save.getId(),
                save.getAgente().getId(),
                save.getFornecedor().getId(),
                save.getCategoriaDespesa().getId(),
                save.getDataEmissao(),
                save.getValor(),
                save.getNumeroDocumento()
        );
    }
}
