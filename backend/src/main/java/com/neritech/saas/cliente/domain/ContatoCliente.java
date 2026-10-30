package com.neritech.saas.cliente.domain;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.cliente.domain.enums.TipoContato;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "contatos_clientes")
public class ContatoCliente extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false, length = 20)
    private TipoContato tipoContato;

    @Column(name = "contato", nullable = false, length = 255)
    private String contato;

    @Column(name = "principal", nullable = false)
    @ColumnDefault("false")
    private Boolean principal = false;

    @Column(name = "nome", length = 160)
    private String nome;
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }

    @Column(name = "sexo", length = 1)
    private String sexo;
    public String getSexo() { return sexo; }
    public void setSexo(String value) { this.sexo = value; }

    @Column(name = "data_nascimento")
    private java.time.LocalDate dataNascimento;
    public java.time.LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(java.time.LocalDate value) { this.dataNascimento = value; }

    @Column(name = "grupo_principal")
    private Boolean grupoPrincipal;
    public Boolean getGrupoPrincipal() { return grupoPrincipal; }
    public void setGrupoPrincipal(Boolean value) { this.grupoPrincipal = value; }

    // Getters e Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
}
