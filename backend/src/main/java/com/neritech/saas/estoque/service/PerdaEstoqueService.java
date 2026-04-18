package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.TipoPerda;
import com.neritech.saas.estoque.dto.PerdaEstoqueRequest;
import com.neritech.saas.estoque.dto.PerdaEstoqueResponse;
import com.neritech.saas.estoque.mapper.PerdaEstoqueMapper;
import com.neritech.saas.estoque.repository.*;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerdaEstoqueService {

    private final PerdaEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final LocalizacaoEstoqueRepository localizacaoRepository;
    private final PerdaEstoqueMapper mapper;

    public PerdaEstoqueService(PerdaEstoqueRepository repository,
            ProdutoRepository produtoRepository,
            LocalizacaoEstoqueRepository localizacaoRepository,
            PerdaEstoqueMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PerdaEstoqueResponse create(PerdaEstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        PerdaEstoque entity = mapper.toEntity(request);
        entity.setProduto(produto);

        if (request.localizacaoId() != null) {
            LocalizacaoEstoque localizacao = localizacaoRepository.findById(request.localizacaoId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada"));
            entity.setLocalizacao(localizacao);
        }

        if (request.aprovadoPor() != null) {
            entity.setDataAprovacao(LocalDateTime.now());
        }

        PerdaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PerdaEstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Perda nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<PerdaEstoqueResponse> findByProduto(Long produtoId, Pageable pageable) {
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PerdaEstoqueResponse> findByTipo(TipoPerda tipo, Pageable pageable) {
        return repository.findByTipoPerda(tipo, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public PerdaEstoqueResponse update(Long id, PerdaEstoqueRequest request) {
        PerdaEstoque entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perda nÃ£o encontrada"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        if (request.localizacaoId() != null) {
            LocalizacaoEstoque localizacao = localizacaoRepository.findById(request.localizacaoId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada"));
            entity.setLocalizacao(localizacao);
        } else {
            entity.setLocalizacao(null);
        }

        if (request.aprovadoPor() != null && entity.getDataAprovacao() == null) {
            entity.setDataAprovacao(LocalDateTime.now());
        }

        PerdaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Perda nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
