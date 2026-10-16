package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.repository.DocumentoClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentoClienteServiceTest {
    private final DocumentoClienteRepository docs = mock(DocumentoClienteRepository.class);
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final DocumentoClienteService service = new DocumentoClienteService(docs, clientes);

    @AfterEach void clearTenant() { TenantContext.clear(); }

    @Test void listMustHaveAuthenticatedTenant() {
        assertThatThrownBy(() -> service.list(10L, PageRequest.of(0, 10)))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(clientes, docs);
    }

    @Test void listUsesAuthenticatedTenantAsDatabaseFilter() {
        TenantContext.setCurrentTenant(7L);
        Cliente client = mock(Cliente.class);
        when(clientes.findByIdScoped(10L)).thenReturn(Optional.of(client));
        var page = PageRequest.of(0, 10);
        when(docs.findScoped(10L, 7L, page)).thenReturn(Page.empty(page));
        assertThat(service.list(10L, page).getContent()).isEmpty();
        verify(docs).findScoped(10L, 7L, page);
    }

    @Test void uploadRejectsSpoofedContentType() {
        TenantContext.setCurrentTenant(7L);
        when(clientes.findByIdScoped(10L)).thenReturn(Optional.of(mock(Cliente.class)));
        var invalid = new MockMultipartFile("file", "fraude.pdf", "application/pdf",
                "conteudo-nao-pdf".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        assertThatThrownBy(() -> service.upload(10L, invalid, "CPF", null))
                .isInstanceOf(BusinessException.class);
        verifyNoInteractions(docs);
    }

    @Test void uploadRejectsOversizedFile() {
        TenantContext.setCurrentTenant(7L);
        when(clientes.findByIdScoped(10L)).thenReturn(Optional.of(mock(Cliente.class)));
        byte[] bytes = new byte[5 * 1024 * 1024 + 1];
        var oversized = new MockMultipartFile("file", "grande.pdf", "application/pdf", bytes);
        assertThatThrownBy(() -> service.upload(10L, oversized, "CPF", null))
                .isInstanceOf(BusinessException.class);
        verifyNoInteractions(docs);
    }
}
