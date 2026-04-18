package com.neritech.saas.relatorios.domain;

import com.neritech.saas.relatorios.domain.enums.OperacaoAuditoria;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "logs_alteracoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id")
    private Long empresaId;

    @Column(name = "tabela_afetada", nullable = false)
    private String tabelaAfetada;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Enumerated(EnumType.STRING)
    private OperacaoAuditoria operacao;

    @Column(name = "campos_alterados", columnDefinition = "text")
    private String camposAlterados; // JSON

    @Column(name = "valores_antigos", columnDefinition = "text")
    private String valoresAntigos; // JSON

    @Column(name = "valores_novos", columnDefinition = "text")
    private String valoresNovos; // JSON

    @Column(name = "usuario_responsavel", nullable = false)
    private Long usuarioResponsavel;

    @Column(name = "ip_origem")
    private String ipOrigem;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @Column(name = "motivo_alteracao", columnDefinition = "text")
    private String motivoAlteracao;

    @Column(name = "contexto_operacao")
    private String contextoOperacao;

    @Column(name = "dados_adicionais", columnDefinition = "text")
    private String dadosAdicionais; // JSON

    @Column(name = "checkpoint_antes", columnDefinition = "text")
    private String checkpointAntes; // JSON

    @Column(name = "checkpoint_depois", columnDefinition = "text")
    private String checkpointDepois; // JSON

    @Builder.Default
    private Boolean reversivel = false;

    @Column(name = "comando_reversao", columnDefinition = "text")
    private String comandoReversao;

    @Builder.Default
    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @Builder.Default
    private Boolean auditado = false;

    @Column(name = "data_auditoria")
    private LocalDateTime dataAuditoria;

    @Column(name = "auditor_responsavel")
    private Long auditorResponsavel;

    @Column(name = "observacoes_auditoria", columnDefinition = "text")
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
    public String getComandoReversao() {
        return this.comandoReversao;
    }
    public void setComandoReversao(String comandoReversao) {
        this.comandoReversao = comandoReversao;
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

    public LocalDateTime getDataAlteracao() {
        return this.dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Boolean getReversivel() {
        return this.reversivel;
    }

    public void setReversivel(Boolean reversivel) {
        this.reversivel = reversivel;
    }

    public Boolean getAuditado() {
        return this.auditado;
    }

    public void setAuditado(Boolean auditado) {
        this.auditado = auditado;
    }
}
