package com.neritech.saas.produtoServico.dto;

import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import java.time.LocalDateTime;

public record TransportadoraResponse(
        Long id, Long empresaId, Long fornecedorId, TipoPessoa tipoPessoa,
        String nomeFantasia, String razaoSocial, String cpf, String cnpj,
        String contato, String telefone, String email, String endereco, String cep,
        String cidade, String estado, String modalidade, Boolean ativo,
        String observacoes, Integer versao, long totalCompras,
        LocalDateTime ultimaCompra,
        String inscricaoEstadual, String logradouro, String numero,
        String bairro, String complemento
) {}
