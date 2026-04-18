package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.ordemservico.domain.enums.MetodoAprovacao;
import com.neritech.saas.ordemservico.domain.enums.NivelCombustivel;
import com.neritech.saas.ordemservico.domain.enums.PrioridadeOS;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "numero_os", nullable = false, length = 20)
    private String numeroOS;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "veiculo_id")
    private Long veiculoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private StatusOS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_os", nullable = false, length = 20)
    private TipoOS tipoOS;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", length = 20)
    private PrioridadeOS prioridade;

    @Column(name = "data_abertura")
    private LocalDateTime dataAbertura;

    @Column(name = "data_promessa")
    private LocalDateTime dataPromessa;

    @Column(name = "data_inicio_execucao")
    private LocalDateTime dataInicioExecucao;

    @Column(name = "data_fim_execucao")
    private LocalDateTime dataFimExecucao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "quilometragem_entrada")
    private Integer quilometragemEntrada;

    @Column(name = "quilometragem_saida")
    private Integer quilometragemSaida;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_combustivel_entrada", length = 10)
    private NivelCombustivel nivelCombustivelEntrada;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_combustivel_saida", length = 10)
    private NivelCombustivel nivelCombustivelSaida;

    @Column(name = "consultor_responsavel_id")
    private Long consultorResponsavelId;

    @Column(name = "mecanico_responsavel_id")
    private Long mecanicoResponsavelId;

    @Column(name = "equipe_execucao", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String equipeExecucao;

    @Column(name = "problema_relatado", columnDefinition = "TEXT")
    private String problemaRelatado;

    @Column(name = "solucao_aplicada", columnDefinition = "TEXT")
    private String solucaoAplicada;

    @Column(name = "observacoes_internas", columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(name = "observacoes_cliente", columnDefinition = "TEXT")
    private String observacoesCliente;

    @Column(name = "valor_servicos", precision = 10, scale = 2)
    private BigDecimal valorServicos = BigDecimal.ZERO;

    @Column(name = "valor_produtos", precision = 10, scale = 2)
    private BigDecimal valorProdutos = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_acrescimo", precision = 10, scale = 2)
    private BigDecimal valorAcrescimo = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "forma_pagamento_id")
    private Long formaPagamentoId;

    @Column(name = "condicao_pagamento_id")
    private Long condicaoPagamentoId;

    @Column(name = "valor_entrada", precision = 10, scale = 2)
    private BigDecimal valorEntrada = BigDecimal.ZERO;

    @Column(name = "valor_financiado", precision = 10, scale = 2)
    private BigDecimal valorFinanciado = BigDecimal.ZERO;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente = false;

    @Column(name = "data_aprovacao_cliente")
    private LocalDateTime dataAprovacaoCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_aprovacao", length = 20)
    private MetodoAprovacao metodoAprovacao;

    @Column(name = "assinatura_digital_cliente", columnDefinition = "TEXT")
    private String assinaturaDigitalCliente;

    @Column(name = "garantia_dias")
    private Integer garantiaDias = 90;

    @Column(name = "data_vencimento_garantia")
    private LocalDate dataVencimentoGarantia;

    @Column(name = "nfe_emitida")
    private Boolean nfeEmitida = false;

    @Column(name = "numero_nfe", length = 20)
    private String numeroNFe;

    @Column(name = "chave_nfe", length = 44)
    private String chaveNFe;

    @Column(name = "url_danfe", length = 500)
    private String urlDanfe;

    @Column(name = "nota_avaliacao_cliente")
    private Integer notaAvaliacaoCliente;

    @Column(name = "comentario_avaliacao_cliente", columnDefinition = "TEXT")
    private String comentarioAvaliacaoCliente;

    @Column(name = "data_avaliacao_cliente")
    private LocalDateTime dataAvaliacaoCliente;

    @Column(name = "tempo_total_execucao_minutos")
    private Integer tempoTotalExecucaoMinutos;

    @Column(name = "margem_lucro_realizada", precision = 5, scale = 2)
    private BigDecimal margemLucroRealizada;

    @Column(name = "custo_total_real", precision = 10, scale = 2)
    private BigDecimal custoTotalReal;

    @Column(name = "rentabilidade_os", precision = 10, scale = 2)
    private BigDecimal rentabilidadeOS;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNumeroOS() {
        return numeroOS;
    }

    public void setNumeroOS(String numeroOS) {
        this.numeroOS = numeroOS;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public StatusOS getStatus() {
        return status;
    }

    public void setStatus(StatusOS status) {
        this.status = status;
    }

    public TipoOS getTipoOS() {
        return tipoOS;
    }

    public void setTipoOS(TipoOS tipoOS) {
        this.tipoOS = tipoOS;
    }

    public PrioridadeOS getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeOS prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataPromessa() {
        return dataPromessa;
    }

    public void setDataPromessa(LocalDateTime dataPromessa) {
        this.dataPromessa = dataPromessa;
    }

    public LocalDateTime getDataInicioExecucao() {
        return dataInicioExecucao;
    }

    public void setDataInicioExecucao(LocalDateTime dataInicioExecucao) {
        this.dataInicioExecucao = dataInicioExecucao;
    }

    public LocalDateTime getDataFimExecucao() {
        return dataFimExecucao;
    }

    public void setDataFimExecucao(LocalDateTime dataFimExecucao) {
        this.dataFimExecucao = dataFimExecucao;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Integer getQuilometragemEntrada() {
        return quilometragemEntrada;
    }

    public void setQuilometragemEntrada(Integer quilometragemEntrada) {
        this.quilometragemEntrada = quilometragemEntrada;
    }

    public Integer getQuilometragemSaida() {
        return quilometragemSaida;
    }

    public void setQuilometragemSaida(Integer quilometragemSaida) {
        this.quilometragemSaida = quilometragemSaida;
    }

    public NivelCombustivel getNivelCombustivelEntrada() {
        return nivelCombustivelEntrada;
    }

    public void setNivelCombustivelEntrada(NivelCombustivel nivelCombustivelEntrada) {
        this.nivelCombustivelEntrada = nivelCombustivelEntrada;
    }

    public NivelCombustivel getNivelCombustivelSaida() {
        return nivelCombustivelSaida;
    }

    public void setNivelCombustivelSaida(NivelCombustivel nivelCombustivelSaida) {
        this.nivelCombustivelSaida = nivelCombustivelSaida;
    }

    public Long getConsultorResponsavelId() {
        return consultorResponsavelId;
    }

    public void setConsultorResponsavelId(Long consultorResponsavelId) {
        this.consultorResponsavelId = consultorResponsavelId;
    }

    public Long getMecanicoResponsavelId() {
        return mecanicoResponsavelId;
    }

    public void setMecanicoResponsavelId(Long mecanicoResponsavelId) {
        this.mecanicoResponsavelId = mecanicoResponsavelId;
    }

    public String getEquipeExecucao() {
        return equipeExecucao;
    }

    public void setEquipeExecucao(String equipeExecucao) {
        this.equipeExecucao = equipeExecucao;
    }

    public String getProblemaRelatado() {
        return problemaRelatado;
    }

    public void setProblemaRelatado(String problemaRelatado) {
        this.problemaRelatado = problemaRelatado;
    }

    public String getSolucaoAplicada() {
        return solucaoAplicada;
    }

    public void setSolucaoAplicada(String solucaoAplicada) {
        this.solucaoAplicada = solucaoAplicada;
    }

    public String getObservacoesInternas() {
        return observacoesInternas;
    }

    public void setObservacoesInternas(String observacoesInternas) {
        this.observacoesInternas = observacoesInternas;
    }

    public String getObservacoesCliente() {
        return observacoesCliente;
    }

    public void setObservacoesCliente(String observacoesCliente) {
        this.observacoesCliente = observacoesCliente;
    }

    public BigDecimal getValorServicos() {
        return valorServicos;
    }

    public void setValorServicos(BigDecimal valorServicos) {
        this.valorServicos = valorServicos;
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public void setValorProdutos(BigDecimal valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorAcrescimo() {
        return valorAcrescimo;
    }

    public void setValorAcrescimo(BigDecimal valorAcrescimo) {
        this.valorAcrescimo = valorAcrescimo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Long formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public Long getCondicaoPagamentoId() {
        return condicaoPagamentoId;
    }

    public void setCondicaoPagamentoId(Long condicaoPagamentoId) {
        this.condicaoPagamentoId = condicaoPagamentoId;
    }

    public BigDecimal getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(BigDecimal valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public BigDecimal getValorFinanciado() {
        return valorFinanciado;
    }

    public void setValorFinanciado(BigDecimal valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    public Boolean getAprovadoCliente() {
        return aprovadoCliente;
    }

    public void setAprovadoCliente(Boolean aprovadoCliente) {
        this.aprovadoCliente = aprovadoCliente;
    }

    public LocalDateTime getDataAprovacaoCliente() {
        return dataAprovacaoCliente;
    }

    public void setDataAprovacaoCliente(LocalDateTime dataAprovacaoCliente) {
        this.dataAprovacaoCliente = dataAprovacaoCliente;
    }

    public MetodoAprovacao getMetodoAprovacao() {
        return metodoAprovacao;
    }

    public void setMetodoAprovacao(MetodoAprovacao metodoAprovacao) {
        this.metodoAprovacao = metodoAprovacao;
    }

    public String getAssinaturaDigitalCliente() {
        return assinaturaDigitalCliente;
    }

    public void setAssinaturaDigitalCliente(String assinaturaDigitalCliente) {
        this.assinaturaDigitalCliente = assinaturaDigitalCliente;
    }

    public Integer getGarantiaDias() {
        return garantiaDias;
    }

    public void setGarantiaDias(Integer garantiaDias) {
        this.garantiaDias = garantiaDias;
    }

    public LocalDate getDataVencimentoGarantia() {
        return dataVencimentoGarantia;
    }

    public void setDataVencimentoGarantia(LocalDate dataVencimentoGarantia) {
        this.dataVencimentoGarantia = dataVencimentoGarantia;
    }

    public Boolean getNfeEmitida() {
        return nfeEmitida;
    }

    public void setNfeEmitida(Boolean nfeEmitida) {
        this.nfeEmitida = nfeEmitida;
    }

    public String getNumeroNFe() {
        return numeroNFe;
    }

    public void setNumeroNFe(String numeroNFe) {
        this.numeroNFe = numeroNFe;
    }

    public String getChaveNFe() {
        return chaveNFe;
    }

    public void setChaveNFe(String chaveNFe) {
        this.chaveNFe = chaveNFe;
    }

    public String getUrlDanfe() {
        return urlDanfe;
    }

    public void setUrlDanfe(String urlDanfe) {
        this.urlDanfe = urlDanfe;
    }

    public Integer getNotaAvaliacaoCliente() {
        return notaAvaliacaoCliente;
    }

    public void setNotaAvaliacaoCliente(Integer notaAvaliacaoCliente) {
        this.notaAvaliacaoCliente = notaAvaliacaoCliente;
    }

    public String getComentarioAvaliacaoCliente() {
        return comentarioAvaliacaoCliente;
    }

    public void setComentarioAvaliacaoCliente(String comentarioAvaliacaoCliente) {
        this.comentarioAvaliacaoCliente = comentarioAvaliacaoCliente;
    }

    public LocalDateTime getDataAvaliacaoCliente() {
        return dataAvaliacaoCliente;
    }

    public void setDataAvaliacaoCliente(LocalDateTime dataAvaliacaoCliente) {
        this.dataAvaliacaoCliente = dataAvaliacaoCliente;
    }

    public Integer getTempoTotalExecucaoMinutos() {
        return tempoTotalExecucaoMinutos;
    }

    public void setTempoTotalExecucaoMinutos(Integer tempoTotalExecucaoMinutos) {
        this.tempoTotalExecucaoMinutos = tempoTotalExecucaoMinutos;
    }

    public BigDecimal getMargemLucroRealizada() {
        return margemLucroRealizada;
    }

    public void setMargemLucroRealizada(BigDecimal margemLucroRealizada) {
        this.margemLucroRealizada = margemLucroRealizada;
    }

    public BigDecimal getCustoTotalReal() {
        return custoTotalReal;
    }

    public void setCustoTotalReal(BigDecimal custoTotalReal) {
        this.custoTotalReal = custoTotalReal;
    }

    public BigDecimal getRentabilidadeOS() {
        return rentabilidadeOS;
    }

    public void setRentabilidadeOS(BigDecimal rentabilidadeOS) {
        this.rentabilidadeOS = rentabilidadeOS;
    }
}
