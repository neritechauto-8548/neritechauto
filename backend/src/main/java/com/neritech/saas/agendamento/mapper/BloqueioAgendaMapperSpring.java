package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.BloqueioAgenda;
import com.neritech.saas.agendamento.dto.BloqueioAgendaRequest;
import com.neritech.saas.agendamento.dto.BloqueioAgendaResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class BloqueioAgendaMapperSpring implements BloqueioAgendaMapper {
    @Override
    public BloqueioAgenda toEntity(BloqueioAgendaRequest request) {
        if (request == null) {
            return null;
        }
        BloqueioAgenda b = new BloqueioAgenda();
        b.setEmpresaId(request.empresaId());
        b.setFuncionarioId(request.funcionarioId());
        b.setTipoBloqueio(request.tipoBloqueio());
        b.setDataInicio(request.dataInicio());
        b.setDataFim(request.dataFim());
        b.setHoraInicio(request.horaInicio());
        b.setHoraFim(request.horaFim());
        b.setRecorrente(Boolean.TRUE.equals(request.recorrente()));
        b.setDiasSemanaRecorrencia(request.diasSemanaRecorrencia());
        b.setMotivo(request.motivo());
        b.setAfetaTodosFuncionarios(Boolean.TRUE.equals(request.afetaTodosFuncionarios()));
        b.setFuncionariosAfetados(request.funcionariosAfetados());
        b.setObservacoes(request.observacoes());
        b.setAtivo(Boolean.TRUE.equals(request.ativo()));
        return b;
    }

    @Override
    public BloqueioAgendaResponse toResponse(BloqueioAgenda entity) {
        if (entity == null) {
            return null;
        }
        return new BloqueioAgendaResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getFuncionarioId(),
                entity.getTipoBloqueio(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getHoraInicio(),
                entity.getHoraFim(),
                entity.getRecorrente(),
                entity.getDiasSemanaRecorrencia(),
                entity.getMotivo(),
                entity.getAfetaTodosFuncionarios(),
                entity.getFuncionariosAfetados(),
                entity.getObservacoes(),
                entity.getAtivo(),
                entity.getDataCadastro()
        );
    }
}
