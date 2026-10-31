package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.Sexo;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.enums.TipoContrato;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_id", "cpf"}, name = "uk_funcionarios_empresa_cpf"))
public class Funcionario extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "matricula", nullable = false, length = 20)
    private String matricula;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "rg", length = 20)
    private String rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 1)
    private Sexo sexo;






    @Enumerated(EnumType.STRING)

    @Column(name = "escolaridade", length = 30)
    private Escolaridade escolaridade;


    @Column(name = "cargo_id")
    private Long cargoId;

    @Column(name = "departamento_id")
    private Long departamentoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", insertable = false, updatable = false)
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", insertable = false, updatable = false)
    private com.neritech.saas.empresa.domain.DepartamentoContabio departamento;

    @Column(name = "perfil_profissional", length = 50)
    private String perfilProfissional;

    @Column(name = "registro_profissional", length = 80)
    private String registroProfissional;

    @Column(name = "tipo_profissional", length = 80)
    private String tipoProfissional;

    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacao;

    @Column(name = "comissao_pecas")
    private BigDecimal comissaoPecas;

    @Column(name = "comissao_servicos")
    private BigDecimal comissaoServicos;

    @Column(name = "linhas_transporte", length = 255)
    private String linhasTransporte;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", length = 20)
    private TipoContrato tipoContrato;

    @Column(name = "salario_base", precision = 10, scale = 2)
    private BigDecimal salarioBase;


    @Column(name = "vale_transporte", precision = 8, scale = 2)
    private BigDecimal valeTransporte = BigDecimal.ZERO;




    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusFuncionario status;

    @Column(name = "motivo_inativacao", columnDefinition = "TEXT")
    private String motivoInativacao;


    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "logradouro", length = 255)
    private String logradouro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;











    @Column(name = "pis_pasep", length = 15)
    private String pisPasep;


    @Column(name = "carteira_trabalho", length = 20)
    private String carteiraTrabalho;





    @Column(name = "foto_funcionario_url", length = 500)
    private String fotoFuncionarioUrl;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;



    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }











    public Escolaridade getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(Escolaridade escolaridade) {
        this.escolaridade = escolaridade;
    }



    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }



    public BigDecimal getValeTransporte() {
        return valeTransporte;
    }

    public void setValeTransporte(BigDecimal valeTransporte) {
        this.valeTransporte = valeTransporte;
    }







    public StatusFuncionario getStatus() {
        return status;
    }

    public void setStatus(StatusFuncionario status) {
        this.status = status;
    }

    public String getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(String motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }



    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }





















    public String getPisPasep() {
        return pisPasep;
    }

    public void setPisPasep(String pisPasep) {
        this.pisPasep = pisPasep;
    }



    public String getCarteiraTrabalho() {
        return carteiraTrabalho;
    }

    public void setCarteiraTrabalho(String carteiraTrabalho) {
        this.carteiraTrabalho = carteiraTrabalho;
    }









    public String getFotoFuncionarioUrl() {
        return fotoFuncionarioUrl;
    }

    public void setFotoFuncionarioUrl(String fotoFuncionarioUrl) {
        this.fotoFuncionarioUrl = fotoFuncionarioUrl;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public com.neritech.saas.empresa.domain.DepartamentoContabio getDepartamento() {
        return departamento;
    }

    public void setDepartamento(com.neritech.saas.empresa.domain.DepartamentoContabio departamento) {
        this.departamento = departamento;
    }
    public String getPerfilProfissional() { return perfilProfissional; }
    public void setPerfilProfissional(String valor) { this.perfilProfissional = valor; }

    public String getRegistroProfissional() { return registroProfissional; }
    public void setRegistroProfissional(String value) { this.registroProfissional = value; }
    public String getTipoProfissional() { return tipoProfissional; }
    public void setTipoProfissional(String value) { this.tipoProfissional = value; }
    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String value) { this.areaAtuacao = value; }
    public BigDecimal getComissaoPecas() { return comissaoPecas; }
    public void setComissaoPecas(BigDecimal value) { this.comissaoPecas = value; }
    public BigDecimal getComissaoServicos() { return comissaoServicos; }
    public void setComissaoServicos(BigDecimal value) { this.comissaoServicos = value; }
    public String getLinhasTransporte() { return linhasTransporte; }
    public void setLinhasTransporte(String value) { this.linhasTransporte = value; }
}
