package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.Sexo;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.enums.TipoContrato;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioRequest(
        @NotNull Long empresaId,
        Long usuarioId,
        @Size(max = 20) String matricula,
        @NotBlank @Size(max = 255) String nomeCompleto,
        @NotBlank @Size(max = 14) String cpf,
        @Size(max = 20) String rg,
        LocalDate dataNascimento,
        Sexo sexo,

        Escolaridade escolaridade,

        Long cargoId,
        Long departamentoId,
        LocalDate dataAdmissao,
        LocalDate dataDemissao,
        TipoContrato tipoContrato,
        BigDecimal salarioBase,

        BigDecimal valeTransporte,

        StatusFuncionario status,
        String motivoInativacao,

        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,

        @Size(max = 15) String pisPasep,

        @Size(max = 20) String carteiraTrabalho,

        @Size(max = 500) String fotoFuncionarioUrl,
        String observacoes,
        Integer versao,
        @Size(max = 50) String perfilProfissional,
        @Size(max = 80) String registroProfissional,
        @Size(max = 80) String tipoProfissional,
        @Size(max = 100) String areaAtuacao,
        @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal comissaoPecas,
        @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal comissaoServicos,
        @Size(max = 255) String linhasTransporte) {
}
