package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import com.neritech.saas.produtoServico.domain.enums.ClassificacaoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;

/**
 * Payload tenant-neutral: a empresa é sempre derivada da sessão autenticada.
 */
public record FornecedorRequest(
                TipoPessoa tipoPessoa,

                @NotBlank(message = "O nome fantasia é obrigatório") @Size(max = 255, message = "O nome fantasia deve ter no máximo 255 caracteres") String nomeFantasia,

                @Size(max = 255, message = "A razão social deve ter no máximo 255 caracteres") String razaoSocial,

                @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres") String cpf,

                @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres") String cnpj,

                @Size(max = 20, message = "A inscrição estadual deve ter no máximo 20 caracteres") String inscricaoEstadual,

                @Size(max = 20, message = "A inscrição municipal deve ter no máximo 20 caracteres") String inscricaoMunicipal,

                @Email(message = "Email inválido") @Size(max = 255, message = "O email principal deve ter no máximo 255 caracteres") String emailPrincipal,

                @Size(max = 20, message = "O telefone principal deve ter no máximo 20 caracteres") String telefonePrincipal,

                @Size(max = 20, message = "O celular principal deve ter no máximo 20 caracteres") String celularPrincipal,

                @Size(max = 255, message = "O website deve ter no máximo 255 caracteres") String website,

                @Size(max = 255, message = "O nome do contato deve ter no máximo 255 caracteres") String nomeContato,

                @Size(max = 100, message = "O cargo do contato deve ter no máximo 100 caracteres") String cargoContato,

                @Email(message = "Email de contato inválido") @Size(max = 255, message = "O email do contato deve ter no máximo 255 caracteres") String emailContato,

                @Size(max = 20, message = "O telefone do contato deve ter no máximo 20 caracteres") String telefoneContato,

                String enderecoCompleto,

                @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres") String cep,

                @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres") String cidade,

                @Size(max = 2, message = "O estado deve ter no máximo 2 caracteres") String estado,

                Integer prazoPagamentoDias,
                BigDecimal limiteCredito,
                BigDecimal descontoPadrao,
                String condicoesEspeciais,

                @Size(max = 100, message = "O nome do banco deve ter no máximo 100 caracteres") String bancoNome,

                @Size(max = 10, message = "A agência bancária deve ter no máximo 10 caracteres") String bancoAgencia,

                @Size(max = 20, message = "A conta bancária deve ter no máximo 20 caracteres") String bancoConta,

                @Size(max = 255, message = "A chave PIX deve ter no máximo 255 caracteres") String bancoPix,

                ClassificacaoFornecedor classificacao,
                String observacoes,
                Boolean ativo) {
}
