package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.mapper.ComunicacaoEnviadaMapper;
import com.neritech.saas.comunicacao.repository.ComunicacaoEnviadaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ComunicacaoEnviadaServiceTenantTest {
    private final ComunicacaoEnviadaRepository repo = mock(ComunicacaoEnviadaRepository.class);
    private final ComunicacaoEnviadaService service = new ComunicacaoEnviadaService(
            repo, mock(ComunicacaoEnviadaMapper.class), mock(EmailSenderService.class));

    @AfterEach void clearContext() { TenantContext.clear(); }

    @Test void deniesListingOtherEmpresaBeforeDatabaseQuery() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.findAll(12L, Pageable.unpaged()))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }

    @Test void deniesReadingOtherEmpresaEntity() {
        TenantContext.setCurrentTenant(7L);
        ComunicacaoEnviada entity = new ComunicacaoEnviada();
        entity.setEmpresaId(12L);
        when(repo.findById(5L)).thenReturn(Optional.of(entity));
        assertThatThrownBy(() -> service.findById(5L))
                .isInstanceOf(ResponseStatusException.class);
    }
}
