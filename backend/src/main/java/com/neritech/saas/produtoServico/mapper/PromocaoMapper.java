package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.Promocao;
import com.neritech.saas.produtoServico.dto.PromocaoRequest;
import com.neritech.saas.produtoServico.dto.PromocaoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromocaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Handled in service
    @Mapping(target = "vezesUtilizada", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Promocao toEntity(PromocaoRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    PromocaoResponse toResponse(Promocao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "vezesUtilizada", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(PromocaoRequest request, @MappingTarget Promocao entity);
}
