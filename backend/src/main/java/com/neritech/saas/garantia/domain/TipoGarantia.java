package com.neritech.saas.garantia.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entidade que representa os tipos de garantia configurÃƒÂ¡veis
 */
@Entity
@Table(name = "gar_tipos_garantia")
@Getter
@Setter
public class TipoGarantia extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "prazo_dias", nullable = false)
    private Integer prazoDias;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobertura", nullable = false, length = 30)
    private TipoCobertura tipoCobertura;

    @Column(name = "percentual_cobertura", precision = 5, scale = 2)
    private BigDecimal percentualCobertura = BigDecimal.valueOf(100.00);

    @Column(name = "valor_maximo_cobertura", precision = 10, scale = 2)
    private BigDecimal valorMaximoCobertura;

    @Column(name = "condicoes_aplicacao", columnDefinition = "TEXT")
    private String condicoesAplicacao;

    @Column(name = "restricoes", columnDefinition = "TEXT")
    private String restricoes;

    @Column(name = "documentacao_necessaria", columnDefinition = "TEXT")
    private String documentacaoNecessaria;

    @Column(name = "processo_acionamento", columnDefinition = "TEXT")
    private String processoAcionamento;

    @Column(name = "sla_atendimento_horas")
    private Integer slaAtendimentoHoras = 24;

    @Column(name = "transferivel")
    private Boolean transferivel = false;

    @Column(name = "renovavel")
    private Boolean renovavel = false;

    @Column(name = "custos_adicionais", columnDefinition = "TEXT")
    private String custosAdicionais;

    @Column(name = "exclusoes", columnDefinition = "TEXT")
    private String exclusoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "padrao_servicos")
    private Boolean padraoServicos = false;

    @Column(name = "padrao_produtos")
    private Boolean padraoProdutos = false;

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
    public Integer getPrazoDias() {
        return this.prazoDias;
    }
    public void setPrazoDias(Integer prazoDias) {
        this.prazoDias = prazoDias;
    }
    public TipoCobertura getTipoCobertura() {
        return this.tipoCobertura;
    }
    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }
    public BigDecimal getValorMaximoCobertura() {
        return this.valorMaximoCobertura;
    }
    public void setValorMaximoCobertura(BigDecimal valorMaximoCobertura) {
        this.valorMaximoCobertura = valorMaximoCobertura;
    }
    public String getCondicoesAplicacao() {
        return this.condicoesAplicacao;
    }
    public void setCondicoesAplicacao(String condicoesAplicacao) {
        this.condicoesAplicacao = condicoesAplicacao;
    }
    public String getRestricoes() {
        return this.restricoes;
    }
    public void setRestricoes(String restricoes) {
        this.restricoes = restricoes;
    }
    public String getDocumentacaoNecessaria() {
        return this.documentacaoNecessaria;
    }
    public void setDocumentacaoNecessaria(String documentacaoNecessaria) {
        this.documentacaoNecessaria = documentacaoNecessaria;
    }
    public String getProcessoAcionamento() {
        return this.processoAcionamento;
    }
    public void setProcessoAcionamento(String processoAcionamento) {
        this.processoAcionamento = processoAcionamento;
    }
    public String getCustosAdicionais() {
        return this.custosAdicionais;
    }
    public void setCustosAdicionais(String custosAdicionais) {
        this.custosAdicionais = custosAdicionais;
    }
    public String getExclusoes() {
        return this.exclusoes;
    }
    public void setExclusoes(String exclusoes) {
        this.exclusoes = exclusoes;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
