package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import com.neritech.saas.financeiro.dto.DashboardFinanceiroDTO;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.mapper.ContasReceberMapper;
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
public class ContasReceberService {

    private final ContasReceberRepository repository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final DepartamentoContabioRepository departamentoContabioRepository;
    private final PlanoContaRepository planoContaRepository;
    private final ContasReceberMapper mapper;
    private final FluxoCaixaRepository fluxoCaixaRepository;
    private final FechamentoCaixaService fechamentoCaixaService;

    @Transactional(readOnly = true)
    public Page<ContasReceberResponse> findAll(
            Long empresaId,
            String termo,
            java.time.LocalDate dataInicio,
            java.time.LocalDate dataFim,
            String status,
            Long contaBancariaId,
            Long centroCustoId,
            Long planoContasId,
            Long formaPagamentoId,
            Pageable pageable) {
        
        org.springframework.data.jpa.domain.Specification<ContasReceber> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("formaPagamento", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("contaBancaria", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("centroCusto", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("planoContas", jakarta.persistence.criteria.JoinType.LEFT);
            }
            return cb.equal(root.get("empresaId"), empresaId);
        };

        if (termo != null && !termo.trim().isEmpty()) {
            String pattern = "%" + termo.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("descricao")), pattern),
                cb.like(cb.lower(root.get("numeroTitulo")), pattern)
            ));
        }

        if (dataInicio != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataVencimento"), dataInicio));
        }
        if (dataFim != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataVencimento"), dataFim));
        }

        if (status != null && !status.trim().isEmpty()) {
            switch (status) {
                case "Todas Quitado":
                case "Receita Quitado":
                    spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), StatusTitulo.PAGO));
                    break;
                case "Todas Aberto":
                case "Receita Aberto":
                    spec = spec.and((root, query, cb) -> cb.and(
                        cb.notEqual(root.get("status"), StatusTitulo.PAGO),
                        cb.notEqual(root.get("status"), StatusTitulo.CANCELADO)
                    ));
                    break;
                case "Transferencias":
                    spec = spec.and((root, query, cb) -> {
                        String transPattern = "%transferência%";
                        return cb.or(
                            cb.like(cb.lower(root.get("descricao")), transPattern),
                            cb.like(cb.lower(root.get("observacoes")), transPattern)
                        );
                    });
                    break;
                default:
                    try {
                        StatusTitulo enumStatus = StatusTitulo.valueOf(status);
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), enumStatus));
                    } catch (IllegalArgumentException e) {
                        // Ignorar se não for um status válido
                    }
                    break;
            }
        }

        if (contaBancariaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("contaBancaria").get("id"), contaBancariaId));
        }
        if (centroCustoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("centroCusto").get("id"), centroCustoId));
        }
        if (planoContasId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("planoContas").get("id"), planoContasId));
        }
        if (formaPagamentoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("formaPagamento").get("id"), formaPagamentoId));
        }

        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContasReceberResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber nÃ£o encontrada"));
    }

    @Transactional
    public ContasReceberResponse create(Long empresaId, ContasReceberRequest request) {
        if (request.status() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, request.dataRecebimento() != null ? request.dataRecebimento() : LocalDate.now());
        }

        ContasReceber entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (entity.getNumeroTitulo() == null || entity.getNumeroTitulo().isBlank()) {
            entity.setNumeroTitulo("CR-" + System.currentTimeMillis());
        }

        updateRelationships(entity, request, empresaId);

        ContasReceber saved = repository.save(entity);
        syncFluxoCaixa(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public ContasReceberResponse update(Long id, Long empresaId, ContasReceberRequest request) {
        ContasReceber entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber não encontrada"));

        if (entity.getStatus() == StatusTitulo.PAGO) {
            java.time.LocalDate dataRec = null;
            if (entity.getRecebimentos() != null && !entity.getRecebimentos().isEmpty()) {
                dataRec = entity.getRecebimentos().get(entity.getRecebimentos().size() - 1).getDataRecebimento();
            }
            fechamentoCaixaService.validarCaixaAberto(empresaId, dataRec != null ? dataRec : LocalDate.now());
        }
        if (request.status() == StatusTitulo.PAGO) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, request.dataRecebimento() != null ? request.dataRecebimento() : LocalDate.now());
        }

        mapper.updateEntityFromDTO(request, entity);
        updateRelationships(entity, request, empresaId);

        // Se o status for PAGO e ainda não houver recebimentos cadastrados, criamos um recebimento correspondente
        if (entity.getStatus() == StatusTitulo.PAGO && (entity.getRecebimentos() == null || entity.getRecebimentos().isEmpty())) {
            if (entity.getRecebimentos() == null) {
                entity.setRecebimentos(new java.util.ArrayList<>());
            }
            RecebimentoTitulo rec = new RecebimentoTitulo();
            rec.setContaReceber(entity);
            rec.setDataRecebimento(request.dataRecebimento() != null ? request.dataRecebimento() : LocalDate.now());
            rec.setValorRecebido(request.valorRecebido() != null ? request.valorRecebido() : entity.getValorNominal());
            rec.setValorJuros(request.valorJuros() != null ? request.valorJuros() : java.math.BigDecimal.ZERO);
            rec.setValorMulta(request.valorMulta() != null ? request.valorMulta() : java.math.BigDecimal.ZERO);
            rec.setValorDesconto(request.valorDesconto() != null ? request.valorDesconto() : java.math.BigDecimal.ZERO);
            rec.setEmpresaId(empresaId);
            rec.setFormaPagamento(entity.getFormaPagamento());
            rec.setContaBancaria(entity.getContaBancaria());
            entity.getRecebimentos().add(rec);
            
            // Garantir que valorPago e valorPendente estão atualizados
            entity.setValorPago(rec.getValorRecebido());
            entity.setValorPendente(java.math.BigDecimal.ZERO);
        }

        ContasReceber saved = repository.save(entity);
        syncFluxoCaixa(saved);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DashboardFinanceiroDTO getDashboard(Long empresaId) {
        java.util.List<ContasReceber> contas = repository.findByEmpresaId(empresaId);
        
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        
        // Filtrar cancelados
        java.util.List<ContasReceber> activeContas = contas.stream()
                .filter(c -> c.getStatus() != StatusTitulo.CANCELADO)
                .toList();
                
        java.math.BigDecimal totalAReceber = activeContas.stream()
                .filter(c -> c.getStatus() != StatusTitulo.PAGO)
                .map(c -> c.getValorNominal().subtract(c.getValorPago() != null ? c.getValorPago() : java.math.BigDecimal.ZERO))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        java.math.BigDecimal recebidoHoje = activeContas.stream()
                .filter(c -> c.getStatus() == StatusTitulo.PAGO)
                .flatMap(c -> {
                    if (c.getRecebimentos() == null || c.getRecebimentos().isEmpty()) {
                        LocalDate dataAt = c.getDataAtualizacao() != null ? c.getDataAtualizacao().toLocalDate() : null;
                        if (hoje.equals(dataAt)) {
                            return java.util.stream.Stream.of(c.getValorPago());
                        }
                        return java.util.stream.Stream.empty();
                    }
                    return c.getRecebimentos().stream()
                            .filter(r -> hoje.equals(r.getDataRecebimento()))
                            .map(RecebimentoTitulo::getValorRecebido);
                })
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        java.math.BigDecimal atrasados = activeContas.stream()
                .filter(c -> c.getStatus() != StatusTitulo.PAGO && c.getDataVencimento().isBefore(hoje))
                .map(c -> c.getValorNominal().subtract(c.getValorPago() != null ? c.getValorPago() : java.math.BigDecimal.ZERO))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        long count = activeContas.size();
        java.math.BigDecimal ticketMedio = count > 0 
                ? activeContas.stream()
                        .map(ContasReceber::getValorNominal)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                        .divide(java.math.BigDecimal.valueOf(count), 2, java.math.RoundingMode.HALF_UP)
                : java.math.BigDecimal.ZERO;
                
        java.math.BigDecimal recebimentosPrevistosMes = activeContas.stream()
                .filter(c -> c.getStatus() != StatusTitulo.PAGO 
                          && !c.getDataVencimento().isBefore(inicioMes) 
                          && !c.getDataVencimento().isAfter(fimMes))
                .map(c -> c.getValorNominal().subtract(c.getValorPago() != null ? c.getValorPago() : java.math.BigDecimal.ZERO))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        long vencendoHoje = activeContas.stream()
                .filter(c -> c.getStatus() != StatusTitulo.PAGO && hoje.equals(c.getDataVencimento()))
                .count();
                
        java.math.BigDecimal totalGeral = totalAReceber.add(
                activeContas.stream()
                        .filter(c -> c.getStatus() == StatusTitulo.PAGO)
                        .map(c -> c.getValorPago() != null ? c.getValorPago() : java.math.BigDecimal.ZERO)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
        );
        java.math.BigDecimal inadimplencia = totalGeral.compareTo(java.math.BigDecimal.ZERO) > 0 
                ? atrasados.multiply(java.math.BigDecimal.valueOf(100)).divide(totalGeral, 2, java.math.RoundingMode.HALF_UP)
                : java.math.BigDecimal.ZERO;
                
        return DashboardFinanceiroDTO.builder()
                .totalAReceber(totalAReceber)
                .recebidoHoje(recebidoHoje)
                .atrasados(atrasados)
                .ticketMedio(ticketMedio)
                .recebimentosPrevistosMes(recebimentosPrevistosMes)
                .contasVencendoHoje((int) vencendoHoje)
                .inadimplencia(inadimplencia)
                .build();
    }

    @Transactional
    public ContasReceberResponse desfazerQuitacao(Long id, Long empresaId) {
        ContasReceber entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber não encontrada"));

        if (entity.getStatus() != StatusTitulo.PAGO) {
            throw new IllegalStateException("Apenas títulos pagos podem ter a quitação desfeita.");
        }

        java.time.LocalDate dataRec = null;
        if (entity.getRecebimentos() != null && !entity.getRecebimentos().isEmpty()) {
            dataRec = entity.getRecebimentos().get(entity.getRecebimentos().size() - 1).getDataRecebimento();
        }
        fechamentoCaixaService.validarCaixaAberto(empresaId, dataRec != null ? dataRec : LocalDate.now());

        LocalDate hoje = LocalDate.now();
        if (entity.getDataVencimento().isBefore(hoje)) {
            entity.setStatus(StatusTitulo.VENCIDO);
        } else {
            entity.setStatus(StatusTitulo.ABERTO);
        }

        entity.setValorPago(java.math.BigDecimal.ZERO);
        entity.setValorPendente(entity.getValorNominal());
        entity.setValorJuros(java.math.BigDecimal.ZERO);
        entity.setValorMulta(java.math.BigDecimal.ZERO);
        entity.setValorDesconto(java.math.BigDecimal.ZERO);

        if (entity.getRecebimentos() != null) {
            entity.getRecebimentos().clear();
        }

        ContasReceber saved = repository.save(entity);
        syncFluxoCaixa(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContasReceber entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber não encontrada"));

        if (entity.getStatus() == StatusTitulo.PAGO) {
            java.time.LocalDate dataRec = null;
            if (entity.getRecebimentos() != null && !entity.getRecebimentos().isEmpty()) {
                dataRec = entity.getRecebimentos().get(entity.getRecebimentos().size() - 1).getDataRecebimento();
            }
            fechamentoCaixaService.validarCaixaAberto(empresaId, dataRec != null ? dataRec : LocalDate.now());
        }
        
        fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
            empresaId, id, com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA
        ).ifPresent(fluxoCaixaRepository::delete);

        repository.delete(entity);
    }

    public void syncFluxoCaixa(ContasReceber entity) {
        if (entity.getStatus() == StatusTitulo.PAGO) {
            FluxoCaixa fc = fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
                entity.getEmpresaId(), entity.getId(), com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA
            ).orElse(new FluxoCaixa());

            fc.setEmpresaId(entity.getEmpresaId());
            fc.setContaBancaria(entity.getContaBancaria());
            fc.setTipoMovimentacao(com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA);
            fc.setCategoria("RECEITA");
            fc.setDescricao(entity.getDescricao());
            fc.setValor(entity.getValorPago() != null && entity.getValorPago().compareTo(java.math.BigDecimal.ZERO) > 0 
                ? entity.getValorPago() : entity.getValorNominal());
            java.time.LocalDate dataRec = null;
            if (entity.getRecebimentos() != null && !entity.getRecebimentos().isEmpty()) {
                dataRec = entity.getRecebimentos().get(entity.getRecebimentos().size() - 1).getDataRecebimento();
            }
            fc.setDataMovimentacao(dataRec != null ? dataRec : LocalDate.now());
            fc.setDataCompetencia(entity.getDataEmissao());
            fc.setDocumentoTipo("RECEBIMENTO");
            fc.setDocumentoId(entity.getId());
            fc.setCentroCusto(entity.getCentroCusto());
            fc.setClienteId(entity.getClienteId());
            fc.setFormaPagamento(entity.getFormaPagamento());
            fc.setUsuarioResponsavel(1L); // TODO: Get from security context
            fluxoCaixaRepository.save(fc);
        } else {
            fluxoCaixaRepository.findByEmpresaIdAndDocumentoIdAndTipoMovimentacao(
                entity.getEmpresaId(), entity.getId(), com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA
            ).ifPresent(fluxoCaixaRepository::delete);
        }
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
            com.neritech.saas.empresa.domain.DepartamentoContabio depto = departamentoContabioRepository.findById(request.centroCustoId())
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
