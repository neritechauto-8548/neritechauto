package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.MetodoConfirmacao;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:53-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AgendamentoMapperImpl implements AgendamentoMapper {

    @Override
    public Agendamento toEntity(AgendamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setTipoAgendamento( agendamentoRequestToTipoAgendamento( request ) );
        agendamento.setConfirmadoCliente( request.confirmadoCliente() );
        agendamento.setEmpresaId( request.empresaId() );
        agendamento.setNumeroAgendamento( request.numeroAgendamento() );
        agendamento.setClienteId( request.clienteId() );
        agendamento.setVeiculoId( request.veiculoId() );
        agendamento.setDataAgendamento( request.dataAgendamento() );
        agendamento.setHoraInicio( request.horaInicio() );
        agendamento.setHoraFim( request.horaFim() );
        agendamento.setDuracaoEstimadaMinutos( request.duracaoEstimadaMinutos() );
        agendamento.setServicosSolicitados( request.servicosSolicitados() );
        agendamento.setProblemaRelatado( request.problemaRelatado() );
        agendamento.setObservacoesCliente( request.observacoesCliente() );
        agendamento.setObservacoesInternas( request.observacoesInternas() );
        agendamento.setMecanicoPreferidoId( request.mecanicoPreferidoId() );
        agendamento.setMecanicoAlocadoId( request.mecanicoAlocadoId() );
        agendamento.setRecursosNecessarios( request.recursosNecessarios() );
        agendamento.setStatus( request.status() );
        agendamento.setMetodoConfirmacao( request.metodoConfirmacao() );
        agendamento.setValorEstimado( request.valorEstimado() );
        agendamento.setFormaPagamentoPreferidaId( request.formaPagamentoPreferidaId() );
        agendamento.setCanalAgendamento( request.canalAgendamento() );

        return agendamento;
    }

    @Override
    public AgendamentoResponse toResponse(Agendamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long tipoAgendamentoId = null;
        String tipoAgendamentoNome = null;
        Long id = null;
        Long empresaId = null;
        String numeroAgendamento = null;
        Long clienteId = null;
        Long veiculoId = null;
        LocalDate dataAgendamento = null;
        LocalTime horaInicio = null;
        LocalTime horaFim = null;
        Integer duracaoEstimadaMinutos = null;
        String servicosSolicitados = null;
        String problemaRelatado = null;
        String observacoesCliente = null;
        String observacoesInternas = null;
        Long mecanicoPreferidoId = null;
        Long mecanicoAlocadoId = null;
        String recursosNecessarios = null;
        StatusAgendamento status = null;
        Boolean confirmadoCliente = null;
        LocalDateTime dataConfirmacao = null;
        MetodoConfirmacao metodoConfirmacao = null;
        Boolean lembreteEnviado = null;
        LocalDateTime dataLembrete = null;
        LocalDateTime chegadaCliente = null;
        LocalDateTime inicioAtendimento = null;
        LocalDateTime fimAtendimento = null;
        Integer avaliacaoAtendimento = null;
        String comentarioAvaliacao = null;
        Long ordemServicoGeradaId = null;
        BigDecimal valorEstimado = null;
        Long formaPagamentoPreferidaId = null;
        CanalAgendamento canalAgendamento = null;
        LocalDateTime dataCadastro = null;

        tipoAgendamentoId = entityTipoAgendamentoId( entity );
        tipoAgendamentoNome = entityTipoAgendamentoNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        numeroAgendamento = entity.getNumeroAgendamento();
        clienteId = entity.getClienteId();
        veiculoId = entity.getVeiculoId();
        dataAgendamento = entity.getDataAgendamento();
        horaInicio = entity.getHoraInicio();
        horaFim = entity.getHoraFim();
        duracaoEstimadaMinutos = entity.getDuracaoEstimadaMinutos();
        servicosSolicitados = entity.getServicosSolicitados();
        problemaRelatado = entity.getProblemaRelatado();
        observacoesCliente = entity.getObservacoesCliente();
        observacoesInternas = entity.getObservacoesInternas();
        mecanicoPreferidoId = entity.getMecanicoPreferidoId();
        mecanicoAlocadoId = entity.getMecanicoAlocadoId();
        recursosNecessarios = entity.getRecursosNecessarios();
        status = entity.getStatus();
        confirmadoCliente = entity.getConfirmadoCliente();
        dataConfirmacao = entity.getDataConfirmacao();
        metodoConfirmacao = entity.getMetodoConfirmacao();
        lembreteEnviado = entity.getLembreteEnviado();
        dataLembrete = entity.getDataLembrete();
        chegadaCliente = entity.getChegadaCliente();
        inicioAtendimento = entity.getInicioAtendimento();
        fimAtendimento = entity.getFimAtendimento();
        avaliacaoAtendimento = entity.getAvaliacaoAtendimento();
        comentarioAvaliacao = entity.getComentarioAvaliacao();
        ordemServicoGeradaId = entity.getOrdemServicoGeradaId();
        valorEstimado = entity.getValorEstimado();
        formaPagamentoPreferidaId = entity.getFormaPagamentoPreferidaId();
        canalAgendamento = entity.getCanalAgendamento();
        dataCadastro = entity.getDataCadastro();

        AgendamentoResponse agendamentoResponse = new AgendamentoResponse( id, empresaId, numeroAgendamento, clienteId, veiculoId, tipoAgendamentoId, tipoAgendamentoNome, dataAgendamento, horaInicio, horaFim, duracaoEstimadaMinutos, servicosSolicitados, problemaRelatado, observacoesCliente, observacoesInternas, mecanicoPreferidoId, mecanicoAlocadoId, recursosNecessarios, status, confirmadoCliente, dataConfirmacao, metodoConfirmacao, lembreteEnviado, dataLembrete, chegadaCliente, inicioAtendimento, fimAtendimento, avaliacaoAtendimento, comentarioAvaliacao, ordemServicoGeradaId, valorEstimado, formaPagamentoPreferidaId, canalAgendamento, dataCadastro );

        return agendamentoResponse;
    }

    protected TipoAgendamento agendamentoRequestToTipoAgendamento(AgendamentoRequest agendamentoRequest) {
        if ( agendamentoRequest == null ) {
            return null;
        }

        TipoAgendamento tipoAgendamento = new TipoAgendamento();

        tipoAgendamento.setId( agendamentoRequest.tipoAgendamentoId() );

        return tipoAgendamento;
    }

    private Long entityTipoAgendamentoId(Agendamento agendamento) {
        if ( agendamento == null ) {
            return null;
        }
        TipoAgendamento tipoAgendamento = agendamento.getTipoAgendamento();
        if ( tipoAgendamento == null ) {
            return null;
        }
        Long id = tipoAgendamento.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTipoAgendamentoNome(Agendamento agendamento) {
        if ( agendamento == null ) {
            return null;
        }
        TipoAgendamento tipoAgendamento = agendamento.getTipoAgendamento();
        if ( tipoAgendamento == null ) {
            return null;
        }
        String nome = tipoAgendamento.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
