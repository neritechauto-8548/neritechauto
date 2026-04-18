package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.EnderecoCliente;
import com.neritech.saas.cliente.dto.EnderecoClienteRequest;
import com.neritech.saas.cliente.dto.EnderecoClienteResponse;

public class EnderecoClienteMapper {

    public static EnderecoCliente toEntity(EnderecoClienteRequest r) {
        EnderecoCliente e = new EnderecoCliente();
        e.setCep(r.getCep());
        e.setLogradouro(r.getLogradouro());
        e.setNumero(r.getNumero());
        e.setComplemento(r.getComplemento());
        e.setBairro(r.getBairro());
        e.setCidade(r.getCidade());
        e.setEstado(r.getEstado());
        e.setPais(r.getPais() != null && !r.getPais().isBlank() ? r.getPais() : "Brasil");
        return e;
    }

    public static void updateEntity(EnderecoCliente e, EnderecoClienteRequest r) {
        e.setCep(r.getCep());
        e.setLogradouro(r.getLogradouro());
        e.setNumero(r.getNumero());
        e.setComplemento(r.getComplemento());
        e.setBairro(r.getBairro());
        e.setCidade(r.getCidade());
        e.setEstado(r.getEstado());
        e.setPais(r.getPais() != null && !r.getPais().isBlank() ? r.getPais() : e.getPais());
    }

    public static EnderecoClienteResponse toResponse(EnderecoCliente e) {
        EnderecoClienteResponse r = new EnderecoClienteResponse();
        r.setId(e.getId());
        r.setClienteId(e.getCliente() != null ? e.getCliente().getId() : null);
        r.setCep(e.getCep());
        r.setLogradouro(e.getLogradouro());
        r.setNumero(e.getNumero());
        r.setComplemento(e.getComplemento());
        r.setBairro(e.getBairro());
        r.setCidade(e.getCidade());
        r.setEstado(e.getEstado());
        r.setPais(e.getPais());
        r.setDataCadastro(e.getDataCadastro());
        r.setDataAtualizacao(e.getDataAtualizacao());
        return r;
    }
}
