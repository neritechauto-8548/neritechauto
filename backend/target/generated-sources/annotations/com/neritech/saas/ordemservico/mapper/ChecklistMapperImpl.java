package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.dto.ChecklistRequest;
import com.neritech.saas.ordemservico.dto.ChecklistResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ChecklistMapperImpl implements ChecklistMapper {

    @Override
    public Checklist toEntity(ChecklistRequest request) {
        if ( request == null ) {
            return null;
        }

        Checklist checklist = new Checklist();

        checklist.setEmpresaId( request.empresaId() );
        checklist.setDsChecklist( request.dsChecklist() );
        checklist.setUrlImagem( request.urlImagem() );

        return checklist;
    }

    @Override
    public ChecklistResponse toResponse(Checklist entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String dsChecklist = null;
        String urlImagem = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        dsChecklist = entity.getDsChecklist();
        urlImagem = entity.getUrlImagem();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ChecklistResponse checklistResponse = new ChecklistResponse( id, empresaId, dsChecklist, urlImagem, dataCadastro, dataAtualizacao );

        return checklistResponse;
    }

    @Override
    public void updateEntityFromRequest(ChecklistRequest request, Checklist entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setDsChecklist( request.dsChecklist() );
        entity.setUrlImagem( request.urlImagem() );
    }
}
