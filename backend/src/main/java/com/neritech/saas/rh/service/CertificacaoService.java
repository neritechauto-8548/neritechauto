package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Certificacao;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.CertificacaoMapper;
import com.neritech.saas.rh.repository.CertificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificacaoService {
    private final CertificacaoRepository repository;
    private final CertificacaoMapper mapper;

    public List<CertificacaoResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public CertificacaoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("CertificaÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional
    public CertificacaoResponse create(CertificacaoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public CertificacaoResponse update(Long id, CertificacaoRequest request) {
        Certificacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CertificaÃ§Ã£o nÃ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("CertificaÃ§Ã£o nÃ£o encontrada");
        repository.deleteById(id);
    }
}
