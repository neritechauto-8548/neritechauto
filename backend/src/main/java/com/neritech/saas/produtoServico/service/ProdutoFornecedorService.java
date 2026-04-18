package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
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
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + request.produtoId()));

        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));

        ProdutoFornecedor entity = mapper.toEntity(request);
        entity.setProduto(produto);
        entity.setFornecedor(fornecedor);

        ProdutoFornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProdutoFornecedorResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("RelaÃ§Ã£o Produto-Fornecedor nÃ£o encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoFornecedorResponse> findByProduto(Long produtoId, Pageable pageable) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + produtoId);
        }
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoFornecedorResponse> findByFornecedor(Long fornecedorId, Pageable pageable) {
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
                        () -> new EntityNotFoundException("RelaÃ§Ã£o Produto-Fornecedor nÃ£o encontrada com ID: " + id));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + request.produtoId()));

        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor nÃ£o encontrado com ID: " + request.fornecedorId()));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);
        entity.setFornecedor(fornecedor);

        ProdutoFornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("RelaÃ§Ã£o Produto-Fornecedor nÃ£o encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
