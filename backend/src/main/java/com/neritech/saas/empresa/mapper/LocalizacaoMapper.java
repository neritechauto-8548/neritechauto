package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Localizacao;
import com.neritech.saas.empresa.dto.LocalizacaoRequest;
import com.neritech.saas.empresa.dto.LocalizacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalizacaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Localizacao toEntity(LocalizacaoRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    LocalizacaoResponse toResponse(Localizacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(LocalizacaoRequest request, @MappingTarget Localizacao entity);
}
