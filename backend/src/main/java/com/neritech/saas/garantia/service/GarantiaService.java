package com.neritech.saas.garantia.service;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.TipoGarantia;
import com.neritech.saas.garantia.dto.GarantiaRequest;
import com.neritech.saas.garantia.dto.GarantiaResponse;
import com.neritech.saas.garantia.mapper.GarantiaMapper;
import com.neritech.saas.garantia.repository.GarantiaRepository;
import com.neritech.saas.garantia.repository.TipoGarantiaRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para Garantia
 */
@Service
@RequiredArgsConstructor
public class GarantiaService {

    private final GarantiaRepository repository;
    private final TipoGarantiaRepository tipoGarantiaRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final GarantiaMapper mapper;

    @Transactional(readOnly = true)
    public Page<GarantiaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public GarantiaResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Garantia nÃ£o encontrada"));
    }

    @Transactional
    public GarantiaResponse create(Long empresaId, GarantiaRequest request) {
        Garantia entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setEmitidaPor(1L); // TODO: Obter do contexto de seguranÃ§a

        // Configurar relacionamentos
        TipoGarantia tipoGarantia = tipoGarantiaRepository.findById(request.tipoGarantiaId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de garantia nÃ£o encontrado"));
        entity.setTipoGarantia(tipoGarantia);

        OrdemServico ordemServico = ordemServicoRepository.findById(request.ordemServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviÃ§o nÃ£o encontrada"));
        entity.setOrdemServico(ordemServico);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public GarantiaResponse update(Long id, Long empresaId, GarantiaRequest request) {
        Garantia entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Garantia nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        // Atualizar relacionamentos se necessÃ¡rio
        if (request.tipoGarantiaId() != null) {
            TipoGarantia tipoGarantia = tipoGarantiaRepository.findById(request.tipoGarantiaId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de garantia nÃ£o encontrado"));
            entity.setTipoGarantia(tipoGarantia);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        Garantia entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Garantia nÃ£o encontrada"));
        repository.delete(entity);
    }
}
