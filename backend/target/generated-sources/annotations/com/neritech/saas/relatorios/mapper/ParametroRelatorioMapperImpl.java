package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.ParametroRelatorio;
import com.neritech.saas.relatorios.domain.RelatorioSalvo;
import com.neritech.saas.relatorios.dto.ParametroRelatorioRequest;
import com.neritech.saas.relatorios.dto.ParametroRelatorioResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ParametroRelatorioMapperImpl implements ParametroRelatorioMapper {

    @Override
    public ParametroRelatorio toEntity(ParametroRelatorioRequest request) {
        if ( request == null ) {
            return null;
        }

        ParametroRelatorio.ParametroRelatorioBuilder parametroRelatorio = ParametroRelatorio.builder();

        parametroRelatorio.ajudaUsuario( request.getAjudaUsuario() );
        parametroRelatorio.ativo( request.getAtivo() );
        parametroRelatorio.condicaoDependencia( request.getCondicaoDependencia() );
        parametroRelatorio.grupoParametro( request.getGrupoParametro() );
        parametroRelatorio.mensagemErro( request.getMensagemErro() );
        parametroRelatorio.nomeParametro( request.getNomeParametro() );
        parametroRelatorio.obrigatorio( request.getObrigatorio() );
        parametroRelatorio.opcoesLista( request.getOpcoesLista() );
        parametroRelatorio.ordemExibicao( request.getOrdemExibicao() );
        parametroRelatorio.tipoParametro( request.getTipoParametro() );
        parametroRelatorio.validacaoRegex( request.getValidacaoRegex() );
        parametroRelatorio.valorPadrao( request.getValorPadrao() );

        return parametroRelatorio.build();
    }

    @Override
    public ParametroRelatorioResponse toResponse(ParametroRelatorio entity) {
        if ( entity == null ) {
            return null;
        }

        ParametroRelatorioResponse parametroRelatorioResponse = new ParametroRelatorioResponse();

        parametroRelatorioResponse.setRelatorioId( entityRelatorioId( entity ) );
        parametroRelatorioResponse.setDependenteDeId( entityDependenteDeId( entity ) );
        parametroRelatorioResponse.setId( entity.getId() );
        parametroRelatorioResponse.setNomeParametro( entity.getNomeParametro() );
        parametroRelatorioResponse.setTipoParametro( entity.getTipoParametro() );
        parametroRelatorioResponse.setObrigatorio( entity.getObrigatorio() );
        parametroRelatorioResponse.setValorPadrao( entity.getValorPadrao() );
        parametroRelatorioResponse.setOpcoesLista( entity.getOpcoesLista() );
        parametroRelatorioResponse.setValidacaoRegex( entity.getValidacaoRegex() );
        parametroRelatorioResponse.setMensagemErro( entity.getMensagemErro() );
        parametroRelatorioResponse.setOrdemExibicao( entity.getOrdemExibicao() );
        parametroRelatorioResponse.setGrupoParametro( entity.getGrupoParametro() );
        parametroRelatorioResponse.setCondicaoDependencia( entity.getCondicaoDependencia() );
        parametroRelatorioResponse.setAjudaUsuario( entity.getAjudaUsuario() );
        parametroRelatorioResponse.setAtivo( entity.getAtivo() );

        return parametroRelatorioResponse;
    }

    @Override
    public void updateEntity(ParametroRelatorio entity, ParametroRelatorioRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.getAtivo() );
        entity.setObrigatorio( request.getObrigatorio() );
        entity.setOrdemExibicao( request.getOrdemExibicao() );
        entity.setNomeParametro( request.getNomeParametro() );
        entity.setTipoParametro( request.getTipoParametro() );
        entity.setValorPadrao( request.getValorPadrao() );
        entity.setOpcoesLista( request.getOpcoesLista() );
        entity.setValidacaoRegex( request.getValidacaoRegex() );
        entity.setMensagemErro( request.getMensagemErro() );
        entity.setGrupoParametro( request.getGrupoParametro() );
        entity.setCondicaoDependencia( request.getCondicaoDependencia() );
        entity.setAjudaUsuario( request.getAjudaUsuario() );
    }

    private Long entityRelatorioId(ParametroRelatorio parametroRelatorio) {
        if ( parametroRelatorio == null ) {
            return null;
        }
        RelatorioSalvo relatorio = parametroRelatorio.getRelatorio();
        if ( relatorio == null ) {
            return null;
        }
        Long id = relatorio.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityDependenteDeId(ParametroRelatorio parametroRelatorio) {
        if ( parametroRelatorio == null ) {
            return null;
        }
        ParametroRelatorio dependenteDe = parametroRelatorio.getDependenteDe();
        if ( dependenteDe == null ) {
            return null;
        }
        Long id = dependenteDe.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
