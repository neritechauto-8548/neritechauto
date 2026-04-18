package com.neritech.saas.relatorios.domain;

import com.neritech.saas.relatorios.domain.enums.TipoParametro;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros_relatorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametroRelatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatorio_id", nullable = false)
    private RelatorioSalvo relatorio;

    @Column(name = "nome_parametro", nullable = false)
    private String nomeParametro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_parametro")
    private TipoParametro tipoParametro;

    @Builder.Default
    private Boolean obrigatorio = false;

    @Column(name = "valor_padrao", columnDefinition = "text")
    private String valorPadrao;

    @Column(name = "opcoes_lista", columnDefinition = "text")
    private String opcoesLista; // JSON

    @Column(name = "validacao_regex")
    private String validacaoRegex;

    @Column(name = "mensagem_erro")
    private String mensagemErro;

    @Builder.Default
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;

    @Column(name = "grupo_parametro")
    private String grupoParametro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependente_de")
    private ParametroRelatorio dependenteDe;

    @Column(name = "condicao_dependencia", columnDefinition = "text")
    private String condicaoDependencia; // JSON

    @Column(name = "ajuda_usuario", columnDefinition = "text")
    private String ajudaUsuario;

    @Builder.Default
    private Boolean ativo = true;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public RelatorioSalvo getRelatorio() {
        return this.relatorio;
    }
    public void setRelatorio(RelatorioSalvo relatorio) {
        this.relatorio = relatorio;
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
    public String getGrupoParametro() {
        return this.grupoParametro;
    }
    public void setGrupoParametro(String grupoParametro) {
        this.grupoParametro = grupoParametro;
    }
    public ParametroRelatorio getDependenteDe() {
        return this.dependenteDe;
    }
    public void setDependenteDe(ParametroRelatorio dependenteDe) {
        this.dependenteDe = dependenteDe;
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
}
