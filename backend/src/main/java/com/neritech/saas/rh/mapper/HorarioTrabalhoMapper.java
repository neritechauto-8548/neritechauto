package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.HorarioTrabalho;
import com.neritech.saas.rh.dto.HorarioTrabalhoRequest;
import com.neritech.saas.rh.dto.HorarioTrabalhoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HorarioTrabalhoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    HorarioTrabalho toEntity(HorarioTrabalhoRequest request);

    HorarioTrabalhoResponse toResponse(HorarioTrabalho entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(HorarioTrabalhoRequest request, @MappingTarget HorarioTrabalho entity);
}
