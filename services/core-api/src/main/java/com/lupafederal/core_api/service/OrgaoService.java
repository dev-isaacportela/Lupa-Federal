package com.lupafederal.core_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.dto.request.CreateOrgaoRequest;
import com.lupafederal.core_api.model.Orgao;
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

    public OrgaoResponse criar(CreateOrgaoRequest request) {
        if(orgaoRepository.existsByCodigoSiafi(request.codigoSiafi())) {
            throw new RuntimeException("Código SIAFI já cadastrado");
        }

        Orgao orgao = new Orgao();
        orgao.setCodigoSiafi(request.codigoSiafi());
        orgao.setNome(request.nome());
        orgao.setSigla(request.sigla());

        Orgao salvo = orgaoRepository.save(orgao);

        return new OrgaoResponse(
                salvo.getId(),
                salvo.getCodigoSiafi(),
                salvo.getNome(),
                salvo.getSigla());
    }

}
