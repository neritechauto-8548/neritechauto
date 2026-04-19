package com.neritech.saas.cliente.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.rh.domain.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

/**
 * Entidade que representa um Cliente no sistema.
 * Pode ser Pessoa Física (CPF) ou Jurídica (CNPJ).
 *
 * <p>
 * Esta entidade é multi-tenant (estende {@link TenantEntity}), garantindo
 * isolamento de dados por empresa.
 * </p>
 *
 * @see TenantEntity
 */
@Entity
@Table(name = "cliente", uniqueConstraints = {
    @UniqueConstraint(name = "uc_cliente_cpf_empresa", columnNames = {"cpf", "empresa_id"}),
    @UniqueConstraint(name = "uc_cliente_cnpj_empresa", columnNames = {"cnpj", "empresa_id"})
})
public class Cliente extends TenantEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "razao_social", length = 255)
    private String razaoSocial;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 1)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_cliente", length = 20)
    private OrigemCliente origemCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusCliente status;

    @Column(name = "observacoes_gerais", columnDefinition = "TEXT")
    private String observacoesGerais;

    // Manual setters for specific fields (Lombok workaround)
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setOrigemCliente(OrigemCliente origemCliente) {
        this.origemCliente = origemCliente;
    }

    public void setStatus(StatusCliente status) {
        this.status = status;
    }

    public void setObservacoesGerais(String observacoesGerais) {
        this.observacoesGerais = observacoesGerais;
    }

    // Getters
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public OrigemCliente getOrigemCliente() {
        return origemCliente;
    }

    public StatusCliente getStatus() {
        return status;
    }

    public String getObservacoesGerais() {
        return observacoesGerais;
    }
}
