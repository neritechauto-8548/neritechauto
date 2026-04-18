package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import com.neritech.saas.ordemservico.mapper.DiagnosticoMapper;
import com.neritech.saas.ordemservico.repository.DiagnosticoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosticoService {

    private final DiagnosticoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final DiagnosticoMapper mapper;

    public DiagnosticoService(DiagnosticoRepository repository, OrdemServicoRepository ordemServicoRepository,
            DiagnosticoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    public DiagnosticoResponse create(DiagnosticoRequest request) {
        Diagnostico entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServicoRepository.findById(request.ordemServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de ServiÃ§o nÃ£o encontrada")));
        Diagnostico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DiagnosticoResponse findById(Long id) {
        Diagnostico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiagnÃ³stico nÃ£o encontrado"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> findByOrdemServicoId(Long ordemServicoId) {
        return repository.findByOrdemServicoId(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public DiagnosticoResponse update(Long id, DiagnosticoRequest request) {
        Diagnostico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiagnÃ³stico nÃ£o encontrado"));
        mapper.updateEntityFromRequest(request, entity);
        Diagnostico updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("DiagnÃ³stico nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
