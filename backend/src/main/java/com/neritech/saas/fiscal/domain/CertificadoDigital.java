package com.neritech.saas.fiscal.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "certificados_digitais")
public class CertificadoDigital extends BaseEntity {

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String senha;

    @Lob
    @Column(nullable = false)
    private byte[] arquivo;

    @Column(nullable = false)
    private LocalDateTime dataValidade;

    private String emissor;

    private String serialNumber;

    private boolean ativo = true;

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    public String getSenha() {
        return this.senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public byte[] getArquivo() {
        return this.arquivo;
    }
    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }
    public LocalDateTime getDataValidade() {
        return this.dataValidade;
    }
    public void setDataValidade(LocalDateTime dataValidade) {
        this.dataValidade = dataValidade;
    }
    public String getEmissor() {
        return this.emissor;
    }
    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }
    public String getSerialNumber() {
        return this.serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
