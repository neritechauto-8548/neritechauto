package com.neritech.saas.garantia.service;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.ItemGarantia;
import com.neritech.saas.garantia.dto.ItemGarantiaRequest;
import com.neritech.saas.garantia.dto.ItemGarantiaResponse;
import com.neritech.saas.garantia.mapper.ItemGarantiaMapper;
import com.neritech.saas.garantia.repository.GarantiaRepository;
import com.neritech.saas.garantia.repository.ItemGarantiaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para ItemGarantia
 */
@Service
@RequiredArgsConstructor
public class ItemGarantiaService {

    private final ItemGarantiaRepository repository;
    private final GarantiaRepository garantiaRepository;
    private final ItemGarantiaMapper mapper;

    @Transactional(readOnly = true)
    public List<ItemGarantiaResponse> findByGarantiaId(Long garantiaId) {
        return repository.findByGarantiaId(garantiaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemGarantiaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Item de garantia nÃ£o encontrado"));
    }

    @Transactional
    public ItemGarantiaResponse create(ItemGarantiaRequest request) {
        ItemGarantia entity = mapper.toEntity(request);

        Garantia garantia = garantiaRepository.findById(request.garantiaId())
                .orElseThrow(() -> new EntityNotFoundException("Garantia nÃ£o encontrada"));
        entity.setGarantia(garantia);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ItemGarantiaResponse update(Long id, ItemGarantiaRequest request) {
        ItemGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de garantia nÃ£o encontrado"));
        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        ItemGarantia entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de garantia nÃ£o encontrado"));
        repository.delete(entity);
    }
}
