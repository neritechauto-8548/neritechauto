package com.neritech.saas.orcamento.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.dto.OrcamentoListCustomerResponse;
import com.neritech.saas.orcamento.dto.OrcamentoListItemResponse;
import com.neritech.saas.orcamento.dto.OrcamentoListResponse;
import com.neritech.saas.orcamento.dto.OrcamentoListVehicleResponse;
import com.neritech.saas.orcamento.dto.OrcamentoMoneyResponse;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.text.Normalizer;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrcamentoQueryService {

    private static final int DEFAULT_SIZE = 25;
    private static final int MAX_SIZE = 100;
    private static final Set<String> CANONICAL_STATUSES = Set.of(
            "RASCUNHO", "ENVIADO", "AGUARDANDO_APROVACAO", "APROVADO", "PARCIAL",
            "RECUSADO", "EXPIRADO", "CONVERTIDO", "CANCELADO");
    private static final Map<String, String> SORT_FIELDS = Map.of(
            "numero", "numeroOS",
            "total", "valorTotal",
            "createdAt", "dataCadastro",
            "updatedAt", "dataAtualizacao");

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public OrcamentoQueryService(
            OrdemServicoRepository ordemServicoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public OrcamentoListResponse list(
            String query,
            String status,
            int page,
            int size,
            String sort) {
        Long tenantId = requireTenant();
        String normalizedStatus = normalizeStatus(status);
        String search = trimToNull(query);
        String normalizedSearch = normalizeSearch(search);
        PageRequest pageable = sanitizePageable(page, size, sort);

        Page<OrdemServico> result = ordemServicoRepository.searchBudgets(
                tenantId,
                TipoOS.ORCAMENTO,
                normalizedStatus,
                search,
                normalizedSearch,
                pageable);

        Map<Long, Cliente> customers = loadCustomers(tenantId, result.getContent());
        Map<Long, Veiculo> vehicles = loadVehicles(tenantId, result.getContent());
        List<OrcamentoListItemResponse> items = result.getContent().stream()
                .map(budget -> toListItem(budget, customers.get(budget.getClienteId()), vehicles.get(budget.getVeiculoId())))
                .toList();

        return new OrcamentoListResponse(
                items,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                false,
                "INDICADORES_AGREGADOS_NAO_DISPONIVEIS");
    }

    @Transactional(readOnly = true)
    public OrcamentoListItemResponse findById(Long id) {
        Long tenantId = requireTenant();
        OrdemServico budget = ordemServicoRepository.findByIdAndEmpresaId(id, tenantId)
                .filter(entity -> entity.getTipoOS() == TipoOS.ORCAMENTO)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado no contexto autenticado."));

        Cliente customer = budget.getClienteId() == null
                ? null
                : clienteRepository.findAllByEmpresaIdAndIdIn(tenantId, Set.of(budget.getClienteId())).stream().findFirst().orElse(null);
        Veiculo vehicle = budget.getVeiculoId() == null
                ? null
                : veiculoRepository.findSummariesByEmpresaIdAndIdIn(tenantId, Set.of(budget.getVeiculoId())).stream().findFirst().orElse(null);
        return toListItem(budget, customer, vehicle);
    }

    private Map<Long, Cliente> loadCustomers(Long tenantId, Collection<OrdemServico> budgets) {
        Set<Long> ids = budgets.stream()
                .map(OrdemServico::getClienteId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (ids.isEmpty()) return Map.of();
        return clienteRepository.findAllByEmpresaIdAndIdIn(tenantId, ids).stream()
                .collect(Collectors.toMap(Cliente::getId, Function.identity()));
    }

    private Map<Long, Veiculo> loadVehicles(Long tenantId, Collection<OrdemServico> budgets) {
        Set<Long> ids = budgets.stream()
                .map(OrdemServico::getVeiculoId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (ids.isEmpty()) return Map.of();
        return veiculoRepository.findSummariesByEmpresaIdAndIdIn(tenantId, ids).stream()
                .collect(Collectors.toMap(Veiculo::getId, Function.identity()));
    }

    private OrcamentoListItemResponse toListItem(OrdemServico budget, Cliente customer, Veiculo vehicle) {
        String status = budget.getStatus() != null && budget.getStatus().getCodigo() != null
                ? budget.getStatus().getCodigo().trim().toUpperCase(Locale.ROOT)
                : "RASCUNHO";
        List<String> allowedActions = "RASCUNHO".equals(status)
                ? List.of("OPEN", "CONTINUE_EDIT")
                : List.of("OPEN");

        return new OrcamentoListItemResponse(
                budget.getId(),
                budget.getNumeroOS(),
                budget.getVersao(),
                customer != null ? new OrcamentoListCustomerResponse(customer.getId(), customerDisplayName(customer)) : null,
                vehicle != null ? new OrcamentoListVehicleResponse(vehicle.getId(), vehicleDescription(vehicle), vehicle.getPlaca()) : null,
                status,
                OrcamentoMoneyResponse.brl(budget.getValorTotal()),
                null,
                budget.getConsultorResponsavelId(),
                null,
                nextAction(status),
                budget.getDataCadastro() != null ? budget.getDataCadastro() : budget.getDataAbertura(),
                budget.getDataAtualizacao() != null ? budget.getDataAtualizacao() : budget.getDataCadastro(),
                allowedActions);
    }

    private String customerDisplayName(Cliente customer) {
        if (customer.getTipoCliente() == TipoCliente.PESSOA_JURIDICA) {
            String tradeName = trimToNull(customer.getNomeFantasia());
            if (tradeName != null) return tradeName;
            String legalName = trimToNull(customer.getRazaoSocial());
            if (legalName != null) return legalName;
        }
        String name = trimToNull(customer.getNomeCompleto());
        return name != null ? name : "Cliente";
    }

    private String vehicleDescription(Veiculo vehicle) {
        String brand = vehicle.getMarca() != null ? trimToNull(vehicle.getMarca().getNome()) : null;
        String model = vehicle.getModelo() != null ? trimToNull(vehicle.getModelo().getNome()) : null;
        if (brand != null && model != null) return brand + " " + model;
        if (model != null) return model;
        if (brand != null) return brand;
        return "Veículo";
    }

    private String nextAction(String status) {
        return switch (status) {
            case "RASCUNHO" -> "CONTINUAR_EDICAO";
            case "ENVIADO", "AGUARDANDO_APROVACAO" -> "ACOMPANHAR_APROVACAO";
            case "APROVADO" -> "CONVERTER_EM_OS";
            case "PARCIAL" -> "REVISAR_DECISAO";
            case "RECUSADO" -> "REGISTRAR_FOLLOW_UP";
            case "EXPIRADO" -> "REVALIDAR";
            case "CONVERTIDO" -> "ABRIR_OS";
            case "CANCELADO" -> "CONSULTAR_HISTORICO";
            default -> "REVISAR_DETALHES";
        };
    }

    private PageRequest sanitizePageable(int page, int size, String sort) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        String[] parts = sort == null ? new String[0] : sort.split(",", 2);
        String requestedField = parts.length > 0 ? parts[0].trim() : "updatedAt";
        String field = SORT_FIELDS.getOrDefault(requestedField, "dataAtualizacao");
        Sort.Direction direction = parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return PageRequest.of(safePage, safeSize, Sort.by(direction, field));
    }

    private String normalizeStatus(String status) {
        String normalized = trimToNull(status);
        if (normalized == null) return null;
        normalized = normalized.toUpperCase(Locale.ROOT);
        if (!CANONICAL_STATUSES.contains(normalized)) {
            throw new IllegalArgumentException("Status de orçamento inválido.");
        }
        return normalized;
    }

    private String normalizeSearch(String value) {
        if (value == null) return null;
        String withoutAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String normalized = withoutAccents.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant autenticado não disponível para listar orçamentos.");
        }
        return tenantId;
    }
}
