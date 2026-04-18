package com.neritech.saas.veiculo.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "marcas_veiculos")
@AttributeOverride(name = "empresaId", column = @Column(name = "empresa_id", nullable = true))
public class MarcaVeiculo extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da marca é obrigatório")
    @Size(max = 100, message = "O nome da marca deve ter no máximo 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100, unique = true)
    private String nome;

    public MarcaVeiculo() {
    }

    public MarcaVeiculo(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
