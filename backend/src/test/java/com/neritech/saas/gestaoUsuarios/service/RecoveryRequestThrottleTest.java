package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.RecoveryRequestAttempt;
import com.neritech.saas.gestaoUsuarios.repository.RecoveryRequestAttemptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecoveryRequestThrottleTest {
    @Mock RecoveryRequestAttemptRepository repository;

    @Test
    void identifiersAreHashedAndUnknownAccountsAreRateLimitedEqually() throws Exception {
        Method m = RecoveryRequestThrottle.class.getMethod("checkAndRecord", String.class, String.class);
        assertEquals(Propagation.REQUIRES_NEW, m.getAnnotation(Transactional.class).propagation());

        RecoveryRequestThrottle service = new RecoveryRequestThrottle(repository);
        assertTrue(service.checkAndRecord("  Usuario@EXEMPLO.COM ", "192.0.2.50"));
        ArgumentCaptor<RecoveryRequestAttempt> captor = ArgumentCaptor.forClass(RecoveryRequestAttempt.class);
        verify(repository).saveAndFlush(captor.capture());
        RecoveryRequestAttempt attempt = captor.getValue();
        assertEquals(64, attempt.getEmailHash().length());
        assertEquals(64, attempt.getIpHash().length());
        assertFalse(attempt.getEmailHash().contains("@"));
        assertFalse(attempt.getIpHash().contains("192.0.2.50"));
    }

    @Test
    void perEmailThrottleReturnsGenericResultInsteadOfAccountSpecific429() {
        when(repository.countByEmailHashAndCreatedAtAfter(anyString(), any(LocalDateTime.class))).thenReturn(3L);
        RecoveryRequestThrottle service = new RecoveryRequestThrottle(repository);
        assertFalse(service.checkAndRecord("unknown@exemplo.com", "192.0.2.51"));
        verify(repository).saveAndFlush(any(RecoveryRequestAttempt.class));
    }

    @Test
    void ipLimitReturns429ForAnyAddressWithoutCheckingAccount() {
        when(repository.countByIpHashAndCreatedAtAfter(anyString(), any(LocalDateTime.class))).thenReturn(12L);
        RecoveryRequestThrottle service = new RecoveryRequestThrottle(repository);
        for (String email : new String[] { "known@exemplo.com", "unknown@exemplo.com" }) {
            ResponseStatusException error = assertThrows(ResponseStatusException.class,
                    () -> service.checkAndRecord(email, "192.0.2.55"));
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
        }
        verify(repository, never()).saveAndFlush(any());
        verify(repository, never()).countByEmailHashAndCreatedAtAfter(anyString(), any());
    }
}
