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
    @Mapping(target = "certificadoDigitalA1", ignore = true)
    @Mapping(target = "senhaCertificado", ignore = true)
    @Mapping(target = "tokenAcessoProvedorHomologacao", ignore = true)
    @Mapping(target = "tokenAcessoProvedorProducao", ignore = true)
    @Mapping(target = "logoNfsePath", ignore = true)
    ConfiguracaoFiscal toEntity(ConfiguracaoFiscalRequest request);

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nomeFantasia", target = "empresaNome")
    @Mapping(target = "tokenAcessoProvedorHomologacao", ignore = true)
    @Mapping(target = "tokenAcessoProvedorProducao", ignore = true)
    ConfiguracaoFiscalResponse toResponse(ConfiguracaoFiscal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "certificadoDigitalA1", ignore = true)
    @Mapping(target = "senhaCertificado", ignore = true)
    @Mapping(target = "tokenAcessoProvedorHomologacao", ignore = true)
    @Mapping(target = "tokenAcessoProvedorProducao", ignore = true)
    @Mapping(target = "logoNfsePath", ignore = true)
    // The legacy company form sends a partial fiscal payload. Never erase
    // previously configured tax document fields or protected credentials.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ConfiguracaoFiscalRequest request, @MappingTarget ConfiguracaoFiscal entity);
}
