package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import jakarta.persistence.*;

@Entity
@Table(name = "pesquisa_resposta_itens")
public class PesquisaRespostaItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resposta_id", nullable = false)
    private PesquisaResposta resposta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private ItQuestionario pergunta;

    @Column(name = "pergunta_texto", nullable = false, length = 255)
    private String perguntaTexto;
    @Enumerated(EnumType.STRING)
    @Column(name = "pergunta_tipo", nullable = false, length = 2)
    private TipoItemQuestionario perguntaTipo;
    @Column(name = "resposta_texto", nullable = false, length = 1000)
    private String respostaTexto;

    public PesquisaResposta getResposta() { return resposta; }
    public void setResposta(PesquisaResposta resposta) { this.resposta = resposta; }
    public ItQuestionario getPergunta() { return pergunta; }
    public void setPergunta(ItQuestionario pergunta) { this.pergunta = pergunta; }
    public String getPerguntaTexto() { return perguntaTexto; }
    public void setPerguntaTexto(String perguntaTexto) { this.perguntaTexto = perguntaTexto; }
    public TipoItemQuestionario getPerguntaTipo() { return perguntaTipo; }
    public void setPerguntaTipo(TipoItemQuestionario perguntaTipo) { this.perguntaTipo = perguntaTipo; }
    public String getRespostaTexto() { return respostaTexto; }
    public void setRespostaTexto(String respostaTexto) { this.respostaTexto = respostaTexto; }
}
