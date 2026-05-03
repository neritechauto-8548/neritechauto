package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.ModeloDocumento;
import com.neritech.saas.empresa.domain.enums.OrientacaoDocumento;
import com.neritech.saas.empresa.domain.enums.TamanhoPapel;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import com.neritech.saas.empresa.dto.ModeloDocumentoRequest;
import com.neritech.saas.empresa.dto.ModeloDocumentoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:01-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ModeloDocumentoMapperImpl implements ModeloDocumentoMapper {

    @Override
    public ModeloDocumento toEntity(ModeloDocumentoRequest request) {
        if ( request == null ) {
            return null;
        }

        ModeloDocumento modeloDocumento = new ModeloDocumento();

        modeloDocumento.setCriadoPor( request.criadoPor() );
        modeloDocumento.setAtualizadoPor( request.atualizadoPor() );
        modeloDocumento.setTipoDocumento( request.tipoDocumento() );
        modeloDocumento.setNome( request.nome() );
        modeloDocumento.setTemplateHtml( request.templateHtml() );
        modeloDocumento.setTemplateCss( request.templateCss() );
        modeloDocumento.setVariaveisDisponiveis( request.variaveisDisponiveis() );
        modeloDocumento.setOrientacao( request.orientacao() );
        modeloDocumento.setTamanhoPapel( request.tamanhoPapel() );
        modeloDocumento.setMargensCm( request.margensCm() );
        modeloDocumento.setCabecalhoPadrao( request.cabecalhoPadrao() );
        modeloDocumento.setRodapePadrao( request.rodapePadrao() );
        modeloDocumento.setNumeracaoAutomatica( request.numeracaoAutomatica() );
        modeloDocumento.setPadrao( request.padrao() );
        modeloDocumento.setAtivo( request.ativo() );

        return modeloDocumento;
    }

    @Override
    public ModeloDocumentoResponse toResponse(ModeloDocumento entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        TipoDocumento tipoDocumento = null;
        String nome = null;
        String templateHtml = null;
        String templateCss = null;
        String variaveisDisponiveis = null;
        OrientacaoDocumento orientacao = null;
        TamanhoPapel tamanhoPapel = null;
        String margensCm = null;
        Boolean cabecalhoPadrao = null;
        Boolean rodapePadrao = null;
        Boolean numeracaoAutomatica = null;
        Boolean padrao = null;
        Boolean ativo = null;
        Long criadoPor = null;
        Long atualizadoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        tipoDocumento = entity.getTipoDocumento();
        nome = entity.getNome();
        templateHtml = entity.getTemplateHtml();
        templateCss = entity.getTemplateCss();
        variaveisDisponiveis = entity.getVariaveisDisponiveis();
        orientacao = entity.getOrientacao();
        tamanhoPapel = entity.getTamanhoPapel();
        margensCm = entity.getMargensCm();
        cabecalhoPadrao = entity.getCabecalhoPadrao();
        rodapePadrao = entity.getRodapePadrao();
        numeracaoAutomatica = entity.getNumeracaoAutomatica();
        padrao = entity.getPadrao();
        ativo = entity.getAtivo();
        criadoPor = entity.getCriadoPor();
        atualizadoPor = entity.getAtualizadoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ModeloDocumentoResponse modeloDocumentoResponse = new ModeloDocumentoResponse( id, empresaId, empresaNome, tipoDocumento, nome, templateHtml, templateCss, variaveisDisponiveis, orientacao, tamanhoPapel, margensCm, cabecalhoPadrao, rodapePadrao, numeracaoAutomatica, padrao, ativo, criadoPor, atualizadoPor, dataCadastro, dataAtualizacao );

        return modeloDocumentoResponse;
    }

    @Override
    public void updateEntityFromRequest(ModeloDocumentoRequest request, ModeloDocumento entity) {
        if ( request == null ) {
            return;
        }

        entity.setCriadoPor( request.criadoPor() );
        entity.setAtualizadoPor( request.atualizadoPor() );
        entity.setTipoDocumento( request.tipoDocumento() );
        entity.setNome( request.nome() );
        entity.setTemplateHtml( request.templateHtml() );
        entity.setTemplateCss( request.templateCss() );
        entity.setVariaveisDisponiveis( request.variaveisDisponiveis() );
        entity.setOrientacao( request.orientacao() );
        entity.setTamanhoPapel( request.tamanhoPapel() );
        entity.setMargensCm( request.margensCm() );
        entity.setCabecalhoPadrao( request.cabecalhoPadrao() );
        entity.setRodapePadrao( request.rodapePadrao() );
        entity.setNumeracaoAutomatica( request.numeracaoAutomatica() );
        entity.setPadrao( request.padrao() );
        entity.setAtivo( request.ativo() );
    }

    private Long entityEmpresaId(ModeloDocumento modeloDocumento) {
        if ( modeloDocumento == null ) {
            return null;
        }
        Empresa empresa = modeloDocumento.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(ModeloDocumento modeloDocumento) {
        if ( modeloDocumento == null ) {
            return null;
        }
        Empresa empresa = modeloDocumento.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        String nomeFantasia = empresa.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
