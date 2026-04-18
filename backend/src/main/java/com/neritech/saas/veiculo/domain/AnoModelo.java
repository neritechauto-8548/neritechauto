package com.neritech.saas.veiculo.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "anos_modelo")
public class AnoModelo extends BaseEntity {

    @NotNull(message = "O modelo ÃƒÂ© obrigatÃƒÂ³rio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private ModeloVeiculo modelo;

    @NotNull(message = "O ano de fabricaÃƒÂ§ÃƒÂ£o ÃƒÂ© obrigatÃƒÂ³rio")
    @Column(name = "ano_fabricacao", nullable = false)
    private Integer anoFabricacao;

    @NotNull(message = "O ano do modelo ÃƒÂ© obrigatÃƒÂ³rio")
    @Column(name = "ano_modelo", nullable = false)
    private Integer anoModelo;

    @Column(name = "codigo_fipe", length = 20)
    private String codigoFipe;

    @Column(name = "valor_fipe", precision = 12, scale = 2)
    private BigDecimal valorFipe;

    @Column(name = "data_valor_fipe")
    private LocalDate dataValorFipe;

    public AnoModelo() {
    }

    // Getters and Setters

    public ModeloVeiculo getModelo() {
        return modelo;
    }

    public void setModelo(ModeloVeiculo modelo) {
        this.modelo = modelo;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getCodigoFipe() {
        return codigoFipe;
    }

    public void setCodigoFipe(String codigoFipe) {
        this.codigoFipe = codigoFipe;
    }

    public BigDecimal getValorFipe() {
        return valorFipe;
    }

    public void setValorFipe(BigDecimal valorFipe) {
        this.valorFipe = valorFipe;
    }

    public LocalDate getDataValorFipe() {
        return dataValorFipe;
    }

    public void setDataValorFipe(LocalDate dataValorFipe) {
        this.dataValorFipe = dataValorFipe;
    }
}

