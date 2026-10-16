package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import com.neritech.saas.common.tenancy.TenantAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.ProdutoFornecedor;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorResponse;
import com.neritech.saas.produtoServico.mapper.ProdutoFornecedorMapper;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;

@Service
public class ProdutoFornecedorService {

    private final ProdutoFornecedorRepository repository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoFornecedorMapper mapper;

    public ProdutoFornecedorService(ProdutoFornecedorRepository repository,
            ProdutoRepository produtoRepository,
            FornecedorRepository fornecedorRepository,
            ProdutoFornecedorMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ProdutoFornecedorResponse create(ProdutoFornecedorRequest request) {
        TenantAccess.requireCurrent();
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + request.produtoId()));

        TenantAccess.require(produto.getEmpresaId());
        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));

        TenantAccess.require(fornecedor.getEmpresaId());
        if (!Boolean.TRUE.equals(fornecedor.getAtivo())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,
                    "Fornecedor inativo para novas cotações");
        }
        ProdutoFornecedor entity = mapper.toEntity(request);
        entity.setProduto(produto);
        entity.setFornecedor(fornecedor);

        ProdutoFornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProdutoFornecedorResponse findById(Long id) {
        return repository.findById(id)
                .map(p -> { TenantAccess.require(p.getFornecedor().getEmpresaId()); return mapper.toResponse(p); })
                .orElseThrow(
                        () -> new EntityNotFoundException("RelaÃ§Ã£o Produto-Fornecedor nÃ£o encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoFornecedorResponse> findByProduto(Long produtoId, Pageable pageable) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        TenantAccess.require(produto.getEmpresaId());
        if (!produtoRepository.existsById(produtoId)) {
            throw new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + produtoId);
        }
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoFornecedorResponse> findByFornecedor(Long fornecedorId, Pageable pageable) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));
        TenantAccess.require(fornecedor.getEmpresaId());
        if (!fornecedorRepository.existsById(fornecedorId)) {
            throw new EntityNotFoundException("Fornecedor nÃ£o encontrado com ID: " + fornecedorId);
        }
        return repository.findByFornecedorId(fornecedorId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ProdutoFornecedorResponse update(Long id, ProdutoFornecedorRequest request) {
        ProdutoFornecedor entity = repository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Relação Produto-Fornecedor não encontrada com ID: " + id));
        TenantAccess.require(entity.getFornecedor().getEmpresaId());

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + request.produtoId()));

        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));

        TenantAccess.require(produto.getEmpresaId());
        TenantAccess.require(fornecedor.getEmpresaId());
        if (!Boolean.TRUE.equals(fornecedor.getAtivo())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,
                    "Fornecedor inativo para novas cotações");
        }
        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);
        entity.setFornecedor(fornecedor);

        ProdutoFornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        ProdutoFornecedor relation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relação Produto-Fornecedor não encontrada"));
        TenantAccess.require(relation.getFornecedor().getEmpresaId());
        repository.delete(relation);
    }
}
