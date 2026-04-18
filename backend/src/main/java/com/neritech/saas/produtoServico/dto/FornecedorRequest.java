package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import com.neritech.saas.produtoServico.domain.enums.ClassificacaoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;

public record FornecedorRequest(
                @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

                TipoPessoa tipoPessoa,

                @NotBlank(message = "O nome fantasia Ã© obrigatÃ³rio") @Size(max = 255, message = "O nome fantasia deve ter no mÃ¡ximo 255 caracteres") String nomeFantasia,

                @Size(max = 255, message = "A razÃ£o social deve ter no mÃ¡ximo 255 caracteres") String razaoSocial,

                @Size(max = 14, message = "O CPF deve ter no mÃ¡ximo 14 caracteres") String cpf,

                @Size(max = 18, message = "O CNPJ deve ter no mÃ¡ximo 18 caracteres") String cnpj,

                @Size(max = 20, message = "A inscriÃ§Ã£o estadual deve ter no mÃ¡ximo 20 caracteres") String inscricaoEstadual,

                @Size(max = 20, message = "A inscriÃ§Ã£o municipal deve ter no mÃ¡ximo 20 caracteres") String inscricaoMunicipal,

                @Email(message = "Email invÃ¡lido") @Size(max = 255, message = "O email principal deve ter no mÃ¡ximo 255 caracteres") String emailPrincipal,

                @Size(max = 20, message = "O telefone principal deve ter no mÃ¡ximo 20 caracteres") String telefonePrincipal,

                @Size(max = 20, message = "O celular principal deve ter no mÃ¡ximo 20 caracteres") String celularPrincipal,

                @Size(max = 255, message = "O website deve ter no mÃ¡ximo 255 caracteres") String website,

                @Size(max = 255, message = "O nome do contato deve ter no mÃ¡ximo 255 caracteres") String nomeContato,

                @Size(max = 100, message = "O cargo do contato deve ter no mÃ¡ximo 100 caracteres") String cargoContato,

                @Email(message = "Email de contato invÃ¡lido") @Size(max = 255, message = "O email do contato deve ter no mÃ¡ximo 255 caracteres") String emailContato,

                @Size(max = 20, message = "O telefone do contato deve ter no mÃ¡ximo 20 caracteres") String telefoneContato,

                String enderecoCompleto,

                @Size(max = 9, message = "O CEP deve ter no mÃ¡ximo 9 caracteres") String cep,

                @Size(max = 100, message = "A cidade deve ter no mÃ¡ximo 100 caracteres") String cidade,

                @Size(max = 2, message = "O estado deve ter no mÃ¡ximo 2 caracteres") String estado,

                Integer prazoPagamentoDias,
                BigDecimal limiteCredito,
                BigDecimal descontoPadrao,
                String condicoesEspeciais,

                @Size(max = 100, message = "O nome do banco deve ter no mÃ¡ximo 100 caracteres") String bancoNome,

                @Size(max = 10, message = "A agÃªncia bancÃ¡ria deve ter no mÃ¡ximo 10 caracteres") String bancoAgencia,

                @Size(max = 20, message = "A conta bancÃ¡ria deve ter no mÃ¡ximo 20 caracteres") String bancoConta,

                @Size(max = 255, message = "A chave PIX deve ter no mÃ¡ximo 255 caracteres") String bancoPix,

                ClassificacaoFornecedor classificacao,
                String observacoes,
                Boolean ativo) {
}
