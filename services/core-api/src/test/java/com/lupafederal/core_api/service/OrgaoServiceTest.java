package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateOrgaoRequest;
import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.model.Orgao;
import com.lupafederal.core_api.repository.OrgaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgaoServiceTest {
    @Mock
    OrgaoRepository orgaoRepository;

    @InjectMocks
    OrgaoService orgaoService;

    @Test
    void criar_deveSalvarOrgao_quandoDadosValidos() {
        CreateOrgaoRequest request = new CreateOrgaoRequest(1L, "001", "Ministério da Saúde", "MS");

        Orgao orgaoMock = new Orgao();
        orgaoMock.setIdOrgaoApi(1L);
        orgaoMock.setCodigoSiafi("001");
        orgaoMock.setNome("Ministério da Saúde");
        orgaoMock.setSigla("MS");

        when(orgaoRepository.existsByIdOrgaoApi(1L)).thenReturn(false);
        when(orgaoRepository.existsByCodigoSiafi("001")).thenReturn(false);
        when(orgaoRepository.save(any())).thenReturn(orgaoMock);

        OrgaoResponse resultado = orgaoService.criar(request);

        assertEquals("Ministério da Saúde", resultado.nome());
    }

    @Test
    void criar_deveLancarConflict_quandoIdOrgaoApiJaExiste() {
        CreateOrgaoRequest request = new CreateOrgaoRequest(1L, "001", "Ministério da Saúde", "MS");

        when(orgaoRepository.existsByIdOrgaoApi(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            orgaoService.criar(request);
        });
    }

    @Test
    void criar_deveLancarConflict_quandoCodigoSiafiJaExiste() {
        CreateOrgaoRequest request = new CreateOrgaoRequest(1L, "001", "Ministério da Saúde", "MS");

        when(orgaoRepository.existsByCodigoSiafi("001")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            orgaoService.criar(request);
        });
    }

    @Test
    void listar_deveRetornarListaOrdenada() {
        Orgao orgao1 = new Orgao();
        orgao1.setNome("Ministério da Saúde");
        orgao1.setCodigoSiafi("001");
        orgao1.setIdOrgaoApi(1L);
        orgao1.setSigla("MS");

        Orgao orgao2 = new Orgao();
        orgao2.setNome("Ministério da Educação");
        orgao2.setCodigoSiafi("002");
        orgao2.setIdOrgaoApi(2L);
        orgao2.setSigla("MEC");

        when(orgaoRepository.findAllByOrderByNomeAsc()).thenReturn(List.of(orgao1, orgao2));

        List<OrgaoResponse> resultado = orgaoService.listar();

        assertEquals(2, resultado.size());
        assertEquals("Ministério da Saúde", resultado.get(0).nome());
        assertEquals("Ministério da Educação", resultado.get(1).nome());
    }
}
