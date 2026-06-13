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
                .orElseThrow(() -> new EntityNotFoundException("Conta a receber não encontrada"));

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

        return mapper.toResponse(repository.save(entity));
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
