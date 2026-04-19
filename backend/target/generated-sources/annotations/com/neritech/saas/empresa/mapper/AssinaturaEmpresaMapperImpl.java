package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaRequest;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:45-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AssinaturaEmpresaMapperImpl implements AssinaturaEmpresaMapper {

    @Override
    public AssinaturaEmpresa toEntity(AssinaturaEmpresaRequest request) {
        if ( request == null ) {
            return null;
        }

        AssinaturaEmpresa assinaturaEmpresa = new AssinaturaEmpresa();

        assinaturaEmpresa.setDataInicio( request.dataInicio() );
        assinaturaEmpresa.setDataFim( request.dataFim() );
        assinaturaEmpresa.setDataVencimentoMensal( request.dataVencimentoMensal() );
        assinaturaEmpresa.setValorMensal( request.valorMensal() );
        assinaturaEmpresa.setValorAnual( request.valorAnual() );
        assinaturaEmpresa.setDescontoPercentual( request.descontoPercentual() );
        assinaturaEmpresa.setStatus( request.status() );
        assinaturaEmpresa.setFormaPagamento( request.formaPagamento() );
        assinaturaEmpresa.setRenovacaoAutomatica( request.renovacaoAutomatica() );
        assinaturaEmpresa.setDataCancelamento( request.dataCancelamento() );
        assinaturaEmpresa.setMotivoCancelamento( request.motivoCancelamento() );

        return assinaturaEmpresa;
    }

    @Override
    public AssinaturaEmpresaResponse toResponse(AssinaturaEmpresa entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long planoId = null;
        String planoNome = null;
        Long id = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        Integer dataVencimentoMensal = null;
        BigDecimal valorMensal = null;
        BigDecimal valorAnual = null;
        BigDecimal descontoPercentual = null;
        StatusAssinatura status = null;
        FormaPagamento formaPagamento = null;
        Boolean renovacaoAutomatica = null;
        LocalDate dataCancelamento = null;
        String motivoCancelamento = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        planoId = entityPlanoId( entity );
        planoNome = entityPlanoNome( entity );
        id = entity.getId();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        dataVencimentoMensal = entity.getDataVencimentoMensal();
        valorMensal = entity.getValorMensal();
        valorAnual = entity.getValorAnual();
        descontoPercentual = entity.getDescontoPercentual();
        status = entity.getStatus();
        formaPagamento = entity.getFormaPagamento();
        renovacaoAutomatica = entity.getRenovacaoAutomatica();
        dataCancelamento = entity.getDataCancelamento();
        motivoCancelamento = entity.getMotivoCancelamento();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        AssinaturaEmpresaResponse assinaturaEmpresaResponse = new AssinaturaEmpresaResponse( id, empresaId, empresaNome, planoId, planoNome, dataInicio, dataFim, dataVencimentoMensal, valorMensal, valorAnual, descontoPercentual, status, formaPagamento, renovacaoAutomatica, dataCancelamento, motivoCancelamento, dataCadastro, dataAtualizacao );

        return assinaturaEmpresaResponse;
    }

    @Override
    public void updateEntityFromRequest(AssinaturaEmpresaRequest request, AssinaturaEmpresa entity) {
        if ( request == null ) {
            return;
        }

        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setDataVencimentoMensal( request.dataVencimentoMensal() );
        entity.setValorMensal( request.valorMensal() );
        entity.setValorAnual( request.valorAnual() );
        entity.setDescontoPercentual( request.descontoPercentual() );
        entity.setStatus( request.status() );
        entity.setFormaPagamento( request.formaPagamento() );
        entity.setRenovacaoAutomatica( request.renovacaoAutomatica() );
        entity.setDataCancelamento( request.dataCancelamento() );
        entity.setMotivoCancelamento( request.motivoCancelamento() );
    }

    private Long entityEmpresaId(AssinaturaEmpresa assinaturaEmpresa) {
        if ( assinaturaEmpresa == null ) {
            return null;
        }
        Empresa empresa = assinaturaEmpresa.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(AssinaturaEmpresa assinaturaEmpresa) {
        if ( assinaturaEmpresa == null ) {
            return null;
        }
        Empresa empresa = assinaturaEmpresa.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        String nomeFantasia = empresa.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }

    private Long entityPlanoId(AssinaturaEmpresa assinaturaEmpresa) {
        if ( assinaturaEmpresa == null ) {
            return null;
        }
        PlanoAssinatura plano = assinaturaEmpresa.getPlano();
        if ( plano == null ) {
            return null;
        }
        Long id = plano.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityPlanoNome(AssinaturaEmpresa assinaturaEmpresa) {
        if ( assinaturaEmpresa == null ) {
            return null;
        }
        PlanoAssinatura plano = assinaturaEmpresa.getPlano();
        if ( plano == null ) {
            return null;
        }
        String nome = plano.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
