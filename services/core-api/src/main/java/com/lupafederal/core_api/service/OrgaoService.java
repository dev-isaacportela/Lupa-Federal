package com.lupafederal.core_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lupafederal.core_api.dto.OrgaoResponse;
import com.lupafederal.core_api.repository.OrgaoRepository;

@Service
public class OrgaoService {

    private final OrgaoRepository orgaoRepository;

    public OrgaoService(OrgaoRepository orgaoRepository) {
        this.orgaoRepository = orgaoRepository;
    }

    public List<OrgaoResponse> listar() {
        return orgaoRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(orgao -> new OrgaoResponse(
                        orgao.getId(),
                        orgao.getCodigoSiafi(),
                        orgao.getNome(),
                        orgao.getSigla()))
                .toList();
    }
}
