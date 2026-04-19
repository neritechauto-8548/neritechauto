package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.TipoCobertura;
import com.neritech.saas.garantia.domain.TipoGarantia;
import com.neritech.saas.garantia.dto.TipoGarantiaRequest;
import com.neritech.saas.garantia.dto.TipoGarantiaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:34-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TipoGarantiaMapperImpl implements TipoGarantiaMapper {

    @Override
    public TipoGarantia toEntity(TipoGarantiaRequest request) {
        if ( request == null ) {
            return null;
        }

        TipoGarantia tipoGarantia = new TipoGarantia();

        tipoGarantia.setAtivo( request.ativo() );
        tipoGarantia.setPadraoProdutos( request.padraoProdutos() );
        tipoGarantia.setPadraoServicos( request.padraoServicos() );
        tipoGarantia.setPercentualCobertura( request.percentualCobertura() );
        tipoGarantia.setRenovavel( request.renovavel() );
        tipoGarantia.setSlaAtendimentoHoras( request.slaAtendimentoHoras() );
        tipoGarantia.setTransferivel( request.transferivel() );
        tipoGarantia.setNome( request.nome() );
        tipoGarantia.setDescricao( request.descricao() );
        tipoGarantia.setPrazoDias( request.prazoDias() );
        tipoGarantia.setTipoCobertura( request.tipoCobertura() );
        tipoGarantia.setValorMaximoCobertura( request.valorMaximoCobertura() );
        tipoGarantia.setCondicoesAplicacao( request.condicoesAplicacao() );
        tipoGarantia.setRestricoes( request.restricoes() );
        tipoGarantia.setDocumentacaoNecessaria( request.documentacaoNecessaria() );
        tipoGarantia.setProcessoAcionamento( request.processoAcionamento() );
        tipoGarantia.setCustosAdicionais( request.custosAdicionais() );
        tipoGarantia.setExclusoes( request.exclusoes() );

        return tipoGarantia;
    }

    @Override
    public TipoGarantiaResponse toResponse(TipoGarantia entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        Integer prazoDias = null;
        TipoCobertura tipoCobertura = null;
        BigDecimal percentualCobertura = null;
        BigDecimal valorMaximoCobertura = null;
        String condicoesAplicacao = null;
        String restricoes = null;
        String documentacaoNecessaria = null;
        String processoAcionamento = null;
        Integer slaAtendimentoHoras = null;
        Boolean transferivel = null;
        Boolean renovavel = null;
        String custosAdicionais = null;
        String exclusoes = null;
        Boolean ativo = null;
        Boolean padraoServicos = null;
        Boolean padraoProdutos = null;
        LocalDateTime dataCadastro = null;
        Long criadoPor = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        prazoDias = entity.getPrazoDias();
        tipoCobertura = entity.getTipoCobertura();
        percentualCobertura = entity.getPercentualCobertura();
        valorMaximoCobertura = entity.getValorMaximoCobertura();
        condicoesAplicacao = entity.getCondicoesAplicacao();
        restricoes = entity.getRestricoes();
        documentacaoNecessaria = entity.getDocumentacaoNecessaria();
        processoAcionamento = entity.getProcessoAcionamento();
        slaAtendimentoHoras = entity.getSlaAtendimentoHoras();
        transferivel = entity.getTransferivel();
        renovavel = entity.getRenovavel();
        custosAdicionais = entity.getCustosAdicionais();
        exclusoes = entity.getExclusoes();
        ativo = entity.getAtivo();
        padraoServicos = entity.getPadraoServicos();
        padraoProdutos = entity.getPadraoProdutos();
        dataCadastro = entity.getDataCadastro();
        criadoPor = entity.getCriadoPor();

        TipoGarantiaResponse tipoGarantiaResponse = new TipoGarantiaResponse( id, empresaId, nome, descricao, prazoDias, tipoCobertura, percentualCobertura, valorMaximoCobertura, condicoesAplicacao, restricoes, documentacaoNecessaria, processoAcionamento, slaAtendimentoHoras, transferivel, renovavel, custosAdicionais, exclusoes, ativo, padraoServicos, padraoProdutos, dataCadastro, criadoPor );

        return tipoGarantiaResponse;
    }

    @Override
    public void updateEntityFromDTO(TipoGarantiaRequest request, TipoGarantia entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.padraoProdutos() != null ) {
            entity.setPadraoProdutos( request.padraoProdutos() );
        }
        if ( request.padraoServicos() != null ) {
            entity.setPadraoServicos( request.padraoServicos() );
        }
        if ( request.percentualCobertura() != null ) {
            entity.setPercentualCobertura( request.percentualCobertura() );
        }
        if ( request.renovavel() != null ) {
            entity.setRenovavel( request.renovavel() );
        }
        if ( request.slaAtendimentoHoras() != null ) {
            entity.setSlaAtendimentoHoras( request.slaAtendimentoHoras() );
        }
        if ( request.transferivel() != null ) {
            entity.setTransferivel( request.transferivel() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.prazoDias() != null ) {
            entity.setPrazoDias( request.prazoDias() );
        }
        if ( request.tipoCobertura() != null ) {
            entity.setTipoCobertura( request.tipoCobertura() );
        }
        if ( request.valorMaximoCobertura() != null ) {
            entity.setValorMaximoCobertura( request.valorMaximoCobertura() );
        }
        if ( request.condicoesAplicacao() != null ) {
            entity.setCondicoesAplicacao( request.condicoesAplicacao() );
        }
        if ( request.restricoes() != null ) {
            entity.setRestricoes( request.restricoes() );
        }
        if ( request.documentacaoNecessaria() != null ) {
            entity.setDocumentacaoNecessaria( request.documentacaoNecessaria() );
        }
        if ( request.processoAcionamento() != null ) {
            entity.setProcessoAcionamento( request.processoAcionamento() );
        }
        if ( request.custosAdicionais() != null ) {
            entity.setCustosAdicionais( request.custosAdicionais() );
        }
        if ( request.exclusoes() != null ) {
            entity.setExclusoes( request.exclusoes() );
        }
    }
}
