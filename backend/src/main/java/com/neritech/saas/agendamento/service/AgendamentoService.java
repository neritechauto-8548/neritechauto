package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import com.neritech.saas.agendamento.mapper.AgendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantAccess;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service para Agendamento
 */
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final TipoAgendamentoRepository tipoAgendamentoRepository;
    private final AgendamentoMapper mapper;
    private final JdbcTemplate jdbc;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Long tenant = TenantAccess.require(request.empresaId());
        lockCalendar(tenant);
        assertReferences(tenant, request.clienteId(), request.veiculoId(),
                request.mecanicoAlocadoId(), request.mecanicoPreferidoId());
        assertResourceIntervalAvailable(tenant, request.recursosNecessarios(), request.status(),
                request.dataAgendamento(), request.horaInicio(), request.horaFim());
        if (request.dataAgendamento() != null && request.dataAgendamento().isBefore(java.time.LocalDate.now())) {
            throw new BusinessException("Não é permitido realizar agendamentos para datas retroativas.");
        }

        if (request.veiculoId() != null) {
            List<Agendamento> conflitos = repository.findConflictingAgendamentos(
                    tenant,
                    request.veiculoId(),
                    request.dataAgendamento(),
                    request.horaInicio(),
                    request.horaFim(),
                    null
            );
            if (!conflitos.isEmpty()) {
                throw new BusinessException("Já existe um agendamento ativo para este veículo no mesmo período de horário.");
            }
        }

        Agendamento agendamento = mapper.toEntity(request);

        // Buscar tipo de agendamento se fornecido
        if (request.tipoAgendamentoId() != null) {
            TipoAgendamento tipoAgendamento = tipoAgendamentoRepository.findById(request.tipoAgendamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de agendamento não encontrado"));
            if (!tenant.equals(tipoAgendamento.getEmpresaId())) {
                throw new EntityNotFoundException("Tipo de agendamento não encontrado");
            }
            agendamento.setTipoAgendamento(tipoAgendamento);
        }

        if (agendamento.getNumeroAgendamento() == null || agendamento.getNumeroAgendamento().isBlank()) {
            agendamento.setNumeroAgendamento("AGD-" + System.currentTimeMillis());
        }

        if (agendamento.getDuracaoEstimadaMinutos() == null) {
            agendamento.setDuracaoEstimadaMinutos(60);
        }

        agendamento.setAgendadoPor(1L); // TODO: Obter do contexto de segurança

        Agendamento salvo = repository.save(agendamento);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃƒÂ£o encontrado"));
        return mapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorEmpresa(Long empresaId) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Long tenant = TenantAccess.require(request.empresaId());
        lockCalendar(tenant);
        Agendamento agendamento = repository.findByIdAndEmpresaId(id, tenant)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃƒÂ£o encontrado"));
        assertReferences(tenant, agendamento.getClienteId(), agendamento.getVeiculoId(),
                request.mecanicoAlocadoId(), agendamento.getMecanicoPreferidoId());
        assertResourceIntervalAvailable(tenant, agendamento.getRecursosNecessarios(), request.status(),
                request.dataAgendamento(), request.horaInicio(), request.horaFim());

        // Validar se a data está sendo alterada para o passado
        if (request.dataAgendamento() != null && request.dataAgendamento().isBefore(java.time.LocalDate.now()) && !agendamento.getDataAgendamento().equals(request.dataAgendamento())) {
            throw new BusinessException("Não é permitido alterar a data do agendamento para uma data retroativa.");
        }

        Long veiculoIdToCheck = request.veiculoId() != null ? request.veiculoId() : agendamento.getVeiculoId();
        if (veiculoIdToCheck != null) {
            List<Agendamento> conflitos = repository.findConflictingAgendamentos(
                    tenant,
                    veiculoIdToCheck,
                    request.dataAgendamento(),
                    request.horaInicio(),
                    request.horaFim(),
                    id
            );
            if (!conflitos.isEmpty()) {
                throw new BusinessException("Já existe um agendamento ativo para este veículo no mesmo período de horário.");
            }
        }

        // Atualizar campos
        agendamento.setDataAgendamento(request.dataAgendamento());
        agendamento.setHoraInicio(request.horaInicio());
        agendamento.setHoraFim(request.horaFim());
        agendamento.setStatus(request.status());
        agendamento.setObservacoesInternas(request.observacoesInternas());
        agendamento.setMecanicoAlocadoId(request.mecanicoAlocadoId());
        agendamento.setAtualizadoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a

        Agendamento atualizado = repository.save(agendamento);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Long tenant = TenantAccess.requireCurrent();
        lockCalendar(tenant);
        Agendamento agendamento = repository.findByIdAndEmpresaId(id, tenant)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        repository.delete(agendamento);
    }

    private void lockCalendar(Long tenant) {
        jdbc.queryForObject("select id from empresa where id=? for update", Long.class, tenant);
    }

    private void assertReferences(Long tenant, Long clientId, Long vehicleId,
            Long employeeId, Long preferredEmployeeId) {
        Boolean client = jdbc.queryForObject("select exists(select 1 from cliente where id=? and empresa_id=?)",
                Boolean.class, clientId, tenant);
        if (!Boolean.TRUE.equals(client)) throw new EntityNotFoundException("Cliente não encontrado");
        if (vehicleId != null) {
            Boolean vehicle = jdbc.queryForObject("""
                    select exists(select 1 from veiculos
                    where id=? and empresa_id=? and cliente_id=?)
                    """, Boolean.class, vehicleId, tenant, clientId);
            if (!Boolean.TRUE.equals(vehicle)) throw new EntityNotFoundException("Veículo não encontrado");
        }
        if (employeeId != null) {
            Boolean employee = jdbc.queryForObject("select exists(select 1 from funcionarios where id=? and empresa_id=?)",
                    Boolean.class, employeeId, tenant);
            if (!Boolean.TRUE.equals(employee)) throw new EntityNotFoundException("Funcionário não encontrado");
        }
        if (preferredEmployeeId != null && !preferredEmployeeId.equals(employeeId)) {
            Boolean preferred = jdbc.queryForObject(
                    "select exists(select 1 from funcionarios where id=? and empresa_id=?)",
                    Boolean.class, preferredEmployeeId, tenant);
            if (!Boolean.TRUE.equals(preferred)) throw new EntityNotFoundException("Funcionário preferido não encontrado");
        }
    }

    private void assertResourceIntervalAvailable(Long tenant, String resources,
            com.neritech.saas.agendamento.domain.enums.StatusAgendamento status,
            java.time.LocalDate day, java.time.LocalTime start, java.time.LocalTime end) {
        if (resources == null || resources.isBlank() || resources.trim().equals("[]")
                || resources.trim().equals("{}") || status == null
                || !java.util.EnumSet.of(
                    com.neritech.saas.agendamento.domain.enums.StatusAgendamento.AGENDADO,
                    com.neritech.saas.agendamento.domain.enums.StatusAgendamento.CONFIRMADO,
                    com.neritech.saas.agendamento.domain.enums.StatusAgendamento.EM_ANDAMENTO).contains(status)) return;
        if (day == null || start == null || end == null || !end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horário inválido para recurso");
        }
        Boolean conflict = jdbc.queryForObject("""
                select exists (select 1 from agd_reservas_recursos_os r
                where r.empresa_id=? and r.estado in ('RESERVADO','OCUPADO')
                  and (r.estado='OCUPADO' or r.inicio<? and r.fim>?))
                """, Boolean.class, tenant, day.atTime(end), day.atTime(start));
        if (Boolean.TRUE.equals(conflict)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Reserva de OS já usa recurso neste horário; confira no Pátio");
        }
    }
}
