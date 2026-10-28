package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "seguradoras")
public class Seguradora extends TenantEntity {
    @Column(name = "nome_fantasia", nullable = false, length = 255)
    private String nomeFantasia;
    @Column(name = "razao_social", length = 255)
    private String razaoSocial;
    @Column(length = 18)
    private String cnpj;
    @Column(name = "registro_susep", length = 60)
    private String registroSusep;
    @Column(name = "nome_contato", length = 255)
    private String nomeContato;
    @Column(length = 20)
    private String telefone;
    @Column(length = 255)
    private String email;
    @Column(length = 500)
    private String endereco;
    @Column(name = "tipo_convenio", length = 20)
    private String tipoConvenio;
    @Column(nullable = false)
    private Boolean ativo = true;
    @Column(columnDefinition = "TEXT")
    private String notas;

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String x) { nomeFantasia = x; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String x) { razaoSocial = x; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String x) { cnpj = x; }
    public String getRegistroSusep() { return registroSusep; }
    public void setRegistroSusep(String x) { registroSusep = x; }
    public String getNomeContato() { return nomeContato; }
    public void setNomeContato(String x) { nomeContato = x; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String x) { telefone = x; }
    public String getEmail() { return email; }
    public void setEmail(String x) { email = x; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String x) { endereco = x; }
    public String getTipoConvenio() { return tipoConvenio; }
    public void setTipoConvenio(String x) { tipoConvenio = x; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean x) { ativo = x; }
    public String getNotas() { return notas; }
    public void setNotas(String x) { notas = x; }
}
