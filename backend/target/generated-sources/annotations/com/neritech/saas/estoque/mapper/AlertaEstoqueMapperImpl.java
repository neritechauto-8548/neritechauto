package com.neritech.saas.estoque.mapper;

import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.domain.AlertaEstoque;
import com.neritech.saas.estoque.domain.enums.NivelPrioridade;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import com.neritech.saas.estoque.dto.AlertaEstoqueRequest;
import com.neritech.saas.estoque.dto.AlertaEstoqueResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AlertaEstoqueMapperImpl implements AlertaEstoqueMapper {

    @Override
    public AlertaEstoque toEntity(AlertaEstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        AlertaEstoque alertaEstoque = new AlertaEstoque();

        alertaEstoque.setEmpresaId( request.empresaId() );
        alertaEstoque.setTipoAlerta( request.tipoAlerta() );
        alertaEstoque.setDescricao( request.descricao() );
        alertaEstoque.setNivelPrioridade( request.nivelPrioridade() );
        alertaEstoque.setQuantidadeAtual( request.quantidadeAtual() );
        alertaEstoque.setQuantidadeReferencia( request.quantidadeReferencia() );
        alertaEstoque.setDataVencimento( request.dataVencimento() );
        alertaEstoque.setDiasParado( request.diasParado() );
        alertaEstoque.setValorEnvolvido( request.valorEnvolvido() );
        alertaEstoque.setStatus( request.status() );
        alertaEstoque.setUsuarioResponsavel( request.usuarioResponsavel() );
        alertaEstoque.setObservacoesResolucao( request.observacoesResolucao() );

        return alertaEstoque;
    }

    @Override
    public AlertaEstoqueResponse toResponse(AlertaEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long id = null;
        Long empresaId = null;
        TipoAlerta tipoAlerta = null;
        String descricao = null;
        NivelPrioridade nivelPrioridade = null;
        BigDecimal quantidadeAtual = null;
        BigDecimal quantidadeReferencia = null;
        LocalDate dataVencimento = null;
        Integer diasParado = null;
        BigDecimal valorEnvolvido = null;
        StatusAlerta status = null;
        Long usuarioResponsavel = null;
        LocalDateTime dataResolucao = null;
        String observacoesResolucao = null;
        Boolean notificacaoEnviada = null;
        LocalDateTime dataNotificacao = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        tipoAlerta = entity.getTipoAlerta();
        descricao = entity.getDescricao();
        nivelPrioridade = entity.getNivelPrioridade();
        quantidadeAtual = entity.getQuantidadeAtual();
        quantidadeReferencia = entity.getQuantidadeReferencia();
        dataVencimento = entity.getDataVencimento();
        diasParado = entity.getDiasParado();
        valorEnvolvido = entity.getValorEnvolvido();
        status = entity.getStatus();
        usuarioResponsavel = entity.getUsuarioResponsavel();
        dataResolucao = entity.getDataResolucao();
        observacoesResolucao = entity.getObservacoesResolucao();
        notificacaoEnviada = entity.getNotificacaoEnviada();
        dataNotificacao = entity.getDataNotificacao();

        AlertaEstoqueResponse alertaEstoqueResponse = new AlertaEstoqueResponse( id, empresaId, produtoId, produtoNome, tipoAlerta, descricao, nivelPrioridade, quantidadeAtual, quantidadeReferencia, dataVencimento, diasParado, valorEnvolvido, status, usuarioResponsavel, dataResolucao, observacoesResolucao, notificacaoEnviada, dataNotificacao );

        return alertaEstoqueResponse;
    }

    @Override
    public void updateEntityFromRequest(AlertaEstoqueRequest request, AlertaEstoque entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setTipoAlerta( request.tipoAlerta() );
        entity.setDescricao( request.descricao() );
        entity.setNivelPrioridade( request.nivelPrioridade() );
        entity.setQuantidadeAtual( request.quantidadeAtual() );
        entity.setQuantidadeReferencia( request.quantidadeReferencia() );
        entity.setDataVencimento( request.dataVencimento() );
        entity.setDiasParado( request.diasParado() );
        entity.setValorEnvolvido( request.valorEnvolvido() );
        entity.setStatus( request.status() );
        entity.setUsuarioResponsavel( request.usuarioResponsavel() );
        entity.setObservacoesResolucao( request.observacoesResolucao() );
    }

    private Long entityProdutoId(AlertaEstoque alertaEstoque) {
        if ( alertaEstoque == null ) {
            return null;
        }
        Produto produto = alertaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(AlertaEstoque alertaEstoque) {
        if ( alertaEstoque == null ) {
            return null;
        }
        Produto produto = alertaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
