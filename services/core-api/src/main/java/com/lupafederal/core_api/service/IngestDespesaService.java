package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.IngestDespesaItemRequest;
import com.lupafederal.core_api.dto.response.IngestDespesaItemErroResponse;
import com.lupafederal.core_api.dto.response.IngestDespesaResponse;
import com.lupafederal.core_api.model.*;
import com.lupafederal.core_api.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngestDespesaService {
    private final DespesaRepository despesaRepository;
    private final AgenteRepository agenteRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaDespesaRepository categoriaDespesaRepository;
    private final OrgaoRepository orgaoRepository;

    public IngestDespesaService (
            DespesaRepository despesaRepository,
            AgenteRepository agenteRepository,
            FornecedorRepository fornecedorRepository,
            CategoriaDespesaRepository categoriaDespesaRepository,
            OrgaoRepository orgaoRepository
    ) {
        this.agenteRepository = agenteRepository;
        this.despesaRepository = despesaRepository;
        this.categoriaDespesaRepository = categoriaDespesaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.orgaoRepository = orgaoRepository;
    }

    public IngestDespesaResponse ingerir(List<IngestDespesaItemRequest> itens) {
        int itensSucesso = 0;
        List<IngestDespesaItemErroResponse> itensErro = new ArrayList<>();
        for(IngestDespesaItemRequest item : itens) {
            try {
                if (despesaRepository.findByIdDespesaApi(item.idDespesaApi()).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Despesa já cadastrada com o ID: " + item.idDespesaApi());
                }

                Despesa despesa = new Despesa();

                despesa.setDataEmissao(item.dataEmissao());
                despesa.setNumeroDocumento(item.numeroDocumento());
                despesa.setIdDespesaApi(item.idDespesaApi());
                despesa.setValor(item.valor());

                Agente agente = agenteRepository.findByIdAgenteApi(item.idAgenteApi())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente não encontrado com o ID: " + item.idAgenteApi())
                        );
                despesa.setAgente(agente);

                Fornecedor fornecedor = fornecedorRepository.findByIdFornecedorApi(item.idFornecedorApi())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado com o ID: " + item.idFornecedorApi())
                        );
                despesa.setFornecedor(fornecedor);

                CategoriaDespesa categoriaDespesa = categoriaDespesaRepository.findByIdCategoriaDespesaApi(item.idCategoriaDespesaApi())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria de despesa não encontrada com o ID: " + item.idCategoriaDespesaApi())
                        );
                despesa.setCategoriaDespesa(categoriaDespesa);

                Despesa save = despesaRepository.save(despesa);
                itensSucesso++;

            } catch (Exception e) {
                int codigo = 500;
                if (e instanceof ResponseStatusException rse) {
                    codigo = rse.getStatusCode().value();
                }
                itensErro.add(new IngestDespesaItemErroResponse(
                        item.idDespesaApi(),
                        codigo,
                        e.getMessage()
                ));
            }
        }

        return new IngestDespesaResponse(itensSucesso, itensErro);

    }
}
