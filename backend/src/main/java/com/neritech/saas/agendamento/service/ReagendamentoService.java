package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.Reagendamento;
import com.neritech.saas.agendamento.dto.ReagendamentoRequest;
import com.neritech.saas.agendamento.dto.ReagendamentoResponse;
import com.neritech.saas.agendamento.mapper.ReagendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.ReagendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReagendamentoService {

    private final ReagendamentoRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final ReagendamentoMapper mapper;

    @Transactional
    public ReagendamentoResponse criar(ReagendamentoRequest request) {
        Reagendamento reagendamento = mapper.toEntity(request);

        Agendamento agendamentoOriginal = agendamentoRepository.findById(request.agendamentoOriginalId())
                .orElseThrow(() -> new EntityNotFoundException("Agendamento original nÃ£o encontrado"));
        reagendamento.setAgendamentoOriginal(agendamentoOriginal);

        if (request.agendamentoNovoId() != null) {
            Agendamento agendamentoNovo = agendamentoRepository.findById(request.agendamentoNovoId())
                    .orElseThrow(() -> new EntityNotFoundException("Novo agendamento nÃ£o encontrado"));
            reagendamento.setAgendamentoNovo(agendamentoNovo);
        }

        Reagendamento salvo = repository.save(reagendamento);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public ReagendamentoResponse buscarPorId(Long id) {
        Reagendamento reagendamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reagendamento nÃ£o encontrado"));
        return mapper.toResponse(reagendamento);
    }

    @Transactional(readOnly = true)
    public List<ReagendamentoResponse> listarPorAgendamentoOriginal(Long agendamentoOriginalId) {
        return repository.findByAgendamentoOriginalId(agendamentoOriginalId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Reagendamento nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
