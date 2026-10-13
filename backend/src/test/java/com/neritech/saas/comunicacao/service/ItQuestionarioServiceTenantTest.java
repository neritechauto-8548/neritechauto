package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.mapper.ItQuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ItQuestionarioServiceTenantTest {
    private final ItQuestionarioRepository repo = mock(ItQuestionarioRepository.class);
    private final QuestionarioRepository questionarios = mock(QuestionarioRepository.class);
    private final ItQuestionarioService service = new ItQuestionarioService(
            repo, questionarios, mock(ItQuestionarioMapper.class),
            mock(com.neritech.saas.comunicacao.repository.PesquisaRespostaRepository.class));

    @AfterEach void clearContext() { TenantContext.clear(); }

    @Test void requiresTenantForGlobalList() {
        assertThatThrownBy(() -> service.findAll(Pageable.unpaged()))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }

    @Test void deniesCrossTenantQuestionnaireItems() {
        TenantContext.setCurrentTenant(7L);
        Questionario questionario = new Questionario();
        questionario.setEmpresaId(12L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(questionario));
        assertThatThrownBy(() -> service.findByQuestionarioId(5L))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }
}
