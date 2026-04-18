package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoFornecedorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "numeroPedido", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    PedidoFornecedor toEntity(PedidoFornecedorRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    @Mapping(source = "fornecedorId", target = "fornecedorId")
    @Mapping(source = "numeroPedido", target = "numeroPedido")
    @Mapping(target = "nomeFornecedor", expression = "java(entity.getFornecedor() != null ? entity.getFornecedor().getNomeFantasia() : null)")
    @Mapping(source = "dataCadastro", target = "dataCadastro")
    PedidoFornecedorResponse toResponse(PedidoFornecedor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "numeroPedido", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(PedidoFornecedorRequest request, @MappingTarget PedidoFornecedor entity);
}
