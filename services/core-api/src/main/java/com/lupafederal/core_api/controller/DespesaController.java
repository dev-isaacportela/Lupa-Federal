package com.lupafederal.core_api.controller;

import com.lupafederal.core_api.dto.request.CreateDespesaRequest;
import com.lupafederal.core_api.dto.request.FiltroDespesaRequest;
import com.lupafederal.core_api.dto.response.DespesaResponse;
import com.lupafederal.core_api.model.Despesa;
import com.lupafederal.core_api.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public DespesaResponse criar(@RequestBody @Valid CreateDespesaRequest request) {
        return despesaService.criar(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesas retornadas com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    })
    @Operation(
            summary = "Busca de despesas",
            description = "Rota para busca de despesas."
    )
    @GetMapping
    public ResponseEntity<Page<DespesaResponse>> filtrar(
            @ParameterObject @Valid FiltroDespesaRequest filtro,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable paginacao) {

        return ResponseEntity.ok(despesaService.filtrar(filtro, paginacao));
    }
}