package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Imposto;
import com.neritech.saas.financeiro.domain.enums.BaseCalculoImposto;
import com.neritech.saas.financeiro.domain.enums.TipoImposto;
import com.neritech.saas.financeiro.dto.ImpostoRequest;
import com.neritech.saas.financeiro.dto.ImpostoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:50-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ImpostoMapperImpl implements ImpostoMapper {

    @Override
    public Imposto toEntity(ImpostoRequest request) {
        if ( request == null ) {
            return null;
        }

        Imposto imposto = new Imposto();

        imposto.setAtivo( request.ativo() );
        imposto.setTipoImposto( request.tipoImposto() );
        imposto.setNomeImposto( request.nomeImposto() );
        imposto.setAliquotaPercentual( request.aliquotaPercentual() );
        imposto.setBaseCalculo( request.baseCalculo() );
        imposto.setCodigoReceita( request.codigoReceita() );
        imposto.setAplicavelRegime( request.aplicavelRegime() );
        imposto.setDataInicioVigencia( request.dataInicioVigencia() );
        imposto.setDataFimVigencia( request.dataFimVigencia() );
        imposto.setObservacoes( request.observacoes() );

        return imposto;
    }

    @Override
    public ImpostoResponse toResponse(Imposto entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        TipoImposto tipoImposto = null;
        String nomeImposto = null;
        BigDecimal aliquotaPercentual = null;
        BaseCalculoImposto baseCalculo = null;
        String codigoReceita = null;
        String aplicavelRegime = null;
        Boolean ativo = null;
        LocalDate dataInicioVigencia = null;
        LocalDate dataFimVigencia = null;
        String observacoes = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        tipoImposto = entity.getTipoImposto();
        nomeImposto = entity.getNomeImposto();
        aliquotaPercentual = entity.getAliquotaPercentual();
        baseCalculo = entity.getBaseCalculo();
        codigoReceita = entity.getCodigoReceita();
        aplicavelRegime = entity.getAplicavelRegime();
        ativo = entity.getAtivo();
        dataInicioVigencia = entity.getDataInicioVigencia();
        dataFimVigencia = entity.getDataFimVigencia();
        observacoes = entity.getObservacoes();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ImpostoResponse impostoResponse = new ImpostoResponse( id, empresaId, tipoImposto, nomeImposto, aliquotaPercentual, baseCalculo, codigoReceita, aplicavelRegime, ativo, dataInicioVigencia, dataFimVigencia, observacoes, createdAt, updatedAt );

        return impostoResponse;
    }

    @Override
    public void updateEntityFromDTO(ImpostoRequest request, Imposto entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.tipoImposto() != null ) {
            entity.setTipoImposto( request.tipoImposto() );
        }
        if ( request.nomeImposto() != null ) {
            entity.setNomeImposto( request.nomeImposto() );
        }
        if ( request.aliquotaPercentual() != null ) {
            entity.setAliquotaPercentual( request.aliquotaPercentual() );
        }
        if ( request.baseCalculo() != null ) {
            entity.setBaseCalculo( request.baseCalculo() );
        }
        if ( request.codigoReceita() != null ) {
            entity.setCodigoReceita( request.codigoReceita() );
        }
        if ( request.aplicavelRegime() != null ) {
            entity.setAplicavelRegime( request.aplicavelRegime() );
        }
        if ( request.dataInicioVigencia() != null ) {
            entity.setDataInicioVigencia( request.dataInicioVigencia() );
        }
        if ( request.dataFimVigencia() != null ) {
            entity.setDataFimVigencia( request.dataFimVigencia() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
