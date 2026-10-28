package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import com.neritech.saas.comunicacao.mapper.NotificacaoSistemaMapper;
import com.neritech.saas.comunicacao.repository.NotificacaoSistemaRepository;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoSistemaServiceTest {
    @Mock NotificacaoSistemaRepository repository;
    @Mock NotificacaoSistemaMapper mapper;
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks NotificacaoSistemaService service;

    private final Long tenantId = 11L;
    private final Long userId = 22L;

    @BeforeEach
    void setup() {
        TenantContext.setCurrentTenant(tenantId);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuario@oficina.test", null, List.of()));
    }

    @AfterEach
    void cleanup() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    private void givenCurrentUser() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(userId);
        when(usuarioRepository.findByEmailIgnoreCaseAndEmpresaId("usuario@oficina.test", tenantId))
                .thenReturn(Optional.of(usuario));
    }

    @Test
    void onlyReturnsAuthenticatedRecipientWithinTenant() {
        givenCurrentUser();
        when(repository.findByEmpresaIdAndUsuarioDestinatarioId(eq(tenantId), eq(userId), any()))
                .thenReturn(new PageImpl<>(List.of(new NotificacaoSistema())));
        var result = service.findMine(PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findByEmpresaIdAndUsuarioDestinatarioId(eq(tenantId), eq(userId), any());
    }

    @Test
    void marksOnlyOwnedNotificationAsReadAndPersists() {
        givenCurrentUser();
        NotificacaoSistema notification = new NotificacaoSistema();
        notification.setLida(false);
        when(repository.findByIdAndEmpresaIdAndUsuarioDestinatarioId(33L, tenantId, userId))
                .thenReturn(Optional.of(notification));
        when(repository.save(notification)).thenReturn(notification);

        service.markMineAsRead(33L);

        assertThat(notification.getLida()).isTrue();
        assertThat(notification.getDataLeitura()).isNotNull();
        verify(repository).save(notification);
    }

    @Test
    void rejectsOtherRecipientsWithoutUpdating() {
        givenCurrentUser();
        when(repository.findByIdAndEmpresaIdAndUsuarioDestinatarioId(33L, tenantId, userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markMineAsRead(33L))
                .isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).save(any(NotificacaoSistema.class));
    }

    @Test
    void refusesCrossTenantAdministrativeReads() {
        assertThatThrownBy(() -> service.findAll(99L, PageRequest.of(0, 10)))
                .isInstanceOf(org.springframework.web.server.ResponseStatusException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void rejectsMissingTenant() {
        TenantContext.clear();
        assertThatThrownBy(() -> service.findMine(PageRequest.of(0, 10)))
                .isInstanceOf(org.springframework.web.server.ResponseStatusException.class);
        verifyNoInteractions(repository);
    }
}
