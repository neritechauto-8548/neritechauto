package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.dto.ContatoClienteRequest;
import com.neritech.saas.cliente.mapper.ContatoClienteMapper;
import com.neritech.saas.cliente.repository.ContatoClienteRepository;
import com.neritech.saas.cliente.service.ClienteService;
import com.neritech.saas.common.tenancy.TenantContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContatoClienteService {

    private final ContatoClienteRepository repository;
    private final ClienteService clienteService;

    public ContatoClienteService(ContatoClienteRepository repository, ClienteService clienteService) {
        this.repository = repository;
        this.clienteService = clienteService;
    }

    @Transactional(readOnly = true)
    public Page<ContatoCliente> listarPorCliente(Long clienteId, Pageable pageable) {
        // Garante que o cliente existe no tenant atual
        clienteService.findById(clienteId);
        return repository.findByClienteId(clienteId, pageable);
    }

    @Transactional(readOnly = true)
    public ContatoCliente buscarPorId(Long id) {
        return repository.findByIdScoped(id)
                .orElseThrow(() -> new IllegalArgumentException("Contato nÃ£o encontrado"));
    }

    @Transactional
    public ContatoCliente criar(Long clienteId, ContatoClienteRequest request) {
        Cliente cliente = clienteService.findById(clienteId);
        ContatoCliente c = ContatoClienteMapper.toEntity(request);
        c.setCliente(cliente);

        if (Boolean.TRUE.equals(c.getPrincipal())) {
            repository.clearPrincipal(clienteId);
        }
        return repository.save(c);
    }

    @Transactional
    public ContatoCliente atualizar(Long id, ContatoClienteRequest request) {
        ContatoCliente existente = buscarPorId(id);
        ContatoClienteMapper.updateEntity(existente, request);

        if (Boolean.TRUE.equals(existente.getPrincipal())) {
            repository.clearPrincipal(existente.getCliente().getId());
        }
        return repository.save(existente);
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsByIdScoped(id)) {
            throw new IllegalArgumentException("Contato nÃ£o encontrado");
        }
        repository.deleteByIdScoped(id);
    }
}
