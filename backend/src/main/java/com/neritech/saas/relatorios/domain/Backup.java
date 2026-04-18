package com.neritech.saas.relatorios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;

import com.neritech.saas.relatorios.domain.enums.LocalArmazenamento;
import com.neritech.saas.relatorios.domain.enums.StatusBackup;
import com.neritech.saas.relatorios.domain.enums.TipoBackup;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "backups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Backup extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_backup")
    private TipoBackup tipoBackup;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "caminho_arquivo", nullable = false)
    private String caminhoArquivo;

    @Column(name = "tamanho_arquivo_bytes")
    private Long tamanhoArquivoBytes;

    @Column(name = "hash_arquivo")
    private String hashArquivo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Enumerated(EnumType.STRING)
    private StatusBackup status;

    @Builder.Default
    private Boolean automatico = true;

    @Column(name = "tabelas_incluidas", columnDefinition = "text")
    private String tabelasIncluidas; // JSON

    @Column(name = "total_registros")
    private Long totalRegistros;

    @Column(name = "compressao_utilizada")
    private String compressaoUtilizada;

    @Column(name = "taxa_compressao")
    private BigDecimal taxaCompressao;

    @Builder.Default
    @Column(name = "senha_protegido")
    private Boolean senhaProtegido = false;

    @Builder.Default
    private Boolean criptografado = false;

    @Column(name = "algoritmo_criptografia")
    private String algoritmoCriptografia;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_armazenamento")
    private LocalArmazenamento localArmazenamento;

    @Column(name = "provedor_nuvem")
    private String provedorNuvem;

    @Column(name = "url_download")
    private String urlDownload;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Builder.Default
    @Column(name = "testado_restauracao")
    private Boolean testadoRestauracao = false;

    @Column(name = "data_teste_restauracao")
    private LocalDateTime dataTesteRestauracao;

    @Column(name = "resultado_teste", columnDefinition = "text")
    private String resultadoTeste;

    @Column(name = "erro_backup", columnDefinition = "text")
    private String erroBackup;

    @Column(columnDefinition = "text")
    private String observacoes;

    @Column(name = "executado_por")
    private Long executadoPor;

    @Builder.Default
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public StatusBackup getStatus() {
        return this.status;
    }
    public void setStatus(StatusBackup status) {
        this.status = status;
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
    public BigDecimal getTaxaCompressao() {
        return this.taxaCompressao;
    }
    public void setTaxaCompressao(BigDecimal taxaCompressao) {
        this.taxaCompressao = taxaCompressao;
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
    public LocalDateTime getDataTesteRestauracao() {
        return this.dataTesteRestauracao;
    }
    public void setDataTesteRestauracao(LocalDateTime dataTesteRestauracao) {
        this.dataTesteRestauracao = dataTesteRestauracao;
    }
    public String getResultadoTeste() {
        return this.resultadoTeste;
    }
    public void setResultadoTeste(String resultadoTeste) {
        this.resultadoTeste = resultadoTeste;
    }
    public String getErroBackup() {
        return this.erroBackup;
    }
    public void setErroBackup(String erroBackup) {
        this.erroBackup = erroBackup;
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

    public LocalDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAutomatico() {
        return this.automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

    public Boolean getSenhaProtegido() {
        return this.senhaProtegido;
    }

    public void setSenhaProtegido(Boolean senhaProtegido) {
        this.senhaProtegido = senhaProtegido;
    }

    public Boolean getCriptografado() {
        return this.criptografado;
    }

    public void setCriptografado(Boolean criptografado) {
        this.criptografado = criptografado;
    }

    public Boolean getTestadoRestauracao() {
        return this.testadoRestauracao;
    }

    public void setTestadoRestauracao(Boolean testadoRestauracao) {
        this.testadoRestauracao = testadoRestauracao;
    }
}
