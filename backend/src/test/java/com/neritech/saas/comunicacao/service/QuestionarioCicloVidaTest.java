package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionarioCicloVidaTest {
    private final QuestionarioRepository repo = mock(QuestionarioRepository.class);
    private final ItQuestionarioRepository itens = mock(ItQuestionarioRepository.class);
    private final QuestionarioMapper mapper = mock(QuestionarioMapper.class);
    private final PesquisaRespostaRepository respostas = mock(PesquisaRespostaRepository.class);
    private final QuestionarioService service = new QuestionarioService(repo, itens, mapper, respostas);
    @AfterEach void cleanup() { TenantContext.clear(); }

    private Questionario survey(StatusPesquisa status) {
        TenantContext.setCurrentTenant(7L);
        Questionario s = new Questionario();
        s.setId(5L);
        s.setEmpresaId(7L);
        s.setStatusPesquisa(status);
        when(repo.findById(5L)).thenReturn(Optional.of(s));
        when(repo.save(s)).thenReturn(s);
        return s;
    }

    @Test void semEmpresaNaoPodePublicar() {
        assertThatThrownBy(() -> service.publicar(5L)).isInstanceOf(ResponseStatusException.class);
        verify(repo, never()).save(any());
    }

    @Test void rascunhoSemPerguntaNaoPublica() {
        survey(StatusPesquisa.DRAFT);
        assertThatThrownBy(() -> service.publicar(5L)).isInstanceOf(BusinessException.class)
                .hasMessageContaining("pergunta");
        verify(repo, never()).save(any());
    }

    @Test void permitePublicarPausarRetomarEncerrar() {
        Questionario s = survey(StatusPesquisa.DRAFT);
        when(itens.findByQuestionarioId(5L)).thenReturn(List.of(new ItQuestionario()));
        service.publicar(5L);
        assertThat(s.getStatusPesquisa()).isEqualTo(StatusPesquisa.PUBLISHED);
        assertThat(s.getSnAtivo()).isTrue();
        service.pausar(5L);
        assertThat(s.getStatusPesquisa()).isEqualTo(StatusPesquisa.PAUSED);
        assertThat(s.getSnAtivo()).isFalse();
        service.retomar(5L);
        assertThat(s.getStatusPesquisa()).isEqualTo(StatusPesquisa.PUBLISHED);
        service.encerrar(5L);
        assertThat(s.getStatusPesquisa()).isEqualTo(StatusPesquisa.CLOSED);
        assertThat(s.getSnAtivo()).isFalse();
    }

    @Test void naoPermiteRetomarEncerradaNemExcluirPublicada() {
        survey(StatusPesquisa.CLOSED);
        assertThatThrownBy(() -> service.retomar(5L)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> service.publicar(5L)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> service.delete(5L)).isInstanceOf(BusinessException.class);
        verify(repo, never()).delete(any(Questionario.class));
    }

    @Test void naoPermiteEditarPesquisaPublicadaMesmoSemRespostas() {
        survey(StatusPesquisa.PUBLISHED);
        assertThatThrownBy(() -> service.update(5L,
                new com.neritech.saas.comunicacao.dto.QuestionarioRequest(7L, "Novo", null, true)))
                .isInstanceOf(BusinessException.class);
        verify(mapper, never()).updateEntityFromRequest(any(), any());
    }

    @Test void empresaDiferenteNaoPublica() {
        Questionario s = survey(StatusPesquisa.DRAFT);
        s.setEmpresaId(8L);
        assertThatThrownBy(() -> service.publicar(5L)).isInstanceOf(ResponseStatusException.class);
        verify(repo, never()).save(any());
    }
}
