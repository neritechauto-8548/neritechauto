package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.MapeamentoDados;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.dto.MapeamentoDadosRequest;
import com.neritech.saas.integracao.dto.MapeamentoDadosResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:45-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class MapeamentoDadosMapperImpl implements MapeamentoDadosMapper {

    @Autowired
    private IntegracaoAtivaMapper integracaoAtivaMapper;

    @Override
    public MapeamentoDados toEntity(MapeamentoDadosRequest request) {
        if ( request == null ) {
            return null;
        }

        MapeamentoDados.MapeamentoDadosBuilder<?, ?> mapeamentoDados = MapeamentoDados.builder();

        mapeamentoDados.ativo( request.ativo() );
        mapeamentoDados.campoDestino( request.campoDestino() );
        mapeamentoDados.campoOrigem( request.campoOrigem() );
        mapeamentoDados.entidade( request.entidade() );
        mapeamentoDados.regrasTransformacao( request.regrasTransformacao() );
        mapeamentoDados.transformacao( request.transformacao() );

        return mapeamentoDados.build();
    }

    @Override
    public MapeamentoDadosResponse toResponse(MapeamentoDados entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        IntegracaoAtivaResponse integracao = null;
        String entidade = null;
        String campoOrigem = null;
        String campoDestino = null;
        String transformacao = null;
        String regrasTransformacao = null;
        boolean ativo = false;

        id = entity.getId();
        integracao = integracaoAtivaMapper.toResponse( entity.getIntegracao() );
        entidade = entity.getEntidade();
        campoOrigem = entity.getCampoOrigem();
        campoDestino = entity.getCampoDestino();
        transformacao = entity.getTransformacao();
        regrasTransformacao = entity.getRegrasTransformacao();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        MapeamentoDadosResponse mapeamentoDadosResponse = new MapeamentoDadosResponse( id, integracao, entidade, campoOrigem, campoDestino, transformacao, regrasTransformacao, ativo, createdAt, updatedAt );

        return mapeamentoDadosResponse;
    }

    @Override
    public void updateEntityFromRequest(MapeamentoDadosRequest request, MapeamentoDados entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setEntidade( request.entidade() );
        entity.setCampoOrigem( request.campoOrigem() );
        entity.setCampoDestino( request.campoDestino() );
        entity.setTransformacao( request.transformacao() );
        entity.setRegrasTransformacao( request.regrasTransformacao() );
    }
}
