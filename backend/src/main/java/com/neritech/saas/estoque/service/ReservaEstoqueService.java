package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.dto.ReservaEstoqueRequest;
import com.neritech.saas.estoque.dto.ReservaEstoqueResponse;
import com.neritech.saas.estoque.mapper.ReservaEstoqueMapper;
import com.neritech.saas.estoque.repository.ReservaEstoqueRepository;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaEstoqueService {

    private final ReservaEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ReservaEstoqueMapper mapper;

    public ReservaEstoqueService(ReservaEstoqueRepository repository,
            ProdutoRepository produtoRepository,
            ReservaEstoqueMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ReservaEstoqueResponse create(ReservaEstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        ReservaEstoque entity = mapper.toEntity(request);
        entity.setProduto(produto);
        entity.setDataReserva(LocalDateTime.now());

        ReservaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ReservaEstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Reserva nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<ReservaEstoqueResponse> findByProduto(Long produtoId, Pageable pageable) {
        return repository.findByProdutoId(produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReservaEstoqueResponse> findByStatus(StatusReserva status, Pageable pageable) {
        return repository.findByStatus(status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReservaEstoqueResponse> findByDocumento(String documentoTipo, Long documentoId, Pageable pageable) {
        return repository.findByDocumentoTipoAndDocumentoId(documentoTipo, documentoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ReservaEstoqueResponse update(Long id, ReservaEstoqueRequest request) {
        ReservaEstoque entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva nÃ£o encontrada"));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setProduto(produto);

        ReservaEstoque saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Reserva nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
