package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.EnderecoCliente;
import com.neritech.saas.cliente.dto.EnderecoClienteRequest;
import com.neritech.saas.cliente.dto.EnderecoClienteResponse;
import com.neritech.saas.cliente.dto.EnderecoClienteSummaryResponse;

import java.util.ArrayList;
import java.util.List;

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

    public static EnderecoClienteSummaryResponse toSummaryResponse(EnderecoCliente e) {
        return new EnderecoClienteSummaryResponse(
                e.getId(),
                buildLocationSummary(e),
                maskPostalCode(e.getCep()),
                e.getPais() != null && !e.getPais().isBlank() ? e.getPais() : "Brasil");
    }

    private static String buildLocationSummary(EnderecoCliente e) {
        List<String> parts = new ArrayList<>();
        if (e.getLogradouro() != null && !e.getLogradouro().isBlank()) parts.add(e.getLogradouro());
        if (e.getNumero() != null && !e.getNumero().isBlank()) parts.add("nº •••");
        if (e.getBairro() != null && !e.getBairro().isBlank()) parts.add(e.getBairro());
        if (e.getCidade() != null && !e.getCidade().isBlank()) parts.add(e.getCidade());
        if (e.getEstado() != null && !e.getEstado().isBlank()) parts.add(e.getEstado().toUpperCase());
        return parts.isEmpty() ? "Endereço protegido" : String.join(", ", parts);
    }

    private static String maskPostalCode(String value) {
        if (value == null || value.isBlank()) return "CEP protegido";
        String digits = value.replaceAll("\\D", "");
        if (digits.length() != 8) return "CEP protegido";
        return digits.substring(0, 2) + "***-" + digits.substring(5);
    }
}
