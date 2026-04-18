package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.RecomendacaoAvaliacao;
import com.neritech.saas.rh.domain.enums.TipoAvaliacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes_funcionarios")
public class AvaliacaoFuncionario extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "avaliador_id", nullable = false)
    private Long avaliadorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_avaliacao", length = 20)
    private TipoAvaliacao tipoAvaliacao;

    @Column(name = "periodo_avaliado", nullable = false, length = 7)
    private String periodoAvaliado;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @Column(name = "nota_produtividade", precision = 3, scale = 2)
    private BigDecimal notaProdutividade;

    @Column(name = "nota_qualidade", precision = 3, scale = 2)
    private BigDecimal notaQualidade;

    @Column(name = "nota_pontualidade", precision = 3, scale = 2)
    private BigDecimal notaPontualidade;

    @Column(name = "nota_trabalho_equipe", precision = 3, scale = 2)
    private BigDecimal notaTrabalhoEquipe;

    @Column(name = "nota_iniciativa", precision = 3, scale = 2)
    private BigDecimal notaIniciativa;

    @Column(name = "nota_conhecimento_tecnico", precision = 3, scale = 2)
    private BigDecimal notaConhecimentoTecnico;

    @Column(name = "nota_atendimento_cliente", precision = 3, scale = 2)
    private BigDecimal notaAtendimentoCliente;

    @Column(name = "nota_organizacao", precision = 3, scale = 2)
    private BigDecimal notaOrganizacao;

    @Column(name = "nota_lideranca", precision = 3, scale = 2)
    private BigDecimal notaLideranca;

    @Column(name = "nota_final", nullable = false, precision = 3, scale = 2)
    private BigDecimal notaFinal;

    @Column(name = "pontos_fortes", columnDefinition = "TEXT")
    private String pontosFortes;

    @Column(name = "pontos_melhoria", columnDefinition = "TEXT")
    private String pontosMelhoria;

    @Column(name = "metas_estabelecidas", columnDefinition = "TEXT")
    private String metasEstabelecidas;

    @Column(name = "plano_desenvolvimento", columnDefinition = "TEXT")
    private String planoDesenvolvimento;

    @Column(name = "treinamentos_recomendados", columnDefinition = "jsonb")
    private String treinamentosRecomendados;

    @Enumerated(EnumType.STRING)
    @Column(name = "recomendacao", length = 30)
    private RecomendacaoAvaliacao recomendacao;

    @Column(name = "aumento_salarial_sugerido", precision = 5, scale = 2)
    private BigDecimal aumentoSalarialSugerido;

    @Column(name = "bonus_sugerido", precision = 8, scale = 2)
    private BigDecimal bonusSugerido;

    @Column(name = "observacoes_avaliador", columnDefinition = "TEXT")
    private String observacoesAvaliador;

    @Column(name = "comentarios_funcionario", columnDefinition = "TEXT")
    private String comentariosFuncionario;

    @Column(name = "data_ciencia_funcionario")
    private LocalDate dataCienciaFuncionario;

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getAvaliadorId() {
        return avaliadorId;
    }

    public void setAvaliadorId(Long avaliadorId) {
        this.avaliadorId = avaliadorId;
    }

    public TipoAvaliacao getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public void setTipoAvaliacao(TipoAvaliacao tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }

    public String getPeriodoAvaliado() {
        return periodoAvaliado;
    }

    public void setPeriodoAvaliado(String periodoAvaliado) {
        this.periodoAvaliado = periodoAvaliado;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public BigDecimal getNotaProdutividade() {
        return notaProdutividade;
    }

    public void setNotaProdutividade(BigDecimal notaProdutividade) {
        this.notaProdutividade = notaProdutividade;
    }

    public BigDecimal getNotaQualidade() {
        return notaQualidade;
    }

    public void setNotaQualidade(BigDecimal notaQualidade) {
        this.notaQualidade = notaQualidade;
    }

    public BigDecimal getNotaPontualidade() {
        return notaPontualidade;
    }

    public void setNotaPontualidade(BigDecimal notaPontualidade) {
        this.notaPontualidade = notaPontualidade;
    }

    public BigDecimal getNotaTrabalhoEquipe() {
        return notaTrabalhoEquipe;
    }

    public void setNotaTrabalhoEquipe(BigDecimal notaTrabalhoEquipe) {
        this.notaTrabalhoEquipe = notaTrabalhoEquipe;
    }

    public BigDecimal getNotaIniciativa() {
        return notaIniciativa;
    }

    public void setNotaIniciativa(BigDecimal notaIniciativa) {
        this.notaIniciativa = notaIniciativa;
    }

    public BigDecimal getNotaConhecimentoTecnico() {
        return notaConhecimentoTecnico;
    }

    public void setNotaConhecimentoTecnico(BigDecimal notaConhecimentoTecnico) {
        this.notaConhecimentoTecnico = notaConhecimentoTecnico;
    }

    public BigDecimal getNotaAtendimentoCliente() {
        return notaAtendimentoCliente;
    }

    public void setNotaAtendimentoCliente(BigDecimal notaAtendimentoCliente) {
        this.notaAtendimentoCliente = notaAtendimentoCliente;
    }

    public BigDecimal getNotaOrganizacao() {
        return notaOrganizacao;
    }

    public void setNotaOrganizacao(BigDecimal notaOrganizacao) {
        this.notaOrganizacao = notaOrganizacao;
    }

    public BigDecimal getNotaLideranca() {
        return notaLideranca;
    }

    public void setNotaLideranca(BigDecimal notaLideranca) {
        this.notaLideranca = notaLideranca;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }

    public String getPontosFortes() {
        return pontosFortes;
    }

    public void setPontosFortes(String pontosFortes) {
        this.pontosFortes = pontosFortes;
    }

    public String getPontosMelhoria() {
        return pontosMelhoria;
    }

    public void setPontosMelhoria(String pontosMelhoria) {
        this.pontosMelhoria = pontosMelhoria;
    }

    public String getMetasEstabelecidas() {
        return metasEstabelecidas;
    }

    public void setMetasEstabelecidas(String metasEstabelecidas) {
        this.metasEstabelecidas = metasEstabelecidas;
    }

    public String getPlanoDesenvolvimento() {
        return planoDesenvolvimento;
    }

    public void setPlanoDesenvolvimento(String planoDesenvolvimento) {
        this.planoDesenvolvimento = planoDesenvolvimento;
    }

    public String getTreinamentosRecomendados() {
        return treinamentosRecomendados;
    }

    public void setTreinamentosRecomendados(String treinamentosRecomendados) {
        this.treinamentosRecomendados = treinamentosRecomendados;
    }

    public RecomendacaoAvaliacao getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(RecomendacaoAvaliacao recomendacao) {
        this.recomendacao = recomendacao;
    }

    public BigDecimal getAumentoSalarialSugerido() {
        return aumentoSalarialSugerido;
    }

    public void setAumentoSalarialSugerido(BigDecimal aumentoSalarialSugerido) {
        this.aumentoSalarialSugerido = aumentoSalarialSugerido;
    }

    public BigDecimal getBonusSugerido() {
        return bonusSugerido;
    }

    public void setBonusSugerido(BigDecimal bonusSugerido) {
        this.bonusSugerido = bonusSugerido;
    }

    public String getObservacoesAvaliador() {
        return observacoesAvaliador;
    }

    public void setObservacoesAvaliador(String observacoesAvaliador) {
        this.observacoesAvaliador = observacoesAvaliador;
    }

    public String getComentariosFuncionario() {
        return comentariosFuncionario;
    }

    public void setComentariosFuncionario(String comentariosFuncionario) {
        this.comentariosFuncionario = comentariosFuncionario;
    }

    public LocalDate getDataCienciaFuncionario() {
        return dataCienciaFuncionario;
    }

    public void setDataCienciaFuncionario(LocalDate dataCienciaFuncionario) {
        this.dataCienciaFuncionario = dataCienciaFuncionario;
    }

    public Long getAprovadaPor() {
        return aprovadaPor;
    }

    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }

    public LocalDate getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDate dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
}
