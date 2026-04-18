package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataDiagnostico", ignore = true)
    @Mapping(target = "dataAprovacaoCliente", ignore = true)
    Diagnostico toEntity(DiagnosticoRequest request);

    @Mapping(source = "ordemServico.id", target = "ordemServicoId")
    DiagnosticoResponse toResponse(Diagnostico entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataDiagnostico", ignore = true)
    @Mapping(target = "dataAprovacaoCliente", ignore = true)
    void updateEntityFromRequest(DiagnosticoRequest request, @MappingTarget Diagnostico entity);
}
