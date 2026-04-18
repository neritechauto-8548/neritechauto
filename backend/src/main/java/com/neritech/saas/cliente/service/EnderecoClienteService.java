package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.EnderecoCliente;
import com.neritech.saas.cliente.dto.EnderecoClienteRequest;
import com.neritech.saas.cliente.mapper.EnderecoClienteMapper;
import com.neritech.saas.cliente.repository.EnderecoClienteRepository;
import com.neritech.saas.cliente.service.ClienteService;
import com.neritech.saas.common.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoClienteService {

    private final EnderecoClienteRepository repository;
    private final ClienteService clienteService;

    public EnderecoClienteService(EnderecoClienteRepository repository, ClienteService clienteService) {
        this.repository = repository;
        this.clienteService = clienteService;
    }

    @Transactional(readOnly = true)
    public Page<EnderecoCliente> listByCliente(Long clienteId, Pageable pageable) {
        // Garante que o cliente existe no tenant atual
        clienteService.findById(clienteId);
        return repository.findByClienteId(clienteId, pageable);
    }

    @Transactional(readOnly = true)
    public EnderecoCliente findById(Long id) {
        return repository.findByIdScoped(id)
                .orElseThrow(() -> new ResourceNotFoundException("EnderecoCliente", "ID", id));
    }

    @Transactional
    public EnderecoCliente create(Long clienteId, EnderecoClienteRequest request) {
        Cliente cliente = clienteService.findById(clienteId);
        EnderecoCliente e = EnderecoClienteMapper.toEntity(request);
        e.setCliente(cliente);

        return repository.save(e);
    }

    @Transactional
    public EnderecoCliente update(Long clienteId, Long id, EnderecoClienteRequest request) {
        // Garante que o cliente existe no tenant
        clienteService.findById(clienteId);
        EnderecoCliente current = findById(id);
        if (current.getCliente() == null || !current.getCliente().getId().equals(clienteId)) {
            throw new ResourceNotFoundException("EnderecoCliente", "ID", id);
        }

        EnderecoClienteMapper.updateEntity(current, request);

        return repository.save(current);
    }

    @Transactional
    public void delete(Long clienteId, Long id) {
        // Garante que o cliente existe no tenant
        clienteService.findById(clienteId);
        EnderecoCliente current = findById(id);
        if (current.getCliente() == null || !current.getCliente().getId().equals(clienteId)) {
            throw new ResourceNotFoundException("EnderecoCliente", "ID", id);
        }
        int affected = repository.deleteByIdScoped(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("EnderecoCliente", "ID", id);
        }
    }
}
