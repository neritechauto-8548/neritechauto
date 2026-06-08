package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.RegraComissao;
import com.neritech.saas.rh.dto.RegraComissaoRequest;
import com.neritech.saas.rh.dto.RegraComissaoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RegraComissaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "setor", ignore = true)
    RegraComissao toEntity(RegraComissaoRequest request);

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    @Mapping(source = "setor.id", target = "setorId")
    @Mapping(source = "setor.nome", target = "setorNome")
    RegraComissaoResponse toResponse(RegraComissao entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "setor", ignore = true)
    void updateEntity(RegraComissaoRequest request, @MappingTarget RegraComissao entity);
}
