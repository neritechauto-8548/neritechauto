package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.FormatoArquivoExportacao;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExportacaoRequest {
    private Long empresaId;
    private Long usuarioSolicitante;
    private String tipoExportacao;
    private FormatoArquivoExportacao formatoArquivo;
    private String nomeArquivo;
    private String criteriosExportacao; // JSON
    private String camposExportados; // JSON
    private Boolean senhaProtegida;
    private Boolean notificarConclusao;
    private String observacoes;

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
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
