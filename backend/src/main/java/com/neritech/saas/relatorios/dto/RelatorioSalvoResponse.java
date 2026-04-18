package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.FormatoSaida;
import com.neritech.saas.relatorios.domain.enums.FrequenciaAgendamento;
import com.neritech.saas.relatorios.domain.enums.OrientacaoPagina;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RelatorioSalvoResponse {
    private Long id;
    private Long empresaId;
    private String nomeRelatorio;
    private String descricao;
    private String tipoRelatorio;
    private String categoria;
    private String consultaSql;
    private String parametrosRelatorio; // JSON
    private String camposExibidos; // JSON
    private String ordenacaoPadrao;
    private String filtrosPadrao; // JSON
    private FormatoSaida formatoSaida;
    private OrientacaoPagina orientacaoPagina;
    private String templateLayout;
    private String agrupamentos; // JSON
    private String totalizadores; // JSON
    private String graficosInclusos; // JSON
    private Boolean publico;
    private String compartilhadoCom; // JSON
    private Boolean agendamentoAtivo;
    private FrequenciaAgendamento frequenciaAgendamento;
    private LocalDateTime proximoEnvio;
    private String emailsEnvio; // JSON
    private String pastaDestino;
    private Boolean ativo;
    private Integer totalExecucoes;
    private LocalDateTime dataUltimaExecucao;
    private Integer tempoMedioExecucao;
    private Long tamanhoMedioArquivo;
    private Long criadoPor;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getNomeRelatorio() {
        return this.nomeRelatorio;
    }
    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getTipoRelatorio() {
        return this.tipoRelatorio;
    }
    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getConsultaSql() {
        return this.consultaSql;
    }
    public void setConsultaSql(String consultaSql) {
        this.consultaSql = consultaSql;
    }
    public String getParametrosRelatorio() {
        return this.parametrosRelatorio;
    }
    public void setParametrosRelatorio(String parametrosRelatorio) {
        this.parametrosRelatorio = parametrosRelatorio;
    }
    public String getCamposExibidos() {
        return this.camposExibidos;
    }
    public void setCamposExibidos(String camposExibidos) {
        this.camposExibidos = camposExibidos;
    }
    public String getOrdenacaoPadrao() {
        return this.ordenacaoPadrao;
    }
    public void setOrdenacaoPadrao(String ordenacaoPadrao) {
        this.ordenacaoPadrao = ordenacaoPadrao;
    }
    public String getFiltrosPadrao() {
        return this.filtrosPadrao;
    }
    public void setFiltrosPadrao(String filtrosPadrao) {
        this.filtrosPadrao = filtrosPadrao;
    }
    public FormatoSaida getFormatoSaida() {
        return this.formatoSaida;
    }
    public void setFormatoSaida(FormatoSaida formatoSaida) {
        this.formatoSaida = formatoSaida;
    }
    public OrientacaoPagina getOrientacaoPagina() {
        return this.orientacaoPagina;
    }
    public void setOrientacaoPagina(OrientacaoPagina orientacaoPagina) {
        this.orientacaoPagina = orientacaoPagina;
    }
    public String getTemplateLayout() {
        return this.templateLayout;
    }
    public void setTemplateLayout(String templateLayout) {
        this.templateLayout = templateLayout;
    }
    public String getAgrupamentos() {
        return this.agrupamentos;
    }
    public void setAgrupamentos(String agrupamentos) {
        this.agrupamentos = agrupamentos;
    }
    public String getTotalizadores() {
        return this.totalizadores;
    }
    public void setTotalizadores(String totalizadores) {
        this.totalizadores = totalizadores;
    }
    public String getGraficosInclusos() {
        return this.graficosInclusos;
    }
    public void setGraficosInclusos(String graficosInclusos) {
        this.graficosInclusos = graficosInclusos;
    }
    public Boolean isPublico() {
        return this.publico;
    }
    public void setPublico(Boolean publico) {
        this.publico = publico;
    }
    public String getCompartilhadoCom() {
        return this.compartilhadoCom;
    }
    public void setCompartilhadoCom(String compartilhadoCom) {
        this.compartilhadoCom = compartilhadoCom;
    }
    public Boolean isAgendamentoAtivo() {
        return this.agendamentoAtivo;
    }
    public void setAgendamentoAtivo(Boolean agendamentoAtivo) {
        this.agendamentoAtivo = agendamentoAtivo;
    }
    public FrequenciaAgendamento getFrequenciaAgendamento() {
        return this.frequenciaAgendamento;
    }
    public void setFrequenciaAgendamento(FrequenciaAgendamento frequenciaAgendamento) {
        this.frequenciaAgendamento = frequenciaAgendamento;
    }
    public LocalDateTime getProximoEnvio() {
        return this.proximoEnvio;
    }
    public void setProximoEnvio(LocalDateTime proximoEnvio) {
        this.proximoEnvio = proximoEnvio;
    }
    public String getEmailsEnvio() {
        return this.emailsEnvio;
    }
    public void setEmailsEnvio(String emailsEnvio) {
        this.emailsEnvio = emailsEnvio;
    }
    public String getPastaDestino() {
        return this.pastaDestino;
    }
    public void setPastaDestino(String pastaDestino) {
        this.pastaDestino = pastaDestino;
    }
    public Boolean isAtivo() {
        return this.ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    public Integer getTotalExecucoes() {
        return this.totalExecucoes;
    }
    public void setTotalExecucoes(Integer totalExecucoes) {
        this.totalExecucoes = totalExecucoes;
    }
    public LocalDateTime getDataUltimaExecucao() {
        return this.dataUltimaExecucao;
    }
    public void setDataUltimaExecucao(LocalDateTime dataUltimaExecucao) {
        this.dataUltimaExecucao = dataUltimaExecucao;
    }
    public Integer getTempoMedioExecucao() {
        return this.tempoMedioExecucao;
    }
    public void setTempoMedioExecucao(Integer tempoMedioExecucao) {
        this.tempoMedioExecucao = tempoMedioExecucao;
    }
    public Long getTamanhoMedioArquivo() {
        return this.tamanhoMedioArquivo;
    }
    public void setTamanhoMedioArquivo(Long tamanhoMedioArquivo) {
        this.tamanhoMedioArquivo = tamanhoMedioArquivo;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
    public LocalDateTime getDataCadastro() {
        return this.dataCadastro;
    }
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    public LocalDateTime getDataAtualizacao() {
        return this.dataAtualizacao;
    }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
