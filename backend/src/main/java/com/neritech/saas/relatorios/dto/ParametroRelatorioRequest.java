package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.TipoParametro;
import lombok.Data;

@Data
public class ParametroRelatorioRequest {
    private Long relatorioId;
    private String nomeParametro;
    private TipoParametro tipoParametro;
    private Boolean obrigatorio;
    private String valorPadrao;
    private String opcoesLista; // JSON
    private String validacaoRegex;
    private String mensagemErro;
    private Integer ordemExibicao;
    private String grupoParametro;
    private Long dependenteDeId;
    private String condicaoDependencia; // JSON
    private String ajudaUsuario;
    private Boolean ativo;

    public Long getRelatorioId() {
        return this.relatorioId;
    }
    public void setRelatorioId(Long relatorioId) {
        this.relatorioId = relatorioId;
    }
    public String getNomeParametro() {
        return this.nomeParametro;
    }
    public void setNomeParametro(String nomeParametro) {
        this.nomeParametro = nomeParametro;
    }
    public TipoParametro getTipoParametro() {
        return this.tipoParametro;
    }
    public void setTipoParametro(TipoParametro tipoParametro) {
        this.tipoParametro = tipoParametro;
    }
    public Boolean isObrigatorio() {
        return this.obrigatorio;
    }
    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
    public String getValorPadrao() {
        return this.valorPadrao;
    }
    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }
    public String getOpcoesLista() {
        return this.opcoesLista;
    }
    public void setOpcoesLista(String opcoesLista) {
        this.opcoesLista = opcoesLista;
    }
    public String getValidacaoRegex() {
        return this.validacaoRegex;
    }
    public void setValidacaoRegex(String validacaoRegex) {
        this.validacaoRegex = validacaoRegex;
    }
    public String getMensagemErro() {
        return this.mensagemErro;
    }
    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
    public Integer getOrdemExibicao() {
        return this.ordemExibicao;
    }
    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }
    public String getGrupoParametro() {
        return this.grupoParametro;
    }
    public void setGrupoParametro(String grupoParametro) {
        this.grupoParametro = grupoParametro;
    }
    public Long getDependenteDeId() {
        return this.dependenteDeId;
    }
    public void setDependenteDeId(Long dependenteDeId) {
        this.dependenteDeId = dependenteDeId;
    }
    public String getCondicaoDependencia() {
        return this.condicaoDependencia;
    }
    public void setCondicaoDependencia(String condicaoDependencia) {
        this.condicaoDependencia = condicaoDependencia;
    }
    public String getAjudaUsuario() {
        return this.ajudaUsuario;
    }
    public void setAjudaUsuario(String ajudaUsuario) {
        this.ajudaUsuario = ajudaUsuario;
    }
    public Boolean isAtivo() {
        return this.ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
