package com.lupafederal.core_api.controller;

import java.util.List;

import com.lupafederal.core_api.dto.request.CreateOrgaoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.service.OrgaoService;

@Tag(name = "Órgãos", description = "Operações relacionadas aos órgãos públicos")
@RestController
@RequestMapping("/api/v1/orgaos")
public class OrgaoController {

    private final OrgaoService orgaoService;

    public OrgaoController(OrgaoService orgaoService) {
        this.orgaoService = orgaoService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno!")
    })
    @Operation(
            summary = "Listar órgãos",
            description = "Retorna a lista de órgãos cadastrados e ordenada por nome"
    )
    @GetMapping
    public List<OrgaoResponse> listar() {
        return orgaoService.listar();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Órgão criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
            @ApiResponse(responseCode = "409", description = "Código SIAFI já cadastrado!")
    })
    @Operation(
            summary = "Cadastro de órgãos",
            description = "Rota para cadastro de órgãos"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrgaoResponse criar(@RequestBody CreateOrgaoRequest request) {
        return orgaoService.criar(request);
    }
}
