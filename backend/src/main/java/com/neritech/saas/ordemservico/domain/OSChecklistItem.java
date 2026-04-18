package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "os_checklist_items")
public class OSChecklistItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private Checklist checklistModelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "it_checklist_id")
    private ItChecklist itemModelo;

    @NotBlank
    @Column(name = "descricao", length = 255, nullable = false)
    private String descricao;

    @Column(name = "feito")
    private Boolean feito = false;

    @Column(name = "ordem")
    private Integer ordem;

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public Checklist getChecklistModelo() { return checklistModelo; }
    public void setChecklistModelo(Checklist checklistModelo) { this.checklistModelo = checklistModelo; }

    public ItChecklist getItemModelo() { return itemModelo; }
    public void setItemModelo(ItChecklist itemModelo) { this.itemModelo = itemModelo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean getFeito() { return feito; }
    public void setFeito(Boolean feito) { this.feito = feito; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}
