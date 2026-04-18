package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.Orcamento;
import com.neritech.saas.ordemservico.dto.OrcamentoRequest;
import com.neritech.saas.ordemservico.dto.OrcamentoResponse;
import com.neritech.saas.ordemservico.mapper.OrcamentoMapper;
import com.neritech.saas.ordemservico.repository.OrcamentoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrcamentoService {

    private final OrcamentoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final OrcamentoMapper mapper;

    public OrcamentoService(OrcamentoRepository repository, OrdemServicoRepository ordemServicoRepository,
            OrcamentoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    public OrcamentoResponse create(OrcamentoRequest request) {
        if (request.numeroOrcamento() != null && repository.existsByNumeroOrcamento(request.numeroOrcamento())) {
            throw new IllegalArgumentException("NÃºmero de orÃ§amento jÃ¡ existe");
        }

        Orcamento entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServicoRepository.findById(request.ordemServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de ServiÃ§o nÃ£o encontrada")));
        Orcamento saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrcamentoResponse findById(Long id) {
        Orcamento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrÃ§amento nÃ£o encontrado"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponse> findByOrdemServicoId(Long ordemServicoId) {
        return repository.findByOrdemServicoId(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public OrcamentoResponse update(Long id, OrcamentoRequest request) {
        Orcamento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrÃ§amento nÃ£o encontrado"));
        mapper.updateEntityFromRequest(request, entity);
        Orcamento updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("OrÃ§amento nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
