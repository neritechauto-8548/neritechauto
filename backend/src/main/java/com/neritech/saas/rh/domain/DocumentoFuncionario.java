package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.TipoDocumento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "documentos_funcionarios")
public class DocumentoFuncionario extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", length = 100)
    private String numeroDocumento;

    @Column(name = "orgao_emissor", length = 100)
    private String orgaoEmissor;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "arquivo_url", length = 500)
    private String arquivoUrl;

    @Column(name = "arquivo_nome", length = 255)
    private String arquivoNome;

    @Column(name = "arquivo_tamanho_kb")
    private Integer arquivoTamanhoKb;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "obrigatorio")
    private Boolean obrigatorio = false;

    @Column(name = "verificado")
    private Boolean verificado = false;

    @Column(name = "verificado_por")
    private Long verificadoPor;

    @Column(name = "data_verificacao")
    private LocalDate dataVerificacao;

    @Column(name = "cadastrado_por")
    private Long cadastradoPor;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getArquivoUrl() {
        return arquivoUrl;
    }

    public void setArquivoUrl(String arquivoUrl) {
        this.arquivoUrl = arquivoUrl;
    }

    public String getArquivoNome() {
        return arquivoNome;
    }

    public void setArquivoNome(String arquivoNome) {
        this.arquivoNome = arquivoNome;
    }

    public Integer getArquivoTamanhoKb() {
        return arquivoTamanhoKb;
    }

    public void setArquivoTamanhoKb(Integer arquivoTamanhoKb) {
        this.arquivoTamanhoKb = arquivoTamanhoKb;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Long getVerificadoPor() {
        return verificadoPor;
    }

    public void setVerificadoPor(Long verificadoPor) {
        this.verificadoPor = verificadoPor;
    }

    public LocalDate getDataVerificacao() {
        return dataVerificacao;
    }

    public void setDataVerificacao(LocalDate dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    public Long getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(Long cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }
}

