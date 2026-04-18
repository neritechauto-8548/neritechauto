package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.Inventario;
import com.neritech.saas.estoque.dto.InventarioRequest;
import com.neritech.saas.estoque.dto.InventarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalItensPlanejados", ignore = true)
    @Mapping(target = "totalItensContados", ignore = true)
    @Mapping(target = "totalDivergencias", ignore = true)
    @Mapping(target = "valorTotalSistema", ignore = true)
    @Mapping(target = "valorTotalContado", ignore = true)
    @Mapping(target = "diferencaValor", ignore = true)
    @Mapping(target = "finalizadoPor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Inventario toEntity(InventarioRequest request);

    InventarioResponse toResponse(Inventario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalItensPlanejados", ignore = true)
    @Mapping(target = "totalItensContados", ignore = true)
    @Mapping(target = "totalDivergencias", ignore = true)
    @Mapping(target = "valorTotalSistema", ignore = true)
    @Mapping(target = "valorTotalContado", ignore = true)
    @Mapping(target = "diferencaValor", ignore = true)
    @Mapping(target = "finalizadoPor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(InventarioRequest request, @MappingTarget Inventario entity);
}
