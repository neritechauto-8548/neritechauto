package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.dto.AnoModeloRequest;
import com.neritech.saas.veiculo.dto.AnoModeloResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:47-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AnoModeloMapperImpl implements AnoModeloMapper {

    @Override
    public AnoModelo toEntity(AnoModeloRequest request) {
        if ( request == null ) {
            return null;
        }

        AnoModelo anoModelo = new AnoModelo();

        anoModelo.setModelo( anoModeloRequestToModeloVeiculo( request ) );
        anoModelo.setAnoFabricacao( request.anoFabricacao() );
        anoModelo.setAnoModelo( request.anoModelo() );
        anoModelo.setCodigoFipe( request.codigoFipe() );
        anoModelo.setValorFipe( request.valorFipe() );
        anoModelo.setDataValorFipe( request.dataValorFipe() );

        return anoModelo;
    }

    @Override
    public AnoModeloResponse toResponse(AnoModelo entity) {
        if ( entity == null ) {
            return null;
        }

        Long modeloId = null;
        String modeloNome = null;
        Long id = null;
        Integer anoFabricacao = null;
        Integer anoModelo = null;
        String codigoFipe = null;
        BigDecimal valorFipe = null;
        LocalDate dataValorFipe = null;

        modeloId = entityModeloId( entity );
        modeloNome = entityModeloNome( entity );
        id = entity.getId();
        anoFabricacao = entity.getAnoFabricacao();
        anoModelo = entity.getAnoModelo();
        codigoFipe = entity.getCodigoFipe();
        valorFipe = entity.getValorFipe();
        dataValorFipe = entity.getDataValorFipe();

        AnoModeloResponse anoModeloResponse = new AnoModeloResponse( id, modeloId, modeloNome, anoFabricacao, anoModelo, codigoFipe, valorFipe, dataValorFipe );

        return anoModeloResponse;
    }

    @Override
    public void updateEntityFromRequest(AnoModeloRequest request, AnoModelo entity) {
        if ( request == null ) {
            return;
        }

        if ( entity.getModelo() == null ) {
            entity.setModelo( new ModeloVeiculo() );
        }
        anoModeloRequestToModeloVeiculo1( request, entity.getModelo() );
        entity.setAnoFabricacao( request.anoFabricacao() );
        entity.setAnoModelo( request.anoModelo() );
        entity.setCodigoFipe( request.codigoFipe() );
        entity.setValorFipe( request.valorFipe() );
        entity.setDataValorFipe( request.dataValorFipe() );
    }

    protected ModeloVeiculo anoModeloRequestToModeloVeiculo(AnoModeloRequest anoModeloRequest) {
        if ( anoModeloRequest == null ) {
            return null;
        }

        ModeloVeiculo modeloVeiculo = new ModeloVeiculo();

        modeloVeiculo.setId( anoModeloRequest.modeloId() );

        return modeloVeiculo;
    }

    private Long entityModeloId(AnoModelo anoModelo) {
        if ( anoModelo == null ) {
            return null;
        }
        ModeloVeiculo modelo = anoModelo.getModelo();
        if ( modelo == null ) {
            return null;
        }
        Long id = modelo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityModeloNome(AnoModelo anoModelo) {
        if ( anoModelo == null ) {
            return null;
        }
        ModeloVeiculo modelo = anoModelo.getModelo();
        if ( modelo == null ) {
            return null;
        }
        String nome = modelo.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    protected void anoModeloRequestToModeloVeiculo1(AnoModeloRequest anoModeloRequest, ModeloVeiculo mappingTarget) {
        if ( anoModeloRequest == null ) {
            return;
        }

        mappingTarget.setId( anoModeloRequest.modeloId() );
    }
}
