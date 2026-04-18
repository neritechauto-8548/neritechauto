package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.domain.enums.MetodoAprovacao;
import com.neritech.saas.ordemservico.domain.enums.NivelCombustivel;
import com.neritech.saas.ordemservico.domain.enums.PrioridadeOS;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.dto.FotoOSResponse;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import com.neritech.saas.ordemservico.dto.OrdemServicoRequest;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:19-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class OrdemServicoMapperImpl extends OrdemServicoMapper {

    @Override
    public OrdemServico toEntity(OrdemServicoRequest request) {
        if ( request == null ) {
            return null;
        }

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setEmpresaId( request.empresaId() );
        ordemServico.setNumeroOS( request.numeroOS() );
        ordemServico.setClienteId( request.clienteId() );
        ordemServico.setVeiculoId( request.veiculoId() );
        ordemServico.setTipoOS( request.tipoOS() );
        ordemServico.setPrioridade( request.prioridade() );
        ordemServico.setDataAbertura( request.dataAbertura() );
        ordemServico.setDataPromessa( request.dataPromessa() );
        ordemServico.setQuilometragemEntrada( request.quilometragemEntrada() );
        ordemServico.setNivelCombustivelEntrada( request.nivelCombustivelEntrada() );
        ordemServico.setConsultorResponsavelId( request.consultorResponsavelId() );
        ordemServico.setMecanicoResponsavelId( request.mecanicoResponsavelId() );
        ordemServico.setEquipeExecucao( request.equipeExecucao() );
        ordemServico.setProblemaRelatado( request.problemaRelatado() );
        ordemServico.setSolucaoAplicada( request.solucaoAplicada() );
        ordemServico.setObservacoesInternas( request.observacoesInternas() );
        ordemServico.setObservacoesCliente( request.observacoesCliente() );
        ordemServico.setValorServicos( request.valorServicos() );
        ordemServico.setValorProdutos( request.valorProdutos() );
        ordemServico.setValorDesconto( request.valorDesconto() );
        ordemServico.setValorAcrescimo( request.valorAcrescimo() );
        ordemServico.setValorTotal( request.valorTotal() );
        ordemServico.setFormaPagamentoId( request.formaPagamentoId() );
        ordemServico.setCondicaoPagamentoId( request.condicaoPagamentoId() );
        ordemServico.setValorEntrada( request.valorEntrada() );
        ordemServico.setAprovadoCliente( request.aprovadoCliente() );
        ordemServico.setMetodoAprovacao( request.metodoAprovacao() );
        ordemServico.setGarantiaDias( request.garantiaDias() );

        return ordemServico;
    }

    @Override
    public OrdemServicoResponse toResponse(OrdemServico entity) {
        if ( entity == null ) {
            return null;
        }

        Long statusId = null;
        String statusNome = null;
        String statusCor = null;
        Long id = null;
        Long empresaId = null;
        String numeroOS = null;
        Long clienteId = null;
        Long veiculoId = null;
        TipoOS tipoOS = null;
        PrioridadeOS prioridade = null;
        LocalDateTime dataAbertura = null;
        LocalDateTime dataPromessa = null;
        LocalDateTime dataInicioExecucao = null;
        LocalDateTime dataFimExecucao = null;
        LocalDateTime dataEntrega = null;
        Integer quilometragemEntrada = null;
        Integer quilometragemSaida = null;
        NivelCombustivel nivelCombustivelEntrada = null;
        NivelCombustivel nivelCombustivelSaida = null;
        Long consultorResponsavelId = null;
        Long mecanicoResponsavelId = null;
        String equipeExecucao = null;
        String problemaRelatado = null;
        String solucaoAplicada = null;
        String observacoesInternas = null;
        String observacoesCliente = null;
        BigDecimal valorServicos = null;
        BigDecimal valorProdutos = null;
        BigDecimal valorDesconto = null;
        BigDecimal valorAcrescimo = null;
        BigDecimal valorTotal = null;
        Long formaPagamentoId = null;
        Long condicaoPagamentoId = null;
        BigDecimal valorEntrada = null;
        BigDecimal valorFinanciado = null;
        Boolean aprovadoCliente = null;
        LocalDateTime dataAprovacaoCliente = null;
        MetodoAprovacao metodoAprovacao = null;
        Integer garantiaDias = null;
        LocalDate dataVencimentoGarantia = null;
        Boolean nfeEmitida = null;
        String numeroNFe = null;
        Integer notaAvaliacaoCliente = null;
        Integer tempoTotalExecucaoMinutos = null;
        BigDecimal margemLucroRealizada = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;
        Integer versao = null;

        statusId = entityStatusId( entity );
        statusNome = entityStatusNome( entity );
        statusCor = entityStatusCorIdentificacao( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        numeroOS = entity.getNumeroOS();
        clienteId = entity.getClienteId();
        veiculoId = entity.getVeiculoId();
        tipoOS = entity.getTipoOS();
        prioridade = entity.getPrioridade();
        dataAbertura = entity.getDataAbertura();
        dataPromessa = entity.getDataPromessa();
        dataInicioExecucao = entity.getDataInicioExecucao();
        dataFimExecucao = entity.getDataFimExecucao();
        dataEntrega = entity.getDataEntrega();
        quilometragemEntrada = entity.getQuilometragemEntrada();
        quilometragemSaida = entity.getQuilometragemSaida();
        nivelCombustivelEntrada = entity.getNivelCombustivelEntrada();
        nivelCombustivelSaida = entity.getNivelCombustivelSaida();
        consultorResponsavelId = entity.getConsultorResponsavelId();
        mecanicoResponsavelId = entity.getMecanicoResponsavelId();
        equipeExecucao = entity.getEquipeExecucao();
        problemaRelatado = entity.getProblemaRelatado();
        solucaoAplicada = entity.getSolucaoAplicada();
        observacoesInternas = entity.getObservacoesInternas();
        observacoesCliente = entity.getObservacoesCliente();
        valorServicos = entity.getValorServicos();
        valorProdutos = entity.getValorProdutos();
        valorDesconto = entity.getValorDesconto();
        valorAcrescimo = entity.getValorAcrescimo();
        valorTotal = entity.getValorTotal();
        formaPagamentoId = entity.getFormaPagamentoId();
        condicaoPagamentoId = entity.getCondicaoPagamentoId();
        valorEntrada = entity.getValorEntrada();
        valorFinanciado = entity.getValorFinanciado();
        aprovadoCliente = entity.getAprovadoCliente();
        dataAprovacaoCliente = entity.getDataAprovacaoCliente();
        metodoAprovacao = entity.getMetodoAprovacao();
        garantiaDias = entity.getGarantiaDias();
        dataVencimentoGarantia = entity.getDataVencimentoGarantia();
        nfeEmitida = entity.getNfeEmitida();
        numeroNFe = entity.getNumeroNFe();
        notaAvaliacaoCliente = entity.getNotaAvaliacaoCliente();
        tempoTotalExecucaoMinutos = entity.getTempoTotalExecucaoMinutos();
        margemLucroRealizada = entity.getMargemLucroRealizada();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();
        versao = entity.getVersao();

        String nomeCliente = getClienteNome(entity.getClienteId());
        String placaVeiculo = getVeiculoPlaca(entity.getVeiculoId());
        String nomeVeiculo = getVeiculoNome(entity.getVeiculoId());
        List<ItemOSServicoResponse> servicos = null;
        List<ItemOSProdutoResponse> produtos = null;
        List<FotoOSResponse> fotos = null;

        OrdemServicoResponse ordemServicoResponse = new OrdemServicoResponse( id, empresaId, numeroOS, clienteId, veiculoId, statusId, tipoOS, prioridade, dataAbertura, dataPromessa, dataInicioExecucao, dataFimExecucao, dataEntrega, quilometragemEntrada, quilometragemSaida, nivelCombustivelEntrada, nivelCombustivelSaida, consultorResponsavelId, mecanicoResponsavelId, equipeExecucao, problemaRelatado, solucaoAplicada, observacoesInternas, observacoesCliente, valorServicos, valorProdutos, valorDesconto, valorAcrescimo, valorTotal, formaPagamentoId, condicaoPagamentoId, valorEntrada, valorFinanciado, aprovadoCliente, dataAprovacaoCliente, metodoAprovacao, garantiaDias, dataVencimentoGarantia, nfeEmitida, numeroNFe, notaAvaliacaoCliente, tempoTotalExecucaoMinutos, margemLucroRealizada, dataCadastro, dataAtualizacao, versao, nomeCliente, placaVeiculo, nomeVeiculo, statusNome, statusCor, servicos, produtos, fotos );

        return ordemServicoResponse;
    }

    @Override
    public void updateEntityFromRequest(OrdemServicoRequest request, OrdemServico entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.numeroOS() != null ) {
            entity.setNumeroOS( request.numeroOS() );
        }
        if ( request.clienteId() != null ) {
            entity.setClienteId( request.clienteId() );
        }
        if ( request.veiculoId() != null ) {
            entity.setVeiculoId( request.veiculoId() );
        }
        if ( request.tipoOS() != null ) {
            entity.setTipoOS( request.tipoOS() );
        }
        if ( request.prioridade() != null ) {
            entity.setPrioridade( request.prioridade() );
        }
        if ( request.dataAbertura() != null ) {
            entity.setDataAbertura( request.dataAbertura() );
        }
        if ( request.dataPromessa() != null ) {
            entity.setDataPromessa( request.dataPromessa() );
        }
        if ( request.quilometragemEntrada() != null ) {
            entity.setQuilometragemEntrada( request.quilometragemEntrada() );
        }
        if ( request.nivelCombustivelEntrada() != null ) {
            entity.setNivelCombustivelEntrada( request.nivelCombustivelEntrada() );
        }
        if ( request.consultorResponsavelId() != null ) {
            entity.setConsultorResponsavelId( request.consultorResponsavelId() );
        }
        if ( request.mecanicoResponsavelId() != null ) {
            entity.setMecanicoResponsavelId( request.mecanicoResponsavelId() );
        }
        if ( request.equipeExecucao() != null ) {
            entity.setEquipeExecucao( request.equipeExecucao() );
        }
        if ( request.problemaRelatado() != null ) {
            entity.setProblemaRelatado( request.problemaRelatado() );
        }
        if ( request.solucaoAplicada() != null ) {
            entity.setSolucaoAplicada( request.solucaoAplicada() );
        }
        if ( request.observacoesInternas() != null ) {
            entity.setObservacoesInternas( request.observacoesInternas() );
        }
        if ( request.observacoesCliente() != null ) {
            entity.setObservacoesCliente( request.observacoesCliente() );
        }
        if ( request.valorServicos() != null ) {
            entity.setValorServicos( request.valorServicos() );
        }
        if ( request.valorProdutos() != null ) {
            entity.setValorProdutos( request.valorProdutos() );
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
        if ( request.formaPagamentoId() != null ) {
            entity.setFormaPagamentoId( request.formaPagamentoId() );
        }
        if ( request.condicaoPagamentoId() != null ) {
            entity.setCondicaoPagamentoId( request.condicaoPagamentoId() );
        }
        if ( request.valorEntrada() != null ) {
            entity.setValorEntrada( request.valorEntrada() );
        }
        if ( request.aprovadoCliente() != null ) {
            entity.setAprovadoCliente( request.aprovadoCliente() );
        }
        if ( request.metodoAprovacao() != null ) {
            entity.setMetodoAprovacao( request.metodoAprovacao() );
        }
        if ( request.garantiaDias() != null ) {
            entity.setGarantiaDias( request.garantiaDias() );
        }
    }

    private Long entityStatusId(OrdemServico ordemServico) {
        if ( ordemServico == null ) {
            return null;
        }
        StatusOS status = ordemServico.getStatus();
        if ( status == null ) {
            return null;
        }
        Long id = status.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityStatusNome(OrdemServico ordemServico) {
        if ( ordemServico == null ) {
            return null;
        }
        StatusOS status = ordemServico.getStatus();
        if ( status == null ) {
            return null;
        }
        String nome = status.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private String entityStatusCorIdentificacao(OrdemServico ordemServico) {
        if ( ordemServico == null ) {
            return null;
        }
        StatusOS status = ordemServico.getStatus();
        if ( status == null ) {
            return null;
        }
        String corIdentificacao = status.getCorIdentificacao();
        if ( corIdentificacao == null ) {
            return null;
        }
        return corIdentificacao;
    }
}
