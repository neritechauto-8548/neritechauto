package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contas_bancarias")
@Getter
@Setter
public class ContaBancaria extends TenantEntity {

    @Column(name = "banco_codigo", nullable = false, length = 5)
    private String bancoCodigo;

    @Column(name = "banco_nome", nullable = false, length = 100)
    private String bancoNome;

    @Column(name = "agencia", nullable = false, length = 10)
    private String agencia;

    @Column(name = "conta", nullable = false, length = 20)
    private String conta;

    @Column(name = "digito_conta", length = 2)
    private String digitoConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", length = 20)
    private TipoConta tipoConta;

    @Column(name = "titular_conta", nullable = false)
    private String titularConta;

    @Column(name = "cpf_cnpj_titular", nullable = false, length = 18)
    private String cpfCnpjTitular;

    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @Column(name = "limite_cheque_especial", precision = 12, scale = 2)
    private BigDecimal limiteChequeEspecial = BigDecimal.ZERO;

    @Column(name = "data_ultimo_saldo")
    private LocalDate dataUltimoSaldo;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public String getBancoCodigo() {
        return this.bancoCodigo;
    }
    public void setBancoCodigo(String bancoCodigo) {
        this.bancoCodigo = bancoCodigo;
    }
    public String getBancoNome() {
        return this.bancoNome;
    }
    public void setBancoNome(String bancoNome) {
        this.bancoNome = bancoNome;
    }
    public String getAgencia() {
        return this.agencia;
    }
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
    public String getConta() {
        return this.conta;
    }
    public void setConta(String conta) {
        this.conta = conta;
    }
    public String getDigitoConta() {
        return this.digitoConta;
    }
    public void setDigitoConta(String digitoConta) {
        this.digitoConta = digitoConta;
    }
    public TipoConta getTipoConta() {
        return this.tipoConta;
    }
    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }
    public String getTitularConta() {
        return this.titularConta;
    }
    public void setTitularConta(String titularConta) {
        this.titularConta = titularConta;
    }
    public String getCpfCnpjTitular() {
        return this.cpfCnpjTitular;
    }
    public void setCpfCnpjTitular(String cpfCnpjTitular) {
        this.cpfCnpjTitular = cpfCnpjTitular;
    }
    public LocalDate getDataUltimoSaldo() {
        return this.dataUltimoSaldo;
    }
    public void setDataUltimoSaldo(LocalDate dataUltimoSaldo) {
        this.dataUltimoSaldo = dataUltimoSaldo;
    }
}
