package com.neritech.saas.garantia.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa a resoluÃƒÂ§ÃƒÂ£o de uma reclamaÃƒÂ§ÃƒÂ£o de garantia
 */
@Entity
@Table(name = "gar_resolucoes_garantia")
@Getter
@Setter
public class ResolucaoGarantia extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamacao_id", nullable = false)
    private ReclamacaoGarantia reclamacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_resolucao", nullable = false, length = 30)
    private TipoResolucao tipoResolucao;

    @Column(name = "descricao_resolucao", columnDefinition = "TEXT", nullable = false)
    private String descricaoResolucao;

    @Column(name = "servicos_executados", columnDefinition = "TEXT")
    private String servicosExecutados; // JSON

    @Column(name = "produtos_fornecidos", columnDefinition = "TEXT")
    private String produtosFornecidos; // JSON

    @Column(name = "valor_servicos", precision = 10, scale = 2)
    private BigDecimal valorServicos = BigDecimal.ZERO;

    @Column(name = "valor_produtos", precision = 8, scale = 2)
    private BigDecimal valorProdutos = BigDecimal.ZERO;

    @Column(name = "valor_total_resolucao", precision = 10, scale = 2)
    private BigDecimal valorTotalResolucao = BigDecimal.ZERO;

    @Column(name = "valor_cobrado_cliente", precision = 8, scale = 2)
    private BigDecimal valorCobradoCliente = BigDecimal.ZERO;

    @Column(name = "desconto_concedido", precision = 8, scale = 2)
    private BigDecimal descontoConcedido = BigDecimal.ZERO;

    @Column(name = "justificativa_cobranca", columnDefinition = "TEXT")
    private String justificativaCobranca;

    @Column(name = "nova_garantia_gerada")
    private Boolean novaGarantiaGerada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nova_garantia_id")
    private Garantia novaGarantia;

    @Column(name = "prazo_nova_garantia_dias")
    private Integer prazoNovaGarantiaDias;

    @Column(name = "data_inicio_execucao")
    private LocalDateTime dataInicioExecucao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "tempo_resolucao_horas")
    private Integer tempoResolucaoHoras;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_executor_id")
    private Funcionario funcionarioExecutor;

    @Column(name = "equipe_execucao", columnDefinition = "TEXT")
    private String equipeExecucao; // JSON

    @Enumerated(EnumType.STRING)
    @Column(name = "qualidade_resolucao", length = 30)
    private QualidadeResolucao qualidadeResolucao;

    @Column(name = "cliente_satisfeito")
    private Boolean clienteSatisfeito;

    @Column(name = "observacoes_execucao", columnDefinition = "TEXT")
    private String observacoesExecucao;

    @Column(name = "fotos_apos_resolucao", columnDefinition = "TEXT")
    private String fotosAposResolucao; // JSON

    @Column(name = "documentos_comprobatorios", columnDefinition = "TEXT")
    private String documentosComprobatorios; // JSON

    @Column(name = "aprovada_gerencia")
    private Boolean aprovadaGerencia = false;

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    public ReclamacaoGarantia getReclamacao() {
        return this.reclamacao;
    }
    public void setReclamacao(ReclamacaoGarantia reclamacao) {
        this.reclamacao = reclamacao;
    }
    public TipoResolucao getTipoResolucao() {
        return this.tipoResolucao;
    }
    public void setTipoResolucao(TipoResolucao tipoResolucao) {
        this.tipoResolucao = tipoResolucao;
    }
    public String getDescricaoResolucao() {
        return this.descricaoResolucao;
    }
    public void setDescricaoResolucao(String descricaoResolucao) {
        this.descricaoResolucao = descricaoResolucao;
    }
    public String getServicosExecutados() {
        return this.servicosExecutados;
    }
    public void setServicosExecutados(String servicosExecutados) {
        this.servicosExecutados = servicosExecutados;
    }
    public String getProdutosFornecidos() {
        return this.produtosFornecidos;
    }
    public void setProdutosFornecidos(String produtosFornecidos) {
        this.produtosFornecidos = produtosFornecidos;
    }
    public String getJustificativaCobranca() {
        return this.justificativaCobranca;
    }
    public void setJustificativaCobranca(String justificativaCobranca) {
        this.justificativaCobranca = justificativaCobranca;
    }
    public Garantia getNovaGarantia() {
        return this.novaGarantia;
    }
    public void setNovaGarantia(Garantia novaGarantia) {
        this.novaGarantia = novaGarantia;
    }
    public Integer getPrazoNovaGarantiaDias() {
        return this.prazoNovaGarantiaDias;
    }
    public void setPrazoNovaGarantiaDias(Integer prazoNovaGarantiaDias) {
        this.prazoNovaGarantiaDias = prazoNovaGarantiaDias;
    }
    public LocalDateTime getDataInicioExecucao() {
        return this.dataInicioExecucao;
    }
    public void setDataInicioExecucao(LocalDateTime dataInicioExecucao) {
        this.dataInicioExecucao = dataInicioExecucao;
    }
    public LocalDateTime getDataConclusao() {
        return this.dataConclusao;
    }
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    public Integer getTempoResolucaoHoras() {
        return this.tempoResolucaoHoras;
    }
    public void setTempoResolucaoHoras(Integer tempoResolucaoHoras) {
        this.tempoResolucaoHoras = tempoResolucaoHoras;
    }
    public Funcionario getFuncionarioExecutor() {
        return this.funcionarioExecutor;
    }
    public void setFuncionarioExecutor(Funcionario funcionarioExecutor) {
        this.funcionarioExecutor = funcionarioExecutor;
    }
    public String getEquipeExecucao() {
        return this.equipeExecucao;
    }
    public void setEquipeExecucao(String equipeExecucao) {
        this.equipeExecucao = equipeExecucao;
    }
    public QualidadeResolucao getQualidadeResolucao() {
        return this.qualidadeResolucao;
    }
    public void setQualidadeResolucao(QualidadeResolucao qualidadeResolucao) {
        this.qualidadeResolucao = qualidadeResolucao;
    }
    public Boolean isClienteSatisfeito() {
        return this.clienteSatisfeito;
    }
    public void setClienteSatisfeito(Boolean clienteSatisfeito) {
        this.clienteSatisfeito = clienteSatisfeito;
    }
    public String getObservacoesExecucao() {
        return this.observacoesExecucao;
    }
    public void setObservacoesExecucao(String observacoesExecucao) {
        this.observacoesExecucao = observacoesExecucao;
    }
    public String getFotosAposResolucao() {
        return this.fotosAposResolucao;
    }
    public void setFotosAposResolucao(String fotosAposResolucao) {
        this.fotosAposResolucao = fotosAposResolucao;
    }
    public String getDocumentosComprobatorios() {
        return this.documentosComprobatorios;
    }
    public void setDocumentosComprobatorios(String documentosComprobatorios) {
        this.documentosComprobatorios = documentosComprobatorios;
    }
    public Long getAprovadaPor() {
        return this.aprovadaPor;
    }
    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }
    public LocalDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }
    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
}
