package com.lupafederal.core_api.controller;

import com.lupafederal.core_api.dto.request.IngestDespesaItemRequest;
import com.lupafederal.core_api.dto.response.IngestDespesaResponse;
import com.lupafederal.core_api.service.IngestDespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ingestão", description = "Operações relacionadas a ingestão de dados.")
@RestController
@RequestMapping("/api/v1/ingest")
public class IngestDespesaController {
    private final IngestDespesaService ingestDespesaService;

    public IngestDespesaController(IngestDespesaService ingestDespesaService) {
        this.ingestDespesaService = ingestDespesaService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesas ingeridas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos!")
    })
    @Operation(
            summary = "Ingestão de despesas",
            description = "Rota para ingestão de despesas"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngestDespesaResponse ingerir(@RequestBody @Valid List<IngestDespesaItemRequest> request) {
        return ingestDespesaService.ingerir(request);
    }
}
