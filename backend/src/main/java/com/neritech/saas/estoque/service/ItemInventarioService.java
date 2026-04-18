package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import com.neritech.saas.estoque.dto.ItemInventarioRequest;
import com.neritech.saas.estoque.dto.ItemInventarioResponse;
import com.neritech.saas.estoque.mapper.ItemInventarioMapper;
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
public class ItemInventarioService {

    private final ItemInventarioRepository repository;
    private final InventarioRepository inventarioRepository;
    private final ProdutoRepository produtoRepository;
    private final LocalizacaoEstoqueRepository localizacaoRepository;
    private final ItemInventarioMapper mapper;

    public ItemInventarioService(ItemInventarioRepository repository,
            InventarioRepository inventarioRepository,
            ProdutoRepository produtoRepository,
            LocalizacaoEstoqueRepository localizacaoRepository,
            ItemInventarioMapper mapper) {
        this.repository = repository;
        this.inventarioRepository = inventarioRepository;
        this.produtoRepository = produtoRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ItemInventarioResponse create(ItemInventarioRequest request) {
        Inventario inventario = inventarioRepository.findById(request.inventarioId())
                .orElseThrow(() -> new EntityNotFoundException("InventÃ¡rio nÃ£o encontrado"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        ItemInventario entity = mapper.toEntity(request);
        entity.setInventario(inventario);
        entity.setProduto(produto);

        if (request.localizacaoId() != null) {
            LocalizacaoEstoque localizacao = localizacaoRepository.findById(request.localizacaoId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada"));
            entity.setLocalizacao(localizacao);
        }

        if (request.quantidadeContada() != null) {
            entity.setDiferenca(request.quantidadeContada().subtract(request.quantidadeSistema()));
            entity.setDataContagem(LocalDateTime.now());
        }

        if (request.valorUnitario() != null) {
            entity.setValorTotalSistema(request.quantidadeSistema().multiply(request.valorUnitario()));
            if (request.quantidadeContada() != null) {
                entity.setValorTotalContado(request.quantidadeContada().multiply(request.valorUnitario()));
                entity.setDiferencaValor(entity.getValorTotalContado().subtract(entity.getValorTotalSistema()));
            }
        }

        ItemInventario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItemInventarioResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Item de inventÃ¡rio nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<ItemInventarioResponse> findByInventario(Long inventarioId, Pageable pageable) {
        return repository.findByInventarioId(inventarioId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ItemInventarioResponse> findByInventarioAndStatus(Long inventarioId, StatusItemInventario status,
            Pageable pageable) {
        return repository.findByInventarioIdAndStatus(inventarioId, status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ItemInventarioResponse update(Long id, ItemInventarioRequest request) {
        ItemInventario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de inventÃ¡rio nÃ£o encontrado"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        if (request.localizacaoId() != null) {
            LocalizacaoEstoque localizacao = localizacaoRepository.findById(request.localizacaoId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o nÃ£o encontrada"));
            entity.setLocalizacao(localizacao);
        }

        if (request.quantidadeContada() != null) {
            entity.setDiferenca(request.quantidadeContada().subtract(request.quantidadeSistema()));
            if (entity.getDataContagem() == null) {
                entity.setDataContagem(LocalDateTime.now());
            }
        }

        if (request.valorUnitario() != null) {
            entity.setValorTotalSistema(request.quantidadeSistema().multiply(request.valorUnitario()));
            if (request.quantidadeContada() != null) {
                entity.setValorTotalContado(request.quantidadeContada().multiply(request.valorUnitario()));
                entity.setDiferencaValor(entity.getValorTotalContado().subtract(entity.getValorTotalSistema()));
            }
        }

        ItemInventario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item de inventÃ¡rio nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
