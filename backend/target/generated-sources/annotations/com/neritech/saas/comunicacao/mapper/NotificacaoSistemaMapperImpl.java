package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.comunicacao.domain.enums.TipoNotificacao;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaRequest;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class NotificacaoSistemaMapperImpl implements NotificacaoSistemaMapper {

    @Override
    public NotificacaoSistema toEntity(NotificacaoSistemaRequest request) {
        if ( request == null ) {
            return null;
        }

        NotificacaoSistema notificacaoSistema = new NotificacaoSistema();

        notificacaoSistema.setEmpresaId( request.empresaId() );
        notificacaoSistema.setUsuarioDestinatarioId( request.usuarioDestinatarioId() );
        notificacaoSistema.setTipoNotificacao( request.tipoNotificacao() );
        notificacaoSistema.setCategoria( request.categoria() );
        notificacaoSistema.setTitulo( request.titulo() );
        notificacaoSistema.setMensagem( request.mensagem() );
        notificacaoSistema.setDadosContexto( request.dadosContexto() );
        notificacaoSistema.setLinkAcao( request.linkAcao() );
        notificacaoSistema.setTextoBotaoAcao( request.textoBotaoAcao() );
        notificacaoSistema.setPrioridade( request.prioridade() );
        notificacaoSistema.setExigeConfirmacao( request.exigeConfirmacao() );
        notificacaoSistema.setDataExpiracao( request.dataExpiracao() );
        notificacaoSistema.setIcone( request.icone() );
        notificacaoSistema.setCor( request.cor() );
        notificacaoSistema.setSomNotificacao( request.somNotificacao() );
        notificacaoSistema.setEnviarEmail( request.enviarEmail() );
        notificacaoSistema.setEnviarSms( request.enviarSms() );
        notificacaoSistema.setEnviarPush( request.enviarPush() );
        notificacaoSistema.setOrigemSistema( request.origemSistema() );

        return notificacaoSistema;
    }

    @Override
    public NotificacaoSistemaResponse toResponse(NotificacaoSistema entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long usuarioDestinatarioId = null;
        TipoNotificacao tipoNotificacao = null;
        String categoria = null;
        String titulo = null;
        String mensagem = null;
        String dadosContexto = null;
        String linkAcao = null;
        String textoBotaoAcao = null;
        Prioridade prioridade = null;
        Boolean exigeConfirmacao = null;
        Boolean lida = null;
        LocalDateTime dataLeitura = null;
        Boolean confirmada = null;
        LocalDateTime dataConfirmacao = null;
        LocalDateTime dataExpiracao = null;
        String icone = null;
        String cor = null;
        String somNotificacao = null;
        Boolean enviarEmail = null;
        Boolean enviarSms = null;
        Boolean enviarPush = null;
        String origemSistema = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        usuarioDestinatarioId = entity.getUsuarioDestinatarioId();
        tipoNotificacao = entity.getTipoNotificacao();
        categoria = entity.getCategoria();
        titulo = entity.getTitulo();
        mensagem = entity.getMensagem();
        dadosContexto = entity.getDadosContexto();
        linkAcao = entity.getLinkAcao();
        textoBotaoAcao = entity.getTextoBotaoAcao();
        prioridade = entity.getPrioridade();
        exigeConfirmacao = entity.getExigeConfirmacao();
        lida = entity.getLida();
        dataLeitura = entity.getDataLeitura();
        confirmada = entity.getConfirmada();
        dataConfirmacao = entity.getDataConfirmacao();
        dataExpiracao = entity.getDataExpiracao();
        icone = entity.getIcone();
        cor = entity.getCor();
        somNotificacao = entity.getSomNotificacao();
        enviarEmail = entity.getEnviarEmail();
        enviarSms = entity.getEnviarSms();
        enviarPush = entity.getEnviarPush();
        origemSistema = entity.getOrigemSistema();
        dataCadastro = entity.getDataCadastro();

        NotificacaoSistemaResponse notificacaoSistemaResponse = new NotificacaoSistemaResponse( id, empresaId, usuarioDestinatarioId, tipoNotificacao, categoria, titulo, mensagem, dadosContexto, linkAcao, textoBotaoAcao, prioridade, exigeConfirmacao, lida, dataLeitura, confirmada, dataConfirmacao, dataExpiracao, icone, cor, somNotificacao, enviarEmail, enviarSms, enviarPush, origemSistema, dataCadastro );

        return notificacaoSistemaResponse;
    }

    @Override
    public void updateEntity(NotificacaoSistemaRequest request, NotificacaoSistema entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setUsuarioDestinatarioId( request.usuarioDestinatarioId() );
        entity.setTipoNotificacao( request.tipoNotificacao() );
        entity.setCategoria( request.categoria() );
        entity.setTitulo( request.titulo() );
        entity.setMensagem( request.mensagem() );
        entity.setDadosContexto( request.dadosContexto() );
        entity.setLinkAcao( request.linkAcao() );
        entity.setTextoBotaoAcao( request.textoBotaoAcao() );
        entity.setPrioridade( request.prioridade() );
        entity.setExigeConfirmacao( request.exigeConfirmacao() );
        entity.setDataExpiracao( request.dataExpiracao() );
        entity.setIcone( request.icone() );
        entity.setCor( request.cor() );
        entity.setSomNotificacao( request.somNotificacao() );
        entity.setEnviarEmail( request.enviarEmail() );
        entity.setEnviarSms( request.enviarSms() );
        entity.setEnviarPush( request.enviarPush() );
        entity.setOrigemSistema( request.origemSistema() );
    }
}
