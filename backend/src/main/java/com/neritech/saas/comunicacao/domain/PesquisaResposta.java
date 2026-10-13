package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pesquisa_respostas",
    uniqueConstraints = @UniqueConstraint(name = "uq_pesquisa_respostas_tenant_chave", columnNames = {"empresa_id","chave_idempotencia"}))
public class PesquisaResposta extends TenantEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questionario_id", nullable = false)
    private Questionario questionario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "chave_idempotencia", nullable = false, length = 36)
    private String chaveIdempotencia;

    @OneToMany(mappedBy = "resposta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PesquisaRespostaItem> itens = new ArrayList<>();

    public Questionario getQuestionario() { return questionario; }
    public void setQuestionario(Questionario questionario) { this.questionario = questionario; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getChaveIdempotencia() { return chaveIdempotencia; }
    public void setChaveIdempotencia(String chaveIdempotencia) { this.chaveIdempotencia = chaveIdempotencia; }
    public List<PesquisaRespostaItem> getItens() { return itens; }
    public void addItem(PesquisaRespostaItem item) { item.setResposta(this); itens.add(item); }
}
