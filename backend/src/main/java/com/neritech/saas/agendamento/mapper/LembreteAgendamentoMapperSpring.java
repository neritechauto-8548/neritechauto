package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.LembreteAgendamento;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoRequest;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class LembreteAgendamentoMapperSpring implements LembreteAgendamentoMapper {
    @Override
    public LembreteAgendamento toEntity(LembreteAgendamentoRequest request) {
        if (request == null) {
            return null;
        }
        LembreteAgendamento l = new LembreteAgendamento();
        Agendamento ag = new Agendamento();
        ag.setId(request.agendamentoId());
        l.setAgendamento(ag);
        l.setTipoLembrete(request.tipoLembrete());
        l.setDestinatario(request.destinatario());
        l.setAssunto(request.assunto());
        l.setMensagem(request.mensagem());
        l.setDataAgendamentoEnvio(request.dataAgendamentoEnvio());
        l.setStatusEnvio(request.statusEnvio());
        l.setTentativasEnvio(request.tentativasEnvio());
        l.setErroEnvio(request.erroEnvio());
        l.setRespostaCliente(request.respostaCliente());
        l.setDataResposta(request.dataResposta());
        l.setCustoEnvio(request.custoEnvio());
        l.setTemplateUsado(request.templateUsado());
        l.setVariaveisTemplate(request.variaveisTemplate());
        l.setAutomatico(request.automatico());
        return l;
    }

    @Override
    public LembreteAgendamentoResponse toResponse(LembreteAgendamento entity) {
        if (entity == null) {
            return null;
        }
        Long agendamentoId = entity.getAgendamento() != null ? entity.getAgendamento().getId() : null;
        String numeroAgendamento = entity.getAgendamento() != null ? entity.getAgendamento().getNumeroAgendamento() : null;
        return new LembreteAgendamentoResponse(
                entity.getId(),
                agendamentoId,
                numeroAgendamento,
                entity.getTipoLembrete(),
                entity.getDestinatario(),
                entity.getAssunto(),
                entity.getMensagem(),
                entity.getDataAgendamentoEnvio(),
                entity.getDataEnvio(),
                entity.getStatusEnvio(),
                entity.getTentativasEnvio(),
                entity.getErroEnvio(),
                entity.getRespostaCliente(),
                entity.getDataResposta(),
                entity.getCustoEnvio(),
                entity.getTemplateUsado(),
                entity.getVariaveisTemplate(),
                entity.getAutomatico(),
                entity.getDataCadastro()
        );
    }
}
