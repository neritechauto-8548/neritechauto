package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.CertificadoDigital;
import com.neritech.saas.fiscal.dto.CertificadoDigitalRequest;
import com.neritech.saas.fiscal.dto.CertificadoDigitalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificadoDigitalMapper {
    CertificadoDigital toEntity(CertificadoDigitalRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CertificadoDigitalResponse toResponse(CertificadoDigital entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(CertificadoDigitalRequest request, @MappingTarget CertificadoDigital entity);
}
