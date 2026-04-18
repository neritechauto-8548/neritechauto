package com.neritech.saas.veiculo.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.enums.CategoriaVeiculo;
import com.neritech.saas.veiculo.domain.enums.SegmentoVeiculo;
import com.neritech.saas.veiculo.domain.enums.TipoTracao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "modelos_veiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_modelo_marca_nome", columnNames = { "marca_id", "nome" })
})
public class ModeloVeiculo extends TenantEntity {

    @NotNull(message = "A marca ÃƒÂ© obrigatÃƒÂ³ria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaVeiculo marca;

    @NotBlank(message = "O nome do modelo ÃƒÂ© obrigatÃƒÂ³rio")
    @Size(max = 150, message = "O nome do modelo deve ter no mÃƒÂ¡ximo 150 caracteres")
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @NotNull(message = "A categoria é obrigatória")
    @Column(name = "categoria", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CategoriaVeiculo categoria;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    public ModeloVeiculo() {
    }

    // Getters and Setters

    public MarcaVeiculo getMarca() {
        return marca;
    }

    public void setMarca(MarcaVeiculo marca) {
        this.marca = marca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaVeiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculo categoria) {
        this.categoria = categoria;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
