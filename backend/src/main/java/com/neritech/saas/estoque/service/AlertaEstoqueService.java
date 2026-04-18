package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import com.neritech.saas.estoque.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.dto.AlertaEstoqueRequest;
import com.neritech.saas.estoque.dto.AlertaEstoqueResponse;
import com.neritech.saas.estoque.mapper.AlertaEstoqueMapper;
import com.neritech.saas.estoque.repository.AlertaEstoqueRepository;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaEstoqueService {

    private final AlertaEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final AlertaEstoqueMapper mapper;

    public AlertaEstoqueService(AlertaEstoqueRepository repository,
            ProdutoRepository produtoRepository,
            AlertaEstoqueMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AlertaEstoqueResponse create(AlertaEstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        AlertaEstoque entity = mapper.toEntity(request);
        entity.setProduto(produto);

        AlertaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AlertaEstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Alerta nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<AlertaEstoqueResponse> findByEmpresa(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlertaEstoqueResponse> findByEmpresaAndStatus(Long empresaId, StatusAlerta status, Pageable pageable) {
        return repository.findByEmpresaIdAndStatus(empresaId, status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlertaEstoqueResponse> findByEmpresaAndTipo(Long empresaId, TipoAlerta tipo, Pageable pageable) {
        return repository.findByEmpresaIdAndTipoAlerta(empresaId, tipo, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public AlertaEstoqueResponse update(Long id, AlertaEstoqueRequest request) {
        AlertaEstoque entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta nÃ£o encontrado"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        AlertaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Alerta nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
