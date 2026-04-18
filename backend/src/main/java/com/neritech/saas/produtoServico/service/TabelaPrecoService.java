package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.TabelaPreco;
import com.neritech.saas.produtoServico.dto.TabelaPrecoRequest;
import com.neritech.saas.produtoServico.dto.TabelaPrecoResponse;
import com.neritech.saas.produtoServico.mapper.TabelaPrecoMapper;
import com.neritech.saas.produtoServico.repository.TabelaPrecoRepository;

@Service
public class TabelaPrecoService {

    private final TabelaPrecoRepository repository;
    private final TabelaPrecoMapper mapper;

    public TabelaPrecoService(TabelaPrecoRepository repository, TabelaPrecoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TabelaPrecoResponse create(TabelaPrecoRequest request) {
        TabelaPreco entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());

        TabelaPreco saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TabelaPrecoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tabela de preÃ§o nÃ£o encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<TabelaPrecoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public TabelaPrecoResponse update(Long id, TabelaPrecoRequest request) {
        TabelaPreco entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tabela de preÃ§o nÃ£o encontrada com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar a empresa da tabela de preÃ§o");
        }

        mapper.updateEntityFromRequest(request, entity);
        TabelaPreco saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tabela de preÃ§o nÃ£o encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
