package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "funcoes")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Funcao extends TenantEntity {

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Builder.Default
    private Boolean sistema = false;

    @Builder.Default
    private Boolean ativo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "funcoes_permissoes",
        joinColumns = @JoinColumn(name = "funcao_id"),
        inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    @Builder.Default
    private Set<Permissao> permissoes = new HashSet<>();
}
