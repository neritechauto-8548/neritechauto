package com.neritech.saas.fiscal.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sped_fiscal")
public class SpedFiscal extends BaseEntity {

    @Column(nullable = false)
    private LocalDate periodoInicio;

    @Column(nullable = false)
    private LocalDate periodoFim;

    @Column(nullable = false)
    private String tipoArquivo; // EFD_ICMS_IPI, EFD_CONTRIBUICOES

    @Column(nullable = false)
    private String status; // GERADO, ERRO, TRANSMITIDO

    @Lob
    private byte[] arquivoGerado;

    private String logProcessamento;

    private LocalDateTime dataGeracao;

    public LocalDate getPeriodoInicio() {
        return this.periodoInicio;
    }
    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }
    public LocalDate getPeriodoFim() {
        return this.periodoFim;
    }
    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }
    public String getTipoArquivo() {
        return this.tipoArquivo;
    }
    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public byte[] getArquivoGerado() {
        return this.arquivoGerado;
    }
    public void setArquivoGerado(byte[] arquivoGerado) {
        this.arquivoGerado = arquivoGerado;
    }
    public String getLogProcessamento() {
        return this.logProcessamento;
    }
    public void setLogProcessamento(String logProcessamento) {
        this.logProcessamento = logProcessamento;
    }
    public LocalDateTime getDataGeracao() {
        return this.dataGeracao;
    }
    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}
