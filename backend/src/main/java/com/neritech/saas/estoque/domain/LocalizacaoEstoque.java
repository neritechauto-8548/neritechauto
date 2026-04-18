package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.estoque.domain.enums.TipoLocalizacao;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "localizacoes_estoque")
public class LocalizacaoEstoque extends TenantEntity {

    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_localizacao", length = 20)
    private TipoLocalizacao tipoLocalizacao;

    @Column(name = "setor", length = 50)
    private String setor;

    @Column(name = "corredor", length = 10)
    private String corredor;

    @Column(name = "prateleira", length = 10)
    private String prateleira;

    @Column(name = "posicao", length = 10)
    private String posicao;

    @Column(name = "capacidade_maxima", precision = 10, scale = 2)
    private BigDecimal capacidadeMaxima;

    @Column(name = "temperatura_controlada")
    private Boolean temperaturaControlada = false;

    @Column(name = "temperatura_min", precision = 4, scale = 1)
    private BigDecimal temperaturaMin;

    @Column(name = "temperatura_max", precision = 4, scale = 1)
    private BigDecimal temperaturaMax;

    @Column(name = "umidade_controlada")
    private Boolean umidadeControlada = false;

    @Column(name = "umidade_min", precision = 5, scale = 2)
    private BigDecimal umidadeMin;

    @Column(name = "umidade_max", precision = 5, scale = 2)
    private BigDecimal umidadeMax;

    @Column(name = "acesso_restrito")
    private Boolean acessoRestrito = false;

    @Column(name = "usuarios_acesso", columnDefinition = "jsonb")
    private String usuariosAcesso;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Getters and Setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public TipoLocalizacao getTipoLocalizacao() {
        return tipoLocalizacao;
    }

    public void setTipoLocalizacao(TipoLocalizacao tipoLocalizacao) {
        this.tipoLocalizacao = tipoLocalizacao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getCorredor() {
        return corredor;
    }

    public void setCorredor(String corredor) {
        this.corredor = corredor;
    }

    public String getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(String prateleira) {
        this.prateleira = prateleira;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public BigDecimal getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(BigDecimal capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Boolean getTemperaturaControlada() {
        return temperaturaControlada;
    }

    public void setTemperaturaControlada(Boolean temperaturaControlada) {
        this.temperaturaControlada = temperaturaControlada;
    }

    public BigDecimal getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(BigDecimal temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public BigDecimal getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(BigDecimal temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public Boolean getUmidadeControlada() {
        return umidadeControlada;
    }

    public void setUmidadeControlada(Boolean umidadeControlada) {
        this.umidadeControlada = umidadeControlada;
    }

    public BigDecimal getUmidadeMin() {
        return umidadeMin;
    }

    public void setUmidadeMin(BigDecimal umidadeMin) {
        this.umidadeMin = umidadeMin;
    }

    public BigDecimal getUmidadeMax() {
        return umidadeMax;
    }

    public void setUmidadeMax(BigDecimal umidadeMax) {
        this.umidadeMax = umidadeMax;
    }

    public Boolean getAcessoRestrito() {
        return acessoRestrito;
    }

    public void setAcessoRestrito(Boolean acessoRestrito) {
        this.acessoRestrito = acessoRestrito;
    }

    public String getUsuariosAcesso() {
        return usuariosAcesso;
    }

    public void setUsuariosAcesso(String usuariosAcesso) {
        this.usuariosAcesso = usuariosAcesso;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
