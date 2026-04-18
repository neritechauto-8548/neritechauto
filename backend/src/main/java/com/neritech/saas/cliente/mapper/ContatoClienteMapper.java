package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.dto.ContatoClienteRequest;
import com.neritech.saas.cliente.dto.ContatoClienteResponse;

public class ContatoClienteMapper {

    public static ContatoCliente toEntity(ContatoClienteRequest r) {
        ContatoCliente c = new ContatoCliente();
        c.setTipoContato(r.getTipoContato());
        c.setContato(r.getContato());
        c.setPrincipal(r.getPrincipal() != null ? r.getPrincipal() : false);
        return c;
    }

    public static void updateEntity(ContatoCliente c, ContatoClienteRequest r) {
        c.setTipoContato(r.getTipoContato());
        c.setContato(r.getContato());
        c.setPrincipal(r.getPrincipal() != null ? r.getPrincipal() : c.getPrincipal());
    }

    public static ContatoClienteResponse toResponse(ContatoCliente c) {
        ContatoClienteResponse r = new ContatoClienteResponse();
        r.setId(c.getId());
        r.setClienteId(c.getCliente() != null ? c.getCliente().getId() : null);
        r.setTipoContato(c.getTipoContato());
        r.setContato(c.getContato());
        r.setPrincipal(c.getPrincipal());
        r.setDataCadastro(c.getDataCadastro());
        r.setDataAtualizacao(c.getDataAtualizacao());
        return r;
    }
}
