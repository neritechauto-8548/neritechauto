package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.CondicaoPagamento;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoRequest;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoResponse;
import com.neritech.saas.financeiro.mapper.CondicaoPagamentoMapper;
import com.neritech.saas.financeiro.repository.CondicaoPagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CondicaoPagamentoService {

    private final CondicaoPagamentoRepository repository;
    private final CondicaoPagamentoMapper mapper;

    @Transactional(readOnly = true)
    public Page<CondicaoPagamentoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CondicaoPagamentoResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("CondiÃ§Ã£o de pagamento nÃ£o encontrada"));
    }

    @Transactional
    public CondicaoPagamentoResponse create(Long empresaId, CondicaoPagamentoRequest request) {
        CondicaoPagamento entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CondicaoPagamentoResponse update(Long id, Long empresaId, CondicaoPagamentoRequest request) {
        CondicaoPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("CondiÃ§Ã£o de pagamento nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        CondicaoPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("CondiÃ§Ã£o de pagamento nÃ£o encontrada"));
        repository.delete(entity);
    }
}
