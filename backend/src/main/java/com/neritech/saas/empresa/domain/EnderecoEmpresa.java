package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.enums.TipoEndereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "enderecos_empresa")
public class EnderecoEmpresa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "tipo_endereco", length = 20)
    @Enumerated(EnumType.STRING)
    private TipoEndereco tipoEndereco;

    @NotBlank(message = "O CEP Ã© obrigatÃ³rio")
    @Size(max = 9)
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @NotBlank(message = "O logradouro Ã© obrigatÃ³rio")
    @Size(max = 255)
    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;

    @NotBlank(message = "O nÃºmero Ã© obrigatÃ³rio")
    @Size(max = 20)
    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Size(max = 100)
    @Column(name = "complemento", length = 100)
    private String complemento;

    @NotBlank(message = "O bairro Ã© obrigatÃ³rio")
    @Size(max = 100)
    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @NotBlank(message = "A cidade Ã© obrigatÃ³ria")
    @Size(max = 100)
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "O estado Ã© obrigatÃ³rio")
    @Size(min = 2, max = 2)
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Size(max = 50)
    @Column(name = "pais", length = 50)
    private String pais = "Brasil";

    @Column(name = "principal")
    private Boolean principal = false;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public EnderecoEmpresa() {
    }

    // Getters and Setters
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
