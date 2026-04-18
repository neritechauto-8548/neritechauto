package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ConfiguracaoFiscal;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfiguracaoFiscalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ConfiguracaoFiscal toEntity(ConfiguracaoFiscalRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    ConfiguracaoFiscalResponse toResponse(ConfiguracaoFiscal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ConfiguracaoFiscalRequest request, @MappingTarget ConfiguracaoFiscal entity);
}
