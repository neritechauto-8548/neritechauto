package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_acesso")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LogAcesso extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "email_tentativa")
    private String emailTentativa;

    @Column(name = "tipo_evento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    private String detalhes;

    @Column(name = "data_evento")
    @Builder.Default
    private LocalDateTime dataEvento = LocalDateTime.now();

    public enum TipoEvento {
        LOGIN_SUCCESS,
        LOGIN_FAIL,
        LOGOUT,
        REFRESH_TOKEN,
        FORCE_LOGOUT
    }
}
