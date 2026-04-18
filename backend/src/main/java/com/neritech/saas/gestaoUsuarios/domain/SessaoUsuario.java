package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessoes_usuario")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoUsuario extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token_sessao", nullable = false)
    private String tokenSessao;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "dispositivo_tipo")
    private String dispositivoTipo;

    @Column(name = "sistema_operacional")
    private String sistemaOperacional;

    private String navegador;

    @Column(name = "data_inicio")
    @Builder.Default
    private LocalDateTime dataInicio = LocalDateTime.now();

    @Column(name = "ultimo_acesso")
    @Builder.Default
    private LocalDateTime ultimoAcesso = LocalDateTime.now();

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Builder.Default
    private Boolean ativo = true;
}
