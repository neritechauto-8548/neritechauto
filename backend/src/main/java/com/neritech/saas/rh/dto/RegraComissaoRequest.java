package com.neritech.saas.rh.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegraComissaoRequest {

    private Long id;
    private Long empresaId;
    private Long funcionarioId;
    private Long setorId;
    private BigDecimal percentual;
    private String sobreServico;
    private String sobreProdutos;
    private String faturamentoGeral;
    private Boolean ativo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;

    // Getters and Setters
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public Long getSetorId() { return setorId; }
    public void setSetorId(Long setorId) { this.setorId = setorId; }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDateTime dataFinal) { this.dataFinal = dataFinal; }
}
