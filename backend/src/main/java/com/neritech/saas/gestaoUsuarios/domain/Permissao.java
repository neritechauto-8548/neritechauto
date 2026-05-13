package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.audit.BaseEntity;
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
public class Permissao extends BaseEntity {

    @Column(nullable = false)
    private String chave;

    private String descricao;

    private String valor;
}
