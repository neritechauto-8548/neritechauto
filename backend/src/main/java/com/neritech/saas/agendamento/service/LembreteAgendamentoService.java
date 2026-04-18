package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.LembreteAgendamento;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoRequest;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoResponse;
import com.neritech.saas.agendamento.mapper.LembreteAgendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.LembreteAgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LembreteAgendamentoService {

    private final LembreteAgendamentoRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final LembreteAgendamentoMapper mapper;

    @Transactional
    public LembreteAgendamentoResponse criar(LembreteAgendamentoRequest request) {
        LembreteAgendamento lembrete = mapper.toEntity(request);

        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃ£o encontrado"));
        lembrete.setAgendamento(agendamento);

        LembreteAgendamento salvo = repository.save(lembrete);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public LembreteAgendamentoResponse buscarPorId(Long id) {
        LembreteAgendamento lembrete = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lembrete nÃ£o encontrado"));
        return mapper.toResponse(lembrete);
    }

    @Transactional(readOnly = true)
    public List<LembreteAgendamentoResponse> listarPorAgendamento(Long agendamentoId) {
        return repository.findByAgendamentoId(agendamentoId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lembrete nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
