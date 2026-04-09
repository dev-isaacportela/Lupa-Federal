package com.lupafederal.core_api.controller;

import java.util.List;

import com.lupafederal.core_api.dto.request.CreateOrgaoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.lupafederal.core_api.dto.response.OrgaoResponse;
import com.lupafederal.core_api.service.OrgaoService;

@RestController
@RequestMapping("/api/orgaos")
public class OrgaoController {

    private final OrgaoService orgaoService;

    public OrgaoController(OrgaoService orgaoService) {
        this.orgaoService = orgaoService;
    }

    @GetMapping
    public List<OrgaoResponse> listar() {
        return orgaoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrgaoResponse criar(@RequestBody CreateOrgaoRequest request) {
        return orgaoService.criar(request);
    }
}
