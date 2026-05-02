package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.domain.ItChecklist;
import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.OSChecklistItemRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class OSChecklistItemMapperImpl implements OSChecklistItemMapper {

    @Override
    public OSChecklistItemResponse toResponse(OSChecklistItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long ordemServicoId = null;
        Long checklistModeloId = null;
        Long itemModeloId = null;
        Long id = null;
        String descricao = null;
        Boolean feito = null;
        Integer ordem = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        ordemServicoId = entityOrdemServicoId( entity );
        checklistModeloId = entityChecklistModeloId( entity );
        itemModeloId = entityItemModeloId( entity );
        id = entity.getId();
        descricao = entity.getDescricao();
        feito = entity.getFeito();
        ordem = entity.getOrdem();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        OSChecklistItemResponse oSChecklistItemResponse = new OSChecklistItemResponse( id, ordemServicoId, checklistModeloId, itemModeloId, descricao, feito, ordem, dataCadastro, dataAtualizacao );

        return oSChecklistItemResponse;
    }

    @Override
    public void updateEntityFromRequest(OSChecklistItemRequest request, OSChecklistItem entity) {
        if ( request == null ) {
            return;
        }

        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.feito() != null ) {
            entity.setFeito( request.feito() );
        }
        if ( request.ordem() != null ) {
            entity.setOrdem( request.ordem() );
        }
    }

    private Long entityOrdemServicoId(OSChecklistItem oSChecklistItem) {
        if ( oSChecklistItem == null ) {
            return null;
        }
        OrdemServico ordemServico = oSChecklistItem.getOrdemServico();
        if ( ordemServico == null ) {
            return null;
        }
        Long id = ordemServico.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityChecklistModeloId(OSChecklistItem oSChecklistItem) {
        if ( oSChecklistItem == null ) {
            return null;
        }
        Checklist checklistModelo = oSChecklistItem.getChecklistModelo();
        if ( checklistModelo == null ) {
            return null;
        }
        Long id = checklistModelo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityItemModeloId(OSChecklistItem oSChecklistItem) {
        if ( oSChecklistItem == null ) {
            return null;
        }
        ItChecklist itemModelo = oSChecklistItem.getItemModelo();
        if ( itemModelo == null ) {
            return null;
        }
        Long id = itemModelo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
