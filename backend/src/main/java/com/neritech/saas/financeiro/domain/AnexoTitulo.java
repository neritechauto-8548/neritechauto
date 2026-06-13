package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_anexos_titulos")
@Getter
@Setter
public class AnexoTitulo extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_receber_id", nullable = false)
    private ContasReceber contaReceber;

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "tipo_arquivo", nullable = false, length = 50)
    private String tipoArquivo;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "caminho_local", nullable = false, length = 500)
    private String caminhoLocal;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
}
