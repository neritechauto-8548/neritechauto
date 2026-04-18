package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.LocalArmazenamento;
import com.neritech.saas.relatorios.domain.enums.TipoBackup;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BackupRequest {
    private Long empresaId;
    private TipoBackup tipoBackup;
    private String nomeArquivo;
    private String caminhoArquivo;
    private Long tamanhoArquivoBytes;
    private String hashArquivo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer duracaoMinutos;
    private Boolean automatico;
    private String tabelasIncluidas; // JSON
    private Long totalRegistros;
    private String compressaoUtilizada;
    private Boolean senhaProtegido;
    private Boolean criptografado;
    private String algoritmoCriptografia;
    private LocalArmazenamento localArmazenamento;
    private String provedorNuvem;
    private String urlDownload;
    private LocalDateTime dataExpiracao;
    private String observacoes;
    private Long executadoPor;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public TipoBackup getTipoBackup() {
        return this.tipoBackup;
    }
    public void setTipoBackup(TipoBackup tipoBackup) {
        this.tipoBackup = tipoBackup;
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
    public String getHashArquivo() {
        return this.hashArquivo;
    }
    public void setHashArquivo(String hashArquivo) {
        this.hashArquivo = hashArquivo;
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
    public Integer getDuracaoMinutos() {
        return this.duracaoMinutos;
    }
    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    public Boolean isAutomatico() {
        return this.automatico;
    }
    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }
    public String getTabelasIncluidas() {
        return this.tabelasIncluidas;
    }
    public void setTabelasIncluidas(String tabelasIncluidas) {
        this.tabelasIncluidas = tabelasIncluidas;
    }
    public Long getTotalRegistros() {
        return this.totalRegistros;
    }
    public void setTotalRegistros(Long totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    public String getCompressaoUtilizada() {
        return this.compressaoUtilizada;
    }
    public void setCompressaoUtilizada(String compressaoUtilizada) {
        this.compressaoUtilizada = compressaoUtilizada;
    }
    public Boolean isSenhaProtegido() {
        return this.senhaProtegido;
    }
    public void setSenhaProtegido(Boolean senhaProtegido) {
        this.senhaProtegido = senhaProtegido;
    }
    public Boolean isCriptografado() {
        return this.criptografado;
    }
    public void setCriptografado(Boolean criptografado) {
        this.criptografado = criptografado;
    }
    public String getAlgoritmoCriptografia() {
        return this.algoritmoCriptografia;
    }
    public void setAlgoritmoCriptografia(String algoritmoCriptografia) {
        this.algoritmoCriptografia = algoritmoCriptografia;
    }
    public LocalArmazenamento getLocalArmazenamento() {
        return this.localArmazenamento;
    }
    public void setLocalArmazenamento(LocalArmazenamento localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }
    public String getProvedorNuvem() {
        return this.provedorNuvem;
    }
    public void setProvedorNuvem(String provedorNuvem) {
        this.provedorNuvem = provedorNuvem;
    }
    public String getUrlDownload() {
        return this.urlDownload;
    }
    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }
    public LocalDateTime getDataExpiracao() {
        return this.dataExpiracao;
    }
    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getExecutadoPor() {
        return this.executadoPor;
    }
    public void setExecutadoPor(Long executadoPor) {
        this.executadoPor = executadoPor;
    }
}
