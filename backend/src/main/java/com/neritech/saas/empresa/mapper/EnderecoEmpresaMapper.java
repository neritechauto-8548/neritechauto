package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnderecoEmpresaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    EnderecoEmpresa toEntity(EnderecoEmpresaRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    EnderecoEmpresaResponse toResponse(EnderecoEmpresa entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(EnderecoEmpresaRequest request, @MappingTarget EnderecoEmpresa entity);
}
