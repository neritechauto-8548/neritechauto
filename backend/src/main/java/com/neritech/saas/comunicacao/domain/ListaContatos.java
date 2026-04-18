package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.FrequenciaAtualizacao;
import com.neritech.saas.comunicacao.domain.enums.TipoLista;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_listas_contatos")
public class ListaContatos extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_lista", nullable = false, length = 30)
    private TipoLista tipoLista;

    @Column(name = "criterios_segmentacao", columnDefinition = "TEXT")
    private String criteriosSegmentacao;

    @Column(name = "total_contatos")
    private Integer totalContatos = 0;

    @Column(name = "contatos_ativos")
    private Integer contatosAtivos = 0;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_atualizacao", length = 30)
    private FrequenciaAtualizacao frequenciaAtualizacao;

    @Column(name = "proxima_atualizacao")
    private LocalDateTime proximaAtualizacao;

    @Column(name = "tags", length = 255)
    private String tags;

    @Column(name = "privada")
    private Boolean privada = false;

    @Column(name = "compartilhada_com", columnDefinition = "TEXT")
    private String compartilhadaCom;

    @Column(name = "origem_lista", length = 100)
    private String origemLista;

    @Column(name = "ativa")
    private Boolean ativa = true;

    @Column(name = "criada_por")
    private Long criadaPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoLista getTipoLista() {
        return tipoLista;
    }

    public void setTipoLista(TipoLista tipoLista) {
        this.tipoLista = tipoLista;
    }

    public String getCriteriosSegmentacao() {
        return criteriosSegmentacao;
    }

    public void setCriteriosSegmentacao(String criteriosSegmentacao) {
        this.criteriosSegmentacao = criteriosSegmentacao;
    }

    public Integer getTotalContatos() {
        return totalContatos;
    }

    public void setTotalContatos(Integer totalContatos) {
        this.totalContatos = totalContatos;
    }

    public Integer getContatosAtivos() {
        return contatosAtivos;
    }

    public void setContatosAtivos(Integer contatosAtivos) {
        this.contatosAtivos = contatosAtivos;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public FrequenciaAtualizacao getFrequenciaAtualizacao() {
        return frequenciaAtualizacao;
    }

    public void setFrequenciaAtualizacao(FrequenciaAtualizacao frequenciaAtualizacao) {
        this.frequenciaAtualizacao = frequenciaAtualizacao;
    }

    public LocalDateTime getProximaAtualizacao() {
        return proximaAtualizacao;
    }

    public void setProximaAtualizacao(LocalDateTime proximaAtualizacao) {
        this.proximaAtualizacao = proximaAtualizacao;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getPrivada() {
        return privada;
    }

    public void setPrivada(Boolean privada) {
        this.privada = privada;
    }

    public String getCompartilhadaCom() {
        return compartilhadaCom;
    }

    public void setCompartilhadaCom(String compartilhadaCom) {
        this.compartilhadaCom = compartilhadaCom;
    }

    public String getOrigemLista() {
        return origemLista;
    }

    public void setOrigemLista(String origemLista) {
        this.origemLista = origemLista;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Long getCriadaPor() {
        return criadaPor;
    }

    public void setCriadaPor(Long criadaPor) {
        this.criadaPor = criadaPor;
    }
}
