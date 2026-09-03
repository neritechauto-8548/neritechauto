package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import com.neritech.saas.agendamento.mapper.AgendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private static final int MAX_NUMBER_ATTEMPTS = 10;

    private final AgendamentoRepository repository;
    private final TipoAgendamentoRepository tipoAgendamentoRepository;
    private final AgendamentoMapper mapper;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Long tenantId = requireTenant();
        validateSchedule(request);
        ReferenceContext context = validateReferences(request, tenantId);
        ensureNoVehicleConflict(request, null);

        Agendamento agendamento = mapper.toEntity(request);
        agendamento.setEmpresaId(tenantId);
        agendamento.setNumeroAgendamento(generateNumber());
        agendamento.setClienteId(context.cliente().getId());
        agendamento.setVeiculoId(context.veiculo() != null ? context.veiculo().getId() : null);
        agendamento.setTipoAgendamento(context.tipo());
        agendamento.setDuracaoEstimadaMinutos(resolveDuration(request));

        // O estado inicial e a confirmação não são autoridade do browser.
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setConfirmadoCliente(false);
        agendamento.setMetodoConfirmacao(null);
        agendamento.setDataConfirmacao(null);

        // Financeiro não pertence à criação de intenção/agendamento.
        agendamento.setValorEstimado(null);
        agendamento.setFormaPagamentoPreferidaId(null);

        // Não usar IDs fixos. Auditoria Spring preenche createdBy quando houver ator disponível.
        agendamento.setAgendadoPor(null);

        Agendamento salvo = repository.save(agendamento);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        return mapper.toResponse(findScoped(id));
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAtual() {
        requireTenant();
        return repository.findAllScoped().stream().map(mapper::toResponse).toList();
    }

    /** Compatibilidade com rota antiga /empresa/{empresaId}; o path não concede tenant. */
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorEmpresa(Long empresaId) {
        Long tenantId = requireTenant();
        if (!tenantId.equals(empresaId)) {
            throw new BusinessException("Empresa informada não corresponde ao contexto autenticado.");
        }
        return repository.findAllScoped().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Long tenantId = requireTenant();
        Agendamento agendamento = findScoped(id);
        validateSchedule(request);
        ReferenceContext context = validateReferences(request, tenantId);
        ensureNoVehicleConflict(request, id);

        agendamento.setClienteId(context.cliente().getId());
        agendamento.setVeiculoId(context.veiculo() != null ? context.veiculo().getId() : null);
        agendamento.setTipoAgendamento(context.tipo());
        agendamento.setDataAgendamento(request.dataAgendamento());
        agendamento.setHoraInicio(request.horaInicio());
        agendamento.setHoraFim(request.horaFim());
        agendamento.setDuracaoEstimadaMinutos(resolveDuration(request));
        agendamento.setServicosSolicitados(trimToNull(request.servicosSolicitados()));
        agendamento.setProblemaRelatado(trimToNull(request.problemaRelatado()));
        agendamento.setObservacoesCliente(trimToNull(request.observacoesCliente()));
        agendamento.setObservacoesInternas(trimToNull(request.observacoesInternas()));
        agendamento.setMecanicoPreferidoId(request.mecanicoPreferidoId());
        agendamento.setMecanicoAlocadoId(request.mecanicoAlocadoId());
        agendamento.setRecursosNecessarios(trimToNull(request.recursosNecessarios()));
        agendamento.setCanalAgendamento(request.canalAgendamento());

        if (request.status() != null) {
            agendamento.setStatus(request.status());
        }

        Agendamento atualizado = repository.save(agendamento);
        return mapper.toResponse(atualizado);
    }

    /** Delete operacional é lógico: preserva histórico e marca CANCELADO. */
    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = findScoped(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        repository.save(agendamento);
    }

    private Agendamento findScoped(Long id) {
        return repository.findByIdScoped(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant autenticado não disponível para a Agenda.");
        }
        return tenantId;
    }

    private void validateSchedule(AgendamentoRequest request) {
        if (request.dataAgendamento() != null && request.dataAgendamento().isBefore(LocalDate.now())) {
            throw new BusinessException("Não é permitido realizar agendamentos para datas retroativas.");
        }
        if (request.horaInicio() != null && request.horaFim() != null && !request.horaFim().isAfter(request.horaInicio())) {
            throw new BusinessException("O horário final deve ser posterior ao horário inicial.");
        }
        int duration = resolveDuration(request);
        if (duration <= 0) {
            throw new BusinessException("A duração prevista deve ser maior que zero.");
        }
    }

    private int resolveDuration(AgendamentoRequest request) {
        if (request.duracaoEstimadaMinutos() != null) {
            return request.duracaoEstimadaMinutos();
        }
        if (request.horaInicio() == null || request.horaFim() == null) {
            return 0;
        }
        return Math.toIntExact(Duration.between(request.horaInicio(), request.horaFim()).toMinutes());
    }

    private ReferenceContext validateReferences(AgendamentoRequest request, Long tenantId) {
        Cliente cliente = clienteRepository.findByIdScoped(request.clienteId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado no contexto autenticado."));
        if (cliente.getStatus() == StatusCliente.INATIVO) {
            throw new BusinessException("Reative o cliente antes de criar ou alterar um agendamento.");
        }

        Veiculo veiculo = null;
        if (request.veiculoId() != null) {
            veiculo = veiculoRepository.findByIdAndEmpresaId(request.veiculoId(), tenantId)
                    .orElseThrow(() -> new BusinessException("Veículo não encontrado no contexto autenticado."));
            if (veiculo.getCliente() == null || !cliente.getId().equals(veiculo.getCliente().getId())) {
                throw new BusinessException("O veículo informado não pertence ao cliente selecionado.");
            }
        }

        TipoAgendamento tipo = null;
        if (request.tipoAgendamentoId() != null) {
            tipo = tipoAgendamentoRepository.findByIdAndEmpresaId(request.tipoAgendamentoId(), tenantId)
                    .orElseThrow(() -> new BusinessException("Tipo de agendamento não encontrado no contexto autenticado."));
            if (!Boolean.TRUE.equals(tipo.getAtivo())) {
                throw new BusinessException("O tipo de agendamento informado está inativo.");
            }
        }

        return new ReferenceContext(cliente, veiculo, tipo);
    }

    private void ensureNoVehicleConflict(AgendamentoRequest request, Long currentId) {
        if (request.veiculoId() == null) return;
        List<Agendamento> conflitos = repository.findConflictingAgendamentos(
                request.veiculoId(),
                request.dataAgendamento(),
                request.horaInicio(),
                request.horaFim(),
                currentId);
        if (!conflitos.isEmpty()) {
            throw new BusinessException("Já existe um agendamento ativo para este veículo no mesmo período de horário.");
        }
    }

    private String generateNumber() {
        String prefix = "AGD-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-";
        for (int attempt = 0; attempt < MAX_NUMBER_ATTEMPTS; attempt++) {
            String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
            String candidate = prefix + suffix;
            if (!repository.existsByNumeroAgendamento(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Não foi possível gerar um número único para o agendamento.");
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private record ReferenceContext(Cliente cliente, Veiculo veiculo, TipoAgendamento tipo) {}
}
