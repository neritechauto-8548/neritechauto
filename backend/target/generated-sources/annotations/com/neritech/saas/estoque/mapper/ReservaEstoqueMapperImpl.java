package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.ReservaEstoque;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.domain.enums.TipoReserva;
import com.neritech.saas.estoque.dto.ReservaEstoqueRequest;
import com.neritech.saas.estoque.dto.ReservaEstoqueResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:47-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReservaEstoqueMapperImpl implements ReservaEstoqueMapper {

    @Override
    public ReservaEstoque toEntity(ReservaEstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        ReservaEstoque reservaEstoque = new ReservaEstoque();

        reservaEstoque.setQuantidadeReservada( request.quantidadeReservada() );
        reservaEstoque.setTipoReserva( request.tipoReserva() );
        reservaEstoque.setDocumentoId( request.documentoId() );
        reservaEstoque.setDocumentoTipo( request.documentoTipo() );
        reservaEstoque.setDataValidadeReserva( request.dataValidadeReserva() );
        reservaEstoque.setStatus( request.status() );
        reservaEstoque.setUsuarioResponsavel( request.usuarioResponsavel() );
        reservaEstoque.setObservacoes( request.observacoes() );
        reservaEstoque.setMotivoCancelamento( request.motivoCancelamento() );

        return reservaEstoque;
    }

    @Override
    public ReservaEstoqueResponse toResponse(ReservaEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long id = null;
        BigDecimal quantidadeReservada = null;
        TipoReserva tipoReserva = null;
        Long documentoId = null;
        String documentoTipo = null;
        LocalDateTime dataReserva = null;
        LocalDateTime dataValidadeReserva = null;
        StatusReserva status = null;
        Long usuarioResponsavel = null;
        String observacoes = null;
        LocalDateTime dataUtilizacao = null;
        LocalDateTime dataCancelamento = null;
        String motivoCancelamento = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        id = entity.getId();
        quantidadeReservada = entity.getQuantidadeReservada();
        tipoReserva = entity.getTipoReserva();
        documentoId = entity.getDocumentoId();
        documentoTipo = entity.getDocumentoTipo();
        dataReserva = entity.getDataReserva();
        dataValidadeReserva = entity.getDataValidadeReserva();
        status = entity.getStatus();
        usuarioResponsavel = entity.getUsuarioResponsavel();
        observacoes = entity.getObservacoes();
        dataUtilizacao = entity.getDataUtilizacao();
        dataCancelamento = entity.getDataCancelamento();
        motivoCancelamento = entity.getMotivoCancelamento();

        ReservaEstoqueResponse reservaEstoqueResponse = new ReservaEstoqueResponse( id, produtoId, produtoNome, quantidadeReservada, tipoReserva, documentoId, documentoTipo, dataReserva, dataValidadeReserva, status, usuarioResponsavel, observacoes, dataUtilizacao, dataCancelamento, motivoCancelamento );

        return reservaEstoqueResponse;
    }

    @Override
    public void updateEntityFromRequest(ReservaEstoqueRequest request, ReservaEstoque entity) {
        if ( request == null ) {
            return;
        }

        entity.setQuantidadeReservada( request.quantidadeReservada() );
        entity.setTipoReserva( request.tipoReserva() );
        entity.setDocumentoId( request.documentoId() );
        entity.setDocumentoTipo( request.documentoTipo() );
        entity.setDataValidadeReserva( request.dataValidadeReserva() );
        entity.setStatus( request.status() );
        entity.setUsuarioResponsavel( request.usuarioResponsavel() );
        entity.setObservacoes( request.observacoes() );
        entity.setMotivoCancelamento( request.motivoCancelamento() );
    }

    private Long entityProdutoId(ReservaEstoque reservaEstoque) {
        if ( reservaEstoque == null ) {
            return null;
        }
        Produto produto = reservaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(ReservaEstoque reservaEstoque) {
        if ( reservaEstoque == null ) {
            return null;
        }
        Produto produto = reservaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
