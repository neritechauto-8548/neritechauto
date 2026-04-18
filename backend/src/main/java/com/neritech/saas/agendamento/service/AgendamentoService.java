package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import com.neritech.saas.agendamento.mapper.AgendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para Agendamento
 */
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final TipoAgendamentoRepository tipoAgendamentoRepository;
    private final AgendamentoMapper mapper;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Agendamento agendamento = mapper.toEntity(request);

        // Buscar tipo de agendamento se fornecido
        if (request.tipoAgendamentoId() != null) {
            TipoAgendamento tipoAgendamento = tipoAgendamentoRepository.findById(request.tipoAgendamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de agendamento não encontrado"));
            agendamento.setTipoAgendamento(tipoAgendamento);
        }

        if (agendamento.getNumeroAgendamento() == null || agendamento.getNumeroAgendamento().isBlank()) {
            agendamento.setNumeroAgendamento("AGD-" + System.currentTimeMillis());
        }

        if (agendamento.getDuracaoEstimadaMinutos() == null) {
            agendamento.setDuracaoEstimadaMinutos(60);
        }

        agendamento.setAgendadoPor(1L); // TODO: Obter do contexto de segurança

        Agendamento salvo = repository.save(agendamento);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃƒÂ£o encontrado"));
        return mapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃƒÂ£o encontrado"));

        // Atualizar campos
        agendamento.setDataAgendamento(request.dataAgendamento());
        agendamento.setHoraInicio(request.horaInicio());
        agendamento.setHoraFim(request.horaFim());
        agendamento.setStatus(request.status());
        agendamento.setObservacoesInternas(request.observacoesInternas());
        agendamento.setMecanicoAlocadoId(request.mecanicoAlocadoId());
        agendamento.setAtualizadoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a

        Agendamento atualizado = repository.save(agendamento);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Agendamento nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}
