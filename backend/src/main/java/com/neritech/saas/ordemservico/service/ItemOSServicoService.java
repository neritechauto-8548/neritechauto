package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import com.neritech.saas.ordemservico.mapper.ItemOSServicoMapper;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.produtoServico.repository.ServicoRepository;
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
    private final ServicoRepository servicoRepository;

    public ItemOSServicoService(ItemOSServicoRepository repository, OrdemServicoRepository ordemServicoRepository,
            ItemOSServicoMapper mapper, ServicoRepository servicoRepository) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
        this.servicoRepository = servicoRepository;
    }

    public ItemOSServicoResponse create(ItemOSServicoRequest request) {
        validateServicoAtivo(request.servicoId());
        ItemOSServico entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServicoRepository.findByIdAndEmpresaId(request.ordemServicoId(), TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada")));
        ItemOSServico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItemOSServicoResponse findById(Long id) {
        ItemOSServico entity = findOwned(id);
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ItemOSServicoResponse> findByOrdemServicoId(Long ordemServicoId) {
        ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada"));
        return repository.findByOrdemServicoId(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ItemOSServicoResponse update(Long id, ItemOSServicoRequest request) {
        ItemOSServico entity = findOwned(id);
        if (!entity.getOrdemServico().getId().equals(request.ordemServicoId())) {
            throw new ResourceNotFoundException("Ordem de Serviço não encontrada");
        }
        if (!java.util.Objects.equals(entity.getServicoId(), request.servicoId())) validateServicoAtivo(request.servicoId());
        mapper.updateEntityFromRequest(request, entity);
        ItemOSServico updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        repository.delete(findOwned(id));
    }

    private ItemOSServico findOwned(Long id) {
        return repository.findByIdAndOrdemServicoEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }

    private void validateServicoAtivo(Long servicoId) {
        if (servicoId == null) return;
        var servico = servicoRepository.findByIdAndEmpresaId(servicoId, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        if (!Boolean.TRUE.equals(servico.getAtivo())) {
            throw new com.neritech.saas.common.exception.BusinessException("Serviço inativo não pode ser incluído na ordem de serviço.");
        }
    }
}
