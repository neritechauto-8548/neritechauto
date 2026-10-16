package com.neritech.saas.produtoServico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.dto.FornecedorResponse;
import com.neritech.saas.produtoServico.mapper.FornecedorMapper;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.util.DocumentoValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;
    private final FornecedorMapper mapper;
    private final FornecedorHistoricoService auditoria;

    public FornecedorService(FornecedorRepository repository, FornecedorMapper mapper,
                             FornecedorHistoricoService auditoria) {
        this.repository = repository;
        this.mapper = mapper;
        this.auditoria = auditoria;
    }

    @Transactional
    public FornecedorResponse create(FornecedorRequest request) {
        Long tenant = TenantAccess.require(request.empresaId());
        validar(request, tenant, null);
        Fornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(tenant);
        entity.setCpf(documento(request.cpf()));
        entity.setCnpj(documento(request.cnpj()));
        if (entity.getAtivo() == null) entity.setAtivo(true);
        Fornecedor saved = repository.saveAndFlush(entity);
        auditoria.registrar(saved, Map.of());
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FornecedorResponse findById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> findAll(Long empresaId, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> search(Long empresaId, String query, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.search(empresaId, query, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> filter(Long empresaId, String termo, Boolean ativo, String cidade, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.filter(empresaId, termo, ativo, cidade, pageable).map(mapper::toResponse);
    }

    @Transactional
    public FornecedorResponse update(Long id, FornecedorRequest request) {
        return update(id, request, null);
    }

    @Transactional
    public FornecedorResponse update(Long id, FornecedorRequest request, Integer expectedVersion) {
        Long tenant = TenantAccess.require(request.empresaId());
        Fornecedor entity = findEntity(id);
        if (expectedVersion != null && !Objects.equals(expectedVersion, entity.getVersao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor alterado por outra sessão; recarregue antes de salvar");
        }
        validar(request, tenant, id);
        Map<String, Object> anterior = FornecedorHistoricoService.campos(entity);
        mapper.updateEntityFromRequest(request, entity);
        entity.setCpf(documento(request.cpf()));
        entity.setCnpj(documento(request.cnpj()));
        Fornecedor saved = repository.saveAndFlush(entity);
        auditoria.registrar(saved, anterior);
        return mapper.toResponse(saved);
    }

    /** O DELETE legado apenas inativa: vínculos fiscais, pedidos e cotações são preservados. */
    @Transactional
    public void delete(Long id) {
        Fornecedor entity = findEntity(id);
        Map<String, Object> anterior = FornecedorHistoricoService.campos(entity);
        entity.setAtivo(false);
        Fornecedor saved = repository.saveAndFlush(entity);
        auditoria.registrar(saved, anterior);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorHistoricoService.Alteracao> historico(Long id, int page, int size) {
        Fornecedor entity = findEntity(id);
        return auditoria.historico(entity.getEmpresaId(), entity.getId(), page, size);
    }

    /** Compartilhado com pedidos e cotações; nunca basta validar somente o ID. */
    @Transactional(readOnly = true)
    public Fornecedor requireActive(Long id, Long empresaId) {
        TenantAccess.require(empresaId);
        Fornecedor entity = findEntity(id);
        if (!Boolean.TRUE.equals(entity.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor inativo; escolha um fornecedor ativo");
        }
        return entity;
    }

    private Fornecedor findEntity(Long id) {
        Fornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));
        TenantAccess.require(entity.getEmpresaId());
        return entity;
    }

    private static String documento(String value) {
        if (value == null || value.isBlank()) return null;
        return value.replaceAll("[^a-zA-Z0-9]", "").toUpperCase(Locale.ROOT);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void validar(FornecedorRequest r, Long tenant, Long editingId) {
        if (r.tipoPessoa() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Informe o tipo de pessoa");
        }
        // Dados bancários pertencem ao Financeiro e jamais ao cadastro geral.
        if (hasText(r.bancoNome()) || hasText(r.bancoAgencia())
                || hasText(r.bancoConta()) || hasText(r.bancoPix())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Dados bancários devem ser configurados no módulo Financeiro");
        }
        String cpf = documento(r.cpf());
        String cnpj = documento(r.cnpj());
        if (r.tipoPessoa() == TipoPessoa.FISICA) {
            if (cpf == null || !DocumentoValidator.isValidCpf(cpf) || cnpj != null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CPF inválido ou CNPJ indevido para pessoa física");
            }
        } else if (r.tipoPessoa() == TipoPessoa.JURIDICA) {
            if (cnpj == null || cnpj.length() != 14 || cnpj.chars().distinct().count() == 1
                    || !DocumentoValidator.isValidCnpj(cnpj) || cpf != null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CNPJ inválido ou CPF indevido para pessoa jurídica");
            }
        }
        if (r.cep() != null && !r.cep().isBlank() && !r.cep().matches("\\d{5}-?\\d{3}")) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CEP inválido");
        }
        if (r.prazoPagamentoDias() != null && r.prazoPagamentoDias() < 0
                || r.limiteCredito() != null && r.limiteCredito().compareTo(BigDecimal.ZERO) < 0
                || r.descontoPadrao() != null && (r.descontoPadrao().compareTo(BigDecimal.ZERO) < 0
                    || r.descontoPadrao().compareTo(new BigDecimal("100")) > 0)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Condições de compra inválidas");
        }
        if (cpf != null && repository.existsOtherCpf(tenant, cpf, editingId)
                || cnpj != null && repository.existsOtherCnpj(tenant, cnpj, editingId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF/CNPJ já cadastrado nesta empresa");
        }
    }
}
