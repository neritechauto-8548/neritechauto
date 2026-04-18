package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.MovimentacaoEstoque;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovimentacaoEstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "localizacaoOrigem", ignore = true)
    @Mapping(target = "localizacaoDestino", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "quantidadeAnterior", ignore = true)
    @Mapping(target = "quantidadeAtual", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(target = "dataMovimentacao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    @Mapping(source = "localizacaoOrigem.id", target = "localizacaoOrigemId")
    @Mapping(source = "localizacaoOrigem.nome", target = "localizacaoOrigemNome")
    @Mapping(source = "localizacaoDestino.id", target = "localizacaoDestinoId")
    @Mapping(source = "localizacaoDestino.nome", target = "localizacaoDestinoNome")
    @Mapping(source = "fornecedor.id", target = "fornecedorId")
    @Mapping(source = "fornecedor.nomeFantasia", target = "fornecedorNome")
    MovimentacaoEstoqueResponse toResponse(MovimentacaoEstoque entity);
}
