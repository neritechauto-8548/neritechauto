package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import com.neritech.saas.financeiro.mapper.ContasPagarMapper;
import com.neritech.saas.financeiro.repository.*;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContasPagarService {

    private final ContasPagarRepository repository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final DepartamentoContabioRepository departamentoContabioRepository;
    private final PlanoContaRepository planoContaRepository;
    private final ContasPagarMapper mapper;
    private final FluxoCaixaRepository fluxoCaixaRepository;
    private final FechamentoCaixaService fechamentoCaixaService;

    @Transactional(readOnly = true)
    public Page<ContasPagarResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContasPagarResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar não encontrada"));
    }

    @Transactional
    public ContasPagarResponse create(Long empresaId, ContasPagarRequest request) {
        if (request.status() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, request.dataPagamento() != null ? request.dataPagamento() : LocalDate.now());
        }

        ContasPagar entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (entity.getNumeroTitulo() == null || entity.getNumeroTitulo().isBlank()) {
            entity.setNumeroTitulo("CP-" + System.currentTimeMillis());
        }

        updateRelationships(entity, request, empresaId);

        ContasPagar saved = repository.save(entity);
        syncFluxoCaixa(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public ContasPagarResponse update(Long id, Long empresaId, ContasPagarRequest request) {
        ContasPagar entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar não encontrada"));

        if (entity.getStatus() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, entity.getDataPagamento() != null ? entity.getDataPagamento() : LocalDate.now());
        }
        if (request.status() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, request.dataPagamento() != null ? request.dataPagamento() : LocalDate.now());
        }

        mapper.updateEntityFromDTO(request, entity);
        updateRelationships(entity, request, empresaId);

        ContasPagar saved = repository.save(entity);
        syncFluxoCaixa(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContasPagar entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a pagar não encontrada"));

        if (entity.getStatus() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, entity.getDataPagamento() != null ? entity.getDataPagamento() : LocalDate.now());
        }

        fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
                empresaId, id, com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA)
                .ifPresent(fluxoCaixaRepository::delete);

        repository.delete(entity);
    }

    private void syncFluxoCaixa(ContasPagar entity) {
        if (entity.getStatus() == StatusTitulo.PAGO) {
            FluxoCaixa fc = fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
                    entity.getEmpresaId(), entity.getId(),
                    com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA).orElse(new FluxoCaixa());

            fc.setEmpresaId(entity.getEmpresaId());
            fc.setContaBancaria(entity.getContaBancaria());
            fc.setTipoMovimentacao(com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA);
            fc.setCategoria("DESPESA");
            fc.setDescricao(entity.getDescricao());
            fc.setValor(entity.getValorPago() != null && entity.getValorPago().compareTo(java.math.BigDecimal.ZERO) > 0
                    ? entity.getValorPago()
                    : entity.getValorNominal());
            fc.setDataMovimentacao(entity.getDataPagamento() != null ? entity.getDataPagamento() : LocalDate.now());
            fc.setDataCompetencia(entity.getDataEmissao());
            fc.setDocumentoTipo("PAGAMENTO");
            fc.setDocumentoId(entity.getId());
            fc.setCentroCusto(entity.getCentroCusto());
            fc.setFornecedorId(entity.getFornecedorId());
            fc.setFormaPagamento(entity.getFormaPagamento());
            fc.setUsuarioResponsavel(1L); // TODO: Get from security context
            fluxoCaixaRepository.save(fc);
        } else {
            fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
                    entity.getEmpresaId(), entity.getId(),
                    com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA)
                    .ifPresent(fluxoCaixaRepository::delete);
        }
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
            com.neritech.saas.empresa.domain.DepartamentoContabio depto = departamentoContabioRepository
                    .findById(request.centroCustoId())
                    .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado"));
            entity.setCentroCusto(depto);
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
