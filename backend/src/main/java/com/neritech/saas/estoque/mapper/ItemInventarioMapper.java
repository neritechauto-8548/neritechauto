package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.ItemInventario;
import com.neritech.saas.estoque.dto.ItemInventarioRequest;
import com.neritech.saas.estoque.dto.ItemInventarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemInventarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventario", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "localizacao", ignore = true)
    @Mapping(target = "diferenca", ignore = true)
    @Mapping(target = "valorTotalSistema", ignore = true)
    @Mapping(target = "valorTotalContado", ignore = true)
    @Mapping(target = "diferencaValor", ignore = true)
    @Mapping(target = "dataContagem", ignore = true)
    @Mapping(target = "dataConferencia", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ItemInventario toEntity(ItemInventarioRequest request);

    @Mapping(source = "inventario.id", target = "inventarioId")
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    @Mapping(source = "localizacao.id", target = "localizacaoId")
    @Mapping(source = "localizacao.nome", target = "localizacaoNome")
    ItemInventarioResponse toResponse(ItemInventario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventario", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "localizacao", ignore = true)
    @Mapping(target = "diferenca", ignore = true)
    @Mapping(target = "valorTotalSistema", ignore = true)
    @Mapping(target = "valorTotalContado", ignore = true)
    @Mapping(target = "diferencaValor", ignore = true)
    @Mapping(target = "dataContagem", ignore = true)
    @Mapping(target = "dataConferencia", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ItemInventarioRequest request, @MappingTarget ItemInventario entity);
}
