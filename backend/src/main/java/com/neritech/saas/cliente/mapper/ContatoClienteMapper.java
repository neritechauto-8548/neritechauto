package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.dto.ContatoClienteRequest;
import com.neritech.saas.cliente.dto.ContatoClienteResponse;
import com.neritech.saas.cliente.dto.ContatoClienteSummaryResponse;

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

    public static ContatoClienteSummaryResponse toSummaryResponse(ContatoCliente c) {
        return new ContatoClienteSummaryResponse(
                c.getId(),
                c.getTipoContato(),
                maskContact(c.getContato()),
                Boolean.TRUE.equals(c.getPrincipal()));
    }

    private static String maskContact(String value) {
        if (value == null || value.isBlank()) return "Não informado";
        String trimmed = value.trim();
        int at = trimmed.indexOf('@');
        if (at > 0 && at < trimmed.length() - 1) {
            String local = trimmed.substring(0, at);
            String domain = trimmed.substring(at + 1);
            int dot = domain.indexOf('.');
            String host = dot >= 0 ? domain.substring(0, dot) : domain;
            String suffix = dot >= 0 ? domain.substring(dot) : "";
            return local.substring(0, 1) + "***@" + (host.isBlank() ? "***" : host.substring(0, 1) + "***") + suffix;
        }

        String digits = trimmed.replaceAll("\\D", "");
        if (digits.length() >= 8) {
            return "(**) *****-" + digits.substring(digits.length() - 4);
        }
        if (trimmed.length() >= 2) {
            return trimmed.substring(0, 1) + "•••" + trimmed.substring(trimmed.length() - 1);
        }
        return "Contato protegido";
    }
}
