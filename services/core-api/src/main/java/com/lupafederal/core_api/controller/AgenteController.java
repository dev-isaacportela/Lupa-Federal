package com.lupafederal.core_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lupafederal.core_api.dto.request.CreateAgenteRequest;
import com.lupafederal.core_api.dto.response.AgenteResponse;
import com.lupafederal.core_api.service.AgenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Agentes Políticos", description = "Operações relacionadas aos agentes políticos")
@RestController
@RequestMapping("/api/v1/agentes")
public class AgenteController {
    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    })
    @Operation(
            summary = "Listar agentes políticos",
            description = "Rota para listar agentes políticos"
    )
    @GetMapping
    public List<AgenteResponse> listar() { return  agenteService.listar(); }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agente criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
            @ApiResponse(responseCode = "409", description = "Número de agente da API já cadastrado!")
    })
    @Operation(
            summary = "Criar agentes políticos",
            description = "Rota para criar agentes políticos"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenteResponse criar(@RequestBody CreateAgenteRequest request) {
        return agenteService.criar(request);
    }
}