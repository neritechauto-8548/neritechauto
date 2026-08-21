package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.repository.ContatoClienteRepository;
import com.neritech.saas.cliente.repository.EnderecoClienteRepository;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceLifecycleTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ContatoClienteRepository contatoRepository;

    @Mock
    private EnderecoClienteRepository enderecoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private ClienteService service;

    @Test
    void legacyDeleteShouldDeactivateAndPreserveRelatedHistory() {
        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        when(repository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));
        when(repository.save(cliente)).thenReturn(cliente);

        service.delete(10L);

        verify(cliente).setStatus(StatusCliente.INATIVO);
        verify(repository).save(cliente);
        verify(repository, never()).deleteByIdScoped(anyLong());
        verifyNoInteractions(contatoRepository, enderecoRepository, veiculoRepository);
    }

    @Test
    void shouldReactivateScopedCustomerWithoutRecreatingIt() {
        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        when(repository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));
        when(repository.save(cliente)).thenReturn(cliente);

        Cliente result = service.reactivate(10L);

        assertSame(cliente, result);
        verify(cliente).setStatus(StatusCliente.ATIVO);
        verify(repository).save(cliente);
        verify(repository, never()).deleteByIdScoped(anyLong());
        verifyNoInteractions(contatoRepository, enderecoRepository, veiculoRepository);
    }
}
