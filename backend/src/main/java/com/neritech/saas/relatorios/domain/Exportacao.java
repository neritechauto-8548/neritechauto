package com.neritech.saas.relatorios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;

import com.neritech.saas.relatorios.domain.enums.FormatoArquivoExportacao;
import com.neritech.saas.relatorios.domain.enums.StatusExportacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "exportacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exportacao extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_solicitante", nullable = false)
    private Long usuarioSolicitante;

    @Column(name = "tipo_exportacao", nullable = false)
    private String tipoExportacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "formato_arquivo")
    private FormatoArquivoExportacao formatoArquivo;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "caminho_arquivo")
    private String caminhoArquivo;

    @Column(name = "tamanho_arquivo_bytes")
    private Long tamanhoArquivoBytes;

    @Column(name = "criterios_exportacao", columnDefinition = "text")
    private String criteriosExportacao; // JSON

    @Column(name = "campos_exportados", columnDefinition = "text")
    private String camposExportados; // JSON

    @Column(name = "total_registros")
    private Integer totalRegistros;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "duracao_segundos")
    private Integer duracaoSegundos;

    @Enumerated(EnumType.STRING)
    private StatusExportacao status;

    @Builder.Default
    @Column(name = "progresso_percentual")
    private Integer progressoPercentual = 0;

    @Column(name = "erro_exportacao", columnDefinition = "text")
    private String erroExportacao;

    @Column(name = "url_download")
    private String urlDownload;

    @Column(name = "data_expiracao_download")
    private LocalDateTime dataExpiracaoDownload;

    @Builder.Default
    @Column(name = "downloads_realizados")
    private Integer downloadsRealizados = 0;

    @Builder.Default
    @Column(name = "limite_downloads")
    private Integer limiteDownloads = 5;

    @Builder.Default
    @Column(name = "senha_protegida")
    private Boolean senhaProtegida = false;

    @Builder.Default
    @Column(name = "notificar_conclusao")
    private Boolean notificarConclusao = true;

    @Builder.Default
    @Column(name = "notificacao_enviada")
    private Boolean notificacaoEnviada = false;

    @Column(columnDefinition = "text")
    private String observacoes;

    @Builder.Default
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Integer getProgressoPercentual() {
        return this.progressoPercentual;
    }

    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
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

    public Boolean getSenhaProtegida() {
        return this.senhaProtegida;
    }

    public void setSenhaProtegida(Boolean senhaProtegida) {
        this.senhaProtegida = senhaProtegida;
    }

    public Boolean getNotificarConclusao() {
        return this.notificarConclusao;
    }

    public void setNotificarConclusao(Boolean notificarConclusao) {
        this.notificarConclusao = notificarConclusao;
    }

    public Boolean getNotificacaoEnviada() {
        return this.notificacaoEnviada;
    }

    public void setNotificacaoEnviada(Boolean notificacaoEnviada) {
        this.notificacaoEnviada = notificacaoEnviada;
    }
}
