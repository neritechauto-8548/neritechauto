package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.ListaEspera;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.domain.enums.PeriodoPreferido;
import com.neritech.saas.agendamento.domain.enums.StatusListaEspera;
import com.neritech.saas.agendamento.domain.enums.UrgenciaListaEspera;
import com.neritech.saas.agendamento.dto.ListaEsperaRequest;
import com.neritech.saas.agendamento.dto.ListaEsperaResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ListaEsperaMapperImpl implements ListaEsperaMapper {

    @Override
    public ListaEspera toEntity(ListaEsperaRequest request) {
        if ( request == null ) {
            return null;
        }

        ListaEspera listaEspera = new ListaEspera();

        listaEspera.setTipoAgendamento( listaEsperaRequestToTipoAgendamento( request ) );
        listaEspera.setFlexibilidadeDataDias( request.flexibilidadeDataDias() );
        listaEspera.setNotificarDisponibilidade( request.notificarDisponibilidade() );
        listaEspera.setRaioDisponibilidadeKm( request.raioDisponibilidadeKm() );
        listaEspera.setEmpresaId( request.empresaId() );
        listaEspera.setClienteId( request.clienteId() );
        listaEspera.setVeiculoId( request.veiculoId() );
        listaEspera.setDataPreferida( request.dataPreferida() );
        listaEspera.setPeriodoPreferido( request.periodoPreferido() );
        listaEspera.setServicosDesejados( request.servicosDesejados() );
        listaEspera.setMecanicoPreferidoId( request.mecanicoPreferidoId() );
        listaEspera.setUrgencia( request.urgencia() );
        listaEspera.setObservacoes( request.observacoes() );
        listaEspera.setTelefoneContato( request.telefoneContato() );
        listaEspera.setEmailContato( request.emailContato() );
        listaEspera.setWhatsappContato( request.whatsappContato() );
        listaEspera.setStatus( request.status() );

        return listaEspera;
    }

    @Override
    public ListaEsperaResponse toResponse(ListaEspera entity) {
        if ( entity == null ) {
            return null;
        }

        Long tipoAgendamentoId = null;
        String tipoAgendamentoNome = null;
        Long agendamentoGeradoId = null;
        Long id = null;
        Long empresaId = null;
        Long clienteId = null;
        Long veiculoId = null;
        LocalDate dataPreferida = null;
        PeriodoPreferido periodoPreferido = null;
        String servicosDesejados = null;
        Long mecanicoPreferidoId = null;
        UrgenciaListaEspera urgencia = null;
        String observacoes = null;
        Boolean notificarDisponibilidade = null;
        String telefoneContato = null;
        String emailContato = null;
        String whatsappContato = null;
        Integer raioDisponibilidadeKm = null;
        Integer flexibilidadeDataDias = null;
        StatusListaEspera status = null;
        LocalDateTime dataNotificacao = null;
        LocalDateTime dataExpiracao = null;
        Integer posicaoLista = null;
        LocalDateTime dataCadastro = null;

        tipoAgendamentoId = entityTipoAgendamentoId( entity );
        tipoAgendamentoNome = entityTipoAgendamentoNome( entity );
        agendamentoGeradoId = entityAgendamentoGeradoId( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        clienteId = entity.getClienteId();
        veiculoId = entity.getVeiculoId();
        dataPreferida = entity.getDataPreferida();
        periodoPreferido = entity.getPeriodoPreferido();
        servicosDesejados = entity.getServicosDesejados();
        mecanicoPreferidoId = entity.getMecanicoPreferidoId();
        urgencia = entity.getUrgencia();
        observacoes = entity.getObservacoes();
        notificarDisponibilidade = entity.getNotificarDisponibilidade();
        telefoneContato = entity.getTelefoneContato();
        emailContato = entity.getEmailContato();
        whatsappContato = entity.getWhatsappContato();
        raioDisponibilidadeKm = entity.getRaioDisponibilidadeKm();
        flexibilidadeDataDias = entity.getFlexibilidadeDataDias();
        status = entity.getStatus();
        dataNotificacao = entity.getDataNotificacao();
        dataExpiracao = entity.getDataExpiracao();
        posicaoLista = entity.getPosicaoLista();
        dataCadastro = entity.getDataCadastro();

        ListaEsperaResponse listaEsperaResponse = new ListaEsperaResponse( id, empresaId, clienteId, veiculoId, tipoAgendamentoId, tipoAgendamentoNome, dataPreferida, periodoPreferido, servicosDesejados, mecanicoPreferidoId, urgencia, observacoes, notificarDisponibilidade, telefoneContato, emailContato, whatsappContato, raioDisponibilidadeKm, flexibilidadeDataDias, status, dataNotificacao, dataExpiracao, agendamentoGeradoId, posicaoLista, dataCadastro );

        return listaEsperaResponse;
    }

    protected TipoAgendamento listaEsperaRequestToTipoAgendamento(ListaEsperaRequest listaEsperaRequest) {
        if ( listaEsperaRequest == null ) {
            return null;
        }

        TipoAgendamento tipoAgendamento = new TipoAgendamento();

        tipoAgendamento.setId( listaEsperaRequest.tipoAgendamentoId() );

        return tipoAgendamento;
    }

    private Long entityTipoAgendamentoId(ListaEspera listaEspera) {
        if ( listaEspera == null ) {
            return null;
        }
        TipoAgendamento tipoAgendamento = listaEspera.getTipoAgendamento();
        if ( tipoAgendamento == null ) {
            return null;
        }
        Long id = tipoAgendamento.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTipoAgendamentoNome(ListaEspera listaEspera) {
        if ( listaEspera == null ) {
            return null;
        }
        TipoAgendamento tipoAgendamento = listaEspera.getTipoAgendamento();
        if ( tipoAgendamento == null ) {
            return null;
        }
        String nome = tipoAgendamento.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityAgendamentoGeradoId(ListaEspera listaEspera) {
        if ( listaEspera == null ) {
            return null;
        }
        Agendamento agendamentoGerado = listaEspera.getAgendamentoGerado();
        if ( agendamentoGerado == null ) {
            return null;
        }
        Long id = agendamentoGerado.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
