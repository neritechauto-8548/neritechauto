package com.neritech.saas.veiculo.domain;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "veiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_veiculo_placa_empresa", columnNames = { "placa", "empresa_id" })
})
public class Veiculo extends TenantEntity {

    @NotNull(message = "O cliente Ã© obrigatÃ³rio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private MarcaVeiculo marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id")
    private ModeloVeiculo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ano_modelo_id")
    private AnoModelo anoModelo;

    @NotBlank(message = "A placa Ã© obrigatÃ³ria")
    @Size(max = 10, message = "A placa deve ter no mÃ¡ximo 10 caracteres")
    @Column(name = "placa", nullable = false, length = 10)
    private String placa;

    @Size(max = 11, message = "O Renavam deve ter no mÃ¡ximo 11 caracteres")
    @Column(name = "renavam", length = 11)
    private String renavam;

    @Size(max = 17, message = "O chassi deve ter no mÃ¡ximo 17 caracteres")
    @Column(name = "chassi", length = 17)
    private String chassi;

    @Size(max = 50, message = "O nÃºmero do motor deve ter no mÃ¡ximo 50 caracteres")
    @Column(name = "numero_motor", length = 50)
    private String numeroMotor;

    @Size(max = 50, message = "A cor externa deve ter no mÃ¡ximo 50 caracteres")
    @Column(name = "cor_externa", length = 50)
    private String corExterna;

    @Column(name = "direcao", length = 30)
    private String direcao;
    @Column(name = "cambio", length = 30)
    private String cambio;
    @Column(name = "ar_condicionado")
    private Boolean arCondicionado;
    @Column(name = "portas")
    private Integer portas;

    public String getDirecao() { return direcao; }
    public void setDirecao(String value) { this.direcao = value; }
    public String getCambio() { return cambio; }
    public void setCambio(String value) { this.cambio = value; }
    public Boolean getArCondicionado() { return arCondicionado; }
    public void setArCondicionado(Boolean value) { this.arCondicionado = value; }
    public Integer getPortas() { return portas; }
    public void setPortas(Integer value) { this.portas = value; }

    @Column(name = "quilometragem_atual")
    private Integer quilometragemAtual = 0;

    @Column(name = "quilometragem_cadastro")
    private Integer quilometragemCadastro = 0;

    @Column(name = "data_ultima_revisao")
    private LocalDate dataUltimaRevisao;

    @Column(name = "proxima_revisao_km")
    private Integer proximaRevisaoKm;

    @Column(name = "proxima_revisao_data")
    private LocalDate proximaRevisaoData;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private StatusVeiculo status;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combustivel_id")
    private TipoCombustivel tipoCombustivel;

    @Column(name = "geracao", length = 40)
    private String geracao;
    public String getGeracao() { return geracao; }
    public void setGeracao(String value) { this.geracao = value; }

    @Column(name = "frota", length = 80)
    private String frota;
    public String getFrota() { return frota; }
    public void setFrota(String value) { this.frota = value; }

    @Column(name = "tipo_veiculo", length = 80)
    private String tipoVeiculo;
    public String getTipoVeiculo() { return tipoVeiculo; }
    public void setTipoVeiculo(String value) { this.tipoVeiculo = value; }

    @Column(name = "acessorios", length = 2000)
    private String acessorios;
    public String getAcessorios() { return acessorios; }
    public void setAcessorios(String value) { this.acessorios = value; }

    @Column(name = "cilindrada", precision = 5, scale = 2)
    private BigDecimal cilindrada;
    public BigDecimal getCilindrada() { return cilindrada; }
    public void setCilindrada(BigDecimal value) { this.cilindrada = value; }

    @Column(name = "quantidade_valvulas")
    private Integer quantidadeValvulas;
    public Integer getQuantidadeValvulas() { return quantidadeValvulas; }
    public void setQuantidadeValvulas(Integer value) { this.quantidadeValvulas = value; }

    @Column(name = "quantidade_cilindros")
    private Integer quantidadeCilindros;
    public Integer getQuantidadeCilindros() { return quantidadeCilindros; }
    public void setQuantidadeCilindros(Integer value) { this.quantidadeCilindros = value; }

    @Column(name = "nome_motor", length = 100)
    private String nomeMotor;
    public String getNomeMotor() { return nomeMotor; }
    public void setNomeMotor(String value) { this.nomeMotor = value; }

    @Column(name = "codigo_motor", length = 80)
    private String codigoMotor;
    public String getCodigoMotor() { return codigoMotor; }
    public void setCodigoMotor(String value) { this.codigoMotor = value; }

    @Column(name = "potencias_cv", length = 180)
    private String potenciasCv;
    public String getPotenciasCv() { return potenciasCv; }
    public void setPotenciasCv(String value) { this.potenciasCv = value; }

    @Column(name = "linha", length = 80)
    private String linha;
    public String getLinha() { return linha; }
    public void setLinha(String value) { this.linha = value; }

    @Column(name = "entrada_ar", length = 80)
    private String entradaAr;
    public String getEntradaAr() { return entradaAr; }
    public void setEntradaAr(String value) { this.entradaAr = value; }

    @Column(name = "sistema_freios", length = 80)
    private String sistemaFreios;
    public String getSistemaFreios() { return sistemaFreios; }
    public void setSistemaFreios(String value) { this.sistemaFreios = value; }

    @Column(name = "sistema_tracao", length = 80)
    private String sistemaTracao;
    public String getSistemaTracao() { return sistemaTracao; }
    public void setSistemaTracao(String value) { this.sistemaTracao = value; }

    @Column(name = "sistema_injecao", length = 80)
    private String sistemaInjecao;
    public String getSistemaInjecao() { return sistemaInjecao; }
    public void setSistemaInjecao(String value) { this.sistemaInjecao = value; }

    @Column(name = "configuracao_motor", length = 80)
    private String configuracaoMotor;
    public String getConfiguracaoMotor() { return configuracaoMotor; }
    public void setConfiguracaoMotor(String value) { this.configuracaoMotor = value; }

    @Column(name = "freio_dianteiro", length = 80)
    private String freioDianteiro;
    public String getFreioDianteiro() { return freioDianteiro; }
    public void setFreioDianteiro(String value) { this.freioDianteiro = value; }

    @Column(name = "freio_traseiro", length = 80)
    private String freioTraseiro;
    public String getFreioTraseiro() { return freioTraseiro; }
    public void setFreioTraseiro(String value) { this.freioTraseiro = value; }

    public Veiculo() {
    }

    // Getters and Setters

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public MarcaVeiculo getMarca() {
        return marca;
    }

    public void setMarca(MarcaVeiculo marca) {
        this.marca = marca;
    }

    public ModeloVeiculo getModelo() {
        return modelo;
    }

    public void setModelo(ModeloVeiculo modelo) {
        this.modelo = modelo;
    }

    public AnoModelo getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(AnoModelo anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public String getCorExterna() {
        return corExterna;
    }

    public void setCorExterna(String corExterna) {
        this.corExterna = corExterna;
    }

    public Integer getQuilometragemAtual() {
        return quilometragemAtual;
    }

    public void setQuilometragemAtual(Integer quilometragemAtual) {
        this.quilometragemAtual = quilometragemAtual;
    }

    public Integer getQuilometragemCadastro() {
        return quilometragemCadastro;
    }

    public void setQuilometragemCadastro(Integer quilometragemCadastro) {
        this.quilometragemCadastro = quilometragemCadastro;
    }

    public LocalDate getDataUltimaRevisao() {
        return dataUltimaRevisao;
    }

    public void setDataUltimaRevisao(LocalDate dataUltimaRevisao) {
        this.dataUltimaRevisao = dataUltimaRevisao;
    }

    public Integer getProximaRevisaoKm() {
        return proximaRevisaoKm;
    }

    public void setProximaRevisaoKm(Integer proximaRevisaoKm) {
        this.proximaRevisaoKm = proximaRevisaoKm;
    }

    public LocalDate getProximaRevisaoData() {
        return proximaRevisaoData;
    }

    public void setProximaRevisaoData(LocalDate proximaRevisaoData) {
        this.proximaRevisaoData = proximaRevisaoData;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public TipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
}
