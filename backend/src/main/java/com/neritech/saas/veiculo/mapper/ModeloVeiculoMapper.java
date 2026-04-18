package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.dto.ModeloVeiculoRequest;
import com.neritech.saas.veiculo.dto.ModeloVeiculoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ModeloVeiculoMapper {

    @Mapping(target = "marca.id", source = "marcaId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    ModeloVeiculo toEntity(ModeloVeiculoRequest request);

    @Mapping(target = "marcaId", source = "marca.id")
    @Mapping(target = "marcaNome", source = "marca.nome")
    ModeloVeiculoResponse toResponse(ModeloVeiculo entity);

    @Mapping(target = "marca.id", source = "marcaId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    void updateEntityFromRequest(ModeloVeiculoRequest request, @MappingTarget ModeloVeiculo entity);
}
