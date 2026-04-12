package com.lupafederal.core_api.controller;

import com.lupafederal.core_api.dto.request.CreateDespesaRequest;
import com.lupafederal.core_api.dto.request.CreateFornecedorRequest;
import com.lupafederal.core_api.dto.response.DespesaResponse;
import com.lupafederal.core_api.dto.response.FornecedorResponse;
import com.lupafederal.core_api.service.DespesaService;
import com.lupafederal.core_api.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Despesas", description = "Operações relacionadas as despesas.")
@RestController
@RequestMapping("/api/v1/despesas")
public class DespesaController {
    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
            @ApiResponse(responseCode = "409", description = "Número de despesa já cadastrado!")
    })
    @Operation(
            summary = "Cadastro de despesas",
            description = "Rota para cadastro de despesas."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaResponse criar(@RequestBody CreateDespesaRequest request) {
        return despesaService.criar(request);
    }
}
