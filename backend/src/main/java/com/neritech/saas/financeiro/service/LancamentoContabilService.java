package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.LancamentoContabil;
import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.dto.LancamentoContabilRequest;
import com.neritech.saas.financeiro.dto.LancamentoContabilResponse;
import com.neritech.saas.financeiro.mapper.LancamentoContabilMapper;
import com.neritech.saas.financeiro.repository.CentroCustoRepository;
import com.neritech.saas.financeiro.repository.LancamentoContabilRepository;
import com.neritech.saas.financeiro.repository.PlanoContaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LancamentoContabilService {

    private final LancamentoContabilRepository repository;
    private final PlanoContaRepository planoContaRepository;
    private final CentroCustoRepository centroCustoRepository;
    private final LancamentoContabilMapper mapper;

    @Transactional(readOnly = true)
    public Page<LancamentoContabilResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public LancamentoContabilResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento contÃ¡bil nÃ£o encontrado"));
    }

    @Transactional
    public LancamentoContabilResponse create( LancamentoContabilRequest request) {
        LancamentoContabil entity = mapper.toEntity(request);
    
        entity.setCriadoPor(1L); // TODO: Get from security context

        updateRelationships(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public LancamentoContabilResponse update(Long id, LancamentoContabilRequest request) {
        LancamentoContabil entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento contÃ¡bil nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);
        updateRelationships(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        LancamentoContabil entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento contÃ¡bil nÃ£o encontrado"));
        repository.delete(entity);
    }

    private void updateRelationships(LancamentoContabil entity, LancamentoContabilRequest request) {
        if (request.contaDebitoId() != null) {
            PlanoConta conta = planoContaRepository.findById(request.contaDebitoId())
                    .orElseThrow(() -> new EntityNotFoundException("Conta dÃ©bito nÃ£o encontrada"));
            entity.setContaDebito(conta);
        }

        if (request.contaCreditoId() != null) {
            PlanoConta conta = planoContaRepository.findById(request.contaCreditoId())
                    .orElseThrow(() -> new EntityNotFoundException("Conta crÃ©dito nÃ£o encontrada"));
            entity.setContaCredito(conta);
        }

        if (request.centroCustoId() != null) {
            CentroCusto centro = centroCustoRepository.findById(request.centroCustoId())
                    .orElseThrow(() -> new EntityNotFoundException("Centro de custo nÃ£o encontrado"));
            entity.setCentroCusto(centro);
        } else {
            entity.setCentroCusto(null);
        }
    }
}
