package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.dto.StatusOSRequest;
import com.neritech.saas.ordemservico.dto.StatusOSResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:03-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class StatusOSMapperImpl implements StatusOSMapper {

    @Override
    public StatusOS toEntity(StatusOSRequest request) {
        if ( request == null ) {
            return null;
        }

        StatusOS statusOS = new StatusOS();

        statusOS.setEmpresaId( request.empresaId() );
        statusOS.setNome( request.nome() );
        statusOS.setDescricao( request.descricao() );
        statusOS.setCodigo( request.codigo() );
        statusOS.setCorIdentificacao( request.corIdentificacao() );
        statusOS.setIcone( request.icone() );
        statusOS.setOrdemExibicao( request.ordemExibicao() );
        statusOS.setPermiteEdicao( request.permiteEdicao() );
        statusOS.setNotificaCliente( request.notificaCliente() );
        statusOS.setTemplateNotificacaoId( request.templateNotificacaoId() );
        statusOS.setExigeAprovacao( request.exigeAprovacao() );
        statusOS.setFinalizaOS( request.finalizaOS() );
        statusOS.setCancelaOS( request.cancelaOS() );
        statusOS.setAtivo( request.ativo() );

        return statusOS;
    }

    @Override
    public StatusOSResponse toResponse(StatusOS entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        String codigo = null;
        String corIdentificacao = null;
        String icone = null;
        Integer ordemExibicao = null;
        Boolean permiteEdicao = null;
        Boolean notificaCliente = null;
        Long templateNotificacaoId = null;
        Boolean exigeAprovacao = null;
        Boolean finalizaOS = null;
        Boolean cancelaOS = null;
        Boolean sistema = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;
        Integer versao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        codigo = entity.getCodigo();
        corIdentificacao = entity.getCorIdentificacao();
        icone = entity.getIcone();
        ordemExibicao = entity.getOrdemExibicao();
        permiteEdicao = entity.getPermiteEdicao();
        notificaCliente = entity.getNotificaCliente();
        templateNotificacaoId = entity.getTemplateNotificacaoId();
        exigeAprovacao = entity.getExigeAprovacao();
        finalizaOS = entity.getFinalizaOS();
        cancelaOS = entity.getCancelaOS();
        sistema = entity.getSistema();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();
        versao = entity.getVersao();

        StatusOSResponse statusOSResponse = new StatusOSResponse( id, empresaId, nome, descricao, codigo, corIdentificacao, icone, ordemExibicao, permiteEdicao, notificaCliente, templateNotificacaoId, exigeAprovacao, finalizaOS, cancelaOS, sistema, ativo, dataCadastro, dataAtualizacao, versao );

        return statusOSResponse;
    }

    @Override
    public void updateEntityFromRequest(StatusOSRequest request, StatusOS entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.codigo() != null ) {
            entity.setCodigo( request.codigo() );
        }
        if ( request.corIdentificacao() != null ) {
            entity.setCorIdentificacao( request.corIdentificacao() );
        }
        if ( request.icone() != null ) {
            entity.setIcone( request.icone() );
        }
        if ( request.ordemExibicao() != null ) {
            entity.setOrdemExibicao( request.ordemExibicao() );
        }
        if ( request.permiteEdicao() != null ) {
            entity.setPermiteEdicao( request.permiteEdicao() );
        }
        if ( request.notificaCliente() != null ) {
            entity.setNotificaCliente( request.notificaCliente() );
        }
        if ( request.templateNotificacaoId() != null ) {
            entity.setTemplateNotificacaoId( request.templateNotificacaoId() );
        }
        if ( request.exigeAprovacao() != null ) {
            entity.setExigeAprovacao( request.exigeAprovacao() );
        }
        if ( request.finalizaOS() != null ) {
            entity.setFinalizaOS( request.finalizaOS() );
        }
        if ( request.cancelaOS() != null ) {
            entity.setCancelaOS( request.cancelaOS() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
