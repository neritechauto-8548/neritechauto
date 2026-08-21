package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.mapper.ClienteMapper;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.repository.ContatoClienteRepository;
import com.neritech.saas.cliente.repository.EnderecoClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.util.DocumentoValidator;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ContatoClienteRepository contatoRepository;
    private final EnderecoClienteRepository enderecoRepository;
    private final VeiculoRepository veiculoRepository;

    public ClienteService(ClienteRepository repository,
                          ContatoClienteRepository contatoRepository,
                          EnderecoClienteRepository enderecoRepository,
                          VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.contatoRepository = contatoRepository;
        this.enderecoRepository = enderecoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return repository.findByIdScoped(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", id));
    }

    @Transactional
    public Cliente create(Cliente c) {
        validateDocumento(c);
        return repository.save(c);
    }

    @Transactional
    public Cliente update(Long id, Cliente c) {
        validateDocumento(c);
        Cliente current = findById(id);
        c.setId(current.getId());
        c.setEmpresaId(current.getEmpresaId());
        return repository.save(c);
    }

    @Transactional
    public Cliente update(Long id, ClienteRequest request) {
        Cliente current = findById(id);
        ClienteMapper.updateEntity(current, request);
        validateDocumento(current);
        return repository.save(current);
    }

    private void validateDocumento(Cliente c) {
        if (c.getTipoCliente() == TipoCliente.PESSOA_FISICA) {
            if (c.getCpf() != null && !c.getCpf().isBlank()) {
                if (!DocumentoValidator.isValidCpf(c.getCpf())) {
                    throw new BusinessException("O CPF informado é inválido.");
                }
                repository.findByCpf(c.getCpf()).ifPresent(existing -> {
                    if (!existing.getId().equals(c.getId())) {
                        throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
                    }
                });
            }
        } else if (c.getTipoCliente() == TipoCliente.PESSOA_JURIDICA) {
            if (c.getCnpj() != null && !c.getCnpj().isBlank()) {
                if (!DocumentoValidator.isValidCnpj(c.getCnpj())) {
                    throw new BusinessException("O CNPJ informado é inválido.");
                }
                repository.findByCnpj(c.getCnpj()).ifPresent(existing -> {
                    if (!existing.getId().equals(c.getId())) {
                        throw new BusinessException("Já existe um cliente cadastrado com este CNPJ.");
                    }
                });
            }
        }
    }

    /**
     * Compatibilidade com o endpoint DELETE legado. A operação é lógica: preserva
     * cliente, veículos, contatos, endereços e histórico relacionado.
     */
    @Transactional
    public void delete(Long id) {
        deactivate(id);
    }

    @Transactional
    public Cliente deactivate(Long id) {
        Cliente cliente = findById(id);
        cliente.setStatus(StatusCliente.INATIVO);
        return repository.save(cliente);
    }

    @Transactional
    public Cliente reactivate(Long id) {
        Cliente cliente = findById(id);
        cliente.setStatus(StatusCliente.ATIVO);
        return repository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public Page<Cliente> search(String nomeCompleto,
            String razaoSocial,
            String cpf,
            String cnpj,
            TipoCliente tipoCliente,
            StatusCliente status,
            Pageable pageable) {

        org.springframework.data.jpa.domain.Specification<Cliente> spec =
            com.neritech.saas.cliente.repository.ClienteSpecification.buildSpecification(
                nomeCompleto, razaoSocial, cpf, cnpj, tipoCliente, status);

        return repository.findAll(spec, pageable);
    }
}
