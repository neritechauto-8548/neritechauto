package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.LoteProduto;
import com.neritech.saas.estoque.dto.LoteProdutoRequest;
import com.neritech.saas.estoque.dto.LoteProdutoResponse;
import com.neritech.saas.estoque.mapper.LoteProdutoMapper;
import com.neritech.saas.estoque.repository.LoteProdutoRepository;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoteProdutoService {

    private final LoteProdutoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final LoteProdutoMapper mapper;

    public LoteProdutoService(LoteProdutoRepository repository,
            ProdutoRepository produtoRepository,
            LoteProdutoMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public LoteProdutoResponse create(LoteProdutoRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        LoteProduto entity = mapper.toEntity(request);
        entity.setProduto(produto);

        LoteProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LoteProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Lote nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<LoteProdutoResponse> findByProduto(Long produtoId, Pageable pageable) {
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<LoteProdutoResponse> findByNumeroLote(String numeroLote, Pageable pageable) {
        return repository.findByNumeroLote(numeroLote, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public LoteProdutoResponse update(Long id, LoteProdutoRequest request) {
        LoteProduto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote nÃ£o encontrado"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        LoteProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lote nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
