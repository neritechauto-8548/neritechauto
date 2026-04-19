package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.FeriasFuncionario;
import com.neritech.saas.rh.domain.enums.StatusFerias;
import com.neritech.saas.rh.dto.FeriasFuncionarioRequest;
import com.neritech.saas.rh.dto.FeriasFuncionarioResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:42-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FeriasFuncionarioMapperImpl implements FeriasFuncionarioMapper {

    @Override
    public FeriasFuncionario toEntity(FeriasFuncionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        FeriasFuncionario feriasFuncionario = new FeriasFuncionario();

        feriasFuncionario.setFuncionarioId( request.funcionarioId() );
        feriasFuncionario.setPeriodoAquisitivoInicio( request.periodoAquisitivoInicio() );
        feriasFuncionario.setPeriodoAquisitivoFim( request.periodoAquisitivoFim() );
        feriasFuncionario.setDiasDireito( request.diasDireito() );
        feriasFuncionario.setDiasGozados( request.diasGozados() );
        feriasFuncionario.setDiasRestantes( request.diasRestantes() );
        feriasFuncionario.setDataInicioFerias( request.dataInicioFerias() );
        feriasFuncionario.setDataFimFerias( request.dataFimFerias() );
        feriasFuncionario.setDataRetornoPrevisto( request.dataRetornoPrevisto() );
        feriasFuncionario.setAbonoPecuniario( request.abonoPecuniario() );
        feriasFuncionario.setDiasAbono( request.diasAbono() );
        feriasFuncionario.setFracionada( request.fracionada() );
        feriasFuncionario.setNumeroFracao( request.numeroFracao() );
        feriasFuncionario.setStatus( request.status() );
        feriasFuncionario.setObservacoes( request.observacoes() );

        return feriasFuncionario;
    }

    @Override
    public FeriasFuncionarioResponse toResponse(FeriasFuncionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        LocalDate periodoAquisitivoInicio = null;
        LocalDate periodoAquisitivoFim = null;
        Integer diasDireito = null;
        Integer diasGozados = null;
        Integer diasRestantes = null;
        LocalDate dataInicioFerias = null;
        LocalDate dataFimFerias = null;
        LocalDate dataRetornoPrevisto = null;
        Boolean abonoPecuniario = null;
        Integer diasAbono = null;
        Boolean fracionada = null;
        Integer numeroFracao = null;
        StatusFerias status = null;
        String observacoes = null;
        Long solicitadoPor = null;
        LocalDate dataSolicitacao = null;
        Long aprovadoPor = null;
        LocalDate dataAprovacao = null;
        Long canceladoPor = null;
        LocalDate dataCancelamento = null;
        String motivoCancelamento = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        periodoAquisitivoInicio = entity.getPeriodoAquisitivoInicio();
        periodoAquisitivoFim = entity.getPeriodoAquisitivoFim();
        diasDireito = entity.getDiasDireito();
        diasGozados = entity.getDiasGozados();
        diasRestantes = entity.getDiasRestantes();
        dataInicioFerias = entity.getDataInicioFerias();
        dataFimFerias = entity.getDataFimFerias();
        dataRetornoPrevisto = entity.getDataRetornoPrevisto();
        abonoPecuniario = entity.getAbonoPecuniario();
        diasAbono = entity.getDiasAbono();
        fracionada = entity.getFracionada();
        numeroFracao = entity.getNumeroFracao();
        status = entity.getStatus();
        observacoes = entity.getObservacoes();
        solicitadoPor = entity.getSolicitadoPor();
        dataSolicitacao = entity.getDataSolicitacao();
        aprovadoPor = entity.getAprovadoPor();
        dataAprovacao = entity.getDataAprovacao();
        canceladoPor = entity.getCanceladoPor();
        dataCancelamento = entity.getDataCancelamento();
        motivoCancelamento = entity.getMotivoCancelamento();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        FeriasFuncionarioResponse feriasFuncionarioResponse = new FeriasFuncionarioResponse( id, funcionarioId, periodoAquisitivoInicio, periodoAquisitivoFim, diasDireito, diasGozados, diasRestantes, dataInicioFerias, dataFimFerias, dataRetornoPrevisto, abonoPecuniario, diasAbono, fracionada, numeroFracao, status, observacoes, solicitadoPor, dataSolicitacao, aprovadoPor, dataAprovacao, canceladoPor, dataCancelamento, motivoCancelamento, dataCadastro, dataAtualizacao );

        return feriasFuncionarioResponse;
    }

    @Override
    public void updateEntity(FeriasFuncionarioRequest request, FeriasFuncionario entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.periodoAquisitivoInicio() != null ) {
            entity.setPeriodoAquisitivoInicio( request.periodoAquisitivoInicio() );
        }
        if ( request.periodoAquisitivoFim() != null ) {
            entity.setPeriodoAquisitivoFim( request.periodoAquisitivoFim() );
        }
        if ( request.diasDireito() != null ) {
            entity.setDiasDireito( request.diasDireito() );
        }
        if ( request.diasGozados() != null ) {
            entity.setDiasGozados( request.diasGozados() );
        }
        if ( request.diasRestantes() != null ) {
            entity.setDiasRestantes( request.diasRestantes() );
        }
        if ( request.dataInicioFerias() != null ) {
            entity.setDataInicioFerias( request.dataInicioFerias() );
        }
        if ( request.dataFimFerias() != null ) {
            entity.setDataFimFerias( request.dataFimFerias() );
        }
        if ( request.dataRetornoPrevisto() != null ) {
            entity.setDataRetornoPrevisto( request.dataRetornoPrevisto() );
        }
        if ( request.abonoPecuniario() != null ) {
            entity.setAbonoPecuniario( request.abonoPecuniario() );
        }
        if ( request.diasAbono() != null ) {
            entity.setDiasAbono( request.diasAbono() );
        }
        if ( request.fracionada() != null ) {
            entity.setFracionada( request.fracionada() );
        }
        if ( request.numeroFracao() != null ) {
            entity.setNumeroFracao( request.numeroFracao() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
