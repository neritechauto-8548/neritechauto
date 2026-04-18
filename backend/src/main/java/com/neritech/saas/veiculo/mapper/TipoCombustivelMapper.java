package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.dto.TipoCombustivelRequest;
import com.neritech.saas.veiculo.dto.TipoCombustivelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TipoCombustivelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    TipoCombustivel toEntity(TipoCombustivelRequest request);

    TipoCombustivelResponse toResponse(TipoCombustivel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(TipoCombustivelRequest request, @MappingTarget TipoCombustivel entity);
}
