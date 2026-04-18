package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.RelatorioSalvo;
import com.neritech.saas.relatorios.dto.RelatorioSalvoRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RelatorioSalvoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalExecucoes", ignore = true)
    @Mapping(target = "dataUltimaExecucao", ignore = true)
    @Mapping(target = "tempoMedioExecucao", ignore = true)
    @Mapping(target = "tamanhoMedioArquivo", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    RelatorioSalvo toEntity(RelatorioSalvoRequest request);

    RelatorioSalvoResponse toResponse(RelatorioSalvo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Usually shouldn't change company
    @Mapping(target = "totalExecucoes", ignore = true)
    @Mapping(target = "dataUltimaExecucao", ignore = true)
    @Mapping(target = "tempoMedioExecucao", ignore = true)
    @Mapping(target = "tamanhoMedioArquivo", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntity(@MappingTarget RelatorioSalvo entity, RelatorioSalvoRequest request);
}
