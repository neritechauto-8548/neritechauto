package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.dto.AnoModeloRequest;
import com.neritech.saas.veiculo.dto.AnoModeloResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnoModeloMapper {

    @Mapping(target = "modelo.id", source = "modeloId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    AnoModelo toEntity(AnoModeloRequest request);

    @Mapping(target = "modeloId", source = "modelo.id")
    @Mapping(target = "modeloNome", source = "modelo.nome")
    AnoModeloResponse toResponse(AnoModelo entity);

    @Mapping(target = "modelo.id", source = "modeloId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(AnoModeloRequest request, @MappingTarget AnoModelo entity);
}
