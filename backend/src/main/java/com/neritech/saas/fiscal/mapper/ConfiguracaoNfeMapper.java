package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.ConfiguracaoNfe;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeRequest;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { CertificadoDigitalMapper.class })
public interface ConfiguracaoNfeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "certificadoDigital", ignore = true)
    ConfiguracaoNfe toEntity(ConfiguracaoNfeRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ConfiguracaoNfeResponse toResponse(ConfiguracaoNfe entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "certificadoDigital", ignore = true)
    void updateEntityFromRequest(ConfiguracaoNfeRequest request, @MappingTarget ConfiguracaoNfe entity);
}
