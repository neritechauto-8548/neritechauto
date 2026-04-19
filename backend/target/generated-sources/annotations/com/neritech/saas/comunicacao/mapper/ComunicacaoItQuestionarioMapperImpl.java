package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.ItQuestionarioResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ComunicacaoItQuestionarioMapperImpl implements ItQuestionarioMapper {

    @Override
    public ItQuestionario toEntity(ItQuestionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        ItQuestionario itQuestionario = new ItQuestionario();

        itQuestionario.setDsItQuestionario( request.dsItQuestionario() );
        itQuestionario.setTpItQuestionario( request.tpItQuestionario() );

        return itQuestionario;
    }

    @Override
    public ItQuestionarioResponse toResponse(ItQuestionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long questionarioId = null;
        Long id = null;
        String dsItQuestionario = null;
        TipoItemQuestionario tpItQuestionario = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        questionarioId = entityQuestionarioId( entity );
        id = entity.getId();
        dsItQuestionario = entity.getDsItQuestionario();
        tpItQuestionario = entity.getTpItQuestionario();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ItQuestionarioResponse itQuestionarioResponse = new ItQuestionarioResponse( id, questionarioId, dsItQuestionario, tpItQuestionario, dataCadastro, dataAtualizacao );

        return itQuestionarioResponse;
    }

    @Override
    public void updateEntityFromRequest(ItQuestionarioRequest request, ItQuestionario entity) {
        if ( request == null ) {
            return;
        }

        entity.setDsItQuestionario( request.dsItQuestionario() );
        entity.setTpItQuestionario( request.tpItQuestionario() );
    }

    private Long entityQuestionarioId(ItQuestionario itQuestionario) {
        if ( itQuestionario == null ) {
            return null;
        }
        Questionario questionario = itQuestionario.getQuestionario();
        if ( questionario == null ) {
            return null;
        }
        Long id = questionario.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
