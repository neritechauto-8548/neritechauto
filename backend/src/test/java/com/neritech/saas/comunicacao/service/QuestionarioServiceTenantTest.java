package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class QuestionarioServiceTenantTest {
    private final QuestionarioRepository repo = mock(QuestionarioRepository.class);
    private final QuestionarioService service = new QuestionarioService(
            repo, mock(ItQuestionarioRepository.class), mock(QuestionarioMapper.class), mock(com.neritech.saas.comunicacao.repository.PesquisaRespostaRepository.class));

    @AfterEach void clearContext() { TenantContext.clear(); }

    @Test void deniesCrossTenantList() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.findAll(12L, Pageable.unpaged()))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }

    @Test void deniesCrossTenantRead() {
        TenantContext.setCurrentTenant(7L);
        Questionario q = new Questionario();
        q.setEmpresaId(12L);
        when(repo.findById(2L)).thenReturn(Optional.of(q));
        assertThatThrownBy(() -> service.findById(2L))
                .isInstanceOf(ResponseStatusException.class);
    }
}
