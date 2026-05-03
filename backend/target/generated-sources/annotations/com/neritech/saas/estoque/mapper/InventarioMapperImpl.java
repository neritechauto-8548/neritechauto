package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.Inventario;
import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.domain.enums.TipoInventario;
import com.neritech.saas.estoque.dto.InventarioRequest;
import com.neritech.saas.estoque.dto.InventarioResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:02-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class InventarioMapperImpl implements InventarioMapper {

    @Override
    public Inventario toEntity(InventarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Inventario inventario = new Inventario();

        inventario.setEmpresaId( request.empresaId() );
        inventario.setCodigo( request.codigo() );
        inventario.setDescricao( request.descricao() );
        inventario.setTipoInventario( request.tipoInventario() );
        inventario.setDataInicio( request.dataInicio() );
        inventario.setDataFim( request.dataFim() );
        inventario.setStatus( request.status() );
        inventario.setObservacoes( request.observacoes() );

        return inventario;
    }

    @Override
    public InventarioResponse toResponse(Inventario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String codigo = null;
        String descricao = null;
        TipoInventario tipoInventario = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        StatusInventario status = null;
        Integer totalItensPlanejados = null;
        Integer totalItensContados = null;
        Integer totalDivergencias = null;
        BigDecimal valorTotalSistema = null;
        BigDecimal valorTotalContado = null;
        BigDecimal diferencaValor = null;
        String observacoes = null;
        Long finalizadoPor = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        codigo = entity.getCodigo();
        descricao = entity.getDescricao();
        tipoInventario = entity.getTipoInventario();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        status = entity.getStatus();
        totalItensPlanejados = entity.getTotalItensPlanejados();
        totalItensContados = entity.getTotalItensContados();
        totalDivergencias = entity.getTotalDivergencias();
        valorTotalSistema = entity.getValorTotalSistema();
        valorTotalContado = entity.getValorTotalContado();
        diferencaValor = entity.getDiferencaValor();
        observacoes = entity.getObservacoes();
        finalizadoPor = entity.getFinalizadoPor();

        String localizacoesIncluidas = null;
        String categoriasIncluidas = null;
        String produtosIncluidos = null;
        String usuariosResponsaveis = null;

        InventarioResponse inventarioResponse = new InventarioResponse( id, empresaId, codigo, descricao, tipoInventario, dataInicio, dataFim, status, localizacoesIncluidas, categoriasIncluidas, produtosIncluidos, usuariosResponsaveis, totalItensPlanejados, totalItensContados, totalDivergencias, valorTotalSistema, valorTotalContado, diferencaValor, observacoes, finalizadoPor );

        return inventarioResponse;
    }

    @Override
    public void updateEntityFromRequest(InventarioRequest request, Inventario entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setCodigo( request.codigo() );
        entity.setDescricao( request.descricao() );
        entity.setTipoInventario( request.tipoInventario() );
        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setStatus( request.status() );
        entity.setObservacoes( request.observacoes() );
    }
}
