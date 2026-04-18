package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.Nfe;
import com.neritech.saas.financeiro.dto.NfeRequest;
import com.neritech.saas.financeiro.dto.NfeResponse;
import com.neritech.saas.financeiro.mapper.NfeMapper;
import com.neritech.saas.financeiro.repository.FaturaRepository;
import com.neritech.saas.financeiro.repository.NfeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NfeService {

    private final NfeRepository repository;
    private final FaturaRepository faturaRepository;
    private final NfeMapper mapper;

    @Transactional(readOnly = true)
    public Page<NfeResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public NfeResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("NFe nÃ£o encontrada"));
    }

    @Transactional
    public NfeResponse create(Long empresaId, NfeRequest request) {
        Nfe entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (request.faturaId() != null) {
            Fatura fatura = faturaRepository.findByIdAndEmpresaId(request.faturaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Fatura nÃ£o encontrada"));
            entity.setFatura(fatura);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public NfeResponse update(Long id, Long empresaId, NfeRequest request) {
        Nfe entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("NFe nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.faturaId() != null) {
            Fatura fatura = faturaRepository.findByIdAndEmpresaId(request.faturaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Fatura nÃ£o encontrada"));
            entity.setFatura(fatura);
        } else {
            entity.setFatura(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        Nfe entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("NFe nÃ£o encontrada"));
        repository.delete(entity);
    }
}
