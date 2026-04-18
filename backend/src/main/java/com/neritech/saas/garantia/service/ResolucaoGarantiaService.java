package com.neritech.saas.garantia.service;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.ReclamacaoGarantia;
import com.neritech.saas.garantia.domain.ResolucaoGarantia;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaResponse;
import com.neritech.saas.garantia.mapper.ResolucaoGarantiaMapper;
import com.neritech.saas.garantia.repository.GarantiaRepository;
import com.neritech.saas.garantia.repository.ReclamacaoGarantiaRepository;
import com.neritech.saas.garantia.repository.ResolucaoGarantiaRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para ResolucaoGarantia
 */
@Service
@RequiredArgsConstructor
public class ResolucaoGarantiaService {

    private final ResolucaoGarantiaRepository repository;
    private final ReclamacaoGarantiaRepository reclamacaoRepository;
    private final GarantiaRepository garantiaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ResolucaoGarantiaMapper mapper;

    @Transactional(readOnly = true)
    public ResolucaoGarantiaResponse findByReclamacaoId(Long reclamacaoId) {
        return repository.findByReclamacaoId(reclamacaoId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ResoluÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public ResolucaoGarantiaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ResoluÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional
    public ResolucaoGarantiaResponse create(ResolucaoGarantiaRequest request) {
        ResolucaoGarantia entity = mapper.toEntity(request);

        ReclamacaoGarantia reclamacao = reclamacaoRepository.findById(request.reclamacaoId())
                .orElseThrow(() -> new EntityNotFoundException("ReclamaÃ§Ã£o nÃ£o encontrada"));
        entity.setReclamacao(reclamacao);

        if (request.funcionarioExecutorId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(request.funcionarioExecutorId())
                    .orElseThrow(() -> new EntityNotFoundException("FuncionÃ¡rio nÃ£o encontrado"));
            entity.setFuncionarioExecutor(funcionario);
        }

        if (request.novaGarantiaId() != null) {
            Garantia novaGarantia = garantiaRepository.findById(request.novaGarantiaId())
                    .orElseThrow(() -> new EntityNotFoundException("Nova garantia nÃ£o encontrada"));
            entity.setNovaGarantia(novaGarantia);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ResolucaoGarantiaResponse update(Long id, ResolucaoGarantiaRequest request) {
        ResolucaoGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ResoluÃ§Ã£o nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.funcionarioExecutorId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(request.funcionarioExecutorId())
                    .orElseThrow(() -> new EntityNotFoundException("FuncionÃ¡rio nÃ£o encontrado"));
            entity.setFuncionarioExecutor(funcionario);
        }

        if (request.novaGarantiaId() != null) {
            Garantia novaGarantia = garantiaRepository.findById(request.novaGarantiaId())
                    .orElseThrow(() -> new EntityNotFoundException("Nova garantia nÃ£o encontrada"));
            entity.setNovaGarantia(novaGarantia);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        ResolucaoGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ResoluÃ§Ã£o nÃ£o encontrada"));
        repository.delete(entity);
    }
}
