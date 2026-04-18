package com.neritech.saas.agendamento.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade de tipos de agendamento
 */
@Entity
@Table(name = "agd_tipos_agendamento")
@Getter
@Setter
public class TipoAgendamento extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "cor_identificacao", length = 7)
    private String corIdentificacao;

    @Column(name = "icone", length = 50)
    private String icone;

    @Column(name = "duracao_padrao_minutos", nullable = false)
    private Integer duracaoPadraoMinutos;

    @Column(name = "permite_encaixe")
    private Boolean permiteEncaixe = false;

    @Column(name = "requer_orcamento")
    private Boolean requerOrcamento = false;

    @Column(name = "servicos_inclusos", columnDefinition = "TEXT")
    private String servicosInclusos; // JSON

    @Column(name = "observacoes_padrao", columnDefinition = "TEXT")
    private String observacoesPadrao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;

    @Column(name = "criado_por")
    private Long criadoPor;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getCorIdentificacao() {
        return this.corIdentificacao;
    }
    public void setCorIdentificacao(String corIdentificacao) {
        this.corIdentificacao = corIdentificacao;
    }
    public String getIcone() {
        return this.icone;
    }
    public void setIcone(String icone) {
        this.icone = icone;
    }
    public Integer getDuracaoPadraoMinutos() {
        return this.duracaoPadraoMinutos;
    }
    public void setDuracaoPadraoMinutos(Integer duracaoPadraoMinutos) {
        this.duracaoPadraoMinutos = duracaoPadraoMinutos;
    }
    public String getServicosInclusos() {
        return this.servicosInclusos;
    }
    public void setServicosInclusos(String servicosInclusos) {
        this.servicosInclusos = servicosInclusos;
    }
    public String getObservacoesPadrao() {
        return this.observacoesPadrao;
    }
    public void setObservacoesPadrao(String observacoesPadrao) {
        this.observacoesPadrao = observacoesPadrao;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }

    public Boolean getPermiteEncaixe() {
        return this.permiteEncaixe;
    }

    public void setPermiteEncaixe(Boolean permiteEncaixe) {
        this.permiteEncaixe = permiteEncaixe;
    }

    public Boolean getRequerOrcamento() {
        return this.requerOrcamento;
    }

    public void setRequerOrcamento(Boolean requerOrcamento) {
        this.requerOrcamento = requerOrcamento;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getOrdemExibicao() {
        return this.ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }
}
