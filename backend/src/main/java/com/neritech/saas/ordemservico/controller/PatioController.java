package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.service.PatioService;
import com.neritech.saas.ordemservico.service.PatioTimerService;
import com.neritech.saas.ordemservico.service.PatioAdjustmentService;
import com.neritech.saas.agendamento.service.ReservaRecursoOsService;
import com.neritech.saas.ordemservico.service.PatioAlertService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/patio")
public class PatioController {
    private final PatioService service;
    private final PatioTimerService timers;
    private final PatioAdjustmentService adjustments;
    private final ReservaRecursoOsService reservations;
    private final PatioAlertService alerts;

    public PatioController(PatioService service, PatioTimerService timers, PatioAdjustmentService adjustments,
            ReservaRecursoOsService reservations, PatioAlertService alerts) {
        this.service = service; this.timers = timers; this.adjustments = adjustments;
        this.reservations = reservations; this.alerts = alerts;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('GERAL_USUARIO','OS_ALT_STATUS','OS_EDITAR')")
    public PatioService.Board board(@RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long responsavelId,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) Boolean atrasado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.board(search, statusId, responsavelId, prioridade, atrasado, page, size);
    }

    @GetMapping("/tv")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public PatioService.TvBoard tv() { return service.tv(); }

    @GetMapping("/recursos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public java.util.List<PatioService.Resource> resources() { return service.resources(); }

    @GetMapping("/recursos/reservas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public ReservaRecursoOsService.ReservationBoard reservations(@RequestParam java.time.LocalDate data) {
        return reservations.list(data);
    }

    @PostMapping("/recursos/{resourceId}/reservas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ReservaRecursoOsService.Reservation reserve(@PathVariable Long resourceId,
            @Valid @RequestBody ReservationRequest request) {
        return reservations.reserve(resourceId, request.osId(), request.inicio(), request.fim(),
                request.idempotencyKey());
    }

    @PutMapping("/recursos/reservas/{reservationId}/{action}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ReservaRecursoOsService.Reservation reservationAction(@PathVariable Long reservationId,
            @PathVariable String action, @Valid @RequestBody ReservationActionRequest request) {
        return reservations.transition(reservationId, action, request.versao());
    }

    @GetMapping("/alertas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public PatioAlertService.AlertBoard alerts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return alerts.list(page, size);
    }

    @GetMapping("/alertas/{alertId}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public java.util.List<PatioAlertService.AlertEvent> alertHistory(@PathVariable Long alertId) {
        return alerts.history(alertId);
    }

    @PutMapping("/alertas/{alertId}/{action}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public PatioAlertService.AlertView alertAction(@PathVariable Long alertId, @PathVariable String action,
            @Valid @RequestBody AlertActionRequest request) {
        return alerts.transition(alertId, action, request.versao(), request.motivo(), request.adiadoAte());
    }

    @GetMapping("/equipe")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public java.util.List<PatioService.TeamMember> team() { return service.team(); }

    @PutMapping("/itens/{itemId}/responsavel")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_ALT_FUNCIONARIO')")
    public PatioService.Assignment assign(@PathVariable Long itemId, @Valid @RequestBody AssignRequest request) {
        return service.assign(itemId, request.funcionarioId(), request.versao());
    }

    @GetMapping("/itens/{itemId}/apontamentos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public java.util.List<PatioTimerService.TimerView> history(@PathVariable Long itemId) {
        return timers.history(itemId);
    }

    @GetMapping("/itens/{itemId}/ajustes")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public java.util.List<PatioAdjustmentService.AdjustmentView> adjustments(@PathVariable Long itemId) {
        return adjustments.history(itemId);
    }

    @PostMapping("/itens/{itemId}/apontamentos/{timeId}/ajustes")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public PatioAdjustmentService.AdjustmentView adjust(@PathVariable Long itemId, @PathVariable Long timeId,
            @Valid @RequestBody AdjustmentRequest request) {
        return adjustments.adjust(itemId, timeId, request.deltaMinutos(), request.motivo(),
                request.versao(), request.idempotencyKey());
    }

    @GetMapping("/me/funcionario")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public PatioTimerService.EmployeeIdentity myEmployee() { return timers.myEmployee(); }

    @PostMapping("/itens/{itemId}/timer/{action}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public PatioTimerService.TimerView timer(@PathVariable Long itemId, @PathVariable String action,
            @Valid @RequestBody TimerRequest request) {
        return timers.command(itemId, action, request.idempotencyKey());
    }

    @PutMapping("/{osId}/etapa")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_ALT_STATUS')")
    public PatioService.MoveResult move(@PathVariable Long osId, @Valid @RequestBody MoveRequest request) {
        return service.move(osId, request.statusId(), request.versao(), request.motivo());
    }

    public record MoveRequest(@NotNull Long statusId, @NotNull Integer versao, String motivo) {}
    public record AssignRequest(@NotNull Long funcionarioId, @NotNull Integer versao) {}
    public record TimerRequest(@NotNull java.util.UUID idempotencyKey) {}
    public record AdjustmentRequest(@NotNull Integer deltaMinutos, @NotNull String motivo,
            @NotNull Integer versao, @NotNull java.util.UUID idempotencyKey) {}
    public record ReservationRequest(@NotNull Long osId, @NotNull java.time.LocalDateTime inicio,
            @NotNull java.time.LocalDateTime fim, @NotNull java.util.UUID idempotencyKey) {}
    public record ReservationActionRequest(@NotNull Integer versao) {}
    public record AlertActionRequest(@NotNull Integer versao, String motivo,
            java.time.LocalDateTime adiadoAte) {}
}
