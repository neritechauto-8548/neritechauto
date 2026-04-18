package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaRequest;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssinaturaEmpresaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    AssinaturaEmpresa toEntity(AssinaturaEmpresaRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    @Mapping(source = "plano.id", target = "planoId")
    @Mapping(source = "plano.nome", target = "planoNome")
    AssinaturaEmpresaResponse toResponse(AssinaturaEmpresa entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(AssinaturaEmpresaRequest request, @MappingTarget AssinaturaEmpresa entity);
}
