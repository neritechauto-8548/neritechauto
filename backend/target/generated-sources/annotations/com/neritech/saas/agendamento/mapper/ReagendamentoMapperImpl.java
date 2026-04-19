package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.Reagendamento;
import com.neritech.saas.agendamento.domain.enums.MotivoReagendamento;
import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import com.neritech.saas.agendamento.dto.ReagendamentoRequest;
import com.neritech.saas.agendamento.dto.ReagendamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:58-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReagendamentoMapperImpl implements ReagendamentoMapper {

    @Override
    public Reagendamento toEntity(ReagendamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Reagendamento reagendamento = new Reagendamento();

        reagendamento.setAgendamentoOriginal( reagendamentoRequestToAgendamento( request ) );
        reagendamento.setAgendamentoNovo( reagendamentoRequestToAgendamento1( request ) );
        reagendamento.setAprovadoCliente( request.aprovadoCliente() );
        reagendamento.setTaxaReagendamento( request.taxaReagendamento() );
        reagendamento.setDataOriginal( request.dataOriginal() );
        reagendamento.setHoraOriginal( request.horaOriginal() );
        reagendamento.setDataNovo( request.dataNovo() );
        reagendamento.setHoraNovo( request.horaNovo() );
        reagendamento.setMotivoReagendamento( request.motivoReagendamento() );
        reagendamento.setDescricaoMotivo( request.descricaoMotivo() );
        reagendamento.setSolicitadoPor( request.solicitadoPor() );
        reagendamento.setObservacoes( request.observacoes() );
        reagendamento.setUsuarioResponsavel( request.usuarioResponsavel() );

        return reagendamento;
    }

    @Override
    public ReagendamentoResponse toResponse(Reagendamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long agendamentoOriginalId = null;
        String numeroAgendamentoOriginal = null;
        Long agendamentoNovoId = null;
        String numeroAgendamentoNovo = null;
        Long id = null;
        LocalDate dataOriginal = null;
        LocalTime horaOriginal = null;
        LocalDate dataNovo = null;
        LocalTime horaNovo = null;
        MotivoReagendamento motivoReagendamento = null;
        String descricaoMotivo = null;
        SolicitadoPor solicitadoPor = null;
        BigDecimal taxaReagendamento = null;
        Boolean aprovadoCliente = null;
        LocalDateTime dataAprovacao = null;
        String observacoes = null;
        Long usuarioResponsavel = null;
        LocalDateTime dataReagendamento = null;

        agendamentoOriginalId = entityAgendamentoOriginalId( entity );
        numeroAgendamentoOriginal = entityAgendamentoOriginalNumeroAgendamento( entity );
        agendamentoNovoId = entityAgendamentoNovoId( entity );
        numeroAgendamentoNovo = entityAgendamentoNovoNumeroAgendamento( entity );
        id = entity.getId();
        dataOriginal = entity.getDataOriginal();
        horaOriginal = entity.getHoraOriginal();
        dataNovo = entity.getDataNovo();
        horaNovo = entity.getHoraNovo();
        motivoReagendamento = entity.getMotivoReagendamento();
        descricaoMotivo = entity.getDescricaoMotivo();
        solicitadoPor = entity.getSolicitadoPor();
        taxaReagendamento = entity.getTaxaReagendamento();
        aprovadoCliente = entity.getAprovadoCliente();
        dataAprovacao = entity.getDataAprovacao();
        observacoes = entity.getObservacoes();
        usuarioResponsavel = entity.getUsuarioResponsavel();
        dataReagendamento = entity.getDataReagendamento();

        ReagendamentoResponse reagendamentoResponse = new ReagendamentoResponse( id, agendamentoOriginalId, numeroAgendamentoOriginal, agendamentoNovoId, numeroAgendamentoNovo, dataOriginal, horaOriginal, dataNovo, horaNovo, motivoReagendamento, descricaoMotivo, solicitadoPor, taxaReagendamento, aprovadoCliente, dataAprovacao, observacoes, usuarioResponsavel, dataReagendamento );

        return reagendamentoResponse;
    }

    protected Agendamento reagendamentoRequestToAgendamento(ReagendamentoRequest reagendamentoRequest) {
        if ( reagendamentoRequest == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setId( reagendamentoRequest.agendamentoOriginalId() );

        return agendamento;
    }

    protected Agendamento reagendamentoRequestToAgendamento1(ReagendamentoRequest reagendamentoRequest) {
        if ( reagendamentoRequest == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setId( reagendamentoRequest.agendamentoNovoId() );

        return agendamento;
    }

    private Long entityAgendamentoOriginalId(Reagendamento reagendamento) {
        if ( reagendamento == null ) {
            return null;
        }
        Agendamento agendamentoOriginal = reagendamento.getAgendamentoOriginal();
        if ( agendamentoOriginal == null ) {
            return null;
        }
        Long id = agendamentoOriginal.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAgendamentoOriginalNumeroAgendamento(Reagendamento reagendamento) {
        if ( reagendamento == null ) {
            return null;
        }
        Agendamento agendamentoOriginal = reagendamento.getAgendamentoOriginal();
        if ( agendamentoOriginal == null ) {
            return null;
        }
        String numeroAgendamento = agendamentoOriginal.getNumeroAgendamento();
        if ( numeroAgendamento == null ) {
            return null;
        }
        return numeroAgendamento;
    }

    private Long entityAgendamentoNovoId(Reagendamento reagendamento) {
        if ( reagendamento == null ) {
            return null;
        }
        Agendamento agendamentoNovo = reagendamento.getAgendamentoNovo();
        if ( agendamentoNovo == null ) {
            return null;
        }
        Long id = agendamentoNovo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAgendamentoNovoNumeroAgendamento(Reagendamento reagendamento) {
        if ( reagendamento == null ) {
            return null;
        }
        Agendamento agendamentoNovo = reagendamento.getAgendamentoNovo();
        if ( agendamentoNovo == null ) {
            return null;
        }
        String numeroAgendamento = agendamentoNovo.getNumeroAgendamento();
        if ( numeroAgendamento == null ) {
            return null;
        }
        return numeroAgendamento;
    }
}
