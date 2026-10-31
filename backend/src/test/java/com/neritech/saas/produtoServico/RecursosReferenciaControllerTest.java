package com.neritech.saas.produtoServico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.service.DepartamentoContabioService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.produtoServico.controller.RecursosReferenciaController;
import com.neritech.saas.produtoServico.service.ServicoService;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Contratos de segurança e validação sem substituir os testes de integração com PostgreSQL.
 */
class RecursosReferenciaControllerTest {
    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final RecursosReferenciaController controller = new RecursosReferenciaController(
        jdbc, mock(ServicoService.class), mock(DepartamentoContabioService.class),
        mock(UsuarioRepository.class), new ObjectMapper());

    @AfterEach void limparTenant() { TenantContext.clear(); }

    @Test void listagemDeAcessoriosSemTenantNaoChegaAoBanco() {
        TenantContext.clear();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.listarAcessorios(null, null, null));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test void listagemDeAreasSemTenantNaoChegaAoBanco() {
        TenantContext.clear();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.listarAreas(null, null));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test void precosEmLoteNaoPodemExecutarSemTenant() {
        TenantContext.clear();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.precosEmLote(new RecursosReferenciaController.PrecosLote(
                "SOMAR_VALOR", new BigDecimal("10.00"), null)));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test void operacaoDeLoteDesconhecidaRejeitada() {
        TenantContext.setCurrentTenant(7L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.precosEmLote(new RecursosReferenciaController.PrecosLote(
                "MULTIPLICAR", BigDecimal.ONE, null)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test void nomeDeAcessorioVazioRejeitado() {
        TenantContext.setCurrentTenant(7L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.criarAcessorio(new RecursosReferenciaController.AcessorioInput(
                "   ", true, false, true)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
        verifyNoInteractions(jdbc);
    }

    @Test void nomeDeAcessorioDuplicadoRejeitado() {
        TenantContext.setCurrentTenant(7L);
        when(jdbc.queryForObject(anyString(), eq(Integer.class), eq(7L), eq("Estepe"), eq(-1L))).thenReturn(1);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> controller.criarAcessorio(new RecursosReferenciaController.AcessorioInput(
                "Estepe", true, false, true)));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(jdbc, never()).update(anyString(), any(Object[].class));
    }
}
