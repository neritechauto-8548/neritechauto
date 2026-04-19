package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.Cfop;
import com.neritech.saas.fiscal.dto.CfopRequest;
import com.neritech.saas.fiscal.dto.CfopResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:47-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CfopMapperImpl implements CfopMapper {

    @Override
    public Cfop toEntity(CfopRequest request) {
        if ( request == null ) {
            return null;
        }

        Cfop.CfopBuilder<?, ?> cfop = Cfop.builder();

        cfop.aplicacao( request.aplicacao() );
        cfop.ativo( request.ativo() );
        cfop.codigo( request.codigo() );
        cfop.descricao( request.descricao() );

        return cfop.build();
    }

    @Override
    public CfopResponse toResponse(Cfop entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String codigo = null;
        String descricao = null;
        String aplicacao = null;
        boolean ativo = false;

        id = entity.getId();
        codigo = entity.getCodigo();
        descricao = entity.getDescricao();
        aplicacao = entity.getAplicacao();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        CfopResponse cfopResponse = new CfopResponse( id, codigo, descricao, aplicacao, ativo, createdAt, updatedAt );

        return cfopResponse;
    }

    @Override
    public void updateEntityFromRequest(CfopRequest request, Cfop entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setCodigo( request.codigo() );
        entity.setDescricao( request.descricao() );
        entity.setAplicacao( request.aplicacao() );
    }
}
