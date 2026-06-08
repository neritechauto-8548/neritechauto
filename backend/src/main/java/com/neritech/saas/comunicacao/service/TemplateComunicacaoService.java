package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoRequest;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoResponse;
import com.neritech.saas.comunicacao.mapper.TemplateComunicacaoMapper;
import com.neritech.saas.comunicacao.repository.TemplateComunicacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import com.neritech.saas.common.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateComunicacaoService {

    private final TemplateComunicacaoRepository repository;
    private final TemplateComunicacaoMapper mapper;

    @Transactional(readOnly = true)
    public Page<TemplateComunicacaoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TemplateComunicacaoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + id));
    }

    public TemplateComunicacaoResponse create(TemplateComunicacaoRequest request) {
        validate(request, null);
        TemplateComunicacao entity = mapper.toEntity(request);
        TemplateComunicacao savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public TemplateComunicacaoResponse update(Long id, TemplateComunicacaoRequest request) {
        TemplateComunicacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + id));

        validate(request, id);
        mapper.updateEntity(request, entity);
        TemplateComunicacao updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Template não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    private void validate(TemplateComunicacaoRequest request, Long id) {
        if (request.empresaId() == null) {
            throw new BusinessException("O ID da empresa é obrigatório.");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new BusinessException("O nome do modelo de envio é obrigatório.");
        }
        if (request.nome().trim().length() < 2 || request.nome().trim().length() > 100) {
            throw new BusinessException("O nome deve ter entre 2 e 100 caracteres.");
        }
        if (request.categoria() == null) {
            throw new BusinessException("A categoria do modelo é obrigatória.");
        }
        if (request.conteudo() == null || request.conteudo().trim().isEmpty()) {
            throw new BusinessException("O conteúdo do modelo é obrigatório.");
        }
        if (request.conteudo().trim().length() < 10) {
            throw new BusinessException("O conteúdo deve ter pelo menos 10 caracteres.");
        }
        if (request.tipoTemplate() == TipoTemplate.EMAIL) {
            if (request.assunto() == null || request.assunto().trim().isEmpty()) {
                throw new BusinessException("O assunto é obrigatório para modelos do tipo E-mail.");
            }
            if (request.assunto().trim().length() < 2 || request.assunto().trim().length() > 255) {
                throw new BusinessException("O assunto deve ter entre 2 e 255 caracteres.");
            }
        }

        boolean exists;
        if (id == null) {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCase(request.empresaId(), request.nome().trim());
        } else {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(request.empresaId(), request.nome().trim(), id);
        }
        if (exists) {
            throw new BusinessException("Já existe um modelo de envio cadastrado com este nome nesta empresa.");
        }
    }
}
