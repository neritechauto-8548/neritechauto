package com.neritech.saas.rh.mapper;

import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.EstadoCivil;
import com.neritech.saas.rh.domain.enums.Sexo;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.enums.TipoContrato;
import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FuncionarioMapperImpl implements FuncionarioMapper {

    @Override
    public Funcionario toEntity(FuncionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Funcionario funcionario = new Funcionario();

        funcionario.setEmpresaId( request.empresaId() );
        funcionario.setUsuarioId( request.usuarioId() );
        funcionario.setMatricula( request.matricula() );
        funcionario.setNomeCompleto( request.nomeCompleto() );
        funcionario.setCpf( request.cpf() );
        funcionario.setRg( request.rg() );
        funcionario.setDataNascimento( request.dataNascimento() );
        funcionario.setSexo( request.sexo() );
        funcionario.setEstadoCivil( request.estadoCivil() );
        funcionario.setNacionalidade( request.nacionalidade() );
        funcionario.setNaturalidade( request.naturalidade() );
        funcionario.setNomeMae( request.nomeMae() );
        funcionario.setNomePai( request.nomePai() );
        funcionario.setEscolaridade( request.escolaridade() );
        funcionario.setProfissao( request.profissao() );
        funcionario.setCargoId( request.cargoId() );
        funcionario.setDepartamentoId( request.departamentoId() );
        funcionario.setDataAdmissao( request.dataAdmissao() );
        funcionario.setDataDemissao( request.dataDemissao() );
        funcionario.setTipoContrato( request.tipoContrato() );
        funcionario.setSalarioBase( request.salarioBase() );
        funcionario.setComissaoPercentual( request.comissaoPercentual() );
        funcionario.setValeTransporte( request.valeTransporte() );
        funcionario.setValeAlimentacao( request.valeAlimentacao() );
        funcionario.setPlanoSaude( request.planoSaude() );
        funcionario.setPlanoOdontologico( request.planoOdontologico() );
        funcionario.setStatus( request.status() );
        funcionario.setMotivoInativacao( request.motivoInativacao() );
        funcionario.setEnderecoCompleto( request.enderecoCompleto() );
        funcionario.setTelefonePrincipal( request.telefonePrincipal() );
        funcionario.setTelefoneEmergencia( request.telefoneEmergencia() );
        funcionario.setContatoEmergenciaNome( request.contatoEmergenciaNome() );
        funcionario.setContatoEmergenciaParentesco( request.contatoEmergenciaParentesco() );
        funcionario.setEmailPessoal( request.emailPessoal() );
        funcionario.setBancoCodigo( request.bancoCodigo() );
        funcionario.setBancoAgencia( request.bancoAgencia() );
        funcionario.setBancoConta( request.bancoConta() );
        funcionario.setBancoTipoConta( request.bancoTipoConta() );
        funcionario.setPisPasep( request.pisPasep() );
        funcionario.setTituloEleitor( request.tituloEleitor() );
        funcionario.setCarteiraTrabalho( request.carteiraTrabalho() );
        funcionario.setCertificadoReservista( request.certificadoReservista() );
        funcionario.setCnhNumero( request.cnhNumero() );
        funcionario.setCnhCategoria( request.cnhCategoria() );
        funcionario.setCnhValidade( request.cnhValidade() );
        funcionario.setFotoFuncionarioUrl( request.fotoFuncionarioUrl() );
        funcionario.setObservacoes( request.observacoes() );

        return funcionario;
    }

    @Override
    public FuncionarioResponse toResponse(Funcionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long usuarioId = null;
        String matricula = null;
        String nomeCompleto = null;
        String cpf = null;
        String rg = null;
        LocalDate dataNascimento = null;
        Sexo sexo = null;
        EstadoCivil estadoCivil = null;
        String nacionalidade = null;
        String naturalidade = null;
        String nomeMae = null;
        String nomePai = null;
        Escolaridade escolaridade = null;
        String profissao = null;
        Long cargoId = null;
        Long departamentoId = null;
        LocalDate dataAdmissao = null;
        LocalDate dataDemissao = null;
        TipoContrato tipoContrato = null;
        BigDecimal salarioBase = null;
        BigDecimal comissaoPercentual = null;
        BigDecimal valeTransporte = null;
        BigDecimal valeAlimentacao = null;
        Boolean planoSaude = null;
        Boolean planoOdontologico = null;
        StatusFuncionario status = null;
        String motivoInativacao = null;
        String enderecoCompleto = null;
        String telefonePrincipal = null;
        String telefoneEmergencia = null;
        String contatoEmergenciaNome = null;
        String contatoEmergenciaParentesco = null;
        String emailPessoal = null;
        String bancoCodigo = null;
        String bancoAgencia = null;
        String bancoConta = null;
        TipoConta bancoTipoConta = null;
        String pisPasep = null;
        String tituloEleitor = null;
        String carteiraTrabalho = null;
        String certificadoReservista = null;
        String cnhNumero = null;
        String cnhCategoria = null;
        LocalDate cnhValidade = null;
        String fotoFuncionarioUrl = null;
        String observacoes = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        usuarioId = entity.getUsuarioId();
        matricula = entity.getMatricula();
        nomeCompleto = entity.getNomeCompleto();
        cpf = entity.getCpf();
        rg = entity.getRg();
        dataNascimento = entity.getDataNascimento();
        sexo = entity.getSexo();
        estadoCivil = entity.getEstadoCivil();
        nacionalidade = entity.getNacionalidade();
        naturalidade = entity.getNaturalidade();
        nomeMae = entity.getNomeMae();
        nomePai = entity.getNomePai();
        escolaridade = entity.getEscolaridade();
        profissao = entity.getProfissao();
        cargoId = entity.getCargoId();
        departamentoId = entity.getDepartamentoId();
        dataAdmissao = entity.getDataAdmissao();
        dataDemissao = entity.getDataDemissao();
        tipoContrato = entity.getTipoContrato();
        salarioBase = entity.getSalarioBase();
        comissaoPercentual = entity.getComissaoPercentual();
        valeTransporte = entity.getValeTransporte();
        valeAlimentacao = entity.getValeAlimentacao();
        planoSaude = entity.getPlanoSaude();
        planoOdontologico = entity.getPlanoOdontologico();
        status = entity.getStatus();
        motivoInativacao = entity.getMotivoInativacao();
        enderecoCompleto = entity.getEnderecoCompleto();
        telefonePrincipal = entity.getTelefonePrincipal();
        telefoneEmergencia = entity.getTelefoneEmergencia();
        contatoEmergenciaNome = entity.getContatoEmergenciaNome();
        contatoEmergenciaParentesco = entity.getContatoEmergenciaParentesco();
        emailPessoal = entity.getEmailPessoal();
        bancoCodigo = entity.getBancoCodigo();
        bancoAgencia = entity.getBancoAgencia();
        bancoConta = entity.getBancoConta();
        bancoTipoConta = entity.getBancoTipoConta();
        pisPasep = entity.getPisPasep();
        tituloEleitor = entity.getTituloEleitor();
        carteiraTrabalho = entity.getCarteiraTrabalho();
        certificadoReservista = entity.getCertificadoReservista();
        cnhNumero = entity.getCnhNumero();
        cnhCategoria = entity.getCnhCategoria();
        cnhValidade = entity.getCnhValidade();
        fotoFuncionarioUrl = entity.getFotoFuncionarioUrl();
        observacoes = entity.getObservacoes();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        FuncionarioResponse funcionarioResponse = new FuncionarioResponse( id, empresaId, usuarioId, matricula, nomeCompleto, cpf, rg, dataNascimento, sexo, estadoCivil, nacionalidade, naturalidade, nomeMae, nomePai, escolaridade, profissao, cargoId, departamentoId, dataAdmissao, dataDemissao, tipoContrato, salarioBase, comissaoPercentual, valeTransporte, valeAlimentacao, planoSaude, planoOdontologico, status, motivoInativacao, enderecoCompleto, telefonePrincipal, telefoneEmergencia, contatoEmergenciaNome, contatoEmergenciaParentesco, emailPessoal, bancoCodigo, bancoAgencia, bancoConta, bancoTipoConta, pisPasep, tituloEleitor, carteiraTrabalho, certificadoReservista, cnhNumero, cnhCategoria, cnhValidade, fotoFuncionarioUrl, observacoes, dataCadastro, dataAtualizacao );

        return funcionarioResponse;
    }

    @Override
    public void updateEntity(FuncionarioRequest request, Funcionario entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.usuarioId() != null ) {
            entity.setUsuarioId( request.usuarioId() );
        }
        if ( request.matricula() != null ) {
            entity.setMatricula( request.matricula() );
        }
        if ( request.nomeCompleto() != null ) {
            entity.setNomeCompleto( request.nomeCompleto() );
        }
        if ( request.cpf() != null ) {
            entity.setCpf( request.cpf() );
        }
        if ( request.rg() != null ) {
            entity.setRg( request.rg() );
        }
        if ( request.dataNascimento() != null ) {
            entity.setDataNascimento( request.dataNascimento() );
        }
        if ( request.sexo() != null ) {
            entity.setSexo( request.sexo() );
        }
        if ( request.estadoCivil() != null ) {
            entity.setEstadoCivil( request.estadoCivil() );
        }
        if ( request.nacionalidade() != null ) {
            entity.setNacionalidade( request.nacionalidade() );
        }
        if ( request.naturalidade() != null ) {
            entity.setNaturalidade( request.naturalidade() );
        }
        if ( request.nomeMae() != null ) {
            entity.setNomeMae( request.nomeMae() );
        }
        if ( request.nomePai() != null ) {
            entity.setNomePai( request.nomePai() );
        }
        if ( request.escolaridade() != null ) {
            entity.setEscolaridade( request.escolaridade() );
        }
        if ( request.profissao() != null ) {
            entity.setProfissao( request.profissao() );
        }
        if ( request.cargoId() != null ) {
            entity.setCargoId( request.cargoId() );
        }
        if ( request.departamentoId() != null ) {
            entity.setDepartamentoId( request.departamentoId() );
        }
        if ( request.dataAdmissao() != null ) {
            entity.setDataAdmissao( request.dataAdmissao() );
        }
        if ( request.dataDemissao() != null ) {
            entity.setDataDemissao( request.dataDemissao() );
        }
        if ( request.tipoContrato() != null ) {
            entity.setTipoContrato( request.tipoContrato() );
        }
        if ( request.salarioBase() != null ) {
            entity.setSalarioBase( request.salarioBase() );
        }
        if ( request.comissaoPercentual() != null ) {
            entity.setComissaoPercentual( request.comissaoPercentual() );
        }
        if ( request.valeTransporte() != null ) {
            entity.setValeTransporte( request.valeTransporte() );
        }
        if ( request.valeAlimentacao() != null ) {
            entity.setValeAlimentacao( request.valeAlimentacao() );
        }
        if ( request.planoSaude() != null ) {
            entity.setPlanoSaude( request.planoSaude() );
        }
        if ( request.planoOdontologico() != null ) {
            entity.setPlanoOdontologico( request.planoOdontologico() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.motivoInativacao() != null ) {
            entity.setMotivoInativacao( request.motivoInativacao() );
        }
        if ( request.enderecoCompleto() != null ) {
            entity.setEnderecoCompleto( request.enderecoCompleto() );
        }
        if ( request.telefonePrincipal() != null ) {
            entity.setTelefonePrincipal( request.telefonePrincipal() );
        }
        if ( request.telefoneEmergencia() != null ) {
            entity.setTelefoneEmergencia( request.telefoneEmergencia() );
        }
        if ( request.contatoEmergenciaNome() != null ) {
            entity.setContatoEmergenciaNome( request.contatoEmergenciaNome() );
        }
        if ( request.contatoEmergenciaParentesco() != null ) {
            entity.setContatoEmergenciaParentesco( request.contatoEmergenciaParentesco() );
        }
        if ( request.emailPessoal() != null ) {
            entity.setEmailPessoal( request.emailPessoal() );
        }
        if ( request.bancoCodigo() != null ) {
            entity.setBancoCodigo( request.bancoCodigo() );
        }
        if ( request.bancoAgencia() != null ) {
            entity.setBancoAgencia( request.bancoAgencia() );
        }
        if ( request.bancoConta() != null ) {
            entity.setBancoConta( request.bancoConta() );
        }
        if ( request.bancoTipoConta() != null ) {
            entity.setBancoTipoConta( request.bancoTipoConta() );
        }
        if ( request.pisPasep() != null ) {
            entity.setPisPasep( request.pisPasep() );
        }
        if ( request.tituloEleitor() != null ) {
            entity.setTituloEleitor( request.tituloEleitor() );
        }
        if ( request.carteiraTrabalho() != null ) {
            entity.setCarteiraTrabalho( request.carteiraTrabalho() );
        }
        if ( request.certificadoReservista() != null ) {
            entity.setCertificadoReservista( request.certificadoReservista() );
        }
        if ( request.cnhNumero() != null ) {
            entity.setCnhNumero( request.cnhNumero() );
        }
        if ( request.cnhCategoria() != null ) {
            entity.setCnhCategoria( request.cnhCategoria() );
        }
        if ( request.cnhValidade() != null ) {
            entity.setCnhValidade( request.cnhValidade() );
        }
        if ( request.fotoFuncionarioUrl() != null ) {
            entity.setFotoFuncionarioUrl( request.fotoFuncionarioUrl() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
