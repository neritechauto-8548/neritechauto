package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.ItQuestionarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, implementationName = "Comunicacao<CLASS_NAME>Impl")
public interface ItQuestionarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ItQuestionario toEntity(ItQuestionarioRequest request);

    @Mapping(source = "questionario.id", target = "questionarioId")
    ItQuestionarioResponse toResponse(ItQuestionario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ItQuestionarioRequest request, @MappingTarget ItQuestionario entity);
}
