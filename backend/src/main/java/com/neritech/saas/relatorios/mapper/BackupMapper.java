package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.Backup;
import com.neritech.saas.relatorios.dto.BackupRequest;
import com.neritech.saas.relatorios.dto.BackupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BackupMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true) // Set by service
    @Mapping(target = "taxaCompressao", ignore = true)
    @Mapping(target = "testadoRestauracao", ignore = true)
    @Mapping(target = "dataTesteRestauracao", ignore = true)
    @Mapping(target = "resultadoTeste", ignore = true)
    @Mapping(target = "erroBackup", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    Backup toEntity(BackupRequest request);

    BackupResponse toResponse(Backup entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    void updateEntity(@MappingTarget Backup entity, BackupRequest request);
}
