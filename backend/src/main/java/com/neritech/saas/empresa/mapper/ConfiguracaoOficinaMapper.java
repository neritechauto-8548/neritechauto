package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ConfiguracaoOficina;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfiguracaoOficinaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ConfiguracaoOficina toEntity(ConfiguracaoOficinaRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    ConfiguracaoOficinaResponse toResponse(ConfiguracaoOficina entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ConfiguracaoOficinaRequest request, @MappingTarget ConfiguracaoOficina entity);
}
