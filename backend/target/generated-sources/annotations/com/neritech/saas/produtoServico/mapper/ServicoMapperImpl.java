package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.Servico;
import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.dto.ServicoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:45-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ServicoMapperImpl implements ServicoMapper {

    @Override
    public Servico toEntity(ServicoRequest request) {
        if ( request == null ) {
            return null;
        }

        Servico servico = new Servico();

        servico.setNome( request.nome() );
        servico.setPrecoBase( request.precoBase() );
        servico.setCusto( request.custo() );
        servico.setInstrucoesExecucao( request.instrucoesExecucao() );
        servico.setAtivo( request.ativo() );

        return servico;
    }

    @Override
    public ServicoResponse toResponse(Servico entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long id = null;
        String nome = null;
        BigDecimal precoBase = null;
        BigDecimal custo = null;
        String instrucoesExecucao = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entity.getEmpresaId();
        id = entity.getId();
        nome = entity.getNome();
        precoBase = entity.getPrecoBase();
        custo = entity.getCusto();
        instrucoesExecucao = entity.getInstrucoesExecucao();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ServicoResponse servicoResponse = new ServicoResponse( id, empresaId, nome, precoBase, custo, instrucoesExecucao, ativo, dataCadastro, dataAtualizacao );

        return servicoResponse;
    }

    @Override
    public void updateEntityFromRequest(ServicoRequest request, Servico entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setPrecoBase( request.precoBase() );
        entity.setCusto( request.custo() );
        entity.setInstrucoesExecucao( request.instrucoesExecucao() );
        entity.setAtivo( request.ativo() );
    }
}
