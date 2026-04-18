package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import com.neritech.saas.financeiro.mapper.ContasPagarMapper;
import com.neritech.saas.financeiro.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContasPagarService {

    private final ContasPagarRepository repository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CentroCustoRepository centroCustoRepository;
    private final PlanoContaRepository planoContaRepository;
    private final ContasPagarMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContasPagarResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContasPagarResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar nÃ£o encontrada"));
    }

    @Transactional
    public ContasPagarResponse create(Long empresaId, ContasPagarRequest request) {
        ContasPagar entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (entity.getNumeroTitulo() == null || entity.getNumeroTitulo().isBlank()) {
            entity.setNumeroTitulo("CP-" + System.currentTimeMillis());
        }

        updateRelationships(entity, request, empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ContasPagarResponse update(Long id, Long empresaId, ContasPagarRequest request) {
        ContasPagar entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        updateRelationships(entity, request, empresaId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContasPagar entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar nÃ£o encontrada"));
        repository.delete(entity);
    }

    private void updateRelationships(ContasPagar entity, ContasPagarRequest request, Long empresaId) {
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
