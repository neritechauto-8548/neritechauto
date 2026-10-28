package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.QuestionarioRequest;
import com.neritech.saas.comunicacao.mapper.ItQuestionarioMapper;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.*;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class QuestionarioRespostasImutaveisTest {
    private final QuestionarioRepository qRepo = mock(QuestionarioRepository.class);
    private final ItQuestionarioRepository itens = mock(ItQuestionarioRepository.class);
    private final PesquisaRespostaRepository respostas = mock(PesquisaRespostaRepository.class);
    private final QuestionarioMapper qMapper = mock(QuestionarioMapper.class);
    private final ItQuestionarioMapper itMapper = mock(ItQuestionarioMapper.class);
    private final QuestionarioService questionarios = new QuestionarioService(qRepo, itens, qMapper, respostas);
    private final ItQuestionarioService perguntas = new ItQuestionarioService(itens, qRepo, itMapper, respostas);

    @AfterEach void clear() { TenantContext.clear(); }

    private Questionario answered() {
        TenantContext.setCurrentTenant(7L);
        Questionario q = new Questionario();
        q.setId(5L);
        q.setEmpresaId(7L);
        q.setDsQuestionario("Qualidade");
        when(qRepo.findById(5L)).thenReturn(Optional.of(q));
        when(respostas.existsByQuestionarioIdAndEmpresaId(5L, 7L)).thenReturn(true);
        return q;
    }

    @Test void naoPodeRenomearQuestionarioComRespostas() {
        answered();
        assertThatThrownBy(() -> questionarios.update(5L,
            new QuestionarioRequest(7L, "Outro título", null, true)))
            .isInstanceOf(BusinessException.class).hasMessageContaining("possui respostas");
        verifyNoInteractions(qMapper);
    }

    @Test void naoPodeExcluirQuestionarioComRespostas() {
        answered();
        assertThatThrownBy(() -> questionarios.delete(5L))
            .isInstanceOf(BusinessException.class).hasMessageContaining("respostas");
        verify(itens, never()).deleteByQuestionarioId(anyLong());
    }

    @Test void naoPodeAdicionarPerguntaAQuestionarioComRespostas() {
        answered();
        assertThatThrownBy(() -> perguntas.create(new ItQuestionarioRequest(
            5L, "Outra pergunta", TipoItemQuestionario.AV)))
            .isInstanceOf(BusinessException.class).hasMessageContaining("protegidas");
        verify(itens, never()).save(any());
    }

    @Test void naoPodeExcluirPerguntaDeQuestionarioComRespostas() {
        Questionario q = answered();
        ItQuestionario item = new ItQuestionario();
        item.setId(9L);
        item.setQuestionario(q);
        when(itens.findById(9L)).thenReturn(Optional.of(item));
        assertThatThrownBy(() -> perguntas.delete(9L))
            .isInstanceOf(BusinessException.class).hasMessageContaining("protegidas");
        verify(itens, never()).delete(any(ItQuestionario.class));
    }
}
