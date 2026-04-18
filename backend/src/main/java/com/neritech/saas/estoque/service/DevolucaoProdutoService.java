package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import com.neritech.saas.estoque.dto.DevolucaoProdutoRequest;
import com.neritech.saas.estoque.dto.DevolucaoProdutoResponse;
import com.neritech.saas.estoque.mapper.DevolucaoProdutoMapper;
import com.neritech.saas.estoque.repository.DevolucaoProdutoRepository;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DevolucaoProdutoService {

    private final DevolucaoProdutoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final DevolucaoProdutoMapper mapper;

    public DevolucaoProdutoService(DevolucaoProdutoRepository repository,
            ProdutoRepository produtoRepository,
            DevolucaoProdutoMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public DevolucaoProdutoResponse create(DevolucaoProdutoRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        DevolucaoProduto entity = mapper.toEntity(request);
        entity.setProduto(produto);
        entity.setDataDevolucao(LocalDateTime.now());

        if (request.aprovadoPor() != null) {
            entity.setDataAprovacao(LocalDateTime.now());
        }

        DevolucaoProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DevolucaoProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("DevoluÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<DevolucaoProdutoResponse> findByProduto(Long produtoId, Pageable pageable) {
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DevolucaoProdutoResponse> findByMotivo(MotivoDevolucao motivo, Pageable pageable) {
        return repository.findByMotivoDevolucao(motivo, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DevolucaoProdutoResponse> findByOrdemServico(Long ordemServicoId, Pageable pageable) {
        return repository.findByOrdemServicoId(ordemServicoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public DevolucaoProdutoResponse update(Long id, DevolucaoProdutoRequest request) {
        DevolucaoProduto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DevoluÃ§Ã£o nÃ£o encontrada"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        if (request.aprovadoPor() != null && entity.getDataAprovacao() == null) {
            entity.setDataAprovacao(LocalDateTime.now());
        }

        DevolucaoProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("DevoluÃ§Ã£o nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
