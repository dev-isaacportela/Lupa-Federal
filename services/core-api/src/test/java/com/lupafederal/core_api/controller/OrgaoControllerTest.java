package com.lupafederal.core_api.controller;

import com.lupafederal.core_api.dto.request.CreateOrgaoRequest;
import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.service.OrgaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrgaoController.class)
class OrgaoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrgaoService orgaoService;

    @Test
    void listar_deveRetornar200_comListaDeOrgaos() throws Exception {
        when(orgaoService.listar()).thenReturn(List.of(
                new OrgaoResponse(1, "001", "Ministério da Saúde", "MS")
        ));

        mockMvc.perform(get("/api/v1/orgaos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ministério da Saúde"))
                .andExpect(jsonPath("$[0].sigla").value("MS"));
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        CreateOrgaoRequest request = new CreateOrgaoRequest(1L, "001", "Ministério da Saúde", "MS");
        OrgaoResponse response = new OrgaoResponse(1, "001", "Ministério da Saúde", "MS");

        when(orgaoService.criar(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/orgaos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ministério da Saúde"));
    }

    @Test
    void criar_deveRetornar400_quandoNomeFaltando() throws Exception {
        String requestInvalida =
                """
                    {
                        "idOrgaoApi": 1,
                        "codigoSiafi": "001",
                        "sigla": "MS"
                    }
                """;

        mockMvc.perform(post("/api/v1/orgaos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalida))
                .andExpect(status().isBadRequest());
    }
}
