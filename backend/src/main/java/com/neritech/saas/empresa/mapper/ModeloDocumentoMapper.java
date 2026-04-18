package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ModeloDocumento;
import com.neritech.saas.empresa.dto.ModeloDocumentoRequest;
import com.neritech.saas.empresa.dto.ModeloDocumentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModeloDocumentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ModeloDocumento toEntity(ModeloDocumentoRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    ModeloDocumentoResponse toResponse(ModeloDocumento entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ModeloDocumentoRequest request, @MappingTarget ModeloDocumento entity);
}
