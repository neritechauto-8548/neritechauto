package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.EscalaTrabalho;
import com.neritech.saas.rh.dto.EscalaTrabalhoRequest;
import com.neritech.saas.rh.dto.EscalaTrabalhoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EscalaTrabalhoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    EscalaTrabalho toEntity(EscalaTrabalhoRequest request);

    EscalaTrabalhoResponse toResponse(EscalaTrabalho entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(EscalaTrabalhoRequest request, @MappingTarget EscalaTrabalho entity);
}
