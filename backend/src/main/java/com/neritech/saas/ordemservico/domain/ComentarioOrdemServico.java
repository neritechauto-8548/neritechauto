package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "comentarios_ordem_servico")
public class ComentarioOrdemServico extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "ordem_servico_id", nullable = false)
    private Long ordemServicoId;

    @Column(name = "usuario_autor_id", nullable = false)
    private Long usuarioAutorId;

    @Column(name = "nome_autor_registrado", nullable = false, length = 180)
    private String nomeAutorRegistrado;

    @Column(name = "conteudo", nullable = false, length = 2000)
    private String conteudo;

    @Column(name = "visibilidade", nullable = false, length = 24)
    private String visibilidade = "INTERNO";

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public Long getOrdemServicoId() { return ordemServicoId; }
    public void setOrdemServicoId(Long ordemServicoId) { this.ordemServicoId = ordemServicoId; }
    public Long getUsuarioAutorId() { return usuarioAutorId; }
    public void setUsuarioAutorId(Long usuarioAutorId) { this.usuarioAutorId = usuarioAutorId; }
    public String getNomeAutorRegistrado() { return nomeAutorRegistrado; }
    public void setNomeAutorRegistrado(String nomeAutorRegistrado) { this.nomeAutorRegistrado = nomeAutorRegistrado; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getVisibilidade() { return visibilidade; }
    public void setVisibilidade(String visibilidade) { this.visibilidade = visibilidade; }
}
