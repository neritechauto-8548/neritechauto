package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.FaltaAtraso;
import com.neritech.saas.rh.domain.enums.TipoJustificativa;
import com.neritech.saas.rh.domain.enums.TipoOcorrencia;
import com.neritech.saas.rh.dto.FaltaAtrasoRequest;
import com.neritech.saas.rh.dto.FaltaAtrasoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:44-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FaltaAtrasoMapperImpl implements FaltaAtrasoMapper {

    @Override
    public FaltaAtraso toEntity(FaltaAtrasoRequest request) {
        if ( request == null ) {
            return null;
        }

        FaltaAtraso faltaAtraso = new FaltaAtraso();

        faltaAtraso.setFuncionarioId( request.funcionarioId() );
        faltaAtraso.setTipoOcorrencia( request.tipoOcorrencia() );
        faltaAtraso.setDataOcorrencia( request.dataOcorrencia() );
        faltaAtraso.setHorarioPrevisto( request.horarioPrevisto() );
        faltaAtraso.setHorarioReal( request.horarioReal() );
        faltaAtraso.setMinutosAtraso( request.minutosAtraso() );
        faltaAtraso.setHorasFalta( request.horasFalta() );
        faltaAtraso.setTipoJustificativa( request.tipoJustificativa() );
        faltaAtraso.setJustificativa( request.justificativa() );
        faltaAtraso.setJustificada( request.justificada() );
        faltaAtraso.setAnexoComprovanteUrl( request.anexoComprovanteUrl() );
        faltaAtraso.setDescontoAplicado( request.descontoAplicado() );
        faltaAtraso.setValorDesconto( request.valorDesconto() );
        faltaAtraso.setAdvertenciaAplicada( request.advertenciaAplicada() );
        faltaAtraso.setObservacoes( request.observacoes() );

        return faltaAtraso;
    }

    @Override
    public FaltaAtrasoResponse toResponse(FaltaAtraso entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        TipoOcorrencia tipoOcorrencia = null;
        LocalDate dataOcorrencia = null;
        LocalTime horarioPrevisto = null;
        LocalTime horarioReal = null;
        Integer minutosAtraso = null;
        BigDecimal horasFalta = null;
        TipoJustificativa tipoJustificativa = null;
        String justificativa = null;
        Boolean justificada = null;
        String anexoComprovanteUrl = null;
        Boolean descontoAplicado = null;
        BigDecimal valorDesconto = null;
        Boolean advertenciaAplicada = null;
        String observacoes = null;
        Long registradoPor = null;
        Long aprovadoPor = null;
        LocalDate dataAprovacao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        tipoOcorrencia = entity.getTipoOcorrencia();
        dataOcorrencia = entity.getDataOcorrencia();
        horarioPrevisto = entity.getHorarioPrevisto();
        horarioReal = entity.getHorarioReal();
        minutosAtraso = entity.getMinutosAtraso();
        horasFalta = entity.getHorasFalta();
        tipoJustificativa = entity.getTipoJustificativa();
        justificativa = entity.getJustificativa();
        justificada = entity.getJustificada();
        anexoComprovanteUrl = entity.getAnexoComprovanteUrl();
        descontoAplicado = entity.getDescontoAplicado();
        valorDesconto = entity.getValorDesconto();
        advertenciaAplicada = entity.getAdvertenciaAplicada();
        observacoes = entity.getObservacoes();
        registradoPor = entity.getRegistradoPor();
        aprovadoPor = entity.getAprovadoPor();
        dataAprovacao = entity.getDataAprovacao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        FaltaAtrasoResponse faltaAtrasoResponse = new FaltaAtrasoResponse( id, funcionarioId, tipoOcorrencia, dataOcorrencia, horarioPrevisto, horarioReal, minutosAtraso, horasFalta, tipoJustificativa, justificativa, justificada, anexoComprovanteUrl, descontoAplicado, valorDesconto, advertenciaAplicada, observacoes, registradoPor, aprovadoPor, dataAprovacao, dataCadastro, dataAtualizacao );

        return faltaAtrasoResponse;
    }

    @Override
    public void updateEntity(FaltaAtrasoRequest request, FaltaAtraso entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.tipoOcorrencia() != null ) {
            entity.setTipoOcorrencia( request.tipoOcorrencia() );
        }
        if ( request.dataOcorrencia() != null ) {
            entity.setDataOcorrencia( request.dataOcorrencia() );
        }
        if ( request.horarioPrevisto() != null ) {
            entity.setHorarioPrevisto( request.horarioPrevisto() );
        }
        if ( request.horarioReal() != null ) {
            entity.setHorarioReal( request.horarioReal() );
        }
        if ( request.minutosAtraso() != null ) {
            entity.setMinutosAtraso( request.minutosAtraso() );
        }
        if ( request.horasFalta() != null ) {
            entity.setHorasFalta( request.horasFalta() );
        }
        if ( request.tipoJustificativa() != null ) {
            entity.setTipoJustificativa( request.tipoJustificativa() );
        }
        if ( request.justificativa() != null ) {
            entity.setJustificativa( request.justificativa() );
        }
        if ( request.justificada() != null ) {
            entity.setJustificada( request.justificada() );
        }
        if ( request.anexoComprovanteUrl() != null ) {
            entity.setAnexoComprovanteUrl( request.anexoComprovanteUrl() );
        }
        if ( request.descontoAplicado() != null ) {
            entity.setDescontoAplicado( request.descontoAplicado() );
        }
        if ( request.valorDesconto() != null ) {
            entity.setValorDesconto( request.valorDesconto() );
        }
        if ( request.advertenciaAplicada() != null ) {
            entity.setAdvertenciaAplicada( request.advertenciaAplicada() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
