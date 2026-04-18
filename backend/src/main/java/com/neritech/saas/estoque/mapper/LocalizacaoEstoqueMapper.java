package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalizacaoEstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    LocalizacaoEstoque toEntity(LocalizacaoEstoqueRequest request);

    LocalizacaoEstoqueResponse toResponse(LocalizacaoEstoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(LocalizacaoEstoqueRequest request, @MappingTarget LocalizacaoEstoque entity);
}
