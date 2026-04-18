package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.RecursoAgenda;
import com.neritech.saas.agendamento.dto.RecursoAgendaRequest;
import com.neritech.saas.agendamento.dto.RecursoAgendaResponse;
import com.neritech.saas.agendamento.mapper.RecursoAgendaMapper;
import com.neritech.saas.agendamento.repository.RecursoAgendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoAgendaService {

    private final RecursoAgendaRepository repository;
    private final RecursoAgendaMapper mapper;

    @Transactional
    public RecursoAgendaResponse criar(RecursoAgendaRequest request) {
        RecursoAgenda recurso = mapper.toEntity(request);
        recurso.setCriadoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a
        RecursoAgenda salvo = repository.save(recurso);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public RecursoAgendaResponse buscarPorId(Long id) {
        RecursoAgenda recurso = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso nÃƒÂ£o encontrado"));
        return mapper.toResponse(recurso);
    }

    @Transactional(readOnly = true)
    public List<RecursoAgendaResponse> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Recurso nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}
