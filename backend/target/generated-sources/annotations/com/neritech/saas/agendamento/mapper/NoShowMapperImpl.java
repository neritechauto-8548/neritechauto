package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.NoShow;
import com.neritech.saas.agendamento.dto.NoShowRequest;
import com.neritech.saas.agendamento.dto.NoShowResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class NoShowMapperImpl implements NoShowMapper {

    @Override
    public NoShow toEntity(NoShowRequest request) {
        if ( request == null ) {
            return null;
        }

        NoShow noShow = new NoShow();

        noShow.setAgendamento( noShowRequestToAgendamento( request ) );
        noShow.setNovoAgendamento( noShowRequestToAgendamento1( request ) );
        noShow.setJustificativaAceita( request.justificativaAceita() );
        noShow.setReagendado( request.reagendado() );
        noShow.setTaxaAplicada( request.taxaAplicada() );
        noShow.setTaxaNoShow( request.taxaNoShow() );
        noShow.setTempoToleranciaMinutos( request.tempoToleranciaMinutos() );
        noShow.setTentativasContato( request.tentativasContato() );
        noShow.setClienteId( request.clienteId() );
        noShow.setDataAgendamento( request.dataAgendamento() );
        noShow.setHoraAgendamento( request.horaAgendamento() );
        noShow.setMeioContatoTentado( request.meioContatoTentado() );
        noShow.setMotivoDeclarado( request.motivoDeclarado() );
        noShow.setObservacoes( request.observacoes() );
        noShow.setRegistradoPor( request.registradoPor() );

        return noShow;
    }

    @Override
    public NoShowResponse toResponse(NoShow entity) {
        if ( entity == null ) {
            return null;
        }

        Long agendamentoId = null;
        String numeroAgendamento = null;
        Long novoAgendamentoId = null;
        String numeroNovoAgendamento = null;
        Long id = null;
        Long clienteId = null;
        LocalDate dataAgendamento = null;
        LocalTime horaAgendamento = null;
        Integer tempoToleranciaMinutos = null;
        Integer tentativasContato = null;
        String meioContatoTentado = null;
        Boolean reagendado = null;
        BigDecimal taxaNoShow = null;
        Boolean taxaAplicada = null;
        String motivoDeclarado = null;
        Boolean justificativaAceita = null;
        String observacoes = null;
        Long registradoPor = null;
        LocalDateTime dataRegistro = null;

        agendamentoId = entityAgendamentoId( entity );
        numeroAgendamento = entityAgendamentoNumeroAgendamento( entity );
        novoAgendamentoId = entityNovoAgendamentoId( entity );
        numeroNovoAgendamento = entityNovoAgendamentoNumeroAgendamento( entity );
        id = entity.getId();
        clienteId = entity.getClienteId();
        dataAgendamento = entity.getDataAgendamento();
        horaAgendamento = entity.getHoraAgendamento();
        tempoToleranciaMinutos = entity.getTempoToleranciaMinutos();
        tentativasContato = entity.getTentativasContato();
        meioContatoTentado = entity.getMeioContatoTentado();
        reagendado = entity.getReagendado();
        taxaNoShow = entity.getTaxaNoShow();
        taxaAplicada = entity.getTaxaAplicada();
        motivoDeclarado = entity.getMotivoDeclarado();
        justificativaAceita = entity.getJustificativaAceita();
        observacoes = entity.getObservacoes();
        registradoPor = entity.getRegistradoPor();
        dataRegistro = entity.getDataRegistro();

        NoShowResponse noShowResponse = new NoShowResponse( id, agendamentoId, numeroAgendamento, clienteId, dataAgendamento, horaAgendamento, tempoToleranciaMinutos, tentativasContato, meioContatoTentado, reagendado, novoAgendamentoId, numeroNovoAgendamento, taxaNoShow, taxaAplicada, motivoDeclarado, justificativaAceita, observacoes, registradoPor, dataRegistro );

        return noShowResponse;
    }

    protected Agendamento noShowRequestToAgendamento(NoShowRequest noShowRequest) {
        if ( noShowRequest == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setId( noShowRequest.agendamentoId() );

        return agendamento;
    }

    protected Agendamento noShowRequestToAgendamento1(NoShowRequest noShowRequest) {
        if ( noShowRequest == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setId( noShowRequest.novoAgendamentoId() );

        return agendamento;
    }

    private Long entityAgendamentoId(NoShow noShow) {
        if ( noShow == null ) {
            return null;
        }
        Agendamento agendamento = noShow.getAgendamento();
        if ( agendamento == null ) {
            return null;
        }
        Long id = agendamento.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAgendamentoNumeroAgendamento(NoShow noShow) {
        if ( noShow == null ) {
            return null;
        }
        Agendamento agendamento = noShow.getAgendamento();
        if ( agendamento == null ) {
            return null;
        }
        String numeroAgendamento = agendamento.getNumeroAgendamento();
        if ( numeroAgendamento == null ) {
            return null;
        }
        return numeroAgendamento;
    }

    private Long entityNovoAgendamentoId(NoShow noShow) {
        if ( noShow == null ) {
            return null;
        }
        Agendamento novoAgendamento = noShow.getNovoAgendamento();
        if ( novoAgendamento == null ) {
            return null;
        }
        Long id = novoAgendamento.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityNovoAgendamentoNumeroAgendamento(NoShow noShow) {
        if ( noShow == null ) {
            return null;
        }
        Agendamento novoAgendamento = noShow.getNovoAgendamento();
        if ( novoAgendamento == null ) {
            return null;
        }
        String numeroAgendamento = novoAgendamento.getNumeroAgendamento();
        if ( numeroAgendamento == null ) {
            return null;
        }
        return numeroAgendamento;
    }
}
