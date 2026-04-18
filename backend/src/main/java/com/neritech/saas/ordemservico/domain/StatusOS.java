package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "status_os")
public class StatusOS extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "cor_identificacao", length = 7)
    private String corIdentificacao;

    @Column(name = "icone", length = 50)
    private String icone;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    @Column(name = "permite_edicao")
    private Boolean permiteEdicao = true;

    @Column(name = "notifica_cliente")
    private Boolean notificaCliente = false;

    @Column(name = "template_notificacao_id")
    private Long templateNotificacaoId;

    @Column(name = "exige_aprovacao")
    private Boolean exigeAprovacao = false;

    @Column(name = "finaliza_os")
    private Boolean finalizaOS = false;

    @Column(name = "cancela_os")
    private Boolean cancelaOS = false;

    @Column(name = "sistema")
    private Boolean sistema = false;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorIdentificacao() {
        return corIdentificacao;
    }

    public void setCorIdentificacao(String corIdentificacao) {
        this.corIdentificacao = corIdentificacao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public Boolean getPermiteEdicao() {
        return permiteEdicao;
    }

    public void setPermiteEdicao(Boolean permiteEdicao) {
        this.permiteEdicao = permiteEdicao;
    }

    public Boolean getNotificaCliente() {
        return notificaCliente;
    }

    public void setNotificaCliente(Boolean notificaCliente) {
        this.notificaCliente = notificaCliente;
    }

    public Long getTemplateNotificacaoId() {
        return templateNotificacaoId;
    }

    public void setTemplateNotificacaoId(Long templateNotificacaoId) {
        this.templateNotificacaoId = templateNotificacaoId;
    }

    public Boolean getExigeAprovacao() {
        return exigeAprovacao;
    }

    public void setExigeAprovacao(Boolean exigeAprovacao) {
        this.exigeAprovacao = exigeAprovacao;
    }

    public Boolean getFinalizaOS() {
        return finalizaOS;
    }

    public void setFinalizaOS(Boolean finalizaOS) {
        this.finalizaOS = finalizaOS;
    }

    public Boolean getCancelaOS() {
        return cancelaOS;
    }

    public void setCancelaOS(Boolean cancelaOS) {
        this.cancelaOS = cancelaOS;
    }

    public Boolean getSistema() {
        return sistema;
    }

    public void setSistema(Boolean sistema) {
        this.sistema = sistema;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
