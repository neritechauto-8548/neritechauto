package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "perfis_usuario")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuario extends TenantEntity {

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String telefone;

    private String cargo;

    private String departamento;

    // Preferencias stored as JSONB string for simplicity in JPA, or could use a converter
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @org.hibernate.annotations.ColumnTransformer(write = "?::jsonb")
    private java.util.Map<String, Object> preferencias;
}
