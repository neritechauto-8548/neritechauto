package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:55-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PedidoFornecedorMapperImpl implements PedidoFornecedorMapper {

    @Override
    public PedidoFornecedor toEntity(PedidoFornecedorRequest request) {
        if ( request == null ) {
            return null;
        }

        PedidoFornecedor pedidoFornecedor = new PedidoFornecedor();

        pedidoFornecedor.setFornecedorId( request.fornecedorId() );
        pedidoFornecedor.setResponsavel( request.responsavel() );
        pedidoFornecedor.setDataPrevisao( request.dataPrevisao() );
        pedidoFornecedor.setNumeroNf( request.numeroNf() );
        pedidoFornecedor.setObservacao( request.observacao() );
        pedidoFornecedor.setDescricaoInterna( request.descricaoInterna() );

        return pedidoFornecedor;
    }

    @Override
    public PedidoFornecedorResponse toResponse(PedidoFornecedor entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long fornecedorId = null;
        Long numeroPedido = null;
        LocalDateTime dataCadastro = null;
        Long id = null;
        String responsavel = null;
        LocalDate dataPrevisao = null;
        String numeroNf = null;
        String observacao = null;
        String descricaoInterna = null;
        StatusPedidoFornecedor status = null;

        empresaId = entity.getEmpresaId();
        fornecedorId = entity.getFornecedorId();
        numeroPedido = entity.getNumeroPedido();
        dataCadastro = entity.getDataCadastro();
        id = entity.getId();
        responsavel = entity.getResponsavel();
        dataPrevisao = entity.getDataPrevisao();
        numeroNf = entity.getNumeroNf();
        observacao = entity.getObservacao();
        descricaoInterna = entity.getDescricaoInterna();
        status = entity.getStatus();

        String nomeFornecedor = entity.getFornecedor() != null ? entity.getFornecedor().getNomeFantasia() : null;

        PedidoFornecedorResponse pedidoFornecedorResponse = new PedidoFornecedorResponse( id, empresaId, fornecedorId, nomeFornecedor, numeroPedido, responsavel, dataPrevisao, numeroNf, observacao, descricaoInterna, status, dataCadastro );

        return pedidoFornecedorResponse;
    }

    @Override
    public void updateEntityFromRequest(PedidoFornecedorRequest request, PedidoFornecedor entity) {
        if ( request == null ) {
            return;
        }

        entity.setFornecedorId( request.fornecedorId() );
        entity.setResponsavel( request.responsavel() );
        entity.setDataPrevisao( request.dataPrevisao() );
        entity.setNumeroNf( request.numeroNf() );
        entity.setObservacao( request.observacao() );
        entity.setDescricaoInterna( request.descricaoInterna() );
    }
}
