package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ListaContatos;
import com.neritech.saas.comunicacao.domain.enums.FrequenciaAtualizacao;
import com.neritech.saas.comunicacao.domain.enums.TipoLista;
import com.neritech.saas.comunicacao.dto.ListaContatosRequest;
import com.neritech.saas.comunicacao.dto.ListaContatosResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:44-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ListaContatosMapperImpl implements ListaContatosMapper {

    @Override
    public ListaContatos toEntity(ListaContatosRequest request) {
        if ( request == null ) {
            return null;
        }

        ListaContatos listaContatos = new ListaContatos();

        listaContatos.setEmpresaId( request.empresaId() );
        listaContatos.setNome( request.nome() );
        listaContatos.setDescricao( request.descricao() );
        listaContatos.setTipoLista( request.tipoLista() );
        listaContatos.setCriteriosSegmentacao( request.criteriosSegmentacao() );
        listaContatos.setFrequenciaAtualizacao( request.frequenciaAtualizacao() );
        listaContatos.setTags( request.tags() );
        listaContatos.setPrivada( request.privada() );
        listaContatos.setCompartilhadaCom( request.compartilhadaCom() );
        listaContatos.setOrigemLista( request.origemLista() );
        listaContatos.setAtiva( request.ativa() );

        return listaContatos;
    }

    @Override
    public ListaContatosResponse toResponse(ListaContatos entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        TipoLista tipoLista = null;
        String criteriosSegmentacao = null;
        Integer totalContatos = null;
        Integer contatosAtivos = null;
        LocalDateTime dataUltimaAtualizacao = null;
        FrequenciaAtualizacao frequenciaAtualizacao = null;
        LocalDateTime proximaAtualizacao = null;
        String tags = null;
        Boolean privada = null;
        String compartilhadaCom = null;
        String origemLista = null;
        Boolean ativa = null;
        Long criadaPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoLista = entity.getTipoLista();
        criteriosSegmentacao = entity.getCriteriosSegmentacao();
        totalContatos = entity.getTotalContatos();
        contatosAtivos = entity.getContatosAtivos();
        dataUltimaAtualizacao = entity.getDataUltimaAtualizacao();
        frequenciaAtualizacao = entity.getFrequenciaAtualizacao();
        proximaAtualizacao = entity.getProximaAtualizacao();
        tags = entity.getTags();
        privada = entity.getPrivada();
        compartilhadaCom = entity.getCompartilhadaCom();
        origemLista = entity.getOrigemLista();
        ativa = entity.getAtiva();
        criadaPor = entity.getCriadaPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ListaContatosResponse listaContatosResponse = new ListaContatosResponse( id, empresaId, nome, descricao, tipoLista, criteriosSegmentacao, totalContatos, contatosAtivos, dataUltimaAtualizacao, frequenciaAtualizacao, proximaAtualizacao, tags, privada, compartilhadaCom, origemLista, ativa, criadaPor, dataCadastro, dataAtualizacao );

        return listaContatosResponse;
    }

    @Override
    public void updateEntity(ListaContatosRequest request, ListaContatos entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setTipoLista( request.tipoLista() );
        entity.setCriteriosSegmentacao( request.criteriosSegmentacao() );
        entity.setFrequenciaAtualizacao( request.frequenciaAtualizacao() );
        entity.setTags( request.tags() );
        entity.setPrivada( request.privada() );
        entity.setCompartilhadaCom( request.compartilhadaCom() );
        entity.setOrigemLista( request.origemLista() );
        entity.setAtiva( request.ativa() );
    }
}
