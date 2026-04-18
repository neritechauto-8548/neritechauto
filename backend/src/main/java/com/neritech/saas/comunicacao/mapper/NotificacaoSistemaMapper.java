package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaRequest;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificacaoSistemaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "lida", ignore = true)
    @Mapping(target = "dataLeitura", ignore = true)
    @Mapping(target = "confirmada", ignore = true)
    @Mapping(target = "dataConfirmacao", ignore = true)
    NotificacaoSistema toEntity(NotificacaoSistemaRequest request);

    NotificacaoSistemaResponse toResponse(NotificacaoSistema entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "lida", ignore = true)
    @Mapping(target = "dataLeitura", ignore = true)
    @Mapping(target = "confirmada", ignore = true)
    @Mapping(target = "dataConfirmacao", ignore = true)
    void updateEntity(NotificacaoSistemaRequest request, @MappingTarget NotificacaoSistema entity);
}
