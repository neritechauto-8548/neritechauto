package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "OrdemServicoItChecklist")
@Table(name = "it_checklist")
public class ItChecklist extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_check_list", nullable = false)
    private Checklist checklist;

    @NotBlank
    @Size(max = 255)
    @Column(name = "ds_itchecklist", nullable = false, length = 255)
    private String dsItChecklist;

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public String getDsItChecklist() {
        return dsItChecklist;
    }

    public void setDsItChecklist(String dsItChecklist) {
        this.dsItChecklist = dsItChecklist;
    }
}
