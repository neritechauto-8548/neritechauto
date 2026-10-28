package com.neritech.saas.cliente.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "documentos_clientes")
public class DocumentoCliente extends TenantEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @Column(name = "arquivo_nome", nullable = false, length = 180)
    private String arquivoNome;
    @Column(name = "arquivo_tipo", nullable = false, length = 80)
    private String arquivoTipo;
    @Column(name = "arquivo_tamanho", nullable = false)
    private Long arquivoTamanho;
    @Column(name = "arquivo_conteudo", nullable = false, columnDefinition = "bytea")
    private byte[] arquivoConteudo;

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getArquivoNome() { return arquivoNome; }
    public void setArquivoNome(String arquivoNome) { this.arquivoNome = arquivoNome; }
    public String getArquivoTipo() { return arquivoTipo; }
    public void setArquivoTipo(String arquivoTipo) { this.arquivoTipo = arquivoTipo; }
    public Long getArquivoTamanho() { return arquivoTamanho; }
    public void setArquivoTamanho(Long arquivoTamanho) { this.arquivoTamanho = arquivoTamanho; }
    public byte[] getArquivoConteudo() { return arquivoConteudo; }
    public void setArquivoConteudo(byte[] arquivoConteudo) { this.arquivoConteudo = arquivoConteudo; }
}
