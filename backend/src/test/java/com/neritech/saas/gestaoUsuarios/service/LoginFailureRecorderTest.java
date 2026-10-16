package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginFailureRecorderTest {
    @Mock TentativaLoginRepository attempts;
    @Mock LogAcessoRepository logs;

    @Test
    void failedLoginUsesIndependentTransactionAndCanonicalRateKey() throws Exception {
        Method m = LoginFailureRecorder.class.getMethod("record", LoginRequest.class, Long.class);
        assertEquals(Propagation.REQUIRES_NEW, m.getAnnotation(Transactional.class).propagation());
        LoginFailureRecorder service = new LoginFailureRecorder(attempts, logs);
        LoginRequest request = new LoginRequest();
        request.setEmail("  OficIna@Exemplo.COM ");
        request.setIpAddress("127.0.0.1");
        service.record(request, null);
        ArgumentCaptor<TentativaLogin> captured = ArgumentCaptor.forClass(TentativaLogin.class);
        verify(attempts).saveAndFlush(captured.capture());
        assertEquals("oficina@exemplo.com", captured.getValue().getEmail());
        assertFalse(captured.getValue().getSucesso());
        verifyNoInteractions(logs);
    }
}
