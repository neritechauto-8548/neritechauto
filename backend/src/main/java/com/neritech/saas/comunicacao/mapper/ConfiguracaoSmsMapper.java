package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoSms;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConfiguracaoSmsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "creditosDisponiveis", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    @Mapping(target = "dataUltimaSincronizacao", ignore = true)
    @Mapping(target = "ativo", source = "ativa")
    ConfiguracaoSms toEntity(ConfiguracaoSmsRequest request);

    @Mapping(target = "ativo", source = "ativo")
    ConfiguracaoSmsResponse toResponse(ConfiguracaoSms entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "creditosDisponiveis", ignore = true)
    @Mapping(target = "dataConfiguracao", ignore = true)
    @Mapping(target = "configuradoPor", ignore = true)
    @Mapping(target = "dataUltimaSincronizacao", ignore = true)
    @Mapping(target = "ativo", source = "ativa")
    void updateEntity(ConfiguracaoSmsRequest request, @MappingTarget ConfiguracaoSms entity);
}
