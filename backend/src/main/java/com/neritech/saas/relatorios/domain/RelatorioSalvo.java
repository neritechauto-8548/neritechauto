package com.neritech.saas.relatorios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.relatorios.domain.enums.FormatoSaida;
import com.neritech.saas.relatorios.domain.enums.FrequenciaAgendamento;
import com.neritech.saas.relatorios.domain.enums.OrientacaoPagina;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "relatorios_salvos")
public class RelatorioSalvo extends TenantEntity {

    @Column(name = "nome_relatorio", nullable = false)
    private String nomeRelatorio;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(name = "tipo_relatorio", nullable = false)
    private String tipoRelatorio;

    private String categoria;

    @Column(name = "consulta_sql", columnDefinition = "text")
    private String consultaSql;

    @Column(name = "parametros_relatorio", columnDefinition = "text")
    private String parametrosRelatorio; // JSON

    @Column(name = "campos_exibidos", columnDefinition = "text")
    private String camposExibidos; // JSON

    @Column(name = "ordenacao_padrao")
    private String ordenacaoPadrao;

    @Column(name = "filtros_padrao", columnDefinition = "text")
    private String filtrosPadrao; // JSON

    @Enumerated(EnumType.STRING)
    @Column(name = "formato_saida")
    private FormatoSaida formatoSaida;

    @Enumerated(EnumType.STRING)
    @Column(name = "orientacao_pagina")
    private OrientacaoPagina orientacaoPagina;

    @Column(name = "template_layout", columnDefinition = "text")
    private String templateLayout;

    @Column(columnDefinition = "text")
    private String agrupamentos; // JSON

    @Column(columnDefinition = "text")
    private String totalizadores; // JSON

    @Column(name = "graficos_inclusos", columnDefinition = "text")
    private String graficosInclusos; // JSON

    private Boolean publico = false;

    @Column(name = "compartilhado_com", columnDefinition = "text")
    private String compartilhadoCom; // JSON

    @Column(name = "agendamento_ativo")
    private Boolean agendamentoAtivo = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_agendamento")
    private FrequenciaAgendamento frequenciaAgendamento;

    @Column(name = "proximo_envio")
    private LocalDateTime proximoEnvio;

    @Column(name = "emails_envio", columnDefinition = "text")
    private String emailsEnvio; // JSON

    @Column(name = "pasta_destino")
    private String pastaDestino;

    private Boolean ativo = true;

    @Column(name = "total_execucoes")
    private Integer totalExecucoes = 0;

    @Column(name = "data_ultima_execucao")
    private LocalDateTime dataUltimaExecucao;

    @Column(name = "tempo_medio_execucao")
    private Integer tempoMedioExecucao;

    @Column(name = "tamanho_medio_arquivo")
    private Long tamanhoMedioArquivo;

    @Column(name = "criado_por")
    private Long criadoPor;
   
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
}
