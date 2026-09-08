package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.financeiro.domain.ContasReceber;
import com.neritech.saas.financeiro.repository.ContasReceberRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.dto.OrdemServicoCockpitResponse;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.mapper.OrdemServicoMapper;
import com.neritech.saas.ordemservico.repository.DiagnosticoRepository;
import com.neritech.saas.ordemservico.repository.FotoOSRepository;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.neritech.saas.ordemservico.repository.OSChecklistItemRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdemServicoCockpitService {

    private static final String REVIEW_BLOCKS = "REVIEW_BLOCKS";
    private static final String START_EXECUTION = "START_EXECUTION";
    private static final String CONTINUE_EXECUTION = "CONTINUE_EXECUTION";
    private static final String REGISTER_PARTS = "REGISTER_PARTS";
    private static final String ADD_EVIDENCE = "ADD_EVIDENCE";
    private static final String REQUEST_ADDITIONAL_APPROVAL = "REQUEST_ADDITIONAL_APPROVAL";
    private static final String START_CLOSURE_PROCESS = "START_CLOSURE_PROCESS";

    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoMapper ordemServicoMapper;
    private final ItemOSProdutoRepository itemOSProdutoRepository;
    private final OSChecklistItemRepository checklistItemRepository;
    private final FotoOSRepository fotoOSRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ContasReceberRepository contasReceberRepository;

    @Transactional(readOnly = true)
    public OrdemServicoCockpitResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico os = ordemServicoRepository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));

        OrdemServicoResponse base = ordemServicoMapper.toResponse(os);
        StatusOS status = os.getStatus();
        boolean finalizada = status != null && Boolean.TRUE.equals(status.getFinalizaOS());
        boolean cancelada = status != null && Boolean.TRUE.equals(status.getCancelaOS());
        boolean exigeAprovacao = status != null && Boolean.TRUE.equals(status.getExigeAprovacao());
        boolean aprovada = Boolean.TRUE.equals(os.getAprovadoCliente());

        List<OrdemServicoCockpitResponse.Block> blocks = buildBlocks(
                os, cancelada, finalizada, exigeAprovacao, aprovada);
        List<OrdemServicoCockpitResponse.AllowedAction> allowedActions = buildAllowedActions(
                os, finalizada, cancelada, blocks);
        OrdemServicoCockpitResponse.NextAction nextAction = buildNextAction(os, allowedActions, blocks);

        int totalParts = itemOSProdutoRepository.findByOrdemServicoId(id).size();
        int checklistCount = checklistItemRepository.findByOrdemServico_Id(id).size();
        int evidenceCount = fotoOSRepository
                .findByOrdemServicoIdAndEmpresaIdOrderByIdAsc(id, tenantId)
                .size();
        int additionalRequestCount = diagnosticoRepository.findByOrdemServicoId(id).size();

        Optional<ContasReceber> receivable = contasReceberRepository
                .findByEmpresaIdAndNumeroTitulo(tenantId, os.getNumeroOS());

        int pendingApprovals = exigeAprovacao && !aprovada ? 1 : 0;
        int approvedApprovals = aprovada ? 1 : 0;

        return new OrdemServicoCockpitResponse(
                os.getId(),
                os.getNumeroOS(),
                tenantId,
                null,
                os.getVersao(),
                buildStage(status),
                nextAction,
                allowedActions,
                new OrdemServicoCockpitResponse.Customer(os.getClienteId(), base.nomeCliente()),
                new OrdemServicoCockpitResponse.Vehicle(
                        os.getVeiculoId(), base.placaVeiculo(), base.nomeVeiculo()),
                buildExecution(os),
                new OrdemServicoCockpitResponse.Parts(totalParts, null, null),
                new OrdemServicoCockpitResponse.Approvals(
                        pendingApprovals,
                        approvedApprovals,
                        null),
                blocks,
                new OrdemServicoCockpitResponse.RelatedCounts(
                        checklistCount,
                        evidenceCount,
                        additionalRequestCount),
                receivable.map(this::buildFinancial).orElse(null),
                buildFiscal(os),
                new OrdemServicoCockpitResponse.Audit(
                        os.getDataCadastro(), os.getDataAtualizacao(), MDC.get("traceId")),
                List.of());
    }

    private OrdemServicoCockpitResponse.Stage buildStage(StatusOS status) {
        if (status == null) {
            return new OrdemServicoCockpitResponse.Stage(
                    "UNDEFINED", "Status não informado", "warning", null);
        }

        String severity = "info";
        if (Boolean.TRUE.equals(status.getFinalizaOS())) {
            severity = "success";
        } else if (Boolean.TRUE.equals(status.getCancelaOS())) {
            severity = "danger";
        } else if (Boolean.TRUE.equals(status.getExigeAprovacao())) {
            severity = "warning";
        }

        return new OrdemServicoCockpitResponse.Stage(
                status.getCodigo(), status.getNome(), severity, status.getCorIdentificacao());
    }

    private OrdemServicoCockpitResponse.Execution buildExecution(OrdemServico os) {
        String executionStatus;
        int progress;
        if (os.getDataFimExecucao() != null) {
            executionStatus = "COMPLETED";
            progress = 100;
        } else if (os.getDataInicioExecucao() != null) {
            executionStatus = "IN_PROGRESS";
            progress = 50;
        } else {
            executionStatus = "NOT_STARTED";
            progress = 0;
        }

        Long responsibleId = os.getMecanicoResponsavelId() != null
                ? os.getMecanicoResponsavelId()
                : os.getConsultorResponsavelId();

        return new OrdemServicoCockpitResponse.Execution(
                executionStatus,
                responsibleId,
                null,
                os.getDataPromessa(),
                os.getDataInicioExecucao(),
                os.getDataFimExecucao(),
                progress);
    }

    private List<OrdemServicoCockpitResponse.Block> buildBlocks(
            OrdemServico os,
            boolean cancelada,
            boolean finalizada,
            boolean exigeAprovacao,
            boolean aprovada) {
        List<OrdemServicoCockpitResponse.Block> blocks = new ArrayList<>();

        if (cancelada) {
            blocks.add(new OrdemServicoCockpitResponse.Block(
                    "ORDER_CANCELLED", "A Ordem de Serviço está cancelada.", "danger"));
            return List.copyOf(blocks);
        }

        if (exigeAprovacao && !aprovada) {
            blocks.add(new OrdemServicoCockpitResponse.Block(
                    "CUSTOMER_APPROVAL_PENDING",
                    "Aprovação do cliente ainda não foi registrada.",
                    "warning"));
        }

        if (!finalizada
                && os.getMecanicoResponsavelId() == null
                && os.getConsultorResponsavelId() == null) {
            blocks.add(new OrdemServicoCockpitResponse.Block(
                    "RESPONSIBLE_PENDING",
                    "Nenhum responsável está associado à execução.",
                    "warning"));
        }

        // Prazo vencido é risco/SLA, não bloqueio de domínio. Ele será exposto em
        // projeção própria quando o contrato de risco estiver disponível.
        return List.copyOf(blocks);
    }

    private List<OrdemServicoCockpitResponse.AllowedAction> buildAllowedActions(
            OrdemServico os,
            boolean finalizada,
            boolean cancelada,
            List<OrdemServicoCockpitResponse.Block> blocks) {
        if (cancelada || finalizada) {
            return List.of();
        }

        List<OrdemServicoCockpitResponse.AllowedAction> actions = new ArrayList<>();

        if (!blocks.isEmpty() && hasAuthority("GERAL_USUARIO")) {
            actions.add(action(REVIEW_BLOCKS, "Revisar pendências"));
        }

        boolean executionBlocked = blocks.stream().anyMatch(block ->
                "CUSTOMER_APPROVAL_PENDING".equals(block.code())
                        || "RESPONSIBLE_PENDING".equals(block.code()));

        if (!executionBlocked && hasAuthority("OS_EDITAR")) {
            if (os.getDataInicioExecucao() == null) {
                actions.add(action(START_EXECUTION, "Iniciar execução"));
            } else if (os.getDataFimExecucao() == null) {
                actions.add(action(CONTINUE_EXECUTION, "Continuar execução"));
            } else {
                actions.add(action(START_CLOSURE_PROCESS, "Iniciar fechamento"));
            }
        }

        if (os.getDataFimExecucao() == null && hasAuthority("OS_INC_ITENS")) {
            actions.add(action(REGISTER_PARTS, "Registrar peças"));
        }

        if (hasAuthority("OS_ENV_FOTOS")) {
            actions.add(action(ADD_EVIDENCE, "Adicionar evidência"));
        }

        if (os.getDataInicioExecucao() != null
                && os.getDataFimExecucao() == null
                && hasAuthority("OS_EDITAR")) {
            actions.add(action(
                    REQUEST_ADDITIONAL_APPROVAL,
                    "Solicitar aprovação adicional"));
        }

        return List.copyOf(actions);
    }

    private OrdemServicoCockpitResponse.NextAction buildNextAction(
            OrdemServico os,
            List<OrdemServicoCockpitResponse.AllowedAction> allowedActions,
            List<OrdemServicoCockpitResponse.Block> blocks) {
        if (!blocks.isEmpty()) {
            return nextIfAllowed(
                    REVIEW_BLOCKS,
                    "Revisar pendências",
                    "Existem bloqueios que precisam ser resolvidos antes de continuar.",
                    "/ordens-servico/" + os.getId() + "#bloqueios",
                    null,
                    allowedActions);
        }

        if (os.getDataInicioExecucao() == null) {
            return nextIfAllowed(
                    START_EXECUTION,
                    "Iniciar execução",
                    "A OS está apta para iniciar a execução.",
                    null,
                    "os.start-execution",
                    allowedActions);
        }

        if (os.getDataFimExecucao() == null) {
            return nextIfAllowed(
                    CONTINUE_EXECUTION,
                    "Continuar execução",
                    "Há uma execução em andamento.",
                    "/ordens-servico/" + os.getId() + "#execucao",
                    null,
                    allowedActions);
        }

        return nextIfAllowed(
                START_CLOSURE_PROCESS,
                "Iniciar fechamento",
                "A execução terminou e a OS pode seguir para revisão e fechamento.",
                null,
                "os.start-closure",
                allowedActions);
    }

    private OrdemServicoCockpitResponse.NextAction nextIfAllowed(
            String code,
            String label,
            String reason,
            String route,
            String event,
            List<OrdemServicoCockpitResponse.AllowedAction> allowedActions) {
        boolean allowed = allowedActions.stream().anyMatch(action -> code.equals(action.code()));
        if (!allowed) {
            return null;
        }
        return new OrdemServicoCockpitResponse.NextAction(code, label, reason, route, event);
    }

    private OrdemServicoCockpitResponse.AllowedAction action(String code, String label) {
        return new OrdemServicoCockpitResponse.AllowedAction(code, label);
    }

    private OrdemServicoCockpitResponse.Financial buildFinancial(ContasReceber receivable) {
        return new OrdemServicoCockpitResponse.Financial(
                receivable.getStatus() != null ? receivable.getStatus().name() : null,
                defaultMoney(receivable.getValorNominal()),
                defaultMoney(receivable.getValorPago()),
                defaultMoney(receivable.getValorPendente()));
    }

    private OrdemServicoCockpitResponse.Fiscal buildFiscal(OrdemServico os) {
        boolean emitted = Boolean.TRUE.equals(os.getNfeEmitida());
        List<String> documents = emitted && os.getNumeroNFe() != null && !os.getNumeroNFe().isBlank()
                ? List.of(os.getNumeroNFe())
                : List.of();
        return new OrdemServicoCockpitResponse.Fiscal(
                emitted ? "EMITIDA" : "NAO_EMITIDA",
                documents);
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.getAuthorities().stream().anyMatch(granted ->
                        authority.equals(granted.getAuthority())
                                || "ROLE_ADMIN".equals(granted.getAuthority()));
    }
}
