package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Funcionario toEntity(FuncionarioRequest request);

    @Mapping(source = "cargo.nome", target = "cargoNome")
    @Mapping(source = "departamento.descricao", target = "departamentoNome")
    FuncionarioResponse toResponse(Funcionario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(FuncionarioRequest request, @MappingTarget Funcionario entity);
}
