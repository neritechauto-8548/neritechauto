package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.DisponibilidadeAgenda;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaRequest;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaResponse;
import com.neritech.saas.agendamento.mapper.DisponibilidadeAgendaMapper;
import com.neritech.saas.agendamento.repository.DisponibilidadeAgendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadeAgendaService {

    private final DisponibilidadeAgendaRepository repository;
    private final DisponibilidadeAgendaMapper mapper;

    @Transactional
    public DisponibilidadeAgendaResponse criar(DisponibilidadeAgendaRequest request) {
        DisponibilidadeAgenda disponibilidade = mapper.toEntity(request);
        disponibilidade.setCriadoPor(1L); // TODO: Obter do contexto de seguranÃ§a
        DisponibilidadeAgenda salvo = repository.save(disponibilidade);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public DisponibilidadeAgendaResponse buscarPorId(Long id) {
        DisponibilidadeAgenda disponibilidade = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disponibilidade nÃ£o encontrada"));
        return mapper.toResponse(disponibilidade);
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadeAgendaResponse> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Disponibilidade nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
