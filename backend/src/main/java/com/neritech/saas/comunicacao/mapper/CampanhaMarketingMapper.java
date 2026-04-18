package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.CampanhaMarketing;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingRequest;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CampanhaMarketingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "custoRealizado", ignore = true)
    @Mapping(target = "alcanceRealizado", ignore = true)
    @Mapping(target = "conversaoRealizada", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "resultadosDetalhados", ignore = true)
    @Mapping(target = "roiCalculado", ignore = true)
    @Mapping(target = "criadaPor", ignore = true)
    CampanhaMarketing toEntity(CampanhaMarketingRequest request);

    CampanhaMarketingResponse toResponse(CampanhaMarketing entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "custoRealizado", ignore = true)
    @Mapping(target = "alcanceRealizado", ignore = true)
    @Mapping(target = "conversaoRealizada", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "resultadosDetalhados", ignore = true)
    @Mapping(target = "roiCalculado", ignore = true)
    @Mapping(target = "criadaPor", ignore = true)
    void updateEntity(CampanhaMarketingRequest request, @MappingTarget CampanhaMarketing entity);
}
