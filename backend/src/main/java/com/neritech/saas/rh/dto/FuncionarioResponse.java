package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.Sexo;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.enums.TipoContrato;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FuncionarioResponse(
        Long id,
        Long empresaId,
        Long usuarioId,
        String matricula,
        String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        Sexo sexo,

        Escolaridade escolaridade,

        Long cargoId,
        Long departamentoId,
        String cargoNome,
        String departamentoNome,
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

        String pisPasep,

        String carteiraTrabalho,

        String fotoFuncionarioUrl,
        String observacoes,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao,
        String perfilProfissional,
        String registroProfissional,
        String tipoProfissional,
        String areaAtuacao,
        BigDecimal comissaoPecas,
        BigDecimal comissaoServicos,
        String linhasTransporte) {
}
