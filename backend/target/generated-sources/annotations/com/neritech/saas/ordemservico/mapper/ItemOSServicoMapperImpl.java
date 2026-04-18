package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:39-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ItemOSServicoMapperImpl implements ItemOSServicoMapper {

    @Override
    public ItemOSServico toEntity(ItemOSServicoRequest request) {
        if ( request == null ) {
            return null;
        }

        ItemOSServico itemOSServico = new ItemOSServico();

        itemOSServico.setServicoId( request.servicoId() );
        itemOSServico.setDescricao( request.descricao() );
        itemOSServico.setQuantidade( request.quantidade() );
        itemOSServico.setValorUnitario( request.valorUnitario() );
        itemOSServico.setValorTotal( request.valorTotal() );
        itemOSServico.setDescontoPercentual( request.descontoPercentual() );
        itemOSServico.setDescontoValor( request.descontoValor() );
        itemOSServico.setValorFinal( request.valorFinal() );
        itemOSServico.setMecanicoExecutorId( request.mecanicoExecutorId() );
        itemOSServico.setTempoExecucaoPrevisto( request.tempoExecucaoPrevisto() );
        itemOSServico.setStatusExecucao( request.statusExecucao() );
        itemOSServico.setGarantiaDias( request.garantiaDias() );
        itemOSServico.setObservacoes( request.observacoes() );
        itemOSServico.setAprovadoCliente( request.aprovadoCliente() );
        itemOSServico.setComissaoPercentual( request.comissaoPercentual() );
        itemOSServico.setOrdemExecucao( request.ordemExecucao() );

        return itemOSServico;
    }

    @Override
    public ItemOSServicoResponse toResponse(ItemOSServico entity) {
        if ( entity == null ) {
            return null;
        }

        Long ordemServicoId = null;
        Long id = null;
        Long servicoId = null;
        String descricao = null;
        BigDecimal quantidade = null;
        BigDecimal valorUnitario = null;
        BigDecimal valorTotal = null;
        BigDecimal descontoPercentual = null;
        BigDecimal descontoValor = null;
        BigDecimal valorFinal = null;
        Long mecanicoExecutorId = null;
        Integer tempoExecucaoPrevisto = null;
        Integer tempoExecucaoReal = null;
        LocalDateTime dataInicioExecucao = null;
        LocalDateTime dataFimExecucao = null;
        StatusExecucao statusExecucao = null;
        Integer garantiaDias = null;
        String observacoes = null;
        Boolean aprovadoCliente = null;
        LocalDateTime dataAprovacaoCliente = null;
        BigDecimal comissaoPercentual = null;
        BigDecimal comissaoValor = null;
        Integer ordemExecucao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;
        Integer versao = null;

        ordemServicoId = entityOrdemServicoId( entity );
        id = entity.getId();
        servicoId = entity.getServicoId();
        descricao = entity.getDescricao();
        quantidade = entity.getQuantidade();
        valorUnitario = entity.getValorUnitario();
        valorTotal = entity.getValorTotal();
        descontoPercentual = entity.getDescontoPercentual();
        descontoValor = entity.getDescontoValor();
        valorFinal = entity.getValorFinal();
        mecanicoExecutorId = entity.getMecanicoExecutorId();
        tempoExecucaoPrevisto = entity.getTempoExecucaoPrevisto();
        tempoExecucaoReal = entity.getTempoExecucaoReal();
        dataInicioExecucao = entity.getDataInicioExecucao();
        dataFimExecucao = entity.getDataFimExecucao();
        statusExecucao = entity.getStatusExecucao();
        garantiaDias = entity.getGarantiaDias();
        observacoes = entity.getObservacoes();
        aprovadoCliente = entity.getAprovadoCliente();
        dataAprovacaoCliente = entity.getDataAprovacaoCliente();
        comissaoPercentual = entity.getComissaoPercentual();
        comissaoValor = entity.getComissaoValor();
        ordemExecucao = entity.getOrdemExecucao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();
        versao = entity.getVersao();

        ItemOSServicoResponse itemOSServicoResponse = new ItemOSServicoResponse( id, ordemServicoId, servicoId, descricao, quantidade, valorUnitario, valorTotal, descontoPercentual, descontoValor, valorFinal, mecanicoExecutorId, tempoExecucaoPrevisto, tempoExecucaoReal, dataInicioExecucao, dataFimExecucao, statusExecucao, garantiaDias, observacoes, aprovadoCliente, dataAprovacaoCliente, comissaoPercentual, comissaoValor, ordemExecucao, dataCadastro, dataAtualizacao, versao );

        return itemOSServicoResponse;
    }

    @Override
    public void updateEntityFromRequest(ItemOSServicoRequest request, ItemOSServico entity) {
        if ( request == null ) {
            return;
        }

        if ( request.servicoId() != null ) {
            entity.setServicoId( request.servicoId() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.quantidade() != null ) {
            entity.setQuantidade( request.quantidade() );
        }
        if ( request.valorUnitario() != null ) {
            entity.setValorUnitario( request.valorUnitario() );
        }
        if ( request.valorTotal() != null ) {
            entity.setValorTotal( request.valorTotal() );
        }
        if ( request.descontoPercentual() != null ) {
            entity.setDescontoPercentual( request.descontoPercentual() );
        }
        if ( request.descontoValor() != null ) {
            entity.setDescontoValor( request.descontoValor() );
        }
        if ( request.valorFinal() != null ) {
            entity.setValorFinal( request.valorFinal() );
        }
        if ( request.mecanicoExecutorId() != null ) {
            entity.setMecanicoExecutorId( request.mecanicoExecutorId() );
        }
        if ( request.tempoExecucaoPrevisto() != null ) {
            entity.setTempoExecucaoPrevisto( request.tempoExecucaoPrevisto() );
        }
        if ( request.statusExecucao() != null ) {
            entity.setStatusExecucao( request.statusExecucao() );
        }
        if ( request.garantiaDias() != null ) {
            entity.setGarantiaDias( request.garantiaDias() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.aprovadoCliente() != null ) {
            entity.setAprovadoCliente( request.aprovadoCliente() );
        }
        if ( request.comissaoPercentual() != null ) {
            entity.setComissaoPercentual( request.comissaoPercentual() );
        }
        if ( request.ordemExecucao() != null ) {
            entity.setOrdemExecucao( request.ordemExecucao() );
        }
    }

    private Long entityOrdemServicoId(ItemOSServico itemOSServico) {
        if ( itemOSServico == null ) {
            return null;
        }
        OrdemServico ordemServico = itemOSServico.getOrdemServico();
        if ( ordemServico == null ) {
            return null;
        }
        Long id = ordemServico.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
