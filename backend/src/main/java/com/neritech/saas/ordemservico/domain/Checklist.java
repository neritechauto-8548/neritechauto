package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "checklist")
public class Checklist extends BaseEntity {

    @NotNull
    @Column(name = "id_empresa", nullable = false)
    private Long empresaId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "ds_checklist", nullable = false, length = 255)
    private String dsChecklist;

    @Size(max = 500)
    @Column(name = "url_imagem", length = 500)
    private String urlImagem;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getDsChecklist() {
        return dsChecklist;
    }

    public void setDsChecklist(String dsChecklist) {
        this.dsChecklist = dsChecklist;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}

