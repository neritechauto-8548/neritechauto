package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.Estoque;
import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import com.neritech.saas.estoque.dto.EstoqueRequest;
import com.neritech.saas.estoque.dto.EstoqueResponse;
import com.neritech.saas.estoque.mapper.EstoqueMapper;
import com.neritech.saas.estoque.repository.EstoqueRepository;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final EstoqueMapper mapper;

    public EstoqueService(EstoqueRepository repository,
            ProdutoRepository produtoRepository,
            FornecedorRepository fornecedorRepository,
            EstoqueMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.mapper = mapper;
    }

    @Transactional
    public EstoqueResponse create(EstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + request.produtoId()));

        Estoque entity = mapper.toEntity(request);
        entity.setProduto(produto);

        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));
            entity.setFornecedor(fornecedor);
        }

        Estoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public EstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Estoque nÃ£o encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<EstoqueResponse> findByEmpresa(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<EstoqueResponse> findByEmpresaAndStatus(Long empresaId, StatusEstoque status, Pageable pageable) {
        return repository.findByEmpresaIdAndStatus(empresaId, status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<EstoqueResponse> findByEmpresaAndProduto(Long empresaId, Long produtoId, Pageable pageable) {
        return repository.findByEmpresaIdAndProdutoId(empresaId, produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public EstoqueResponse update(Long id, EstoqueRequest request) {
        Estoque entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque nÃ£o encontrado com ID: " + id));

        mapper.updateEntityFromRequest(request, entity);

        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));
            entity.setFornecedor(fornecedor);
        } else {
            entity.setFornecedor(null);
        }

        Estoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Estoque nÃ£o encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
