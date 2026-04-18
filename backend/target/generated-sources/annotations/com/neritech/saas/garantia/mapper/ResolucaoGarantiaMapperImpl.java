package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.QualidadeResolucao;
import com.neritech.saas.garantia.domain.ResolucaoGarantia;
import com.neritech.saas.garantia.domain.TipoResolucao;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaResponse;
import com.neritech.saas.rh.domain.Funcionario;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:58-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ResolucaoGarantiaMapperImpl implements ResolucaoGarantiaMapper {

    @Override
    public ResolucaoGarantia toEntity(ResolucaoGarantiaRequest request) {
        if ( request == null ) {
            return null;
        }

        ResolucaoGarantia resolucaoGarantia = new ResolucaoGarantia();

        resolucaoGarantia.setAprovadaGerencia( request.aprovadaGerencia() );
        resolucaoGarantia.setDescontoConcedido( request.descontoConcedido() );
        resolucaoGarantia.setNovaGarantiaGerada( request.novaGarantiaGerada() );
        resolucaoGarantia.setValorCobradoCliente( request.valorCobradoCliente() );
        resolucaoGarantia.setValorProdutos( request.valorProdutos() );
        resolucaoGarantia.setValorServicos( request.valorServicos() );
        resolucaoGarantia.setValorTotalResolucao( request.valorTotalResolucao() );
        resolucaoGarantia.setTipoResolucao( request.tipoResolucao() );
        resolucaoGarantia.setDescricaoResolucao( request.descricaoResolucao() );
        resolucaoGarantia.setServicosExecutados( request.servicosExecutados() );
        resolucaoGarantia.setProdutosFornecidos( request.produtosFornecidos() );
        resolucaoGarantia.setJustificativaCobranca( request.justificativaCobranca() );
        resolucaoGarantia.setPrazoNovaGarantiaDias( request.prazoNovaGarantiaDias() );
        resolucaoGarantia.setDataInicioExecucao( request.dataInicioExecucao() );
        resolucaoGarantia.setDataConclusao( request.dataConclusao() );
        resolucaoGarantia.setTempoResolucaoHoras( request.tempoResolucaoHoras() );
        resolucaoGarantia.setEquipeExecucao( request.equipeExecucao() );
        resolucaoGarantia.setQualidadeResolucao( request.qualidadeResolucao() );
        resolucaoGarantia.setClienteSatisfeito( request.clienteSatisfeito() );
        resolucaoGarantia.setObservacoesExecucao( request.observacoesExecucao() );
        resolucaoGarantia.setFotosAposResolucao( request.fotosAposResolucao() );
        resolucaoGarantia.setDocumentosComprobatorios( request.documentosComprobatorios() );

        return resolucaoGarantia;
    }

    @Override
    public ResolucaoGarantiaResponse toResponse(ResolucaoGarantia entity) {
        if ( entity == null ) {
            return null;
        }

        String funcionarioExecutorNome = null;
        Long id = null;
        TipoResolucao tipoResolucao = null;
        String descricaoResolucao = null;
        String servicosExecutados = null;
        String produtosFornecidos = null;
        BigDecimal valorServicos = null;
        BigDecimal valorProdutos = null;
        BigDecimal valorTotalResolucao = null;
        BigDecimal valorCobradoCliente = null;
        BigDecimal descontoConcedido = null;
        String justificativaCobranca = null;
        Boolean novaGarantiaGerada = null;
        Integer prazoNovaGarantiaDias = null;
        LocalDateTime dataInicioExecucao = null;
        LocalDateTime dataConclusao = null;
        Integer tempoResolucaoHoras = null;
        String equipeExecucao = null;
        QualidadeResolucao qualidadeResolucao = null;
        Boolean clienteSatisfeito = null;
        String observacoesExecucao = null;
        String fotosAposResolucao = null;
        String documentosComprobatorios = null;
        Boolean aprovadaGerencia = null;
        Long aprovadaPor = null;
        LocalDateTime dataAprovacao = null;

        funcionarioExecutorNome = entityFuncionarioExecutorNomeCompleto( entity );
        id = entity.getId();
        tipoResolucao = entity.getTipoResolucao();
        descricaoResolucao = entity.getDescricaoResolucao();
        servicosExecutados = entity.getServicosExecutados();
        produtosFornecidos = entity.getProdutosFornecidos();
        valorServicos = entity.getValorServicos();
        valorProdutos = entity.getValorProdutos();
        valorTotalResolucao = entity.getValorTotalResolucao();
        valorCobradoCliente = entity.getValorCobradoCliente();
        descontoConcedido = entity.getDescontoConcedido();
        justificativaCobranca = entity.getJustificativaCobranca();
        novaGarantiaGerada = entity.getNovaGarantiaGerada();
        prazoNovaGarantiaDias = entity.getPrazoNovaGarantiaDias();
        dataInicioExecucao = entity.getDataInicioExecucao();
        dataConclusao = entity.getDataConclusao();
        tempoResolucaoHoras = entity.getTempoResolucaoHoras();
        equipeExecucao = entity.getEquipeExecucao();
        qualidadeResolucao = entity.getQualidadeResolucao();
        clienteSatisfeito = entity.getClienteSatisfeito();
        observacoesExecucao = entity.getObservacoesExecucao();
        fotosAposResolucao = entity.getFotosAposResolucao();
        documentosComprobatorios = entity.getDocumentosComprobatorios();
        aprovadaGerencia = entity.getAprovadaGerencia();
        aprovadaPor = entity.getAprovadaPor();
        dataAprovacao = entity.getDataAprovacao();

        Long reclamacaoId = null;
        Long novaGarantiaId = null;
        Long funcionarioExecutorId = null;

        ResolucaoGarantiaResponse resolucaoGarantiaResponse = new ResolucaoGarantiaResponse( id, reclamacaoId, tipoResolucao, descricaoResolucao, servicosExecutados, produtosFornecidos, valorServicos, valorProdutos, valorTotalResolucao, valorCobradoCliente, descontoConcedido, justificativaCobranca, novaGarantiaGerada, novaGarantiaId, prazoNovaGarantiaDias, dataInicioExecucao, dataConclusao, tempoResolucaoHoras, funcionarioExecutorId, funcionarioExecutorNome, equipeExecucao, qualidadeResolucao, clienteSatisfeito, observacoesExecucao, fotosAposResolucao, documentosComprobatorios, aprovadaGerencia, aprovadaPor, dataAprovacao );

        return resolucaoGarantiaResponse;
    }

    @Override
    public void updateEntityFromDTO(ResolucaoGarantiaRequest request, ResolucaoGarantia entity) {
        if ( request == null ) {
            return;
        }

        if ( request.aprovadaGerencia() != null ) {
            entity.setAprovadaGerencia( request.aprovadaGerencia() );
        }
        if ( request.descontoConcedido() != null ) {
            entity.setDescontoConcedido( request.descontoConcedido() );
        }
        if ( request.novaGarantiaGerada() != null ) {
            entity.setNovaGarantiaGerada( request.novaGarantiaGerada() );
        }
        if ( request.valorCobradoCliente() != null ) {
            entity.setValorCobradoCliente( request.valorCobradoCliente() );
        }
        if ( request.valorProdutos() != null ) {
            entity.setValorProdutos( request.valorProdutos() );
        }
        if ( request.valorServicos() != null ) {
            entity.setValorServicos( request.valorServicos() );
        }
        if ( request.valorTotalResolucao() != null ) {
            entity.setValorTotalResolucao( request.valorTotalResolucao() );
        }
        if ( request.tipoResolucao() != null ) {
            entity.setTipoResolucao( request.tipoResolucao() );
        }
        if ( request.descricaoResolucao() != null ) {
            entity.setDescricaoResolucao( request.descricaoResolucao() );
        }
        if ( request.servicosExecutados() != null ) {
            entity.setServicosExecutados( request.servicosExecutados() );
        }
        if ( request.produtosFornecidos() != null ) {
            entity.setProdutosFornecidos( request.produtosFornecidos() );
        }
        if ( request.justificativaCobranca() != null ) {
            entity.setJustificativaCobranca( request.justificativaCobranca() );
        }
        if ( request.prazoNovaGarantiaDias() != null ) {
            entity.setPrazoNovaGarantiaDias( request.prazoNovaGarantiaDias() );
        }
        if ( request.dataInicioExecucao() != null ) {
            entity.setDataInicioExecucao( request.dataInicioExecucao() );
        }
        if ( request.dataConclusao() != null ) {
            entity.setDataConclusao( request.dataConclusao() );
        }
        if ( request.tempoResolucaoHoras() != null ) {
            entity.setTempoResolucaoHoras( request.tempoResolucaoHoras() );
        }
        if ( request.equipeExecucao() != null ) {
            entity.setEquipeExecucao( request.equipeExecucao() );
        }
        if ( request.qualidadeResolucao() != null ) {
            entity.setQualidadeResolucao( request.qualidadeResolucao() );
        }
        if ( request.clienteSatisfeito() != null ) {
            entity.setClienteSatisfeito( request.clienteSatisfeito() );
        }
        if ( request.observacoesExecucao() != null ) {
            entity.setObservacoesExecucao( request.observacoesExecucao() );
        }
        if ( request.fotosAposResolucao() != null ) {
            entity.setFotosAposResolucao( request.fotosAposResolucao() );
        }
        if ( request.documentosComprobatorios() != null ) {
            entity.setDocumentosComprobatorios( request.documentosComprobatorios() );
        }
    }

    private String entityFuncionarioExecutorNomeCompleto(ResolucaoGarantia resolucaoGarantia) {
        if ( resolucaoGarantia == null ) {
            return null;
        }
        Funcionario funcionarioExecutor = resolucaoGarantia.getFuncionarioExecutor();
        if ( funcionarioExecutor == null ) {
            return null;
        }
        String nomeCompleto = funcionarioExecutor.getNomeCompleto();
        if ( nomeCompleto == null ) {
            return null;
        }
        return nomeCompleto;
    }
}
