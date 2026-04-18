package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.dto.CentroCustoRequest;
import com.neritech.saas.financeiro.dto.CentroCustoResponse;
import com.neritech.saas.financeiro.mapper.CentroCustoMapper;
import com.neritech.saas.financeiro.repository.CentroCustoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CentroCustoService {

    private final CentroCustoRepository repository;
    private final CentroCustoMapper mapper;

    @Transactional(readOnly = true)
    public Page<CentroCustoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CentroCustoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Centro de custo nÃ£o encontrado"));
    }

    @Transactional
    public CentroCustoResponse create(Long empresaId, CentroCustoRequest request) {
        CentroCusto entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (request.centroCustoPaiId() != null) {
            CentroCusto pai = repository.findById(request.centroCustoPaiId())
                    .orElseThrow(() -> new EntityNotFoundException("Centro de custo pai nÃ£o encontrado"));
            entity.setCentroCustoPai(pai);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CentroCustoResponse update(Long id, CentroCustoRequest request) {
        CentroCusto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Centro de custo nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.centroCustoPaiId() != null) {
            CentroCusto pai = repository.findById(request.centroCustoPaiId())
                    .orElseThrow(() -> new EntityNotFoundException("Centro de custo pai nÃ£o encontrado"));
            entity.setCentroCustoPai(pai);
        } else {
            entity.setCentroCustoPai(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        CentroCusto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Centro de custo nÃ£o encontrado"));
        repository.delete(entity);
    }
}
