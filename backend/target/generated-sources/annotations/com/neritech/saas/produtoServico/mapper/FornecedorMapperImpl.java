package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.enums.ClassificacaoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.dto.FornecedorResponse;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:07-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FornecedorMapperImpl implements FornecedorMapper {

    @Override
    public Fornecedor toEntity(FornecedorRequest request) {
        if ( request == null ) {
            return null;
        }

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setTipoPessoa( request.tipoPessoa() );
        fornecedor.setNomeFantasia( request.nomeFantasia() );
        fornecedor.setRazaoSocial( request.razaoSocial() );
        fornecedor.setCpf( request.cpf() );
        fornecedor.setCnpj( request.cnpj() );
        fornecedor.setInscricaoEstadual( request.inscricaoEstadual() );
        fornecedor.setInscricaoMunicipal( request.inscricaoMunicipal() );
        fornecedor.setEmailPrincipal( request.emailPrincipal() );
        fornecedor.setTelefonePrincipal( request.telefonePrincipal() );
        fornecedor.setCelularPrincipal( request.celularPrincipal() );
        fornecedor.setWebsite( request.website() );
        fornecedor.setNomeContato( request.nomeContato() );
        fornecedor.setCargoContato( request.cargoContato() );
        fornecedor.setEmailContato( request.emailContato() );
        fornecedor.setTelefoneContato( request.telefoneContato() );
        fornecedor.setEnderecoCompleto( request.enderecoCompleto() );
        fornecedor.setCep( request.cep() );
        fornecedor.setCidade( request.cidade() );
        fornecedor.setEstado( request.estado() );
        fornecedor.setPrazoPagamentoDias( request.prazoPagamentoDias() );
        fornecedor.setLimiteCredito( request.limiteCredito() );
        fornecedor.setDescontoPadrao( request.descontoPadrao() );
        fornecedor.setCondicoesEspeciais( request.condicoesEspeciais() );
        fornecedor.setBancoNome( request.bancoNome() );
        fornecedor.setBancoAgencia( request.bancoAgencia() );
        fornecedor.setBancoConta( request.bancoConta() );
        fornecedor.setBancoPix( request.bancoPix() );
        fornecedor.setClassificacao( request.classificacao() );
        fornecedor.setObservacoes( request.observacoes() );
        fornecedor.setAtivo( request.ativo() );

        return fornecedor;
    }

    @Override
    public FornecedorResponse toResponse(Fornecedor entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long id = null;
        TipoPessoa tipoPessoa = null;
        String nomeFantasia = null;
        String razaoSocial = null;
        String cpf = null;
        String cnpj = null;
        String inscricaoEstadual = null;
        String inscricaoMunicipal = null;
        String emailPrincipal = null;
        String telefonePrincipal = null;
        String celularPrincipal = null;
        String website = null;
        String nomeContato = null;
        String cargoContato = null;
        String emailContato = null;
        String telefoneContato = null;
        String enderecoCompleto = null;
        String cep = null;
        String cidade = null;
        String estado = null;
        Integer prazoPagamentoDias = null;
        BigDecimal limiteCredito = null;
        BigDecimal descontoPadrao = null;
        String condicoesEspeciais = null;
        String bancoNome = null;
        String bancoAgencia = null;
        String bancoConta = null;
        String bancoPix = null;
        ClassificacaoFornecedor classificacao = null;
        String observacoes = null;
        Boolean ativo = null;

        empresaId = entity.getEmpresaId();
        id = entity.getId();
        tipoPessoa = entity.getTipoPessoa();
        nomeFantasia = entity.getNomeFantasia();
        razaoSocial = entity.getRazaoSocial();
        cpf = entity.getCpf();
        cnpj = entity.getCnpj();
        inscricaoEstadual = entity.getInscricaoEstadual();
        inscricaoMunicipal = entity.getInscricaoMunicipal();
        emailPrincipal = entity.getEmailPrincipal();
        telefonePrincipal = entity.getTelefonePrincipal();
        celularPrincipal = entity.getCelularPrincipal();
        website = entity.getWebsite();
        nomeContato = entity.getNomeContato();
        cargoContato = entity.getCargoContato();
        emailContato = entity.getEmailContato();
        telefoneContato = entity.getTelefoneContato();
        enderecoCompleto = entity.getEnderecoCompleto();
        cep = entity.getCep();
        cidade = entity.getCidade();
        estado = entity.getEstado();
        prazoPagamentoDias = entity.getPrazoPagamentoDias();
        limiteCredito = entity.getLimiteCredito();
        descontoPadrao = entity.getDescontoPadrao();
        condicoesEspeciais = entity.getCondicoesEspeciais();
        bancoNome = entity.getBancoNome();
        bancoAgencia = entity.getBancoAgencia();
        bancoConta = entity.getBancoConta();
        bancoPix = entity.getBancoPix();
        classificacao = entity.getClassificacao();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();

        FornecedorResponse fornecedorResponse = new FornecedorResponse( id, empresaId, tipoPessoa, nomeFantasia, razaoSocial, cpf, cnpj, inscricaoEstadual, inscricaoMunicipal, emailPrincipal, telefonePrincipal, celularPrincipal, website, nomeContato, cargoContato, emailContato, telefoneContato, enderecoCompleto, cep, cidade, estado, prazoPagamentoDias, limiteCredito, descontoPadrao, condicoesEspeciais, bancoNome, bancoAgencia, bancoConta, bancoPix, classificacao, observacoes, ativo );

        return fornecedorResponse;
    }

    @Override
    public void updateEntityFromRequest(FornecedorRequest request, Fornecedor entity) {
        if ( request == null ) {
            return;
        }

        entity.setTipoPessoa( request.tipoPessoa() );
        entity.setNomeFantasia( request.nomeFantasia() );
        entity.setRazaoSocial( request.razaoSocial() );
        entity.setCpf( request.cpf() );
        entity.setCnpj( request.cnpj() );
        entity.setInscricaoEstadual( request.inscricaoEstadual() );
        entity.setInscricaoMunicipal( request.inscricaoMunicipal() );
        entity.setEmailPrincipal( request.emailPrincipal() );
        entity.setTelefonePrincipal( request.telefonePrincipal() );
        entity.setCelularPrincipal( request.celularPrincipal() );
        entity.setWebsite( request.website() );
        entity.setNomeContato( request.nomeContato() );
        entity.setCargoContato( request.cargoContato() );
        entity.setEmailContato( request.emailContato() );
        entity.setTelefoneContato( request.telefoneContato() );
        entity.setEnderecoCompleto( request.enderecoCompleto() );
        entity.setCep( request.cep() );
        entity.setCidade( request.cidade() );
        entity.setEstado( request.estado() );
        entity.setPrazoPagamentoDias( request.prazoPagamentoDias() );
        entity.setLimiteCredito( request.limiteCredito() );
        entity.setDescontoPadrao( request.descontoPadrao() );
        entity.setCondicoesEspeciais( request.condicoesEspeciais() );
        entity.setBancoNome( request.bancoNome() );
        entity.setBancoAgencia( request.bancoAgencia() );
        entity.setBancoConta( request.bancoConta() );
        entity.setBancoPix( request.bancoPix() );
        entity.setClassificacao( request.classificacao() );
        entity.setObservacoes( request.observacoes() );
        entity.setAtivo( request.ativo() );
    }
}
