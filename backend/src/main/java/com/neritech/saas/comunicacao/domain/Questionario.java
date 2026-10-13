package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "questionario")
public class Questionario extends BaseEntity {

    @NotNull
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "ds_questionario", nullable = false, length = 255)
    private String dsQuestionario;

    @Size(max = 500)
    @Column(name = "url_imagem", length = 500)
    private String urlImagem;

    @Column(name = "sn_ativo")
    private Boolean snAtivo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pesquisa", nullable = false, length = 20)
    private StatusPesquisa statusPesquisa = StatusPesquisa.DRAFT;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getDsQuestionario() {
        return dsQuestionario;
    }

    public void setDsQuestionario(String dsQuestionario) {
        this.dsQuestionario = dsQuestionario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public StatusPesquisa getStatusPesquisa() { return statusPesquisa; }
    public void setStatusPesquisa(StatusPesquisa statusPesquisa) { this.statusPesquisa = statusPesquisa; }

    public Boolean getSnAtivo() {
        return snAtivo;
    }

    public void setSnAtivo(Boolean snAtivo) {
        this.snAtivo = snAtivo;
    }
}

