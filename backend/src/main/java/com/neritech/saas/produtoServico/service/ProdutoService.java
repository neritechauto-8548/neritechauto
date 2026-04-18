package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.estoque.domain.Estoque;
import com.neritech.saas.estoque.repository.EstoqueRepository;
import com.neritech.saas.estoque.repository.ItemInventarioRepository;
import com.neritech.saas.produtoServico.dto.ProdutoRequest;
import com.neritech.saas.produtoServico.dto.ProdutoResponse;
import com.neritech.saas.produtoServico.mapper.ProdutoMapper;
import com.neritech.saas.produtoServico.repository.CategoriaProdutoRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.produtoServico.repository.UnidadeMedidaRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaProdutoRepository categoriaRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final EstoqueRepository estoqueRepository;
    private final ItemInventarioRepository itemInventarioRepository;
    private final ProdutoMapper mapper;

    public ProdutoService(ProdutoRepository repository,
            CategoriaProdutoRepository categoriaRepository,
            UnidadeMedidaRepository unidadeMedidaRepository,
            EstoqueRepository estoqueRepository,
            ItemInventarioRepository itemInventarioRepository,
            ProdutoMapper mapper) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.estoqueRepository = estoqueRepository;
        this.itemInventarioRepository = itemInventarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ProdutoResponse create(ProdutoRequest request) {
        if (repository.existsByEmpresaIdAndCodigoInterno(request.empresaId(), request.codigoInterno())) {
            throw new IllegalArgumentException("JÃ¡ existe um produto com este cÃ³digo interno nesta empresa");
        }

        CategoriaProduto categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Categoria nÃ£o encontrada com ID: " + request.categoriaId()));
        }

        UnidadeMedida unidadeMedida = null;
        if (request.unidadeMedidaId() != null) {
            unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Unidade de medida nÃ£o encontrada com ID: " + request.unidadeMedidaId()));
        }

        Produto entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());
        entity.setCategoria(categoria);
        entity.setUnidadeMedida(unidadeMedida);

        Produto saved = repository.save(entity);

        if (request.quantidadeEstoque() != null) {
            Estoque estoque = new Estoque();
            estoque.setEmpresaId(saved.getEmpresaId());
            estoque.setProduto(saved);
            estoque.setQuantidadeAtual(request.quantidadeEstoque());
            estoque.setPrecoCustoLote(saved.getPrecoCusto() != null ? saved.getPrecoCusto() : java.math.BigDecimal.ZERO);
            estoqueRepository.save(estoque);
        }

        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> search(Long empresaId, String query, Pageable pageable) {
        return repository.search(empresaId, query, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ProdutoResponse update(Long id, ProdutoRequest request) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar a empresa do produto");
        }

        if (!entity.getCodigoInterno().equals(request.codigoInterno()) &&
                repository.existsByEmpresaIdAndCodigoInterno(request.empresaId(), request.codigoInterno())) {
            throw new IllegalArgumentException("JÃ¡ existe um produto com este cÃ³digo interno nesta empresa");
        }

        CategoriaProduto categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Categoria nÃ£o encontrada com ID: " + request.categoriaId()));
        }

        UnidadeMedida unidadeMedida = null;
        if (request.unidadeMedidaId() != null) {
            unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Unidade de medida nÃ£o encontrada com ID: " + request.unidadeMedidaId()));
        }

        mapper.updateEntityFromRequest(request, entity);
        entity.setCategoria(categoria);
        entity.setUnidadeMedida(unidadeMedida);

        Produto saved = repository.save(entity);

        if (request.quantidadeEstoque() != null) {
            Estoque estoque = estoqueRepository.findFirstByEmpresaIdAndProdutoId(saved.getEmpresaId(), saved.getId())
                    .orElseGet(() -> {
                        Estoque novo = new Estoque();
                        novo.setEmpresaId(saved.getEmpresaId());
                        novo.setProduto(saved);
                        novo.setPrecoCustoLote(saved.getPrecoCusto() != null ? saved.getPrecoCusto() : java.math.BigDecimal.ZERO);
                        return novo;
                    });
            estoque.setQuantidadeAtual(request.quantidadeEstoque());
            estoqueRepository.save(estoque);
        }

        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + id);
        }
        
        // Remove dependências no estoque para evitar violação de FK
        estoqueRepository.deleteByProdutoId(id);
        itemInventarioRepository.deleteByProdutoId(id);
        
        repository.deleteById(id);
    }
}
