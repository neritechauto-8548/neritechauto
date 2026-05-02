package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.domain.enums.CategoriaTemplate;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoRequest;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TemplateComunicacaoMapperImpl implements TemplateComunicacaoMapper {

    @Override
    public TemplateComunicacao toEntity(TemplateComunicacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        TemplateComunicacao templateComunicacao = new TemplateComunicacao();

        templateComunicacao.setEmpresaId( request.empresaId() );
        templateComunicacao.setNome( request.nome() );
        templateComunicacao.setTipoTemplate( request.tipoTemplate() );
        templateComunicacao.setCategoria( request.categoria() );
        templateComunicacao.setAssunto( request.assunto() );
        templateComunicacao.setConteudo( request.conteudo() );
        templateComunicacao.setVariaveisDisponiveis( request.variaveisDisponiveis() );
        templateComunicacao.setAnexosPadrao( request.anexosPadrao() );
        templateComunicacao.setAtivo( request.ativo() );
        templateComunicacao.setPadraoCategoria( request.padraoCategoria() );
        templateComunicacao.setIdioma( request.idioma() );
        templateComunicacao.setPersonalizavel( request.personalizavel() );
        templateComunicacao.setRequerAprovacao( request.requerAprovacao() );
        templateComunicacao.setTags( request.tags() );

        return templateComunicacao;
    }

    @Override
    public TemplateComunicacaoResponse toResponse(TemplateComunicacao entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        TipoTemplate tipoTemplate = null;
        CategoriaTemplate categoria = null;
        String assunto = null;
        String conteudo = null;
        String variaveisDisponiveis = null;
        String anexosPadrao = null;
        Boolean ativo = null;
        Boolean padraoCategoria = null;
        String idioma = null;
        Boolean personalizavel = null;
        Boolean requerAprovacao = null;
        String tags = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        tipoTemplate = entity.getTipoTemplate();
        categoria = entity.getCategoria();
        assunto = entity.getAssunto();
        conteudo = entity.getConteudo();
        variaveisDisponiveis = entity.getVariaveisDisponiveis();
        anexosPadrao = entity.getAnexosPadrao();
        ativo = entity.getAtivo();
        padraoCategoria = entity.getPadraoCategoria();
        idioma = entity.getIdioma();
        personalizavel = entity.getPersonalizavel();
        requerAprovacao = entity.getRequerAprovacao();
        tags = entity.getTags();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        TemplateComunicacaoResponse templateComunicacaoResponse = new TemplateComunicacaoResponse( id, empresaId, nome, tipoTemplate, categoria, assunto, conteudo, variaveisDisponiveis, anexosPadrao, ativo, padraoCategoria, idioma, personalizavel, requerAprovacao, tags, dataCadastro, dataAtualizacao );

        return templateComunicacaoResponse;
    }

    @Override
    public void updateEntity(TemplateComunicacaoRequest request, TemplateComunicacao entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setNome( request.nome() );
        entity.setTipoTemplate( request.tipoTemplate() );
        entity.setCategoria( request.categoria() );
        entity.setAssunto( request.assunto() );
        entity.setConteudo( request.conteudo() );
        entity.setVariaveisDisponiveis( request.variaveisDisponiveis() );
        entity.setAnexosPadrao( request.anexosPadrao() );
        entity.setAtivo( request.ativo() );
        entity.setPadraoCategoria( request.padraoCategoria() );
        entity.setIdioma( request.idioma() );
        entity.setPersonalizavel( request.personalizavel() );
        entity.setRequerAprovacao( request.requerAprovacao() );
        entity.setTags( request.tags() );
    }
}
