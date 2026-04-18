package com.neritech.saas.garantia.service;

import com.neritech.saas.garantia.domain.TipoGarantia;
import com.neritech.saas.garantia.dto.TipoGarantiaRequest;
import com.neritech.saas.garantia.dto.TipoGarantiaResponse;
import com.neritech.saas.garantia.mapper.TipoGarantiaMapper;
import com.neritech.saas.garantia.repository.TipoGarantiaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para TipoGarantia
 */
@Service
@RequiredArgsConstructor
public class TipoGarantiaService {

    private final TipoGarantiaRepository repository;
    private final TipoGarantiaMapper mapper;

    @Transactional(readOnly = true)
    public Page<TipoGarantiaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TipoGarantiaResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de garantia nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public List<TipoGarantiaResponse> findAtivos(Long empresaId) {
        return repository.findByEmpresaIdAndAtivo(empresaId, true).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TipoGarantiaResponse create(Long empresaId, TipoGarantiaRequest request) {
        TipoGarantia entity = mapper.toEntity(request);       
        entity.setCriadoPor(1L); // TODO: Obter do contexto de seguranÃ§a
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public TipoGarantiaResponse update(Long id, Long empresaId, TipoGarantiaRequest request) {
        TipoGarantia entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de garantia nÃ£o encontrado"));
        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        TipoGarantia entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de garantia nÃ£o encontrado"));
        repository.delete(entity);
    }
}
