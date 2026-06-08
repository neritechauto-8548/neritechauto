package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.dto.ContaBancariaRequest;
import com.neritech.saas.financeiro.dto.ContaBancariaResponse;
import com.neritech.saas.financeiro.mapper.ContaBancariaMapper;
import com.neritech.saas.financeiro.repository.ContaBancariaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository repository;
    private final ContaBancariaMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContaBancariaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContaBancariaResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
    }

    @Transactional
    public ContaBancariaResponse create(Long empresaId, ContaBancariaRequest request) {
        validarContaBancaria(empresaId, null, request);
        ContaBancaria entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ContaBancariaResponse update(Long id, Long empresaId, ContaBancariaRequest request) {
        validarContaBancaria(empresaId, id, request);
        ContaBancaria entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancária não encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    private void validarContaBancaria(Long empresaId, Long id, ContaBancariaRequest request) {
        if (request.bancoCodigo() == null || request.bancoCodigo().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O código do banco é obrigatório.");
        }
        if (request.bancoNome() == null || request.bancoNome().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome do banco é obrigatório.");
        }
        if (request.agencia() == null || request.agencia().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("A agência é obrigatória.");
        }
        if (request.conta() == null || request.conta().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O número da conta é obrigatório.");
        }
        if (request.titularConta() == null || request.titularConta().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome do titular é obrigatório.");
        }
        if (request.cpfCnpjTitular() == null || request.cpfCnpjTitular().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O CPF ou CNPJ do titular é obrigatório.");
        }

        String digits = request.cpfCnpjTitular().replaceAll("[^a-zA-Z0-9]", "");
        if (digits.length() != 11 && digits.length() != 14) {
            throw new com.neritech.saas.common.exception.BusinessException("O documento do titular deve conter 11 dígitos (CPF) ou 14 caracteres (CNPJ).");
        }

        // Verifica duplicidade (mesma agencia e conta no mesmo banco para a mesma empresa)
        String bancoCod = request.bancoCodigo().trim();
        String ag = request.agencia().trim();
        String ct = request.conta().trim();
        boolean duplicada = id == null
                ? repository.existsByEmpresaIdAndBancoCodigoAndAgenciaAndConta(empresaId, bancoCod, ag, ct)
                : repository.existsByEmpresaIdAndBancoCodigoAndAgenciaAndContaAndIdNot(empresaId, bancoCod, ag, ct, id);

        if (duplicada) {
            throw new com.neritech.saas.common.exception.BusinessException("Já existe uma conta bancária cadastrada com esta mesma agência e conta neste banco.");
        }
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContaBancaria entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
        repository.delete(entity);
    }
}
