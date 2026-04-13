package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateDespesaRequest;
import com.lupafederal.core_api.dto.request.FiltroDespesaRequest;
import com.lupafederal.core_api.dto.response.DespesaResponse;
import com.lupafederal.core_api.model.Agente;
import com.lupafederal.core_api.model.CategoriaDespesa;
import com.lupafederal.core_api.model.Despesa;
import com.lupafederal.core_api.model.Fornecedor;
import com.lupafederal.core_api.repository.AgenteRepository;
import com.lupafederal.core_api.repository.CategoriaDespesaRepository;
import com.lupafederal.core_api.repository.DespesaRepository;
import com.lupafederal.core_api.repository.FornecedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

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

    public Page<DespesaResponse> filtrar(FiltroDespesaRequest filtro, Pageable paginacao) {
        Page<Despesa> paginaEntidades = despesaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filtro.getIdAgente() != null) {
                predicates.add(cb.equal(root.get("agente").get("id"), filtro.getIdAgente()));
            }
            if(filtro.getIdFornecedor() != null) {
                predicates.add(cb.equal(root.get("fornecedor").get("id"), filtro.getIdFornecedor()));
            }
            if(filtro.getIdCategoriaDespesa() != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), filtro.getIdCategoriaDespesa()));
            }
            if(filtro.getDataInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("data"), filtro.getDataInicio()));
            }
            if(filtro.getDataFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("data"), filtro.getDataFim()));
            }
            if(filtro.getValorMinimo() != null) {
                predicates.add(cb.ge(root.get("valor"), filtro.getValorMinimo()));
            }
            if(filtro.getValorMaximo() != null) {
                predicates.add(cb.le(root.get("valor"), filtro.getValorMaximo()));
            }
            if(filtro.getNumeroDocumento() != null) {
                predicates.add(cb.equal(root.get("documento"), filtro.getNumeroDocumento()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, paginacao);

        return paginaEntidades.map(DespesaResponse::new);
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
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com o ID: " + request.idFornecedor())
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
