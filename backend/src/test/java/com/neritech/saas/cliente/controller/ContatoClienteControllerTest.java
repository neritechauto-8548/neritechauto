package com.neritech.saas.cliente.controller;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.service.ContatoClienteService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ContatoClienteControllerTest {
    @Test void naoPodeAtualizarContatoDeOutroClientePelaRota() {
        ContatoClienteService service = mock(ContatoClienteService.class);
        ContatoClienteController controller = new ContatoClienteController(service);
        Cliente clienteReal = mock(Cliente.class);
        when(clienteReal.getId()).thenReturn(22L);
        ContatoCliente contato = mock(ContatoCliente.class);
        when(contato.getCliente()).thenReturn(clienteReal);
        when(service.buscarPorId(55L)).thenReturn(contato);

        assertThatThrownBy(() -> controller.atualizar(11L, 55L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pertence");
        verify(service).buscarPorId(55L);
        verifyNoMoreInteractions(service);
    }
}
