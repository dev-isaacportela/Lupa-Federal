package com.lupafederal.core_api.controller;

import com.lupafederal.core_api.dto.request.CreateFornecedorRequest;
import com.lupafederal.core_api.dto.response.FornecedorResponse;
import com.lupafederal.core_api.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fornecedores", description = "Operações relacionadas aos fornecedores públicos")
@RestController
@RequestMapping("/api/v1/fornecedores")
public class FornecedorController {
    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) { this.fornecedorService = fornecedorService; }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    })
    @Operation(
            summary = "Listar fornecedores",
            description = "Rota para listar fornecedores ordenado por razão social."
    )
    @GetMapping
    public List<FornecedorResponse> listar() { return fornecedorService.listar(); }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
            @ApiResponse(responseCode = "409", description = "Número de CNPJ já cadastrado!")
    })
    @Operation(
            summary = "Cadastro de órgãos",
            description = "Rota para cadastro de órgãos"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FornecedorResponse criar(@RequestBody CreateFornecedorRequest request) {
        return fornecedorService.criar(request);
    }
}
