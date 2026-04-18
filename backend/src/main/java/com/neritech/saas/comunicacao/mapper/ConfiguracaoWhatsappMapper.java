package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoWhatsapp;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConfiguracaoWhatsappMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    @Mapping(target = "dataUltimaVerificacao", ignore = true)
    @Mapping(target = "statusVerificacao", ignore = true)
    ConfiguracaoWhatsapp toEntity(ConfiguracaoWhatsappRequest request);

    ConfiguracaoWhatsappResponse toResponse(ConfiguracaoWhatsapp entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    @Mapping(target = "dataUltimaVerificacao", ignore = true)
    @Mapping(target = "statusVerificacao", ignore = true)
    void updateEntity(ConfiguracaoWhatsappRequest request, @MappingTarget ConfiguracaoWhatsapp entity);
}
