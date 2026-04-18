package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import com.neritech.saas.financeiro.mapper.ContasReceberMapper;
import com.neritech.saas.financeiro.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContasReceberService {

    private final ContasReceberRepository repository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CentroCustoRepository centroCustoRepository;
    private final PlanoContaRepository planoContaRepository;
    private final ContasReceberMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContasReceberResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContasReceberResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber nÃ£o encontrada"));
    }

    @Transactional
    public ContasReceberResponse create(Long empresaId, ContasReceberRequest request) {
        ContasReceber entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (entity.getNumeroTitulo() == null || entity.getNumeroTitulo().isBlank()) {
            entity.setNumeroTitulo("CR-" + System.currentTimeMillis());
        }

        updateRelationships(entity, request, empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ContasReceberResponse update(Long id, Long empresaId, ContasReceberRequest request) {
        ContasReceber entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        updateRelationships(entity, request, empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContasReceber entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber nÃ£o encontrada"));
        repository.delete(entity);
    }

    private void updateRelationships(ContasReceber entity, ContasReceberRequest request, Long empresaId) {
        if (request.formaPagamentoId() != null) {
            FormaPagamento forma = formaPagamentoRepository.findByIdAndEmpresaId(request.formaPagamentoId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));
            entity.setFormaPagamento(forma);
        } else {
            entity.setFormaPagamento(null);
        }

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(conta);
        } else {
            entity.setContaBancaria(null);
        }

        if (request.centroCustoId() != null) {
            CentroCusto centro = centroCustoRepository.findById(request.centroCustoId())
                    .orElseThrow(() -> new EntityNotFoundException("Centro de custo nÃ£o encontrado"));
            entity.setCentroCusto(centro);
        } else {
            entity.setCentroCusto(null);
        }

        if (request.planoContasId() != null) {
            PlanoConta plano = planoContaRepository.findById(request.planoContasId())
                    .orElseThrow(() -> new EntityNotFoundException("Plano de contas nÃ£o encontrado"));
            entity.setPlanoContas(plano);
        } else {
            entity.setPlanoContas(null);
        }
    }
}
