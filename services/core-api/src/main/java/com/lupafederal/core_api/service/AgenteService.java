package com.lupafederal.core_api.service;

import com.lupafederal.core_api.dto.request.CreateAgenteRequest;
import com.lupafederal.core_api.dto.response.AgenteResponse;
import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.model.Agente;
import com.lupafederal.core_api.model.Orgao;
import com.lupafederal.core_api.repository.AgenteRepository;
import com.lupafederal.core_api.repository.OrgaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AgenteService {

    private final OrgaoRepository orgaoRepository;
    private final AgenteRepository agenteRepository;

    public AgenteService(AgenteRepository agenteRepository, OrgaoRepository orgaoRepository) {
        this.agenteRepository = agenteRepository;
        this.orgaoRepository = orgaoRepository;
    }

    public List<AgenteResponse> listar() {
        return agenteRepository.findByOrderByNomeAsc()
                .stream().map(agente -> new AgenteResponse(
                        agente.getId(),
                        agente.getIdPortalApi(),
                        agente.getNome(),
                        agente.getCpfMascarado(),
                        agente.getOrgao().getId()
                ))
                .toList();
    }

    public AgenteResponse criar(CreateAgenteRequest request) {
        if(agenteRepository.existsAgentesByIdPortalApi(request.idPortalApi())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agente já cadastrado!");
        }

        Agente agente = new Agente();
        agente.setIdPortalApi(request.idPortalApi());
        agente.setNome(request.nome());
        agente.setCpfMascarado(request.cpfMascarado());

        Orgao orgao = orgaoRepository.findById(request.idOrgao())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orgão não encontrado com o ID: " + request.idOrgao())
                        );

        agente.setOrgao(orgao);

        Agente salvo = agenteRepository.save(agente);

        return new AgenteResponse(
                salvo.getId(),
                salvo.getIdPortalApi(),
                salvo.getNome(),
                salvo.getCpfMascarado(),
                salvo.getOrgao().getId()
        );
    }
}
