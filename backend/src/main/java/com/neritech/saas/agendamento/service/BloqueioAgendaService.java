package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.BloqueioAgenda;
import com.neritech.saas.agendamento.dto.BloqueioAgendaRequest;
import com.neritech.saas.agendamento.dto.BloqueioAgendaResponse;
import com.neritech.saas.agendamento.mapper.BloqueioAgendaMapper;
import com.neritech.saas.agendamento.repository.BloqueioAgendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloqueioAgendaService {

    private final BloqueioAgendaRepository repository;
    private final BloqueioAgendaMapper mapper;

    @Transactional
    public BloqueioAgendaResponse criar(BloqueioAgendaRequest request) {
        BloqueioAgenda bloqueio = mapper.toEntity(request);
        bloqueio.setCriadoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a
        BloqueioAgenda salvo = repository.save(bloqueio);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public BloqueioAgendaResponse buscarPorId(Long id) {
        BloqueioAgenda bloqueio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bloqueio nÃƒÂ£o encontrado"));
        return mapper.toResponse(bloqueio);
    }

    @Transactional(readOnly = true)
    public List<BloqueioAgendaResponse> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Bloqueio nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}
