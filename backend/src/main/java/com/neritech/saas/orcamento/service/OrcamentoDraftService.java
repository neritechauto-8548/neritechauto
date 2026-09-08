package com.neritech.saas.orcamento.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.dto.OrcamentoDraftRequest;
import com.neritech.saas.orcamento.dto.OrcamentoDraftResponse;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

/**
 * Adapter D5 inicial para criação de orçamento.
 *
 * O armazenamento legado ainda reutiliza OrdemServico com tipo ORCAMENTO, porém
 * o contrato externo já segue as regras novas: tenant e número comercial são
 * definidos exclusivamente pelo backend e todos os vínculos são revalidados.
 */
@Service
public class OrcamentoDraftService {

    private static final int MAX_NUMBER_ATTEMPTS = 10;

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final OrcamentoCreationIdempotencyService idempotencyService;

    public OrcamentoDraftService(
            OrdemServicoRepository ordemServicoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            OrcamentoCreationIdempotencyService idempotencyService) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.idempotencyService = idempotencyService;
    }

    @Transactional
    public OrcamentoDraftResponse create(OrcamentoDraftRequest request) {
        Long tenantId = requireTenant();

        OrcamentoCreationIdempotencyService.Reservation reservation =
                idempotencyService.reserve(tenantId, request);

        if (!reservation.created()) {
            OrdemServico existing = ordemServicoRepository
                    .findByIdAndEmpresaId(reservation.ordemServicoId(), tenantId)
                    .filter(ordem -> ordem.getTipoOS() == TipoOS.ORCAMENTO)
                    .orElseThrow(() -> new IllegalStateException(
                            "Reserva idempotente aponta para um orçamento indisponível no tenant autenticado."));
            return toResponse(existing);
        }

        Cliente cliente = clienteRepository.findByIdScoped(request.clienteId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado no contexto autenticado."));

        if (cliente.getStatus() == StatusCliente.INATIVO) {
            throw new BusinessException("Reative o cliente antes de criar um novo orçamento.");
        }

        Veiculo veiculo = null;
        if (request.veiculoId() != null) {
            veiculo = veiculoRepository.findByIdAndEmpresaId(request.veiculoId(), tenantId)
                    .orElseThrow(() -> new BusinessException("Veículo não encontrado no contexto autenticado."));

            if (veiculo.getCliente() == null || !request.clienteId().equals(veiculo.getCliente().getId())) {
                throw new BusinessException("O veículo informado não pertence ao cliente selecionado.");
            }
        }

        OrdemServico draft = new OrdemServico();
        draft.setEmpresaId(tenantId);
        draft.setNumeroOS(generateCommercialNumber(tenantId));
        draft.setClienteId(cliente.getId());
        draft.setVeiculoId(veiculo != null ? veiculo.getId() : null);
        draft.setTipoOS(TipoOS.ORCAMENTO);
        draft.setDataAbertura(LocalDateTime.now());
        draft.setQuilometragemEntrada(request.quilometragemEntrada());
        draft.setConsultorResponsavelId(request.responsavelId());
        draft.setProblemaRelatado(trimToNull(request.relatoCliente()));
        draft.setObservacoesInternas(trimToNull(request.observacoesInternas()));
        draft.setObservacoesCliente(trimToNull(request.observacoesCliente()));
        draft.setValorServicos(BigDecimal.ZERO);
        draft.setValorProdutos(BigDecimal.ZERO);
        draft.setValorDesconto(BigDecimal.ZERO);
        draft.setValorAcrescimo(BigDecimal.ZERO);
        draft.setValorTotal(BigDecimal.ZERO);

        OrdemServico saved = ordemServicoRepository.save(draft);
        if (saved.getId() == null) {
            throw new IllegalStateException("Orçamento criado sem identificador persistido.");
        }

        idempotencyService.complete(tenantId, reservation, saved.getId());
        return toResponse(saved);
    }

    private OrcamentoDraftResponse toResponse(OrdemServico ordem) {
        return new OrcamentoDraftResponse(
                ordem.getId(),
                ordem.getNumeroOS(),
                "RASCUNHO",
                ordem.getClienteId(),
                ordem.getVeiculoId(),
                ordem.getDataAbertura());
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant autenticado não disponível para criar orçamento.");
        }
        return tenantId;
    }

    private String generateCommercialNumber(Long tenantId) {
        String prefix = "ORC-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-";
        for (int attempt = 0; attempt < MAX_NUMBER_ATTEMPTS; attempt++) {
            String suffix = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 6)
                    .toUpperCase(Locale.ROOT);
            String candidate = prefix + suffix;
            if (!ordemServicoRepository.existsByEmpresaIdAndNumeroOS(tenantId, candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Não foi possível gerar um número único para o orçamento.");
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
