package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.AlertaEstoque;
import com.neritech.saas.estoque.dto.AlertaEstoqueRequest;
import com.neritech.saas.estoque.dto.AlertaEstoqueResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlertaEstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataResolucao", ignore = true)
    @Mapping(target = "notificacaoEnviada", ignore = true)
    @Mapping(target = "dataNotificacao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    AlertaEstoque toEntity(AlertaEstoqueRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    AlertaEstoqueResponse toResponse(AlertaEstoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataResolucao", ignore = true)
    @Mapping(target = "notificacaoEnviada", ignore = true)
    @Mapping(target = "dataNotificacao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(AlertaEstoqueRequest request, @MappingTarget AlertaEstoque entity);
}
