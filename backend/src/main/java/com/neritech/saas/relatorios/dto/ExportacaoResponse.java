package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.FormatoArquivoExportacao;
import com.neritech.saas.relatorios.domain.enums.StatusExportacao;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExportacaoResponse {
    private Long id;
    private Long empresaId;
    private Long usuarioSolicitante;
    private String tipoExportacao;
    private FormatoArquivoExportacao formatoArquivo;
    private String nomeArquivo;
    private String caminhoArquivo;
    private Long tamanhoArquivoBytes;
    private String criteriosExportacao; // JSON
    private String camposExportados; // JSON
    private Integer totalRegistros;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer duracaoSegundos;
    private StatusExportacao status;
    private Integer progressoPercentual;
    private String erroExportacao;
    private String urlDownload;
    private LocalDateTime dataExpiracaoDownload;
    private Integer downloadsRealizados;
    private Integer limiteDownloads;
    private Boolean senhaProtegida;
    private Boolean notificarConclusao;
    private Boolean notificacaoEnviada;
    private String observacoes;
    private LocalDateTime dataCadastro;

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
    public Long getUsuarioSolicitante() {
        return this.usuarioSolicitante;
    }
    public void setUsuarioSolicitante(Long usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }
    public String getTipoExportacao() {
        return this.tipoExportacao;
    }
    public void setTipoExportacao(String tipoExportacao) {
        this.tipoExportacao = tipoExportacao;
    }
    public FormatoArquivoExportacao getFormatoArquivo() {
        return this.formatoArquivo;
    }
    public void setFormatoArquivo(FormatoArquivoExportacao formatoArquivo) {
        this.formatoArquivo = formatoArquivo;
    }
    public String getNomeArquivo() {
        return this.nomeArquivo;
    }
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    public String getCaminhoArquivo() {
        return this.caminhoArquivo;
    }
    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }
    public Long getTamanhoArquivoBytes() {
        return this.tamanhoArquivoBytes;
    }
    public void setTamanhoArquivoBytes(Long tamanhoArquivoBytes) {
        this.tamanhoArquivoBytes = tamanhoArquivoBytes;
    }
    public String getCriteriosExportacao() {
        return this.criteriosExportacao;
    }
    public void setCriteriosExportacao(String criteriosExportacao) {
        this.criteriosExportacao = criteriosExportacao;
    }
    public String getCamposExportados() {
        return this.camposExportados;
    }
    public void setCamposExportados(String camposExportados) {
        this.camposExportados = camposExportados;
    }
    public Integer getTotalRegistros() {
        return this.totalRegistros;
    }
    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    public LocalDateTime getDataInicio() {
        return this.dataInicio;
    }
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDateTime getDataFim() {
        return this.dataFim;
    }
    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
    public Integer getDuracaoSegundos() {
        return this.duracaoSegundos;
    }
    public void setDuracaoSegundos(Integer duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }
    public StatusExportacao getStatus() {
        return this.status;
    }
    public void setStatus(StatusExportacao status) {
        this.status = status;
    }
    public Integer getProgressoPercentual() {
        return this.progressoPercentual;
    }
    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
    public String getErroExportacao() {
        return this.erroExportacao;
    }
    public void setErroExportacao(String erroExportacao) {
        this.erroExportacao = erroExportacao;
    }
    public String getUrlDownload() {
        return this.urlDownload;
    }
    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }
    public LocalDateTime getDataExpiracaoDownload() {
        return this.dataExpiracaoDownload;
    }
    public void setDataExpiracaoDownload(LocalDateTime dataExpiracaoDownload) {
        this.dataExpiracaoDownload = dataExpiracaoDownload;
    }
    public Integer getDownloadsRealizados() {
        return this.downloadsRealizados;
    }
    public void setDownloadsRealizados(Integer downloadsRealizados) {
        this.downloadsRealizados = downloadsRealizados;
    }
    public Integer getLimiteDownloads() {
        return this.limiteDownloads;
    }
    public void setLimiteDownloads(Integer limiteDownloads) {
        this.limiteDownloads = limiteDownloads;
    }
    public Boolean isSenhaProtegida() {
        return this.senhaProtegida;
    }
    public void setSenhaProtegida(Boolean senhaProtegida) {
        this.senhaProtegida = senhaProtegida;
    }
    public Boolean isNotificarConclusao() {
        return this.notificarConclusao;
    }
    public void setNotificarConclusao(Boolean notificarConclusao) {
        this.notificarConclusao = notificarConclusao;
    }
    public Boolean isNotificacaoEnviada() {
        return this.notificacaoEnviada;
    }
    public void setNotificacaoEnviada(Boolean notificacaoEnviada) {
        this.notificacaoEnviada = notificacaoEnviada;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public LocalDateTime getDataCadastro() {
        return this.dataCadastro;
    }
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
