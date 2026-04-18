package com.neritech.saas.produtoServico.service;

import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import com.neritech.saas.produtoServico.mapper.PedidoFornecedorMapper;
import com.neritech.saas.produtoServico.repository.PedidoFornecedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoFornecedorService {

    private final PedidoFornecedorRepository repository;
    private final PedidoFornecedorMapper mapper;

    public PedidoFornecedorService(PedidoFornecedorRepository repository, PedidoFornecedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PedidoFornecedorResponse create(PedidoFornecedorRequest request) {
        PedidoFornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());
        entity.setFornecedorId(request.fornecedorId());
        entity.setNumeroPedido(repository.nextNumeroPedido(request.empresaId()));
        PedidoFornecedor saved = repository.save(entity);
        // Re-fetch com fornecedor para popular nomeFornecedor no response
        return mapper.toResponse(repository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado após salvar")));
    }

    @Transactional(readOnly = true)
    public PedidoFornecedorResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<PedidoFornecedorResponse> findAll(Long empresaId, String termo, Pageable pageable) {
        if (termo != null && !termo.isBlank()) {
            return repository.findByEmpresaIdAndTermo(empresaId, termo, pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    @Transactional
    public PedidoFornecedorResponse update(Long id, PedidoFornecedorRequest request) {
        PedidoFornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
        mapper.updateEntityFromRequest(request, entity);
        entity.setFornecedorId(request.fornecedorId());
        PedidoFornecedor saved = repository.save(entity);
        return mapper.toResponse(repository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado após atualizar")));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
