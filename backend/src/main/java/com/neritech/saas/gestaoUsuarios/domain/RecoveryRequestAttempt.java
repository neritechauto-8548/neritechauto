package com.neritech.saas.gestaoUsuarios.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacao_senha_tentativas")
@Getter
@Setter
public class RecoveryRequestAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email_hash", nullable = false, length = 64)
    private String emailHash;
    @Column(name = "ip_hash", nullable = false, length = 64)
    private String ipHash;
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
