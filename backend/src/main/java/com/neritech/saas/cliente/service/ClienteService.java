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
    // Mantidos temporariamente no construtor para compatibilidade estrutural durante o rebuild.
    @SuppressWarnings("unused")
    private final ContatoClienteRepository contatoRepository;
    @SuppressWarnings("unused")
    private final EnderecoClienteRepository enderecoRepository;
    @SuppressWarnings("unused")
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
    public Cliente create(Cliente cliente) {
        validateIdentity(cliente);
        validateDocumento(cliente);
        // Lifecycle é autoridade do backend: criação operacional sempre inicia ativa.
        cliente.setStatus(StatusCliente.ATIVO);
        return repository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        Cliente current = findById(id);
        cliente.setId(current.getId());
        cliente.setEmpresaId(current.getEmpresaId());
        cliente.setStatus(current.getStatus());
        validateIdentity(cliente);
        validateDocumento(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, ClienteRequest request) {
        Cliente current = findById(id);
        StatusCliente lifecycleStatus = current.getStatus();
        ClienteMapper.updateEntity(current, request);
        current.setStatus(lifecycleStatus);
        validateIdentity(current);
        validateDocumento(current);
        return repository.save(current);
    }

    private void validateIdentity(Cliente cliente) {
        if (cliente == null || cliente.getTipoCliente() == null) {
            throw new BusinessException("Informe o tipo do cliente.");
        }

        if (cliente.getTipoCliente() == TipoCliente.PESSOA_FISICA) {
            if (cliente.getNomeCompleto() == null || cliente.getNomeCompleto().isBlank()) {
                throw new BusinessException("Informe o nome completo do cliente.");
            }
            return;
        }

        if (cliente.getTipoCliente() == TipoCliente.PESSOA_JURIDICA
                && (cliente.getRazaoSocial() == null || cliente.getRazaoSocial().isBlank())) {
            throw new BusinessException("Informe a razão social do cliente.");
        }
    }

    private void validateDocumento(Cliente cliente) {
        if (cliente.getTipoCliente() == TipoCliente.PESSOA_FISICA) {
            if (cliente.getCpf() != null && !cliente.getCpf().isBlank()) {
                if (!DocumentoValidator.isValidCpf(cliente.getCpf())) {
                    throw new BusinessException("O CPF informado é inválido.");
                }
                repository.findByCpf(cliente.getCpf()).ifPresent(existing -> {
                    if (!existing.getId().equals(cliente.getId())) {
                        throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
                    }
                });
            }
        } else if (cliente.getTipoCliente() == TipoCliente.PESSOA_JURIDICA) {
            if (cliente.getCnpj() != null && !cliente.getCnpj().isBlank()) {
                if (!DocumentoValidator.isValidCnpj(cliente.getCnpj())) {
                    throw new BusinessException("O CNPJ informado é inválido.");
                }
                repository.findByCnpj(cliente.getCnpj()).ifPresent(existing -> {
                    if (!existing.getId().equals(cliente.getId())) {
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
