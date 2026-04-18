package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ComissaoFuncionario;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioRequest;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComissaoFuncionarioMapper {
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "fatura", ignore = true)
    @Mapping(target = "itemFatura", ignore = true)
    ComissaoFuncionario toEntity(ComissaoFuncionarioRequest request);

    @Mapping(target = "funcionarioNome", source = "funcionario.nomeCompleto")
    @Mapping(target = "faturaNumero", source = "fatura.numeroFatura")
    @Mapping(target = "itemFaturaDescricao", source = "itemFatura.descricao")
    ComissaoFuncionarioResponse toResponse(ComissaoFuncionario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "fatura", ignore = true)
    @Mapping(target = "itemFatura", ignore = true)
    void updateEntityFromDTO(ComissaoFuncionarioRequest request, @MappingTarget ComissaoFuncionario entity);
}
