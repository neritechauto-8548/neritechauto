package com.neritech.saas.cliente.controller;

import com.neritech.saas.cliente.service.ContatoClienteService;
import com.neritech.saas.cliente.service.EnderecoClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(CustomerPiiAuthorizationTest.TestConfig.class)
class CustomerPiiAuthorizationTest {

    @Autowired
    private ContatoClienteController contatoController;

    @Autowired
    private EnderecoClienteController enderecoController;

    @Autowired
    private ContatoClienteService contatoService;

    @Autowired
    private EnderecoClienteService enderecoService;

    @Test
    @WithMockUser(authorities = "GERAL_USUARIO")
    void commonReaderCanUseMinimizedCustomer360Resources() {
        when(contatoService.listarPorCliente(eq(42L), any(Pageable.class))).thenReturn(Page.empty());
        when(enderecoService.listByCliente(eq(42L), any(Pageable.class))).thenReturn(Page.empty());

        assertDoesNotThrow(() -> contatoController.listarResumo(42L, Pageable.unpaged()));
        assertDoesNotThrow(() -> enderecoController.listSummary(42L, Pageable.unpaged()));
    }

    @Test
    @WithMockUser(authorities = "GERAL_USUARIO")
    void commonReaderCannotReadFullContactOrAddressContracts() {
        assertThrows(
                AccessDeniedException.class,
                () -> contatoController.listar(42L, Pageable.unpaged()));
        assertThrows(
                AccessDeniedException.class,
                () -> enderecoController.list(42L, Pageable.unpaged()));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE_EDITAR")
    void editorCanReadFullContactAndAddressContracts() {
        when(contatoService.listarPorCliente(eq(42L), any(Pageable.class))).thenReturn(Page.empty());
        when(enderecoService.listByCliente(eq(42L), any(Pageable.class))).thenReturn(Page.empty());

        assertDoesNotThrow(() -> contatoController.listar(42L, Pageable.unpaged()));
        assertDoesNotThrow(() -> enderecoController.list(42L, Pageable.unpaged()));
    }

    @Configuration
    @EnableMethodSecurity
    static class TestConfig {

        @Bean
        ContatoClienteService contatoClienteService() {
            return mock(ContatoClienteService.class);
        }

        @Bean
        EnderecoClienteService enderecoClienteService() {
            return mock(EnderecoClienteService.class);
        }

        @Bean
        ContatoClienteController contatoClienteController(ContatoClienteService service) {
            return new ContatoClienteController(service);
        }

        @Bean
        EnderecoClienteController enderecoClienteController(EnderecoClienteService service) {
            return new EnderecoClienteController(service);
        }
    }
}
