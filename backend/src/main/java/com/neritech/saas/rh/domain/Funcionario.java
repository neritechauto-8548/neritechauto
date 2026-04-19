package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.rh.domain.enums.Escolaridade;
import com.neritech.saas.rh.domain.enums.EstadoCivil;
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
    @Column(name = "estado_civil", length = 20)
    private EstadoCivil estadoCivil;

    @Column(name = "nacionalidade", length = 50)
    private String nacionalidade = "Brasileira";

    @Column(name = "naturalidade", length = 100)
    private String naturalidade;

    @Column(name = "nome_mae", length = 255)
    private String nomeMae;

    @Column(name = "nome_pai", length = 255)
    private String nomePai;

    @Enumerated(EnumType.STRING)
    @Column(name = "escolaridade", length = 30)
    private Escolaridade escolaridade;

    @Column(name = "profissao", length = 100)
    private String profissao;

    @Column(name = "cargo_id")
    private Long cargoId;

    @Column(name = "departamento_id")
    private Long departamentoId;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", length = 20)
    private TipoContrato tipoContrato;

    @Column(name = "salario_base", precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(name = "comissao_percentual", precision = 5, scale = 2)
    private BigDecimal comissaoPercentual = BigDecimal.ZERO;

    @Column(name = "vale_transporte", precision = 8, scale = 2)
    private BigDecimal valeTransporte = BigDecimal.ZERO;

    @Column(name = "vale_alimentacao", precision = 8, scale = 2)
    private BigDecimal valeAlimentacao = BigDecimal.ZERO;

    @Column(name = "plano_saude")
    private Boolean planoSaude = false;

    @Column(name = "plano_odontologico")
    private Boolean planoOdontologico = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusFuncionario status;

    @Column(name = "motivo_inativacao", columnDefinition = "TEXT")
    private String motivoInativacao;

    @Column(name = "endereco_completo", columnDefinition = "TEXT")
    private String enderecoCompleto;

    @Column(name = "telefone_principal", length = 20)
    private String telefonePrincipal;

    @Column(name = "telefone_emergencia", length = 20)
    private String telefoneEmergencia;

    @Column(name = "contato_emergencia_nome", length = 255)
    private String contatoEmergenciaNome;

    @Column(name = "contato_emergencia_parentesco", length = 50)
    private String contatoEmergenciaParentesco;

    @Column(name = "email_pessoal", length = 255)
    private String emailPessoal;

    @Column(name = "banco_codigo", length = 5)
    private String bancoCodigo;

    @Column(name = "banco_agencia", length = 10)
    private String bancoAgencia;

    @Column(name = "banco_conta", length = 20)
    private String bancoConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "banco_tipo_conta", length = 10)
    private TipoConta bancoTipoConta;

    @Column(name = "pis_pasep", length = 15)
    private String pisPasep;

    @Column(name = "titulo_eleitor", length = 15)
    private String tituloEleitor;

    @Column(name = "carteira_trabalho", length = 20)
    private String carteiraTrabalho;

    @Column(name = "certificado_reservista", length = 20)
    private String certificadoReservista;

    @Column(name = "cnh_numero", length = 15)
    private String cnhNumero;

    @Column(name = "cnh_categoria", length = 5)
    private String cnhCategoria;

    @Column(name = "cnh_validade")
    private LocalDate cnhValidade;

    @Column(name = "foto_funcionario_url", length = 500)
    private String fotoFuncionarioUrl;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_por")
    private Long criadoPor;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

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

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public Escolaridade getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(Escolaridade escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
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

    public BigDecimal getComissaoPercentual() {
        return comissaoPercentual;
    }

    public void setComissaoPercentual(BigDecimal comissaoPercentual) {
        this.comissaoPercentual = comissaoPercentual;
    }

    public BigDecimal getValeTransporte() {
        return valeTransporte;
    }

    public void setValeTransporte(BigDecimal valeTransporte) {
        this.valeTransporte = valeTransporte;
    }

    public BigDecimal getValeAlimentacao() {
        return valeAlimentacao;
    }

    public void setValeAlimentacao(BigDecimal valeAlimentacao) {
        this.valeAlimentacao = valeAlimentacao;
    }

    public Boolean getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(Boolean planoSaude) {
        this.planoSaude = planoSaude;
    }

    public Boolean getPlanoOdontologico() {
        return planoOdontologico;
    }

    public void setPlanoOdontologico(Boolean planoOdontologico) {
        this.planoOdontologico = planoOdontologico;
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

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getTelefoneEmergencia() {
        return telefoneEmergencia;
    }

    public void setTelefoneEmergencia(String telefoneEmergencia) {
        this.telefoneEmergencia = telefoneEmergencia;
    }

    public String getContatoEmergenciaNome() {
        return contatoEmergenciaNome;
    }

    public void setContatoEmergenciaNome(String contatoEmergenciaNome) {
        this.contatoEmergenciaNome = contatoEmergenciaNome;
    }

    public String getContatoEmergenciaParentesco() {
        return contatoEmergenciaParentesco;
    }

    public void setContatoEmergenciaParentesco(String contatoEmergenciaParentesco) {
        this.contatoEmergenciaParentesco = contatoEmergenciaParentesco;
    }

    public String getEmailPessoal() {
        return emailPessoal;
    }

    public void setEmailPessoal(String emailPessoal) {
        this.emailPessoal = emailPessoal;
    }

    public String getBancoCodigo() {
        return bancoCodigo;
    }

    public void setBancoCodigo(String bancoCodigo) {
        this.bancoCodigo = bancoCodigo;
    }

    public String getBancoAgencia() {
        return bancoAgencia;
    }

    public void setBancoAgencia(String bancoAgencia) {
        this.bancoAgencia = bancoAgencia;
    }

    public String getBancoConta() {
        return bancoConta;
    }

    public void setBancoConta(String bancoConta) {
        this.bancoConta = bancoConta;
    }

    public TipoConta getBancoTipoConta() {
        return bancoTipoConta;
    }

    public void setBancoTipoConta(TipoConta bancoTipoConta) {
        this.bancoTipoConta = bancoTipoConta;
    }

    public String getPisPasep() {
        return pisPasep;
    }

    public void setPisPasep(String pisPasep) {
        this.pisPasep = pisPasep;
    }

    public String getTituloEleitor() {
        return tituloEleitor;
    }

    public void setTituloEleitor(String tituloEleitor) {
        this.tituloEleitor = tituloEleitor;
    }

    public String getCarteiraTrabalho() {
        return carteiraTrabalho;
    }

    public void setCarteiraTrabalho(String carteiraTrabalho) {
        this.carteiraTrabalho = carteiraTrabalho;
    }

    public String getCertificadoReservista() {
        return certificadoReservista;
    }

    public void setCertificadoReservista(String certificadoReservista) {
        this.certificadoReservista = certificadoReservista;
    }

    public String getCnhNumero() {
        return cnhNumero;
    }

    public void setCnhNumero(String cnhNumero) {
        this.cnhNumero = cnhNumero;
    }

    public String getCnhCategoria() {
        return cnhCategoria;
    }

    public void setCnhCategoria(String cnhCategoria) {
        this.cnhCategoria = cnhCategoria;
    }

    public LocalDate getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(LocalDate cnhValidade) {
        this.cnhValidade = cnhValidade;
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

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }

    public Long getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(Long atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
}
