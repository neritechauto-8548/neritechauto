package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "unidades_medida")
public class UnidadeMedida extends TenantEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @NotBlank
    @Size(max = 10)
    @Column(name = "sigla", nullable = false, length = 10, unique = true)
    private String sigla;

    @Builder.Default
    @Column(name = "ativo")
    private Boolean ativo = true;

}
