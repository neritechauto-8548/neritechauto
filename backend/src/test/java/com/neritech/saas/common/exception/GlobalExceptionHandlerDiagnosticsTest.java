package com.neritech.saas.common.exception;

import com.neritech.saas.common.dto.ApiErrorResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerDiagnosticsTest {

    private GlobalExceptionHandler handler(boolean enabled, String remoteAddr) {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ReflectionTestUtils.setField(handler, "exposeExceptions", enabled);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddr);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        return handler;
    }

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void modoLocalExibeClasseECausaDaExcecaoReal() {
        var exception = new IllegalStateException("Erro ao confirmar gravação",
                new IllegalArgumentException("Chave estrangeira inválida"));
        var result = handler(true, "127.0.0.1").handleGeneric(exception);
        ApiErrorResponse body = result.getBody();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body).isNotNull();
        assertThat(body.exception()).isEqualTo("java.lang.IllegalStateException");
        assertThat(body.cause()).isEqualTo("Chave estrangeira inválida");
        assertThat(body.message()).isEqualTo("Erro interno do servidor. Tente novamente mais tarde.");
    }

    @Test
    void servidorRemotoNaoRecebeStackMesmoSeHouverFlagLigada() {
        var exception = new IllegalStateException("falha secreta", new RuntimeException("detalhe interno"));
        var result = handler(true, "203.0.113.10").handleGeneric(exception);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().exception()).isNull();
        assertThat(result.getBody().cause()).isNull();
    }

    @Test
    void configuracaoDesligadaProtegeMesmoEmLocalhost() {
        var result = handler(false, "127.0.0.1")
                .handleGeneric(new IllegalStateException("dados internos"));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().exception()).isNull();
        assertThat(result.getBody().cause()).isNull();
    }

    @Test
    void erroDeNegocioTambemIdentificaExcecaoNoDiagnosticoLocal() {
        var result = handler(true, "::1").handleBusinessException(
                new BusinessException("Regra de cadastro inválida"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().exception()).isEqualTo(BusinessException.class.getName());
        assertThat(result.getBody().cause()).isEqualTo("Regra de cadastro inválida");
    }
}
