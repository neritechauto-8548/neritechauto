package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.CampanhaMarketing;
import com.neritech.saas.comunicacao.domain.enums.StatusCampanha;
import com.neritech.saas.comunicacao.domain.enums.TipoCampanha;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingRequest;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:48-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CampanhaMarketingMapperImpl implements CampanhaMarketingMapper {

    @Override
    public CampanhaMarketing toEntity(CampanhaMarketingRequest request) {
        if ( request == null ) {
            return null;
        }

        CampanhaMarketing campanhaMarketing = new CampanhaMarketing();

        campanhaMarketing.setEmpresaId( request.empresaId() );
        campanhaMarketing.setNome( request.nome() );
        campanhaMarketing.setDescricao( request.descricao() );
        campanhaMarketing.setTipoCampanha( request.tipoCampanha() );
        campanhaMarketing.setObjetivo( request.objetivo() );
        campanhaMarketing.setPublicoAlvo( request.publicoAlvo() );
        campanhaMarketing.setCanaisComunicacao( request.canaisComunicacao() );
        campanhaMarketing.setDataInicio( request.dataInicio() );
        campanhaMarketing.setDataFim( request.dataFim() );
        campanhaMarketing.setOrcamentoTotal( request.orcamentoTotal() );
        campanhaMarketing.setMetaAlcance( request.metaAlcance() );
        campanhaMarketing.setMetaConversao( request.metaConversao() );
        campanhaMarketing.setTemplateEmailId( request.templateEmailId() );
        campanhaMarketing.setTemplateSmsId( request.templateSmsId() );
        campanhaMarketing.setTemplateWhatsappId( request.templateWhatsappId() );
        campanhaMarketing.setPromocaoAssociadaId( request.promocaoAssociadaId() );
        campanhaMarketing.setStatus( request.status() );
        campanhaMarketing.setObservacoes( request.observacoes() );

        return campanhaMarketing;
    }

    @Override
    public CampanhaMarketingResponse toResponse(CampanhaMarketing entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        TipoCampanha tipoCampanha = null;
        String objetivo = null;
        String publicoAlvo = null;
        String canaisComunicacao = null;
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        BigDecimal orcamentoTotal = null;
        BigDecimal custoRealizado = null;
        Integer metaAlcance = null;
        Integer alcanceRealizado = null;
        Integer metaConversao = null;
        Integer conversaoRealizada = null;
        Long templateEmailId = null;
        Long templateSmsId = null;
        Long templateWhatsappId = null;
        Long promocaoAssociadaId = null;
        StatusCampanha status = null;
        Long aprovadaPor = null;
        LocalDateTime dataAprovacao = null;
        String observacoes = null;
        String resultadosDetalhados = null;
        BigDecimal roiCalculado = null;
        Long criadaPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoCampanha = entity.getTipoCampanha();
        objetivo = entity.getObjetivo();
        publicoAlvo = entity.getPublicoAlvo();
        canaisComunicacao = entity.getCanaisComunicacao();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        orcamentoTotal = entity.getOrcamentoTotal();
        custoRealizado = entity.getCustoRealizado();
        metaAlcance = entity.getMetaAlcance();
        alcanceRealizado = entity.getAlcanceRealizado();
        metaConversao = entity.getMetaConversao();
        conversaoRealizada = entity.getConversaoRealizada();
        templateEmailId = entity.getTemplateEmailId();
        templateSmsId = entity.getTemplateSmsId();
        templateWhatsappId = entity.getTemplateWhatsappId();
        promocaoAssociadaId = entity.getPromocaoAssociadaId();
        status = entity.getStatus();
        aprovadaPor = entity.getAprovadaPor();
        dataAprovacao = entity.getDataAprovacao();
        observacoes = entity.getObservacoes();
        resultadosDetalhados = entity.getResultadosDetalhados();
        roiCalculado = entity.getRoiCalculado();
        criadaPor = entity.getCriadaPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        CampanhaMarketingResponse campanhaMarketingResponse = new CampanhaMarketingResponse( id, empresaId, nome, descricao, tipoCampanha, objetivo, publicoAlvo, canaisComunicacao, dataInicio, dataFim, orcamentoTotal, custoRealizado, metaAlcance, alcanceRealizado, metaConversao, conversaoRealizada, templateEmailId, templateSmsId, templateWhatsappId, promocaoAssociadaId, status, aprovadaPor, dataAprovacao, observacoes, resultadosDetalhados, roiCalculado, criadaPor, dataCadastro, dataAtualizacao );

        return campanhaMarketingResponse;
    }

    @Override
    public void updateEntity(CampanhaMarketingRequest request, CampanhaMarketing entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setTipoCampanha( request.tipoCampanha() );
        entity.setObjetivo( request.objetivo() );
        entity.setPublicoAlvo( request.publicoAlvo() );
        entity.setCanaisComunicacao( request.canaisComunicacao() );
        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setOrcamentoTotal( request.orcamentoTotal() );
        entity.setMetaAlcance( request.metaAlcance() );
        entity.setMetaConversao( request.metaConversao() );
        entity.setTemplateEmailId( request.templateEmailId() );
        entity.setTemplateSmsId( request.templateSmsId() );
        entity.setTemplateWhatsappId( request.templateWhatsappId() );
        entity.setPromocaoAssociadaId( request.promocaoAssociadaId() );
        entity.setStatus( request.status() );
        entity.setObservacoes( request.observacoes() );
    }
}
