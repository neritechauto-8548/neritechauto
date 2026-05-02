package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.domain.ItChecklist;
import com.neritech.saas.ordemservico.dto.ItChecklistRequest;
import com.neritech.saas.ordemservico.dto.ItChecklistResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class OrdemServicoItChecklistMapperImpl implements ItChecklistMapper {

    @Override
    public ItChecklist toEntity(ItChecklistRequest request) {
        if ( request == null ) {
            return null;
        }

        ItChecklist itChecklist = new ItChecklist();

        itChecklist.setDsItChecklist( request.dsItChecklist() );

        return itChecklist;
    }

    @Override
    public ItChecklistResponse toResponse(ItChecklist entity) {
        if ( entity == null ) {
            return null;
        }

        Long checkListId = null;
        Long id = null;
        String dsItChecklist = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        checkListId = entityChecklistId( entity );
        id = entity.getId();
        dsItChecklist = entity.getDsItChecklist();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ItChecklistResponse itChecklistResponse = new ItChecklistResponse( id, checkListId, dsItChecklist, dataCadastro, dataAtualizacao );

        return itChecklistResponse;
    }

    @Override
    public void updateEntityFromRequest(ItChecklistRequest request, ItChecklist entity) {
        if ( request == null ) {
            return;
        }

        entity.setDsItChecklist( request.dsItChecklist() );
    }

    private Long entityChecklistId(ItChecklist itChecklist) {
        if ( itChecklist == null ) {
            return null;
        }
        Checklist checklist = itChecklist.getChecklist();
        if ( checklist == null ) {
            return null;
        }
        Long id = checklist.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
