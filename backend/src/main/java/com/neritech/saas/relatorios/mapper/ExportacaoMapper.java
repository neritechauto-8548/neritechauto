package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.Exportacao;
import com.neritech.saas.relatorios.dto.ExportacaoRequest;
import com.neritech.saas.relatorios.dto.ExportacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExportacaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caminhoArquivo", ignore = true)
    @Mapping(target = "tamanhoArquivoBytes", ignore = true)
    @Mapping(target = "totalRegistros", ignore = true)
    @Mapping(target = "dataInicio", ignore = true) // Set by service
    @Mapping(target = "dataFim", ignore = true)
    @Mapping(target = "duracaoSegundos", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progressoPercentual", ignore = true)
    @Mapping(target = "erroExportacao", ignore = true)
    @Mapping(target = "urlDownload", ignore = true)
    @Mapping(target = "dataExpiracaoDownload", ignore = true)
    @Mapping(target = "downloadsRealizados", ignore = true)
    @Mapping(target = "limiteDownloads", ignore = true)
    @Mapping(target = "notificacaoEnviada", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    Exportacao toEntity(ExportacaoRequest request);

    ExportacaoResponse toResponse(Exportacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "usuarioSolicitante", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    void updateEntity(@MappingTarget Exportacao entity, ExportacaoRequest request);
}
