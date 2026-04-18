package com.neritech.saas.fiscal.service;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.fiscal.domain.CertificadoDigital;
import com.neritech.saas.fiscal.domain.ConfiguracaoNfe;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeRequest;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeResponse;
import com.neritech.saas.fiscal.mapper.ConfiguracaoNfeMapper;
import com.neritech.saas.fiscal.repository.CertificadoDigitalRepository;
import com.neritech.saas.fiscal.repository.ConfiguracaoNfeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracaoNfeService {

    private final ConfiguracaoNfeRepository repository;
    private final CertificadoDigitalRepository certificadoDigitalRepository;
    private final ConfiguracaoNfeMapper mapper;

    @Transactional(readOnly = true)
    public List<ConfiguracaoNfeResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConfiguracaoNfeResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o NFe nÃ£o encontrada com id: " + id));
    }

    @Transactional
    public ConfiguracaoNfeResponse create(ConfiguracaoNfeRequest request) {
        if (repository.findByAmbiente(request.ambiente()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe uma configuraÃ§Ã£o para este ambiente.");
        }

        ConfiguracaoNfe entity = mapper.toEntity(request);

        if (request.certificadoDigitalId() != null) {
            CertificadoDigital certificado = certificadoDigitalRepository.findById(request.certificadoDigitalId())
                    .orElseThrow(() -> new EntityNotFoundException("Certificado Digital nÃ£o encontrado"));
            entity.setCertificadoDigital(certificado);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ConfiguracaoNfeResponse update(Long id, ConfiguracaoNfeRequest request) {
        ConfiguracaoNfe entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o NFe nÃ£o encontrada com id: " + id));

        mapper.updateEntityFromRequest(request, entity);

        if (request.certificadoDigitalId() != null) {
            CertificadoDigital certificado = certificadoDigitalRepository.findById(request.certificadoDigitalId())
                    .orElseThrow(() -> new EntityNotFoundException("Certificado Digital nÃ£o encontrado"));
            entity.setCertificadoDigital(certificado);
        } else {
            entity.setCertificadoDigital(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o NFe nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
