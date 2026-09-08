package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.dto.ClienteDetailResponse;
import com.neritech.saas.cliente.dto.ClienteListResponse;
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
        // Status é lifecycle e não pode ser alterado por update cadastral genérico.
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

    public static ClienteListResponse toListResponse(Cliente c) {
        return new ClienteListResponse(
                c.getId(), displayName(c), c.getTipoCliente(),
                maskDocument(firstNonBlank(c.getCpf(), c.getCnpj(), null)),
                maskEmail(c.getEmail()), c.getStatus());
    }

    public static ClienteDetailResponse toDetailResponse(Cliente c) {
        return new ClienteDetailResponse(
                c.getId(), displayName(c), c.getTipoCliente(), c.getStatus(),
                maskDocument(firstNonBlank(c.getCpf(), c.getCnpj(), null)),
                maskEmail(c.getEmail()), c.getOrigemCliente(),
                c.getObservacoesGerais() != null && !c.getObservacoesGerais().isBlank());
    }

    private static String displayName(Cliente c) {
        if (c == null) return "Cliente";
        return switch (c.getTipoCliente()) {
            case PESSOA_JURIDICA -> firstNonBlank(c.getRazaoSocial(), c.getNomeFantasia(), c.getNomeCompleto(), "Cliente #" + c.getId());
            default -> firstNonBlank(c.getNomeCompleto(), c.getNomeFantasia(), c.getRazaoSocial(), "Cliente #" + c.getId());
        };
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String value : values) if (value != null && !value.isBlank()) return value;
        return null;
    }

    private static String maskDocument(String value) {
        if (value == null || value.isBlank()) return null;
        String normalized = value.replaceAll("[^A-Za-z0-9]", "");
        if (normalized.length() == 11) return "***." + normalized.substring(3, 6) + "." + normalized.substring(6, 9) + "-**";
        if (normalized.length() == 14) return "**." + normalized.substring(2, 5) + "." + normalized.substring(5, 8) + "/****-**";
        if (normalized.length() >= 4) return normalized.substring(0, 2) + "••••" + normalized.substring(normalized.length() - 2);
        return "Documento protegido";
    }

    private static String maskEmail(String value) {
        if (value == null || value.isBlank()) return null;
        int at = value.indexOf('@');
        if (at <= 0 || at == value.length() - 1) return "Contato protegido";
        String local = value.substring(0, at);
        String domain = value.substring(at + 1);
        int dot = domain.indexOf('.');
        String host = dot >= 0 ? domain.substring(0, dot) : domain;
        String suffix = dot >= 0 ? domain.substring(dot) : "";
        return local.substring(0, 1) + "***@" + (host.isBlank() ? "***" : host.substring(0, 1) + "***") + suffix;
    }
}
