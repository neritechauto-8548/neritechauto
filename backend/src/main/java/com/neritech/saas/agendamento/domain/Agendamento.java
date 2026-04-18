package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.MetodoConfirmacao;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade principal de agendamentos
 */
@Entity
@Table(name = "agd_agendamentos")
@Getter
@Setter
public class Agendamento extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "numero_agendamento", unique = true, nullable = false, length = 20)
    private String numeroAgendamento;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "veiculo_id")
    private Long veiculoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_agendamento_id")
    private TipoAgendamento tipoAgendamento;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Column(name = "duracao_estimada_minutos", nullable = false)
    private Integer duracaoEstimadaMinutos;

    @Column(name = "servicos_solicitados", columnDefinition = "TEXT")
    private String servicosSolicitados; // JSON

    @Column(name = "problema_relatado", columnDefinition = "TEXT")
    private String problemaRelatado;

    @Column(name = "observacoes_cliente", columnDefinition = "TEXT")
    private String observacoesCliente;

    @Column(name = "observacoes_internas", columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(name = "mecanico_preferido_id")
    private Long mecanicoPreferidoId;

    @Column(name = "mecanico_alocado_id")
    private Long mecanicoAlocadoId;

    @Column(name = "recursos_necessarios", columnDefinition = "TEXT")
    private String recursosNecessarios; // JSON

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private StatusAgendamento status;

    @Column(name = "confirmado_cliente")
    private Boolean confirmadoCliente = false;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_confirmacao", length = 30)
    private MetodoConfirmacao metodoConfirmacao;

    @Column(name = "lembrete_enviado")
    private Boolean lembreteEnviado = false;

    @Column(name = "data_lembrete")
    private LocalDateTime dataLembrete;

    @Column(name = "chegada_cliente")
    private LocalDateTime chegadaCliente;

    @Column(name = "inicio_atendimento")
    private LocalDateTime inicioAtendimento;

    @Column(name = "fim_atendimento")
    private LocalDateTime fimAtendimento;

    @Column(name = "avaliacao_atendimento")
    private Integer avaliacaoAtendimento;

    @Column(name = "comentario_avaliacao", columnDefinition = "TEXT")
    private String comentarioAvaliacao;

    @Column(name = "ordem_servico_gerada_id")
    private Long ordemServicoGeradaId;

    @Column(name = "valor_estimado", precision = 10, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "forma_pagamento_preferida_id")
    private Long formaPagamentoPreferidaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_agendamento", length = 30, nullable = false)
    private CanalAgendamento canalAgendamento;

    @Column(name = "agendado_por")
    private Long agendadoPor;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    public void setTipoAgendamento(TipoAgendamento tipoAgendamento) {
        this.tipoAgendamento = tipoAgendamento;
    }

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getNumeroAgendamento() {
        return this.numeroAgendamento;
    }
    public void setNumeroAgendamento(String numeroAgendamento) {
        this.numeroAgendamento = numeroAgendamento;
    }
    public Long getClienteId() {
        return this.clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public Long getVeiculoId() {
        return this.veiculoId;
    }
    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }
    public TipoAgendamento getTipoAgendamento() {
        return this.tipoAgendamento;
    }
    public LocalDate getDataAgendamento() {
        return this.dataAgendamento;
    }
    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    public LocalTime getHoraFim() {
        return this.horaFim;
    }
    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
    public Integer getDuracaoEstimadaMinutos() {
        return this.duracaoEstimadaMinutos;
    }
    public void setDuracaoEstimadaMinutos(Integer duracaoEstimadaMinutos) {
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
    }
    public String getServicosSolicitados() {
        return this.servicosSolicitados;
    }
    public void setServicosSolicitados(String servicosSolicitados) {
        this.servicosSolicitados = servicosSolicitados;
    }
    public String getProblemaRelatado() {
        return this.problemaRelatado;
    }
    public void setProblemaRelatado(String problemaRelatado) {
        this.problemaRelatado = problemaRelatado;
    }
    public String getObservacoesCliente() {
        return this.observacoesCliente;
    }
    public void setObservacoesCliente(String observacoesCliente) {
        this.observacoesCliente = observacoesCliente;
    }
    public String getObservacoesInternas() {
        return this.observacoesInternas;
    }
    public void setObservacoesInternas(String observacoesInternas) {
        this.observacoesInternas = observacoesInternas;
    }
    public Long getMecanicoPreferidoId() {
        return this.mecanicoPreferidoId;
    }
    public void setMecanicoPreferidoId(Long mecanicoPreferidoId) {
        this.mecanicoPreferidoId = mecanicoPreferidoId;
    }
    public Long getMecanicoAlocadoId() {
        return this.mecanicoAlocadoId;
    }
    public void setMecanicoAlocadoId(Long mecanicoAlocadoId) {
        this.mecanicoAlocadoId = mecanicoAlocadoId;
    }
    public String getRecursosNecessarios() {
        return this.recursosNecessarios;
    }
    public void setRecursosNecessarios(String recursosNecessarios) {
        this.recursosNecessarios = recursosNecessarios;
    }
    public StatusAgendamento getStatus() {
        return this.status;
    }
    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
    public LocalDateTime getDataConfirmacao() {
        return this.dataConfirmacao;
    }
    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
    public MetodoConfirmacao getMetodoConfirmacao() {
        return this.metodoConfirmacao;
    }
    public void setMetodoConfirmacao(MetodoConfirmacao metodoConfirmacao) {
        this.metodoConfirmacao = metodoConfirmacao;
    }
    public LocalDateTime getDataLembrete() {
        return this.dataLembrete;
    }
    public void setDataLembrete(LocalDateTime dataLembrete) {
        this.dataLembrete = dataLembrete;
    }
    public LocalDateTime getChegadaCliente() {
        return this.chegadaCliente;
    }
    public void setChegadaCliente(LocalDateTime chegadaCliente) {
        this.chegadaCliente = chegadaCliente;
    }
    public LocalDateTime getInicioAtendimento() {
        return this.inicioAtendimento;
    }
    public void setInicioAtendimento(LocalDateTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }
    public LocalDateTime getFimAtendimento() {
        return this.fimAtendimento;
    }
    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }
    public Integer getAvaliacaoAtendimento() {
        return this.avaliacaoAtendimento;
    }
    public void setAvaliacaoAtendimento(Integer avaliacaoAtendimento) {
        this.avaliacaoAtendimento = avaliacaoAtendimento;
    }
    public String getComentarioAvaliacao() {
        return this.comentarioAvaliacao;
    }
    public void setComentarioAvaliacao(String comentarioAvaliacao) {
        this.comentarioAvaliacao = comentarioAvaliacao;
    }
    public Long getOrdemServicoGeradaId() {
        return this.ordemServicoGeradaId;
    }
    public void setOrdemServicoGeradaId(Long ordemServicoGeradaId) {
        this.ordemServicoGeradaId = ordemServicoGeradaId;
    }
    public BigDecimal getValorEstimado() {
        return this.valorEstimado;
    }
    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }
    public Long getFormaPagamentoPreferidaId() {
        return this.formaPagamentoPreferidaId;
    }
    public void setFormaPagamentoPreferidaId(Long formaPagamentoPreferidaId) {
        this.formaPagamentoPreferidaId = formaPagamentoPreferidaId;
    }
    public CanalAgendamento getCanalAgendamento() {
        return this.canalAgendamento;
    }
    public void setCanalAgendamento(CanalAgendamento canalAgendamento) {
        this.canalAgendamento = canalAgendamento;
    }
    public Long getAgendadoPor() {
        return this.agendadoPor;
    }
    public void setAgendadoPor(Long agendadoPor) {
        this.agendadoPor = agendadoPor;
    }
    public Long getAtualizadoPor() {
        return this.atualizadoPor;
    }
    public void setAtualizadoPor(Long atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
}
