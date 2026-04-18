package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.PeriodoPreferido;
import com.neritech.saas.agendamento.domain.enums.StatusListaEspera;
import com.neritech.saas.agendamento.domain.enums.UrgenciaListaEspera;
import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agd_lista_espera")
@Getter
@Setter
public class ListaEspera extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "veiculo_id", nullable = false)
    private Long veiculoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_agendamento_id")
    private TipoAgendamento tipoAgendamento;

    @Column(name = "data_preferida")
    private LocalDate dataPreferida;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodo_preferido", length = 30)
    private PeriodoPreferido periodoPreferido;

    @Column(name = "servicos_desejados", columnDefinition = "TEXT")
    private String servicosDesejados;

    @Column(name = "mecanico_preferido_id")
    private Long mecanicoPreferidoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgencia", length = 30, nullable = false)
    private UrgenciaListaEspera urgencia;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "notificar_disponibilidade")
    private Boolean notificarDisponibilidade = true;

    @Column(name = "telefone_contato", nullable = false, length = 20)
    private String telefoneContato;

    @Column(name = "email_contato", length = 255)
    private String emailContato;

    @Column(name = "whatsapp_contato", length = 20)
    private String whatsappContato;

    @Column(name = "raio_disponibilidade_km")
    private Integer raioDisponibilidadeKm = 0;

    @Column(name = "flexibilidade_data_dias")
    private Integer flexibilidadeDataDias = 7;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private StatusListaEspera status;

    @Column(name = "data_notificacao")
    private LocalDateTime dataNotificacao;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_gerado_id")
    private Agendamento agendamentoGerado;

    @Column(name = "posicao_lista")
    private Integer posicaoLista;

    @Column(name = "cadastrado_por")
    private Long cadastradoPor;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
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
    public void setTipoAgendamento(TipoAgendamento tipoAgendamento) {
        this.tipoAgendamento = tipoAgendamento;
    }
    public LocalDate getDataPreferida() {
        return this.dataPreferida;
    }
    public void setDataPreferida(LocalDate dataPreferida) {
        this.dataPreferida = dataPreferida;
    }
    public PeriodoPreferido getPeriodoPreferido() {
        return this.periodoPreferido;
    }
    public void setPeriodoPreferido(PeriodoPreferido periodoPreferido) {
        this.periodoPreferido = periodoPreferido;
    }
    public String getServicosDesejados() {
        return this.servicosDesejados;
    }
    public void setServicosDesejados(String servicosDesejados) {
        this.servicosDesejados = servicosDesejados;
    }
    public Long getMecanicoPreferidoId() {
        return this.mecanicoPreferidoId;
    }
    public void setMecanicoPreferidoId(Long mecanicoPreferidoId) {
        this.mecanicoPreferidoId = mecanicoPreferidoId;
    }
    public UrgenciaListaEspera getUrgencia() {
        return this.urgencia;
    }
    public void setUrgencia(UrgenciaListaEspera urgencia) {
        this.urgencia = urgencia;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public String getTelefoneContato() {
        return this.telefoneContato;
    }
    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }
    public String getEmailContato() {
        return this.emailContato;
    }
    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
    public String getWhatsappContato() {
        return this.whatsappContato;
    }
    public void setWhatsappContato(String whatsappContato) {
        this.whatsappContato = whatsappContato;
    }
    public StatusListaEspera getStatus() {
        return this.status;
    }
    public void setStatus(StatusListaEspera status) {
        this.status = status;
    }
    public LocalDateTime getDataNotificacao() {
        return this.dataNotificacao;
    }
    public void setDataNotificacao(LocalDateTime dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }
    public LocalDateTime getDataExpiracao() {
        return this.dataExpiracao;
    }
    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
    public Agendamento getAgendamentoGerado() {
        return this.agendamentoGerado;
    }
    public void setAgendamentoGerado(Agendamento agendamentoGerado) {
        this.agendamentoGerado = agendamentoGerado;
    }
    public Integer getPosicaoLista() {
        return this.posicaoLista;
    }
    public void setPosicaoLista(Integer posicaoLista) {
        this.posicaoLista = posicaoLista;
    }
    public Long getCadastradoPor() {
        return this.cadastradoPor;
    }
    public void setCadastradoPor(Long cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }
}
