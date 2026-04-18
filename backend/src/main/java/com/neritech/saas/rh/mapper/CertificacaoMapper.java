package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Certificacao;
import com.neritech.saas.rh.dto.CertificacaoRequest;
import com.neritech.saas.rh.dto.CertificacaoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CertificacaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "cadastradoPor", ignore = true)
    Certificacao toEntity(CertificacaoRequest request);

    CertificacaoResponse toResponse(Certificacao entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "cadastradoPor", ignore = true)
    void updateEntity(CertificacaoRequest request, @MappingTarget Certificacao entity);
}
