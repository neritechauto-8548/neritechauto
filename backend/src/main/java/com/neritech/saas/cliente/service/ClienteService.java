package com.neritech.saas.cliente.service;

import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.mapper.ClienteMapper;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.repository.ContatoClienteRepository;
import com.neritech.saas.cliente.repository.EnderecoClienteRepository;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import com.neritech.saas.common.exception.BusinessException;

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

    /**
     * Busca um cliente pelo seu ID.
     *
     * @param id Identificador Ãºnico do cliente
     * @return A entidade Cliente encontrada
     * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
     */
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return repository.findByIdScoped(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", id));
    }

    /**
     * Cria um novo cliente.
     *
     * @param c Entidade Cliente a ser persistida
     * @return O cliente persistido com ID gerado
     * @throws BusinessException se houver violaÃ§Ã£o de regras de negÃ³cio (ex:
     *                           duplicidade)
     */
    @Transactional
    public Cliente create(Cliente c) {
        // ValidaÃ§Ãµes de negÃ³cio adicionais podem ser inseridas aqui
        return repository.save(c);
    }

    /**
     * Atualiza um cliente existente com base em uma entidade completa.
     * Preserva o ID e o Tenant (empresaId) originais.
     *
     * @param id Identificador do cliente a ser atualizado
     * @param c  Entidade com os novos dados
     * @return O cliente atualizado
     * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
     */
    @Transactional
    public Cliente update(Long id, Cliente c) {
        Cliente current = findById(id);
        // Garante que o id e o empresaId do registro nÃ£o sejam alterados
        c.setId(current.getId());
        c.setEmpresaId(current.getEmpresaId());
        return repository.save(c);
    }

    /**
     * Atualiza um cliente existente com base em um DTO de requisiÃ§Ã£o.
     * Aplica as alteraÃ§Ãµes na entidade recuperada do banco.
     *
     * @param id      Identificador do cliente a ser atualizado
     * @param request DTO com os dados a serem atualizados
     * @return O cliente atualizado
     * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
     */
    @Transactional
    public Cliente update(Long id, ClienteRequest request) {
        Cliente current = findById(id);
        // Aplica as alteraÃ§Ãµes do request na entidade carregada do banco
        ClienteMapper.updateEntity(current, request);
        // Garante que empresaId nÃ£o serÃ¡ modificado por payload
        current.setEmpresaId(current.getEmpresaId());
        return repository.save(current);
    }

    /**
     * Remove um cliente do sistema.
     * Realiza exclusÃ£o lÃ³gica ou fÃ­sica dependendo da implementaÃ§Ã£o do repositÃ³rio.
     *
     * @param id Identificador do cliente a ser removido
     * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
     */
    @Transactional
    public void delete(Long id) {
        // Verifica existÃªncia restrita ao tenant
        if (!repository.existsByIdScoped(id)) {
            throw new ResourceNotFoundException("Cliente", "ID", id);
        }

        // Remove filhos vinculados ao cliente no banco de dados para evitar FK constraint
        veiculoRepository.deleteByClienteIdScoped(id);
        contatoRepository.deleteByClienteIdScoped(id);
        enderecoRepository.deleteByClienteIdScoped(id);

        // Executa deleÃ§Ã£o restrita ao tenant e valida que ocorreu
        int affected = repository.deleteByIdScoped(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("Cliente", "ID", id);
        }
    }

    /**
     * Busca um cliente pelo CPF.
     *
     * @param cpf CPF a ser pesquisado
     * @return Optional contendo o cliente se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    /**
     * Busca um cliente pelo CNPJ.
     *
     * @param cnpj CNPJ a ser pesquisado
     * @return Optional contendo o cliente se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    /**
     * Realiza uma busca paginada de clientes com base em mÃºltiplos filtros.
     *
     * @param nomeCompleto Filtro por nome (contÃ©m, case insensitive)
     * @param razaoSocial  Filtro por razÃ£o social (contÃ©m, case insensitive)
     * @param cpf          Filtro por CPF (exato)
     * @param cnpj         Filtro por CNPJ (exato)
     * @param tipoCliente  Filtro por tipo de cliente
     * @param status       Filtro por status
     * @param pageable     ParÃ¢metros de paginaÃ§Ã£o
     * @return PÃ¡gina de clientes encontrados
     */
    @Transactional(readOnly = true)
    public Page<Cliente> search(String nomeCompleto,
            String razaoSocial,
            String cpf,
            String cnpj,
            TipoCliente tipoCliente,
            StatusCliente status,
            Pageable pageable) {
        if (cpf != null && !cpf.isBlank()) {
            Optional<Cliente> opt = repository.findByCpf(cpf);
            return opt.map(e -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(e), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        }
        if (cnpj != null && !cnpj.isBlank()) {
            Optional<Cliente> opt = repository.findByCnpj(cnpj);
            return opt.map(e -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(e), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        }
        if (nomeCompleto != null && !nomeCompleto.isBlank()) {
            return repository.findByNomeCompletoContainingIgnoreCase(nomeCompleto, pageable);
        }
        if (razaoSocial != null && !razaoSocial.isBlank()) {
            return repository.findByRazaoSocialContainingIgnoreCase(razaoSocial, pageable);
        }
        if (tipoCliente != null) {
            return repository.findByTipoCliente(tipoCliente, pageable);
        }
        if (status != null) {
            return repository.findByStatus(status, pageable);
        }
        return repository.findAll(pageable);
    }
}
