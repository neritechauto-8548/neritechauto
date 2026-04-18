package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;

import com.neritech.saas.produtoServico.domain.enums.ClassificacaoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;

public record FornecedorResponse(
                Long id,
                Long empresaId,
                TipoPessoa tipoPessoa,
                String nomeFantasia,
                String razaoSocial,
                String cpf,
                String cnpj,
                String inscricaoEstadual,
                String inscricaoMunicipal,
                String emailPrincipal,
                String telefonePrincipal,
                String celularPrincipal,
                String website,
                String nomeContato,
                String cargoContato,
                String emailContato,
                String telefoneContato,
                String enderecoCompleto,
                String cep,
                String cidade,
                String estado,
                Integer prazoPagamentoDias,
                BigDecimal limiteCredito,
                BigDecimal descontoPadrao,
                String condicoesEspeciais,
                String bancoNome,
                String bancoAgencia,
                String bancoConta,
                String bancoPix,
                ClassificacaoFornecedor classificacao,
                String observacoes,
                Boolean ativo) {
}
