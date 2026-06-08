package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.Setor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_regras_comissao")
public class RegraComissao extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;

    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual;

    @Column(name = "sobre_servico", length = 20)
    private String sobreServico;

    @Column(name = "sobre_produtos", length = 20)
    private String sobreProdutos;

    @Column(name = "faturamento_geral", length = 50)
    private String faturamentoGeral;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_final")
    private LocalDateTime dataFinal;

    // Getters and Setters
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public BigDecimal getPercentual() { return percentual; }
    public void setPercentual(BigDecimal percentual) { this.percentual = percentual; }

    public String getSobreServico() { return sobreServico; }
    public void setSobreServico(String sobreServico) { this.sobreServico = sobreServico; }

    public String getSobreProdutos() { return sobreProdutos; }
    public void setSobreProdutos(String sobreProdutos) { this.sobreProdutos = sobreProdutos; }

    public String getFaturamentoGeral() { return faturamentoGeral; }
    public void setFaturamentoGeral(String faturamentoGeral) { this.faturamentoGeral = faturamentoGeral; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDateTime dataFinal) { this.dataFinal = dataFinal; }
}
