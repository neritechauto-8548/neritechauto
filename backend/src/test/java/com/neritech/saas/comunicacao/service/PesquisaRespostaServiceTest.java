package com.neritech.saas.comunicacao.service;
import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.*;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import com.neritech.saas.comunicacao.dto.PesquisaRespostaRequest;
import com.neritech.saas.comunicacao.repository.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
class PesquisaRespostaServiceTest {
    private final QuestionarioRepository questionarios = mock(QuestionarioRepository.class);
    private final ItQuestionarioRepository perguntas = mock(ItQuestionarioRepository.class);
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final PesquisaRespostaRepository repo = mock(PesquisaRespostaRepository.class);
    private final PesquisaRespostaService service = new PesquisaRespostaService(questionarios, perguntas, clientes, repo);
    @AfterEach void clearTenant() { TenantContext.clear(); }
    private Questionario q(Long empresaId) {
        Questionario q = new Questionario();
        q.setId(5L); q.setEmpresaId(empresaId); q.setSnAtivo(true);
        return q;
    }
    private PesquisaRespostaRequest request(String valor) {
        return new PesquisaRespostaRequest(3L, UUID.randomUUID().toString(),
            List.of(new PesquisaRespostaRequest.Item(21L, valor)));
    }
    private ItQuestionario question(TipoItemQuestionario tipo) {
        ItQuestionario item = new ItQuestionario();
        item.setId(21L); item.setDsItQuestionario("Avalie o atendimento"); item.setTpItQuestionario(tipo);
        return item;
    }
    @Test void deniesWhenNoAuthenticatedCompany() {
        assertThatThrownBy(() -> service.resumo(5L)).isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(questionarios, repo);
    }
    @Test void rejectsOtherCompanyBeforeReadingResults() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(8L)));
        assertThatThrownBy(() -> service.listar(5L, PageRequest.of(0, 10)))
            .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(repo);
    }
    @Test void rejectsUnscopedClient() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(7L)));
        assertThatThrownBy(() -> service.registrar(5L, request("10")))
            .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
        verify(clientes).findByIdScoped(3L);
        verify(repo, never()).saveAndFlush(any());
    }
    @Test void requiresEveryQuestion() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(7L)));
        when(clientes.findByIdScoped(3L)).thenReturn(Optional.of(mock(Cliente.class)));
        when(perguntas.findByQuestionarioId(5L)).thenReturn(List.of(question(TipoItemQuestionario.AV),
            question(TipoItemQuestionario.AB)));
        assertThatThrownBy(() -> service.registrar(5L, request("9")))
            .isInstanceOf(BusinessException.class);
        verify(repo, never()).saveAndFlush(any());
    }
    @Test void invalidScoreNeverPersists() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(7L)));
        when(clientes.findByIdScoped(3L)).thenReturn(Optional.of(mock(Cliente.class)));
        when(perguntas.findByQuestionarioId(5L)).thenReturn(List.of(question(TipoItemQuestionario.AV)));
        assertThatThrownBy(() -> service.registrar(5L, request("11")))
            .isInstanceOf(BusinessException.class).hasMessageContaining("1 e 10");
        verify(repo, never()).saveAndFlush(any());
    }
    @Test void validRatingIsPersistedAgainstAuthenticatedClient() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(7L)));
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(3L);
        when(clientes.findByIdScoped(3L)).thenReturn(Optional.of(cliente));
        when(perguntas.findByQuestionarioId(5L)).thenReturn(List.of(question(TipoItemQuestionario.AV)));
        when(repo.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        var result = service.registrar(5L, request("8"));
        assertThat(result.respostas()).hasSize(1);
        assertThat(result.respostas().get(0).valor()).isEqualTo("8");
        verify(repo).saveAndFlush(argThat(x -> x.getEmpresaId().equals(7L) && x.getCliente() == cliente));
    }
    @Test void countsOnlyAuthenticatedCompany() {
        TenantContext.setCurrentTenant(7L);
        when(questionarios.findById(5L)).thenReturn(Optional.of(q(7L)));
        when(repo.countByQuestionarioIdAndEmpresaId(5L, 7L)).thenReturn(12L);
        assertThat(service.resumo(5L).totalRespostas()).isEqualTo(12L);
        verify(repo).countByQuestionarioIdAndEmpresaId(5L, 7L);
    }
}
