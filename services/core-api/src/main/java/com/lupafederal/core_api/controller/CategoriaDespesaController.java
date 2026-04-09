package com.lupafederal.core_api.controller;


import com.lupafederal.core_api.dto.request.CreateCategoriaDespesaRequest;
import com.lupafederal.core_api.dto.response.CategoriaDespesaResponse;
import com.lupafederal.core_api.service.CategoriaDespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias de despesas", description = "Operações relacionadas as categorias de despesas")
@RestController
@RequestMapping("/api/v1/categoria-despesa")
public class CategoriaDespesaController {
    private final CategoriaDespesaService categoriaDespesaService;

    public CategoriaDespesaController(CategoriaDespesaService categoriaDespesaService) {
        this.categoriaDespesaService = categoriaDespesaService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    })
    @Operation(
            summary = "Listar categorias de despesas",
            description = "Rota para listar categorias de despesas"
    )
    @GetMapping
    public List<CategoriaDespesaResponse> listar() { return categoriaDespesaService.listar(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDespesaResponse criar(@RequestBody CreateCategoriaDespesaRequest request) {
        return categoriaDespesaService.criar(request);
    }
}
