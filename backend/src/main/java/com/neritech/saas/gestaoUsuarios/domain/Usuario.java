package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends TenantEntity {

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Builder.Default
    private Boolean ativo = true;

    @Builder.Default
    private Boolean bloqueado = false;

    @Column(name = "data_bloqueio")
    private LocalDateTime dataBloqueio;

    @Column(name = "motivo_bloqueio")
    private String motivoBloqueio;

    @Column(name = "deve_trocar_senha")
    @Builder.Default
    private Boolean deveTrocarSenha = false;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilUsuario perfil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_funcoes",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "funcao_id")
    )
    @Builder.Default
    private Set<Funcao> funcoes = new HashSet<>();
}
