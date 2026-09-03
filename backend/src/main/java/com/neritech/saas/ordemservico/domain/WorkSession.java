package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.ordemservico.domain.enums.WorkSessionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "os_work_sessions")
public class WorkSession extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "ordem_servico_id", nullable = false)
    private Long ordemServicoId;

    @Column(name = "item_os_servico_id", nullable = false)
    private Long itemServicoId;

    @Column(name = "technician_user_id", nullable = false)
    private Long technicianUserId;

    @Column(name = "source", nullable = false, length = 20)
    private String source = "WEB";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WorkSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "active_segment_started_at")
    private LocalDateTime activeSegmentStartedAt;

    @Column(name = "paused_at")
    private LocalDateTime pausedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "pause_reason", length = 40)
    private String pauseReason;

    @Column(name = "pause_note", length = 500)
    private String pauseNote;

    @Column(name = "elapsed_seconds", nullable = false)
    private Long elapsedSeconds = 0L;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getOrdemServicoId() {
        return ordemServicoId;
    }

    public void setOrdemServicoId(Long ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
    }

    public Long getItemServicoId() {
        return itemServicoId;
    }

    public void setItemServicoId(Long itemServicoId) {
        this.itemServicoId = itemServicoId;
    }

    public Long getTechnicianUserId() {
        return technicianUserId;
    }

    public void setTechnicianUserId(Long technicianUserId) {
        this.technicianUserId = technicianUserId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public WorkSessionStatus getStatus() {
        return status;
    }

    public void setStatus(WorkSessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getActiveSegmentStartedAt() {
        return activeSegmentStartedAt;
    }

    public void setActiveSegmentStartedAt(LocalDateTime activeSegmentStartedAt) {
        this.activeSegmentStartedAt = activeSegmentStartedAt;
    }

    public LocalDateTime getPausedAt() {
        return pausedAt;
    }

    public void setPausedAt(LocalDateTime pausedAt) {
        this.pausedAt = pausedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }

    public String getPauseNote() {
        return pauseNote;
    }

    public void setPauseNote(String pauseNote) {
        this.pauseNote = pauseNote;
    }

    public Long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(Long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
