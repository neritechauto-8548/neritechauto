package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.CertificadoDigital;
import com.neritech.saas.fiscal.dto.CertificadoDigitalRequest;
import com.neritech.saas.fiscal.dto.CertificadoDigitalResponse;
import com.neritech.saas.fiscal.mapper.CertificadoDigitalMapper;
import com.neritech.saas.fiscal.repository.CertificadoDigitalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificadoDigitalService {

    private final CertificadoDigitalRepository repository;
    private final CertificadoDigitalMapper mapper;

    @Transactional(readOnly = true)
    public List<CertificadoDigitalResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CertificadoDigitalResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Certificado Digital nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public CertificadoDigitalResponse create(CertificadoDigitalRequest request) {
        // Aqui poderia ter lÃ³gica para validar o arquivo do certificado, extrair dados,
        // etc.
        CertificadoDigital entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CertificadoDigitalResponse update(Long id, CertificadoDigitalRequest request) {
        CertificadoDigital entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado Digital nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Certificado Digital nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
