package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.TipoRecurso;
import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agd_recursos_agenda")
@Getter
@Setter
public class RecursoAgenda extends TenantEntity {

    @Column(name = "nome_recurso", nullable = false, length = 100)
    private String nomeRecurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso", length = 30, nullable = false)
    private TipoRecurso tipoRecurso;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "capacidade_simultanea")
    private Integer capacidadeSimultanea = 1;

    @Column(name = "localizacao", length = 255)
    private String localizacao;

    @Column(name = "disponivel")
    private Boolean disponivel = true;

    @Column(name = "requer_agendamento")
    private Boolean requerAgendamento = true;

    @Column(name = "tempo_setup_minutos")
    private Integer tempoSetupMinutos = 0;

    @Column(name = "tempo_cleanup_minutos")
    private Integer tempoCleanupMinutos = 0;

    @Column(name = "custo_hora", precision = 8, scale = 2)
    private BigDecimal custoHora = BigDecimal.ZERO;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    public String getNomeRecurso() {
        return this.nomeRecurso;
    }
    public void setNomeRecurso(String nomeRecurso) {
        this.nomeRecurso = nomeRecurso;
    }
    public TipoRecurso getTipoRecurso() {
        return this.tipoRecurso;
    }
    public void setTipoRecurso(TipoRecurso tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getLocalizacao() {
        return this.localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getResponsavelId() {
        return this.responsavelId;
    }
    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }
}
