package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.OperacaoAuditoria;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogAlteracaoResponse {
    private Long id;
    private Long empresaId;
    private String tabelaAfetada;
    private Long registroId;
    private OperacaoAuditoria operacao;
    private String camposAlterados; // JSON
    private String valoresAntigos; // JSON
    private String valoresNovos; // JSON
    private Long usuarioResponsavel;
    private String ipOrigem;
    private String userAgent;
    private String motivoAlteracao;
    private String contextoOperacao;
    private String dadosAdicionais; // JSON
    private String checkpointAntes; // JSON
    private String checkpointDepois; // JSON
    private Boolean reversivel;
    private String comandoReversao;
    private LocalDateTime dataAlteracao;
    private Boolean auditado;
    private LocalDateTime dataAuditoria;
    private Long auditorResponsavel;
    private String observacoesAuditoria;

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
    public String getTabelaAfetada() {
        return this.tabelaAfetada;
    }
    public void setTabelaAfetada(String tabelaAfetada) {
        this.tabelaAfetada = tabelaAfetada;
    }
    public Long getRegistroId() {
        return this.registroId;
    }
    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }
    public OperacaoAuditoria getOperacao() {
        return this.operacao;
    }
    public void setOperacao(OperacaoAuditoria operacao) {
        this.operacao = operacao;
    }
    public String getCamposAlterados() {
        return this.camposAlterados;
    }
    public void setCamposAlterados(String camposAlterados) {
        this.camposAlterados = camposAlterados;
    }
    public String getValoresAntigos() {
        return this.valoresAntigos;
    }
    public void setValoresAntigos(String valoresAntigos) {
        this.valoresAntigos = valoresAntigos;
    }
    public String getValoresNovos() {
        return this.valoresNovos;
    }
    public void setValoresNovos(String valoresNovos) {
        this.valoresNovos = valoresNovos;
    }
    public Long getUsuarioResponsavel() {
        return this.usuarioResponsavel;
    }
    public void setUsuarioResponsavel(Long usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
    public String getIpOrigem() {
        return this.ipOrigem;
    }
    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }
    public String getUserAgent() {
        return this.userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getMotivoAlteracao() {
        return this.motivoAlteracao;
    }
    public void setMotivoAlteracao(String motivoAlteracao) {
        this.motivoAlteracao = motivoAlteracao;
    }
    public String getContextoOperacao() {
        return this.contextoOperacao;
    }
    public void setContextoOperacao(String contextoOperacao) {
        this.contextoOperacao = contextoOperacao;
    }
    public String getDadosAdicionais() {
        return this.dadosAdicionais;
    }
    public void setDadosAdicionais(String dadosAdicionais) {
        this.dadosAdicionais = dadosAdicionais;
    }
    public String getCheckpointAntes() {
        return this.checkpointAntes;
    }
    public void setCheckpointAntes(String checkpointAntes) {
        this.checkpointAntes = checkpointAntes;
    }
    public String getCheckpointDepois() {
        return this.checkpointDepois;
    }
    public void setCheckpointDepois(String checkpointDepois) {
        this.checkpointDepois = checkpointDepois;
    }
    public Boolean isReversivel() {
        return this.reversivel;
    }
    public void setReversivel(Boolean reversivel) {
        this.reversivel = reversivel;
    }
    public String getComandoReversao() {
        return this.comandoReversao;
    }
    public void setComandoReversao(String comandoReversao) {
        this.comandoReversao = comandoReversao;
    }
    public LocalDateTime getDataAlteracao() {
        return this.dataAlteracao;
    }
    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
    public Boolean isAuditado() {
        return this.auditado;
    }
    public void setAuditado(Boolean auditado) {
        this.auditado = auditado;
    }
    public LocalDateTime getDataAuditoria() {
        return this.dataAuditoria;
    }
    public void setDataAuditoria(LocalDateTime dataAuditoria) {
        this.dataAuditoria = dataAuditoria;
    }
    public Long getAuditorResponsavel() {
        return this.auditorResponsavel;
    }
    public void setAuditorResponsavel(Long auditorResponsavel) {
        this.auditorResponsavel = auditorResponsavel;
    }
    public String getObservacoesAuditoria() {
        return this.observacoesAuditoria;
    }
    public void setObservacoesAuditoria(String observacoesAuditoria) {
        this.observacoesAuditoria = observacoesAuditoria;
    }
}
