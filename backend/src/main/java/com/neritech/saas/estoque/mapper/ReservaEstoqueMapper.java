package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.ReservaEstoque;
import com.neritech.saas.estoque.dto.ReservaEstoqueRequest;
import com.neritech.saas.estoque.dto.ReservaEstoqueResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservaEstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataReserva", ignore = true)
    @Mapping(target = "dataUtilizacao", ignore = true)
    @Mapping(target = "dataCancelamento", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ReservaEstoque toEntity(ReservaEstoqueRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    ReservaEstoqueResponse toResponse(ReservaEstoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataReserva", ignore = true)
    @Mapping(target = "dataUtilizacao", ignore = true)
    @Mapping(target = "dataCancelamento", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ReservaEstoqueRequest request, @MappingTarget ReservaEstoque entity);
}
