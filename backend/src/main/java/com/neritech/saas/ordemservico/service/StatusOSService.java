package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.dto.StatusOSRequest;
import com.neritech.saas.ordemservico.dto.StatusOSResponse;
import com.neritech.saas.ordemservico.mapper.StatusOSMapper;
import com.neritech.saas.ordemservico.repository.StatusOSRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatusOSService {

    private final StatusOSRepository repository;
    private final StatusOSMapper mapper;

    public StatusOSService(StatusOSRepository repository, StatusOSMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StatusOSResponse create(StatusOSRequest request) {
        if (repository.existsByEmpresaIdAndCodigo(request.empresaId(), request.codigo())) {
            throw new IllegalArgumentException("CÃ³digo de status jÃ¡ existe para esta empresa");
        }
        StatusOS entity = mapper.toEntity(request);
        StatusOS saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StatusOSResponse findById(Long id) {
        StatusOS entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status nÃ£o encontrado"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<StatusOSResponse> findByEmpresaId(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    public StatusOSResponse update(Long id, StatusOSRequest request) {
        StatusOS entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status nÃ£o encontrado"));
        mapper.updateEntityFromRequest(request, entity);
        StatusOS updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Status nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
