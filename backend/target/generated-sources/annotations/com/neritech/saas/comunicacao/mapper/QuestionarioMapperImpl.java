package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.dto.QuestionarioRequest;
import com.neritech.saas.comunicacao.dto.QuestionarioResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:04-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class QuestionarioMapperImpl implements QuestionarioMapper {

    @Override
    public Questionario toEntity(QuestionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Questionario questionario = new Questionario();

        questionario.setEmpresaId( request.empresaId() );
        questionario.setDsQuestionario( request.dsQuestionario() );
        questionario.setUrlImagem( request.urlImagem() );
        questionario.setSnAtivo( request.snAtivo() );

        return questionario;
    }

    @Override
    public QuestionarioResponse toResponse(Questionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String dsQuestionario = null;
        String urlImagem = null;
        Boolean snAtivo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        dsQuestionario = entity.getDsQuestionario();
        urlImagem = entity.getUrlImagem();
        snAtivo = entity.getSnAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        QuestionarioResponse questionarioResponse = new QuestionarioResponse( id, empresaId, dsQuestionario, urlImagem, snAtivo, dataCadastro, dataAtualizacao );

        return questionarioResponse;
    }

    @Override
    public void updateEntityFromRequest(QuestionarioRequest request, Questionario entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setDsQuestionario( request.dsQuestionario() );
        entity.setUrlImagem( request.urlImagem() );
        entity.setSnAtivo( request.snAtivo() );
    }
}
