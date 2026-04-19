package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.domain.MovimentacaoEstoque;
import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueResponse;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class MovimentacaoEstoqueMapperImpl implements MovimentacaoEstoqueMapper {

    @Override
    public MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();

        movimentacaoEstoque.setEmpresaId( request.empresaId() );
        movimentacaoEstoque.setTipoMovimentacao( request.tipoMovimentacao() );
        movimentacaoEstoque.setSubtipoMovimentacao( request.subtipoMovimentacao() );
        movimentacaoEstoque.setQuantidade( request.quantidade() );
        movimentacaoEstoque.setValorUnitario( request.valorUnitario() );
        movimentacaoEstoque.setDocumentoTipo( request.documentoTipo() );
        movimentacaoEstoque.setDocumentoNumero( request.documentoNumero() );
        movimentacaoEstoque.setDocumentoId( request.documentoId() );
        movimentacaoEstoque.setLoteNumero( request.loteNumero() );
        movimentacaoEstoque.setDataValidade( request.dataValidade() );
        movimentacaoEstoque.setDataFabricacao( request.dataFabricacao() );
        movimentacaoEstoque.setMotivo( request.motivo() );
        movimentacaoEstoque.setObservacoes( request.observacoes() );
        movimentacaoEstoque.setUsuarioResponsavel( request.usuarioResponsavel() );
        movimentacaoEstoque.setOrdemServicoId( request.ordemServicoId() );

        return movimentacaoEstoque;
    }

    @Override
    public MovimentacaoEstoqueResponse toResponse(MovimentacaoEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long localizacaoOrigemId = null;
        String localizacaoOrigemNome = null;
        Long localizacaoDestinoId = null;
        String localizacaoDestinoNome = null;
        Long fornecedorId = null;
        String fornecedorNome = null;
        Long id = null;
        Long empresaId = null;
        TipoMovimentacao tipoMovimentacao = null;
        String subtipoMovimentacao = null;
        BigDecimal quantidade = null;
        BigDecimal quantidadeAnterior = null;
        BigDecimal quantidadeAtual = null;
        BigDecimal valorUnitario = null;
        BigDecimal valorTotal = null;
        String documentoTipo = null;
        String documentoNumero = null;
        Long documentoId = null;
        String loteNumero = null;
        LocalDate dataValidade = null;
        LocalDate dataFabricacao = null;
        String motivo = null;
        String observacoes = null;
        Long usuarioResponsavel = null;
        LocalDateTime dataMovimentacao = null;
        Long ordemServicoId = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        localizacaoOrigemId = entityLocalizacaoOrigemId( entity );
        localizacaoOrigemNome = entityLocalizacaoOrigemNome( entity );
        localizacaoDestinoId = entityLocalizacaoDestinoId( entity );
        localizacaoDestinoNome = entityLocalizacaoDestinoNome( entity );
        fornecedorId = entityFornecedorId( entity );
        fornecedorNome = entityFornecedorNomeFantasia( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        tipoMovimentacao = entity.getTipoMovimentacao();
        subtipoMovimentacao = entity.getSubtipoMovimentacao();
        quantidade = entity.getQuantidade();
        quantidadeAnterior = entity.getQuantidadeAnterior();
        quantidadeAtual = entity.getQuantidadeAtual();
        valorUnitario = entity.getValorUnitario();
        valorTotal = entity.getValorTotal();
        documentoTipo = entity.getDocumentoTipo();
        documentoNumero = entity.getDocumentoNumero();
        documentoId = entity.getDocumentoId();
        loteNumero = entity.getLoteNumero();
        dataValidade = entity.getDataValidade();
        dataFabricacao = entity.getDataFabricacao();
        motivo = entity.getMotivo();
        observacoes = entity.getObservacoes();
        usuarioResponsavel = entity.getUsuarioResponsavel();
        dataMovimentacao = entity.getDataMovimentacao();
        ordemServicoId = entity.getOrdemServicoId();

        MovimentacaoEstoqueResponse movimentacaoEstoqueResponse = new MovimentacaoEstoqueResponse( id, empresaId, produtoId, produtoNome, tipoMovimentacao, subtipoMovimentacao, quantidade, quantidadeAnterior, quantidadeAtual, valorUnitario, valorTotal, localizacaoOrigemId, localizacaoOrigemNome, localizacaoDestinoId, localizacaoDestinoNome, documentoTipo, documentoNumero, documentoId, fornecedorId, fornecedorNome, loteNumero, dataValidade, dataFabricacao, motivo, observacoes, usuarioResponsavel, dataMovimentacao, ordemServicoId );

        return movimentacaoEstoqueResponse;
    }

    private Long entityProdutoId(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        Produto produto = movimentacaoEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        Produto produto = movimentacaoEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityLocalizacaoOrigemId(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacaoOrigem = movimentacaoEstoque.getLocalizacaoOrigem();
        if ( localizacaoOrigem == null ) {
            return null;
        }
        Long id = localizacaoOrigem.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityLocalizacaoOrigemNome(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacaoOrigem = movimentacaoEstoque.getLocalizacaoOrigem();
        if ( localizacaoOrigem == null ) {
            return null;
        }
        String nome = localizacaoOrigem.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityLocalizacaoDestinoId(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacaoDestino = movimentacaoEstoque.getLocalizacaoDestino();
        if ( localizacaoDestino == null ) {
            return null;
        }
        Long id = localizacaoDestino.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityLocalizacaoDestinoNome(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacaoDestino = movimentacaoEstoque.getLocalizacaoDestino();
        if ( localizacaoDestino == null ) {
            return null;
        }
        String nome = localizacaoDestino.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityFornecedorId(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        Fornecedor fornecedor = movimentacaoEstoque.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        Long id = fornecedor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFornecedorNomeFantasia(MovimentacaoEstoque movimentacaoEstoque) {
        if ( movimentacaoEstoque == null ) {
            return null;
        }
        Fornecedor fornecedor = movimentacaoEstoque.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        String nomeFantasia = fornecedor.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
