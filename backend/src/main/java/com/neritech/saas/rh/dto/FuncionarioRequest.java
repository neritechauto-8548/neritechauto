package com.neritech.saas.rh.dto;

import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.EstadoCivil;
import com.neritech.saas.rh.domain.enums.Sexo;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.enums.TipoContrato;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioRequest(
        @NotNull Long empresaId,
        Long usuarioId,
        @NotBlank @Size(max = 20) String matricula,
        @NotBlank @Size(max = 255) String nomeCompleto,
        @NotBlank @Size(max = 14) String cpf,
        @Size(max = 20) String rg,
        LocalDate dataNascimento,
        Sexo sexo,
        EstadoCivil estadoCivil,
        @Size(max = 50) String nacionalidade,
        @Size(max = 100) String naturalidade,
        @Size(max = 255) String nomeMae,
        @Size(max = 255) String nomePai,
        Escolaridade escolaridade,
        @Size(max = 100) String profissao,
        Long cargoId,
        Long departamentoId,
        @NotNull LocalDate dataAdmissao,
        LocalDate dataDemissao,
        TipoContrato tipoContrato,
        BigDecimal salarioBase,
        BigDecimal comissaoPercentual,
        BigDecimal valeTransporte,
        BigDecimal valeAlimentacao,
        Boolean planoSaude,
        Boolean planoOdontologico,
        StatusFuncionario status,
        String motivoInativacao,
        String enderecoCompleto,
        @Size(max = 20) String telefonePrincipal,
        @Size(max = 20) String telefoneEmergencia,
        @Size(max = 255) String contatoEmergenciaNome,
        @Size(max = 50) String contatoEmergenciaParentesco,
        @Email @Size(max = 255) String emailPessoal,
        @Size(max = 5) String bancoCodigo,
        @Size(max = 10) String bancoAgencia,
        @Size(max = 20) String bancoConta,
        TipoConta bancoTipoConta,
        @Size(max = 15) String pisPasep,
        @Size(max = 15) String tituloEleitor,
        @Size(max = 20) String carteiraTrabalho,
        @Size(max = 20) String certificadoReservista,
        @Size(max = 15) String cnhNumero,
        @Size(max = 5) String cnhCategoria,
        LocalDate cnhValidade,
        @Size(max = 500) String fotoFuncionarioUrl,
        String observacoes) {
}
