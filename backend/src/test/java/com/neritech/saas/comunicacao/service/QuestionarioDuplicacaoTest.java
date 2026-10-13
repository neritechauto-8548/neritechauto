package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import com.neritech.saas.comunicacao.dto.QuestionarioDuplicacaoRequest;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuestionarioDuplicacaoTest {
    private final QuestionarioRepository questions = mock(QuestionarioRepository.class);
    private final ItQuestionarioRepository itens = mock(ItQuestionarioRepository.class);
    private final QuestionarioService service = new QuestionarioService(questions, itens,
        mock(QuestionarioMapper.class), mock(PesquisaRespostaRepository.class));

    @AfterEach void cleanup() { TenantContext.clear(); }

    private Questionario origem(Long empresaId) {
        TenantContext.setCurrentTenant(7L);
        Questionario q = new Questionario();
        q.setId(10L); q.setEmpresaId(empresaId); q.setDsQuestionario("Original");
        q.setStatusPesquisa(StatusPesquisa.PUBLISHED);
        when(questions.findById(10L)).thenReturn(Optional.of(q));
        return q;
    }

    @Test void duplicaPerguntasMasNaoAlteraAPesquisaOriginal() {
        Questionario original = origem(7L);
        ItQuestionario it = new ItQuestionario();
        it.setDsItQuestionario("Avalie");
        it.setTpItQuestionario(TipoItemQuestionario.AV);
        when(itens.findByQuestionarioId(10L)).thenReturn(List.of(it));
        when(questions.save(any(Questionario.class))).thenAnswer(inv -> inv.getArgument(0));
        service.duplicar(10L, new QuestionarioDuplicacaoRequest("Nova versão"));
        var novoCaptor = org.mockito.ArgumentCaptor.forClass(Questionario.class);
        verify(questions).save(novoCaptor.capture());
        Questionario novo = novoCaptor.getValue();
        assertThat(novo).isNotSameAs(original);
        assertThat(novo.getEmpresaId()).isEqualTo(7L);
        assertThat(novo.getStatusPesquisa()).isEqualTo(StatusPesquisa.DRAFT);
        assertThat(novo.getDsQuestionario()).isEqualTo("Nova versão");
        verify(itens).saveAll(argThat((Iterable<ItQuestionario> list) -> {
            var iterator = list.iterator();
            if (!iterator.hasNext()) return false;
            ItQuestionario answer = iterator.next();
            return !iterator.hasNext() && answer.getQuestionario() == novo
                && answer.getTpItQuestionario() == TipoItemQuestionario.AV;
        }));
        assertThat(original.getStatusPesquisa()).isEqualTo(StatusPesquisa.PUBLISHED);
    }

    @Test void naoDuplicaPesquisaDeOutraEmpresa() {
        origem(8L);
        assertThatThrownBy(() -> service.duplicar(10L, new QuestionarioDuplicacaoRequest("Invasão")))
                .isInstanceOf(ResponseStatusException.class);
        verify(questions, never()).save(any(Questionario.class));
    }

    @Test void naoDuplicaTituloJaUtilizado() {
        origem(7L);
        when(questions.existsByEmpresaIdAndDsQuestionarioIgnoreCase(7L, "Existente"))
            .thenReturn(true);
        assertThatThrownBy(() -> service.duplicar(10L, new QuestionarioDuplicacaoRequest("Existente")))
                .isInstanceOf(BusinessException.class);
        verify(questions, never()).save(any(Questionario.class));
    }
}
