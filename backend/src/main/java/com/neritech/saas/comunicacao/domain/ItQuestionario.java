package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "it_questionario")
public class ItQuestionario extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_questionario", nullable = false)
    private Questionario questionario;

    @NotBlank
    @Size(max = 255)
    @Column(name = "ds_itquestionario", nullable = false, length = 255)
    private String dsItQuestionario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tp_itquestionario", nullable = false, length = 2)
    private TipoItemQuestionario tpItQuestionario;

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public String getDsItQuestionario() {
        return dsItQuestionario;
    }

    public void setDsItQuestionario(String dsItQuestionario) {
        this.dsItQuestionario = dsItQuestionario;
    }

    public TipoItemQuestionario getTpItQuestionario() {
        return tpItQuestionario;
    }

    public void setTpItQuestionario(TipoItemQuestionario tpItQuestionario) {
        this.tpItQuestionario = tpItQuestionario;
    }
}
