package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import com.neritech.saas.comunicacao.mapper.ComunicacaoEnviadaMapper;
import com.neritech.saas.comunicacao.repository.ComunicacaoEnviadaRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ComunicacaoFiltrosTenantTest {
    private final ComunicacaoEnviadaRepository repo = mock(ComunicacaoEnviadaRepository.class);
    private final ComunicacaoEnviadaService service = new ComunicacaoEnviadaService(
        repo, mock(ComunicacaoEnviadaMapper.class), mock(EmailSenderService.class));

    @AfterEach void resetTenant() { TenantContext.clear(); }

    @Test void rejeitaFiltrosDeOutraEmpresa() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.findAll(8L, PageRequest.of(0, 10), 12L,
            TipoComunicacao.EMAIL, StatusComunicacao.FALHOU, null, null))
            .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }

    @Test void datasInvertidasNaoAcessamBanco() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.findAll(7L, PageRequest.of(0, 10), null,
            null, null, LocalDate.of(2026, 10, 30), LocalDate.of(2026, 10, 1)))
            .isInstanceOf(BusinessException.class);
        verifyNoInteractions(repo);
    }

    @Test void consultaValidaAplicaFiltroEspecificoNoRepositorio() {
        TenantContext.setCurrentTenant(7L);
        var page = PageRequest.of(0, 10);
        when(repo.findAll(any(Specification.class), eq(page))).thenReturn(Page.empty(page));
        assertThat(service.findAll(7L, page, 12L, TipoComunicacao.EMAIL,
            StatusComunicacao.ENVIADA, null, null).getContent()).isEmpty();
        verify(repo).findAll(any(Specification.class), eq(page));
    }
}
