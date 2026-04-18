package com.neritech.saas.rh.mapper;

import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import com.neritech.saas.rh.domain.FeriasFuncionario;
import com.neritech.saas.rh.dto.FeriasFuncionarioRequest;
import com.neritech.saas.rh.dto.FeriasFuncionarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FeriasFuncionarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "solicitadoPor", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "aprovadoPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "canceladoPor", ignore = true)
    @Mapping(target = "dataCancelamento", ignore = true)
    @Mapping(target = "motivoCancelamento", ignore = true)
    FeriasFuncionario toEntity(FeriasFuncionarioRequest request);

    FeriasFuncionarioResponse toResponse(FeriasFuncionario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "solicitadoPor", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "aprovadoPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "canceladoPor", ignore = true)
    @Mapping(target = "dataCancelamento", ignore = true)
    @Mapping(target = "motivoCancelamento", ignore = true)
    void updateEntity(FeriasFuncionarioRequest request, @MappingTarget FeriasFuncionario entity);
}
