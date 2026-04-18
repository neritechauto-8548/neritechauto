package com.neritech.saas.gestaoUsuarios.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "tentativas_login")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TentativaLogin extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Builder.Default
    private Boolean sucesso = false;

    @Column(name = "data_tentativa")
    @Builder.Default
    private LocalDateTime dataTentativa = LocalDateTime.now();

    @Column(name = "empresa_id")
    private Long empresaId;
}
