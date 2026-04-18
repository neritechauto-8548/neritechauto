package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.dto.ClienteResponse;

public class ClienteMapper {
    public static Cliente toEntity(ClienteRequest r) {
        Cliente c = new Cliente();
        c.setTipoCliente(r.getTipoCliente());
        c.setNomeCompleto(r.getNomeCompleto());
        c.setNomeFantasia(r.getNomeFantasia());
        c.setRazaoSocial(r.getRazaoSocial());
        c.setEmail(r.getEmail());
        c.setCpf(r.getCpf());
        c.setCnpj(r.getCnpj());
        c.setInscricaoEstadual(r.getInscricaoEstadual());
        c.setInscricaoMunicipal(r.getInscricaoMunicipal());
        c.setDataNascimento(r.getDataNascimento());
        c.setSexo(r.getSexo());
        c.setOrigemCliente(r.getOrigemCliente());
        c.setStatus(r.getStatus());
        c.setObservacoesGerais(r.getObservacoesGerais());
        return c;
    }

    public static void updateEntity(Cliente c, ClienteRequest r) {
        c.setTipoCliente(r.getTipoCliente());
        c.setNomeCompleto(r.getNomeCompleto());
        c.setNomeFantasia(r.getNomeFantasia());
        c.setRazaoSocial(r.getRazaoSocial());
        c.setEmail(r.getEmail());
        c.setCpf(r.getCpf());
        c.setCnpj(r.getCnpj());
        c.setInscricaoEstadual(r.getInscricaoEstadual());
        c.setInscricaoMunicipal(r.getInscricaoMunicipal());
        c.setDataNascimento(r.getDataNascimento());
        c.setSexo(r.getSexo());
        c.setOrigemCliente(r.getOrigemCliente());
        c.setStatus(r.getStatus());
        c.setObservacoesGerais(r.getObservacoesGerais());
    }

    public static ClienteResponse toResponse(Cliente c) {
        ClienteResponse r = new ClienteResponse();
        r.setId(c.getId());
        r.setEmpresaId(c.getEmpresaId());
        r.setTipoCliente(c.getTipoCliente());
        r.setNomeCompleto(c.getNomeCompleto());
        r.setNomeFantasia(c.getNomeFantasia());
        r.setRazaoSocial(c.getRazaoSocial());
        r.setEmail(c.getEmail());
        r.setCpf(c.getCpf());
        r.setCnpj(c.getCnpj());
        r.setInscricaoEstadual(c.getInscricaoEstadual());
        r.setInscricaoMunicipal(c.getInscricaoMunicipal());
        r.setDataNascimento(c.getDataNascimento());
        r.setSexo(c.getSexo());
        r.setOrigemCliente(c.getOrigemCliente());
        r.setStatus(c.getStatus());
        r.setObservacoesGerais(c.getObservacoesGerais());
        return r;
    }
}
