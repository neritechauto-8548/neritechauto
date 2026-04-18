package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Orcamento;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.MetodoEnvio;
import com.neritech.saas.ordemservico.domain.enums.StatusOrcamento;
import com.neritech.saas.ordemservico.domain.enums.TipoOrcamento;
import com.neritech.saas.ordemservico.dto.OrcamentoRequest;
import com.neritech.saas.ordemservico.dto.OrcamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:50-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class OrcamentoMapperImpl implements OrcamentoMapper {

    @Override
    public Orcamento toEntity(OrcamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Orcamento orcamento = new Orcamento();

        orcamento.setNumeroOrcamento( request.numeroOrcamento() );
        orcamento.setTipoOrcamento( request.tipoOrcamento() );
        orcamento.setValorServicos( request.valorServicos() );
        orcamento.setValorProdutos( request.valorProdutos() );
        orcamento.setValorMaoObra( request.valorMaoObra() );
        orcamento.setValorDesconto( request.valorDesconto() );
        orcamento.setValorAcrescimo( request.valorAcrescimo() );
        orcamento.setValorTotal( request.valorTotal() );
        orcamento.setPrazoValidadeDias( request.prazoValidadeDias() );
        orcamento.setDataVencimento( request.dataVencimento() );
        orcamento.setPrazoExecucaoDias( request.prazoExecucaoDias() );
        orcamento.setDataPrevistaConclusao( request.dataPrevistaConclusao() );
        orcamento.setCondicoesPagamento( request.condicoesPagamento() );
        orcamento.setObservacoesComerciais( request.observacoesComerciais() );
        orcamento.setTermosCondicoes( request.termosCondicoes() );
        orcamento.setStatus( request.status() );
        orcamento.setMetodoEnvio( request.metodoEnvio() );
        orcamento.setResponsavelOrcamentoId( request.responsavelOrcamentoId() );

        return orcamento;
    }

    @Override
    public OrcamentoResponse toResponse(Orcamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long ordemServicoId = null;
        Integer versaoRegistro = null;
        Long id = null;
        String numeroOrcamento = null;
        Integer versao = null;
        TipoOrcamento tipoOrcamento = null;
        BigDecimal valorServicos = null;
        BigDecimal valorProdutos = null;
        BigDecimal valorMaoObra = null;
        BigDecimal valorDesconto = null;
        BigDecimal valorAcrescimo = null;
        BigDecimal valorTotal = null;
        Integer prazoValidadeDias = null;
        LocalDate dataVencimento = null;
        Integer prazoExecucaoDias = null;
        LocalDate dataPrevistaConclusao = null;
        String condicoesPagamento = null;
        String observacoesComerciais = null;
        String termosCondicoes = null;
        StatusOrcamento status = null;
        LocalDateTime dataEnvioCliente = null;
        LocalDateTime dataRespostaCliente = null;
        MetodoEnvio metodoEnvio = null;
        String aprovadoPorCliente = null;
        String documentoCliente = null;
        String ipAprovacao = null;
        LocalDateTime dataAprovacao = null;
        String motivoRejeicao = null;
        Long responsavelOrcamentoId = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        ordemServicoId = entityOrdemServicoId( entity );
        versaoRegistro = entity.getVersao();
        id = entity.getId();
        numeroOrcamento = entity.getNumeroOrcamento();
        versao = entity.getVersao();
        tipoOrcamento = entity.getTipoOrcamento();
        valorServicos = entity.getValorServicos();
        valorProdutos = entity.getValorProdutos();
        valorMaoObra = entity.getValorMaoObra();
        valorDesconto = entity.getValorDesconto();
        valorAcrescimo = entity.getValorAcrescimo();
        valorTotal = entity.getValorTotal();
        prazoValidadeDias = entity.getPrazoValidadeDias();
        dataVencimento = entity.getDataVencimento();
        prazoExecucaoDias = entity.getPrazoExecucaoDias();
        dataPrevistaConclusao = entity.getDataPrevistaConclusao();
        condicoesPagamento = entity.getCondicoesPagamento();
        observacoesComerciais = entity.getObservacoesComerciais();
        termosCondicoes = entity.getTermosCondicoes();
        status = entity.getStatus();
        dataEnvioCliente = entity.getDataEnvioCliente();
        dataRespostaCliente = entity.getDataRespostaCliente();
        metodoEnvio = entity.getMetodoEnvio();
        aprovadoPorCliente = entity.getAprovadoPorCliente();
        documentoCliente = entity.getDocumentoCliente();
        ipAprovacao = entity.getIpAprovacao();
        dataAprovacao = entity.getDataAprovacao();
        motivoRejeicao = entity.getMotivoRejeicao();
        responsavelOrcamentoId = entity.getResponsavelOrcamentoId();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        OrcamentoResponse orcamentoResponse = new OrcamentoResponse( id, ordemServicoId, numeroOrcamento, versao, tipoOrcamento, valorServicos, valorProdutos, valorMaoObra, valorDesconto, valorAcrescimo, valorTotal, prazoValidadeDias, dataVencimento, prazoExecucaoDias, dataPrevistaConclusao, condicoesPagamento, observacoesComerciais, termosCondicoes, status, dataEnvioCliente, dataRespostaCliente, metodoEnvio, aprovadoPorCliente, documentoCliente, ipAprovacao, dataAprovacao, motivoRejeicao, responsavelOrcamentoId, dataCadastro, dataAtualizacao, versaoRegistro );

        return orcamentoResponse;
    }

    @Override
    public void updateEntityFromRequest(OrcamentoRequest request, Orcamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.numeroOrcamento() != null ) {
            entity.setNumeroOrcamento( request.numeroOrcamento() );
        }
        if ( request.versao() != null ) {
            entity.setVersao( request.versao() );
        }
        if ( request.tipoOrcamento() != null ) {
            entity.setTipoOrcamento( request.tipoOrcamento() );
        }
        if ( request.valorServicos() != null ) {
            entity.setValorServicos( request.valorServicos() );
        }
        if ( request.valorProdutos() != null ) {
            entity.setValorProdutos( request.valorProdutos() );
        }
        if ( request.valorMaoObra() != null ) {
            entity.setValorMaoObra( request.valorMaoObra() );
        }
        if ( request.valorDesconto() != null ) {
            entity.setValorDesconto( request.valorDesconto() );
        }
        if ( request.valorAcrescimo() != null ) {
            entity.setValorAcrescimo( request.valorAcrescimo() );
        }
        if ( request.valorTotal() != null ) {
            entity.setValorTotal( request.valorTotal() );
        }
        if ( request.prazoValidadeDias() != null ) {
            entity.setPrazoValidadeDias( request.prazoValidadeDias() );
        }
        if ( request.dataVencimento() != null ) {
            entity.setDataVencimento( request.dataVencimento() );
        }
        if ( request.prazoExecucaoDias() != null ) {
            entity.setPrazoExecucaoDias( request.prazoExecucaoDias() );
        }
        if ( request.dataPrevistaConclusao() != null ) {
            entity.setDataPrevistaConclusao( request.dataPrevistaConclusao() );
        }
        if ( request.condicoesPagamento() != null ) {
            entity.setCondicoesPagamento( request.condicoesPagamento() );
        }
        if ( request.observacoesComerciais() != null ) {
            entity.setObservacoesComerciais( request.observacoesComerciais() );
        }
        if ( request.termosCondicoes() != null ) {
            entity.setTermosCondicoes( request.termosCondicoes() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.metodoEnvio() != null ) {
            entity.setMetodoEnvio( request.metodoEnvio() );
        }
        if ( request.responsavelOrcamentoId() != null ) {
            entity.setResponsavelOrcamentoId( request.responsavelOrcamentoId() );
        }
    }

    private Long entityOrdemServicoId(Orcamento orcamento) {
        if ( orcamento == null ) {
            return null;
        }
        OrdemServico ordemServico = orcamento.getOrdemServico();
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
