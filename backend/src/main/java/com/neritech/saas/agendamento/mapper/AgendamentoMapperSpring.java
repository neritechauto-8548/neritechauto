package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Primary
public class AgendamentoMapperSpring implements AgendamentoMapper {
    @Override
    public Agendamento toEntity(AgendamentoRequest request) {
        if (request == null) {
            return null;
        }
        Agendamento a = new Agendamento();
        a.setEmpresaId(request.empresaId());
        a.setNumeroAgendamento(request.numeroAgendamento());
        a.setClienteId(request.clienteId());
        a.setVeiculoId(request.veiculoId());
        if (request.tipoAgendamentoId() != null) {
            TipoAgendamento tipo = new TipoAgendamento();
            tipo.setId(request.tipoAgendamentoId());
            a.setTipoAgendamento(tipo);
        }
        a.setDataAgendamento(request.dataAgendamento());
        a.setHoraInicio(request.horaInicio());
        a.setHoraFim(request.horaFim());
        a.setDuracaoEstimadaMinutos(request.duracaoEstimadaMinutos());
        a.setServicosSolicitados(request.servicosSolicitados());
        a.setProblemaRelatado(request.problemaRelatado());
        a.setObservacoesCliente(request.observacoesCliente());
        a.setObservacoesInternas(request.observacoesInternas());
        a.setMecanicoPreferidoId(request.mecanicoPreferidoId());
        a.setMecanicoAlocadoId(request.mecanicoAlocadoId());
        a.setRecursosNecessarios(request.recursosNecessarios());
        a.setStatus(request.status());
        a.setConfirmadoCliente(request.confirmadoCliente());
        a.setMetodoConfirmacao(request.metodoConfirmacao());
        a.setValorEstimado(request.valorEstimado());
        a.setFormaPagamentoPreferidaId(request.formaPagamentoPreferidaId());
        a.setCanalAgendamento(request.canalAgendamento());
        return a;
    }

    @Override
    public AgendamentoResponse toResponse(Agendamento entity) {
        if (entity == null) {
            return null;
        }
        Long tipoId = entity.getTipoAgendamento() != null ? entity.getTipoAgendamento().getId() : null;
        String tipoNome = entity.getTipoAgendamento() != null ? entity.getTipoAgendamento().getNome() : null;
        LocalDateTime dataCadastro = entity.getDataCadastro();
        return new AgendamentoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getNumeroAgendamento(),
                entity.getClienteId(),
                entity.getVeiculoId(),
                tipoId,
                tipoNome,
                entity.getDataAgendamento(),
                entity.getHoraInicio(),
                entity.getHoraFim(),
                entity.getDuracaoEstimadaMinutos(),
                entity.getServicosSolicitados(),
                entity.getProblemaRelatado(),
                entity.getObservacoesCliente(),
                entity.getObservacoesInternas(),
                entity.getMecanicoPreferidoId(),
                entity.getMecanicoAlocadoId(),
                entity.getRecursosNecessarios(),
                entity.getStatus(),
                entity.getConfirmadoCliente(),
                entity.getDataConfirmacao(),
                entity.getMetodoConfirmacao(),
                entity.getLembreteEnviado(),
                entity.getDataLembrete(),
                entity.getChegadaCliente(),
                entity.getInicioAtendimento(),
                entity.getFimAtendimento(),
                entity.getAvaliacaoAtendimento(),
                entity.getComentarioAvaliacao(),
                entity.getOrdemServicoGeradaId(),
                entity.getValorEstimado(),
                entity.getFormaPagamentoPreferidaId(),
                entity.getCanalAgendamento(),
                dataCadastro
        );
    }
}
