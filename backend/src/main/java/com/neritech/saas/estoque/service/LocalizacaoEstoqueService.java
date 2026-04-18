package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueResponse;
import com.neritech.saas.estoque.mapper.LocalizacaoEstoqueMapper;
import com.neritech.saas.estoque.repository.LocalizacaoEstoqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalizacaoEstoqueService {

    private final LocalizacaoEstoqueRepository repository;
    private final LocalizacaoEstoqueMapper mapper;

    public LocalizacaoEstoqueService(LocalizacaoEstoqueRepository repository,
            LocalizacaoEstoqueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public LocalizacaoEstoqueResponse create(LocalizacaoEstoqueRequest request) {
        if (repository.existsByEmpresaIdAndCodigo(request.empresaId(), request.codigo())) {
            throw new IllegalArgumentException(
                    "JÃ¡ existe uma localizaÃ§Ã£o com o cÃ³digo " + request.codigo() + " para esta empresa");
        }

        LocalizacaoEstoque entity = mapper.toEntity(request);
        LocalizacaoEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LocalizacaoEstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<LocalizacaoEstoqueResponse> findByEmpresa(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<LocalizacaoEstoqueResponse> findByEmpresaAtivas(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaIdAndAtivoTrue(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public LocalizacaoEstoqueResponse update(Long id, LocalizacaoEstoqueRequest request) {
        LocalizacaoEstoque entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar o ID da empresa");
        }

        if (!entity.getCodigo().equals(request.codigo()) &&
                repository.existsByEmpresaIdAndCodigo(request.empresaId(), request.codigo())) {
            throw new IllegalArgumentException(
                    "JÃ¡ existe uma localizaÃ§Ã£o com o cÃ³digo " + request.codigo() + " para esta empresa");
        }

        mapper.updateEntityFromRequest(request, entity);
        LocalizacaoEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
