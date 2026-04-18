package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.TipoPerda;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "perdas_estoque")
public class PerdaEstoque extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "lote_numero", length = 50)
    private String loteNumero;

    @Column(name = "quantidade_perdida", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadePerdida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_perda", length = 30)
    private TipoPerda tipoPerda;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "valor_perda", precision = 10, scale = 2)
    private BigDecimal valorPerda;

    @Column(name = "responsavel_perda")
    private String responsavelPerda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizacao_id")
    private LocalizacaoEstoque localizacao;

    @Column(name = "foi_segurado")
    private Boolean foiSegurado = false;

    @Column(name = "numero_sinistro", length = 50)
    private String numeroSinistro;

    @Column(name = "valor_indenizado", precision = 10, scale = 2)
    private BigDecimal valorIndenizado;

    @Column(name = "data_ocorrencia", nullable = false)
    private LocalDate dataOcorrencia;

    @Column(name = "data_descoberta")
    private LocalDate dataDescoberta;

    @Column(name = "boletim_ocorrencia", length = 100)
    private String boletimOcorrencia;

    @Column(name = "fotos_comprovantes_url", columnDefinition = "jsonb")
    private String fotosComprovantesUrl;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "usuario_cadastro")
    private Long usuarioCadastro;

    // Getters and Setters
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getLoteNumero() {
        return loteNumero;
    }

    public void setLoteNumero(String loteNumero) {
        this.loteNumero = loteNumero;
    }

    public BigDecimal getQuantidadePerdida() {
        return quantidadePerdida;
    }

    public void setQuantidadePerdida(BigDecimal quantidadePerdida) {
        this.quantidadePerdida = quantidadePerdida;
    }

    public TipoPerda getTipoPerda() {
        return tipoPerda;
    }

    public void setTipoPerda(TipoPerda tipoPerda) {
        this.tipoPerda = tipoPerda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorPerda() {
        return valorPerda;
    }

    public void setValorPerda(BigDecimal valorPerda) {
        this.valorPerda = valorPerda;
    }

    public String getResponsavelPerda() {
        return responsavelPerda;
    }

    public void setResponsavelPerda(String responsavelPerda) {
        this.responsavelPerda = responsavelPerda;
    }

    public LocalizacaoEstoque getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoEstoque localizacao) {
        this.localizacao = localizacao;
    }

    public Boolean getFoiSegurado() {
        return foiSegurado;
    }

    public void setFoiSegurado(Boolean foiSegurado) {
        this.foiSegurado = foiSegurado;
    }

    public String getNumeroSinistro() {
        return numeroSinistro;
    }

    public void setNumeroSinistro(String numeroSinistro) {
        this.numeroSinistro = numeroSinistro;
    }

    public BigDecimal getValorIndenizado() {
        return valorIndenizado;
    }

    public void setValorIndenizado(BigDecimal valorIndenizado) {
        this.valorIndenizado = valorIndenizado;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public LocalDate getDataDescoberta() {
        return dataDescoberta;
    }

    public void setDataDescoberta(LocalDate dataDescoberta) {
        this.dataDescoberta = dataDescoberta;
    }

    public String getBoletimOcorrencia() {
        return boletimOcorrencia;
    }

    public void setBoletimOcorrencia(String boletimOcorrencia) {
        this.boletimOcorrencia = boletimOcorrencia;
    }

    public String getFotosComprovantesUrl() {
        return fotosComprovantesUrl;
    }

    public void setFotosComprovantesUrl(String fotosComprovantesUrl) {
        this.fotosComprovantesUrl = fotosComprovantesUrl;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getAprovadoPor() {
        return aprovadoPor;
    }

    public void setAprovadoPor(Long aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public Long getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Long usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }
}
