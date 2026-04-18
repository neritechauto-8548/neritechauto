package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoEmail;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConfiguracaoEmailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "enviosRealizadosHoje", ignore = true)
    @Mapping(target = "testado", ignore = true)
    @Mapping(target = "dataUltimoTeste", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    ConfiguracaoEmail toEntity(ConfiguracaoEmailRequest request);

    ConfiguracaoEmailResponse toResponse(ConfiguracaoEmail entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "enviosRealizadosHoje", ignore = true)
    @Mapping(target = "testado", ignore = true)
    @Mapping(target = "dataUltimoTeste", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    void updateEntity(ConfiguracaoEmailRequest request, @MappingTarget ConfiguracaoEmail entity);
}
