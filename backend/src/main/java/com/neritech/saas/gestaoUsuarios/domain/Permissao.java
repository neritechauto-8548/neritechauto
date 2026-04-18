package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "permissoes")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Permissao extends TenantEntity {

    @Column(nullable = false)
    private String nome;

    private String descricao;

    private String modulo;
}
