package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EmpresaRequest;
import com.neritech.saas.empresa.dto.EmpresaResponse;

public class EmpresaMapper {

    public static Empresa toEntity(EmpresaRequest dto) {
        Empresa e = new Empresa();
        e.setNomeFantasia(dto.getNomeFantasia());
        e.setRazaoSocial(dto.getRazaoSocial());
        e.setCnpj(dto.getCnpj());
        e.setInscricaoEstadual(dto.getInscricaoEstadual());
        e.setInscricaoMunicipal(dto.getInscricaoMunicipal());
        e.setEmail(dto.getEmail());
        e.setTelefone(dto.getTelefone());
        e.setSite(dto.getSite());
        e.setDataAbertura(dto.getDataAbertura());
        e.setObservacoes(dto.getObservacoes());
        e.setAtivo(dto.getAtivo());
        return e;
    }

    public static void updateEntity(Empresa target, EmpresaRequest dto) {
        target.setNomeFantasia(dto.getNomeFantasia());
        target.setRazaoSocial(dto.getRazaoSocial());
        target.setCnpj(dto.getCnpj());
        target.setInscricaoEstadual(dto.getInscricaoEstadual());
        target.setInscricaoMunicipal(dto.getInscricaoMunicipal());
        target.setEmail(dto.getEmail());
        target.setTelefone(dto.getTelefone());
        target.setSite(dto.getSite());
        target.setDataAbertura(dto.getDataAbertura());
        target.setObservacoes(dto.getObservacoes());
        target.setAtivo(dto.getAtivo());
    }

    public static EmpresaResponse toResponse(Empresa e) {
        EmpresaResponse r = new EmpresaResponse();
        r.setId(e.getId());
        r.setNomeFantasia(e.getNomeFantasia());
        r.setRazaoSocial(e.getRazaoSocial());
        r.setCnpj(e.getCnpj());
        r.setInscricaoEstadual(e.getInscricaoEstadual());
        r.setInscricaoMunicipal(e.getInscricaoMunicipal());
        r.setEmail(e.getEmail());
        r.setTelefone(e.getTelefone());
        r.setSite(e.getSite());
        r.setDataAbertura(e.getDataAbertura());
        r.setObservacoes(e.getObservacoes());
        r.setAtivo(e.getAtivo());
        return r;
    }
}
