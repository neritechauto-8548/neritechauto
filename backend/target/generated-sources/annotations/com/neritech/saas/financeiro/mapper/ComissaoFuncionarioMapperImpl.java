package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ComissaoFuncionario;
import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.ItemFatura;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioRequest;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioResponse;
import com.neritech.saas.rh.domain.Funcionario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:05-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ComissaoFuncionarioMapperImpl implements ComissaoFuncionarioMapper {

    @Override
    public ComissaoFuncionario toEntity(ComissaoFuncionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        ComissaoFuncionario comissaoFuncionario = new ComissaoFuncionario();

        comissaoFuncionario.setTipoComissao( request.tipoComissao() );
        comissaoFuncionario.setPercentualComissao( request.percentualComissao() );
        comissaoFuncionario.setValorComissao( request.valorComissao() );
        comissaoFuncionario.setDataPagamento( request.dataPagamento() );
        comissaoFuncionario.setObservacoes( request.observacoes() );

        return comissaoFuncionario;
    }

    @Override
    public ComissaoFuncionarioResponse toResponse(ComissaoFuncionario entity) {
        if ( entity == null ) {
            return null;
        }

        String funcionarioNome = null;
        String faturaNumero = null;
        String itemFaturaDescricao = null;
        Long id = null;
        Long empresaId = null;
        BigDecimal percentualComissao = null;
        BigDecimal valorComissao = null;
        TipoComissao tipoComissao = null;
        LocalDate dataPagamento = null;
        String observacoes = null;

        funcionarioNome = entityFuncionarioNomeCompleto( entity );
        faturaNumero = entityFaturaNumeroFatura( entity );
        itemFaturaDescricao = entityItemFaturaDescricao( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        percentualComissao = entity.getPercentualComissao();
        valorComissao = entity.getValorComissao();
        tipoComissao = entity.getTipoComissao();
        dataPagamento = entity.getDataPagamento();
        observacoes = entity.getObservacoes();

        Long funcionarioId = null;
        Long faturaId = null;
        Long itemFaturaId = null;
        LocalDate dataReferencia = null;
        BigDecimal valorBase = null;
        Boolean pago = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ComissaoFuncionarioResponse comissaoFuncionarioResponse = new ComissaoFuncionarioResponse( id, empresaId, funcionarioId, funcionarioNome, faturaId, faturaNumero, itemFaturaId, itemFaturaDescricao, dataReferencia, valorBase, percentualComissao, valorComissao, tipoComissao, pago, dataPagamento, observacoes, createdAt, updatedAt );

        return comissaoFuncionarioResponse;
    }

    @Override
    public void updateEntityFromDTO(ComissaoFuncionarioRequest request, ComissaoFuncionario entity) {
        if ( request == null ) {
            return;
        }

        if ( request.tipoComissao() != null ) {
            entity.setTipoComissao( request.tipoComissao() );
        }
        if ( request.percentualComissao() != null ) {
            entity.setPercentualComissao( request.percentualComissao() );
        }
        if ( request.valorComissao() != null ) {
            entity.setValorComissao( request.valorComissao() );
        }
        if ( request.dataPagamento() != null ) {
            entity.setDataPagamento( request.dataPagamento() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }

    private String entityFuncionarioNomeCompleto(ComissaoFuncionario comissaoFuncionario) {
        if ( comissaoFuncionario == null ) {
            return null;
        }
        Funcionario funcionario = comissaoFuncionario.getFuncionario();
        if ( funcionario == null ) {
            return null;
        }
        String nomeCompleto = funcionario.getNomeCompleto();
        if ( nomeCompleto == null ) {
            return null;
        }
        return nomeCompleto;
    }

    private String entityFaturaNumeroFatura(ComissaoFuncionario comissaoFuncionario) {
        if ( comissaoFuncionario == null ) {
            return null;
        }
        Fatura fatura = comissaoFuncionario.getFatura();
        if ( fatura == null ) {
            return null;
        }
        String numeroFatura = fatura.getNumeroFatura();
        if ( numeroFatura == null ) {
            return null;
        }
        return numeroFatura;
    }

    private String entityItemFaturaDescricao(ComissaoFuncionario comissaoFuncionario) {
        if ( comissaoFuncionario == null ) {
            return null;
        }
        ItemFatura itemFatura = comissaoFuncionario.getItemFatura();
        if ( itemFatura == null ) {
            return null;
        }
        String descricao = itemFatura.getDescricao();
        if ( descricao == null ) {
            return null;
        }
        return descricao;
    }
}
