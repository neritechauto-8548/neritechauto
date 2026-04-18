package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.AliquotaImposto;
import com.neritech.saas.fiscal.dto.AliquotaImpostoRequest;
import com.neritech.saas.fiscal.dto.AliquotaImpostoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AliquotaImpostoMapperImpl implements AliquotaImpostoMapper {

    @Override
    public AliquotaImposto toEntity(AliquotaImpostoRequest request) {
        if ( request == null ) {
            return null;
        }

        AliquotaImposto.AliquotaImpostoBuilder<?, ?> aliquotaImposto = AliquotaImposto.builder();

        aliquotaImposto.aliquota( request.aliquota() );
        aliquotaImposto.descricao( request.descricao() );
        aliquotaImposto.nomeImposto( request.nomeImposto() );
        aliquotaImposto.padrao( request.padrao() );
        aliquotaImposto.uf( request.uf() );

        return aliquotaImposto.build();
    }

    @Override
    public AliquotaImpostoResponse toResponse(AliquotaImposto entity) {
        if ( entity == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Long id = null;
        String nomeImposto = null;
        BigDecimal aliquota = null;
        String descricao = null;
        String uf = null;
        boolean padrao = false;

        createdAt = entity.getDataCadastro();
        updatedAt = entity.getDataAtualizacao();
        id = entity.getId();
        nomeImposto = entity.getNomeImposto();
        aliquota = entity.getAliquota();
        descricao = entity.getDescricao();
        uf = entity.getUf();
        padrao = entity.isPadrao();

        AliquotaImpostoResponse aliquotaImpostoResponse = new AliquotaImpostoResponse( id, nomeImposto, aliquota, descricao, uf, padrao, createdAt, updatedAt );

        return aliquotaImpostoResponse;
    }

    @Override
    public void updateEntityFromRequest(AliquotaImpostoRequest request, AliquotaImposto entity) {
        if ( request == null ) {
            return;
        }

        entity.setPadrao( request.padrao() );
        entity.setNomeImposto( request.nomeImposto() );
        entity.setAliquota( request.aliquota() );
        entity.setDescricao( request.descricao() );
        entity.setUf( request.uf() );
    }
}
