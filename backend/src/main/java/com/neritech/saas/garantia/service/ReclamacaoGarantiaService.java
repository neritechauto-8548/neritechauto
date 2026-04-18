package com.neritech.saas.garantia.service;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.ReclamacaoGarantia;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaResponse;
import com.neritech.saas.garantia.mapper.ReclamacaoGarantiaMapper;
import com.neritech.saas.garantia.repository.GarantiaRepository;
import com.neritech.saas.garantia.repository.ReclamacaoGarantiaRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para ReclamacaoGarantia
 */
@Service
@RequiredArgsConstructor
public class ReclamacaoGarantiaService {

    private final ReclamacaoGarantiaRepository repository;
    private final GarantiaRepository garantiaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ReclamacaoGarantiaMapper mapper;

    @Transactional(readOnly = true)
    public Page<ReclamacaoGarantiaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByGarantiaEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ReclamacaoGarantiaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ReclamaÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional
    public ReclamacaoGarantiaResponse create(ReclamacaoGarantiaRequest request) {
        ReclamacaoGarantia entity = mapper.toEntity(request);
        entity.setAbertaPor(1L); // TODO: Obter do contexto de seguranÃ§a

        Garantia garantia = garantiaRepository.findById(request.garantiaId())
                .orElseThrow(() -> new EntityNotFoundException("Garantia nÃ£o encontrada"));
        entity.setGarantia(garantia);

        if (request.tecnicoResponsavelId() != null) {
            Funcionario tecnico = funcionarioRepository.findById(request.tecnicoResponsavelId())
                    .orElseThrow(() -> new EntityNotFoundException("TÃ©cnico nÃ£o encontrado"));
            entity.setTecnicoResponsavel(tecnico);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ReclamacaoGarantiaResponse update(Long id, ReclamacaoGarantiaRequest request) {
        ReclamacaoGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReclamaÃ§Ã£o nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.tecnicoResponsavelId() != null) {
            Funcionario tecnico = funcionarioRepository.findById(request.tecnicoResponsavelId())
                    .orElseThrow(() -> new EntityNotFoundException("TÃ©cnico nÃ£o encontrado"));
            entity.setTecnicoResponsavel(tecnico);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        ReclamacaoGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReclamaÃ§Ã£o nÃ£o encontrada"));
        repository.delete(entity);
    }
}
