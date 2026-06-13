package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "fin_renegociacoes")
@Getter
@Setter
public class Renegociacao extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titulo_original_id", nullable = false)
    private ContasReceber tituloOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novo_titulo_id", nullable = false)
    private ContasReceber novoTitulo;

    @Column(name = "data_renegociacao", nullable = false)
    private LocalDate dataRenegociacao;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;
}
