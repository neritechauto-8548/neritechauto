package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import com.neritech.saas.ordemservico.mapper.ItemOSServicoMapper;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemOSServicoService {

    private final ItemOSServicoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ItemOSServicoMapper mapper;

    public ItemOSServicoService(ItemOSServicoRepository repository, OrdemServicoRepository ordemServicoRepository,
            ItemOSServicoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    public ItemOSServicoResponse create(ItemOSServicoRequest request) {
        ItemOSServico entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServicoRepository.findById(request.ordemServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de ServiÃ§o nÃ£o encontrada")));
        ItemOSServico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItemOSServicoResponse findById(Long id) {
        ItemOSServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item nÃ£o encontrado"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ItemOSServicoResponse> findByOrdemServicoId(Long ordemServicoId) {
        return repository.findByOrdemServicoId(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ItemOSServicoResponse update(Long id, ItemOSServicoRequest request) {
        ItemOSServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item nÃ£o encontrado"));
        mapper.updateEntityFromRequest(request, entity);
        ItemOSServico updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
