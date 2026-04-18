package com.neritech.saas.produtoServico.service;

import com.neritech.saas.produtoServico.domain.Servico;
import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.dto.ServicoResponse;
import com.neritech.saas.produtoServico.mapper.ServicoMapper;
import com.neritech.saas.produtoServico.repository.ServicoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper mapper;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ServicoResponse create(ServicoRequest request) {
        Servico entity = mapper.toEntity(request);
        Servico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ServicoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponse> search(Long empresaId, String query, Pageable pageable) {
        return repository.search(empresaId, query, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ServicoResponse update(Long id, ServicoRequest request) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));

        mapper.updateEntityFromRequest(request, entity);
        Servico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
