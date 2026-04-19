package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.enums.CategoriaVeiculo;
import com.neritech.saas.veiculo.dto.ModeloVeiculoRequest;
import com.neritech.saas.veiculo.dto.ModeloVeiculoResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:55-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ModeloVeiculoMapperImpl implements ModeloVeiculoMapper {

    @Override
    public ModeloVeiculo toEntity(ModeloVeiculoRequest request) {
        if ( request == null ) {
            return null;
        }

        ModeloVeiculo modeloVeiculo = new ModeloVeiculo();

        modeloVeiculo.setMarca( modeloVeiculoRequestToMarcaVeiculo( request ) );
        modeloVeiculo.setNome( request.nome() );
        modeloVeiculo.setCategoria( request.categoria() );
        modeloVeiculo.setLogoUrl( request.logoUrl() );

        return modeloVeiculo;
    }

    @Override
    public ModeloVeiculoResponse toResponse(ModeloVeiculo entity) {
        if ( entity == null ) {
            return null;
        }

        Long marcaId = null;
        String marcaNome = null;
        Long id = null;
        String nome = null;
        CategoriaVeiculo categoria = null;
        String logoUrl = null;

        marcaId = entityMarcaId( entity );
        marcaNome = entityMarcaNome( entity );
        id = entity.getId();
        nome = entity.getNome();
        categoria = entity.getCategoria();
        logoUrl = entity.getLogoUrl();

        ModeloVeiculoResponse modeloVeiculoResponse = new ModeloVeiculoResponse( id, marcaId, marcaNome, nome, categoria, logoUrl );

        return modeloVeiculoResponse;
    }

    @Override
    public void updateEntityFromRequest(ModeloVeiculoRequest request, ModeloVeiculo entity) {
        if ( request == null ) {
            return;
        }

        if ( entity.getMarca() == null ) {
            entity.setMarca( new MarcaVeiculo() );
        }
        modeloVeiculoRequestToMarcaVeiculo1( request, entity.getMarca() );
        entity.setNome( request.nome() );
        entity.setCategoria( request.categoria() );
        entity.setLogoUrl( request.logoUrl() );
    }

    protected MarcaVeiculo modeloVeiculoRequestToMarcaVeiculo(ModeloVeiculoRequest modeloVeiculoRequest) {
        if ( modeloVeiculoRequest == null ) {
            return null;
        }

        MarcaVeiculo marcaVeiculo = new MarcaVeiculo();

        marcaVeiculo.setId( modeloVeiculoRequest.marcaId() );

        return marcaVeiculo;
    }

    private Long entityMarcaId(ModeloVeiculo modeloVeiculo) {
        if ( modeloVeiculo == null ) {
            return null;
        }
        MarcaVeiculo marca = modeloVeiculo.getMarca();
        if ( marca == null ) {
            return null;
        }
        Long id = marca.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityMarcaNome(ModeloVeiculo modeloVeiculo) {
        if ( modeloVeiculo == null ) {
            return null;
        }
        MarcaVeiculo marca = modeloVeiculo.getMarca();
        if ( marca == null ) {
            return null;
        }
        String nome = marca.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    protected void modeloVeiculoRequestToMarcaVeiculo1(ModeloVeiculoRequest modeloVeiculoRequest, MarcaVeiculo mappingTarget) {
        if ( modeloVeiculoRequest == null ) {
            return;
        }

        mappingTarget.setId( modeloVeiculoRequest.marcaId() );
    }
}
