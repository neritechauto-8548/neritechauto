package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.dto.QuestionarioDuplicacaoRequest;
import java.util.ArrayList;
import java.util.List;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;
import com.neritech.saas.comunicacao.dto.QuestionarioRequest;
import com.neritech.saas.comunicacao.dto.QuestionarioResponse;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
import com.neritech.saas.comunicacao.repository.PesquisaRespostaRepository;
import java.util.Objects;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.common.exception.BusinessException;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionarioService {

    private final QuestionarioRepository repository;
    private final ItQuestionarioRepository itQuestionarioRepository;
    private final QuestionarioMapper mapper;
    private final PesquisaRespostaRepository respostas;

    private void validate(QuestionarioRequest request, Long id) {
        TenantAccess.require(request.empresaId());
        if (request.empresaId() == null) {
            throw new BusinessException("O ID da empresa é obrigatório.");
        }
        if (request.dsQuestionario() == null || request.dsQuestionario().trim().isEmpty()) {
            throw new BusinessException("O nome do questionário é obrigatório.");
        }
        String ds = request.dsQuestionario().trim();
        if (ds.length() < 2) {
            throw new BusinessException("O nome do questionário deve ter pelo menos 2 caracteres.");
        }
        if (ds.length() > 255) {
            throw new BusinessException("O nome do questionário não pode exceder 255 caracteres.");
        }

        boolean exists;
        if (id == null) {
            exists = repository.existsByEmpresaIdAndDsQuestionarioIgnoreCase(request.empresaId(), ds);
        } else {
            exists = repository.existsByEmpresaIdAndDsQuestionarioIgnoreCaseAndIdNot(request.empresaId(), ds, id);
        }
        if (exists) {
            throw new BusinessException("Já existe um questionário cadastrado com este nome nesta empresa.");
        }
    }

    public QuestionarioResponse create(QuestionarioRequest request) {
        validate(request, null);
        Questionario entity = mapper.toEntity(request);
        entity.setStatusPesquisa(StatusPesquisa.DRAFT);
        Questionario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuestionarioResponse findById(Long id) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<QuestionarioResponse> findAll(Long empresaId, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public QuestionarioResponse update(Long id, QuestionarioRequest request) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        if (entity.getStatusPesquisa() != StatusPesquisa.DRAFT) {
            throw new BusinessException("Questionários publicados ou encerrados não podem ser editados; crie uma nova versão.");
        }
        if (respostas.existsByQuestionarioIdAndEmpresaId(id, TenantAccess.requireCurrent())
                && (!Objects.equals(entity.getDsQuestionario(), request.dsQuestionario())
                || !Objects.equals(entity.getUrlImagem(), request.urlImagem()))) {
            throw new BusinessException("Este questionário já possui respostas: apenas ativar ou inativar é permitido.");
        }
        validate(request, id);
        mapper.updateEntityFromRequest(request, entity);
        Questionario updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        if (respostas.existsByQuestionarioIdAndEmpresaId(id, TenantAccess.requireCurrent())) {
            throw new BusinessException("Um questionário com respostas não pode ser excluído.");
        }
        if (entity.getStatusPesquisa() != StatusPesquisa.DRAFT) {
            throw new BusinessException("Somente questionários em rascunho podem ser excluídos.");
        }
        itQuestionarioRepository.deleteByQuestionarioId(id);
        repository.delete(entity);
    }

    private Questionario authorized(Long id) {
        TenantAccess.requireCurrent();
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        return entity;
    }

    private QuestionarioResponse transition(Long id, StatusPesquisa expected, StatusPesquisa next) {
        Questionario entity = authorized(id);
        if (entity.getStatusPesquisa() != expected) {
            throw new BusinessException("Transição inválida: a pesquisa está em " + entity.getStatusPesquisa()
                    + ", mas a operação exige " + expected + ".");
        }
        if (next == StatusPesquisa.PUBLISHED && expected == StatusPesquisa.DRAFT
                && itQuestionarioRepository.findByQuestionarioId(id).isEmpty()) {
            throw new BusinessException("Cadastre pelo menos uma pergunta antes de publicar.");
        }
        entity.setStatusPesquisa(next);
        entity.setSnAtivo(next == StatusPesquisa.PUBLISHED);
        return mapper.toResponse(repository.save(entity));
    }

    /**
     * Duplica título, imagem e perguntas em um novo rascunho sem respostas,
     * preservando integralmente a pesquisa original e seu histórico.
     */
    public QuestionarioResponse duplicar(Long origemId, QuestionarioDuplicacaoRequest input) {
        Questionario original = authorized(origemId);
        Long empresaId = TenantAccess.requireCurrent();
        validate(new QuestionarioRequest(empresaId, input.titulo(), original.getUrlImagem(), true), null);

        Questionario novo = new Questionario();
        novo.setEmpresaId(empresaId);
        novo.setDsQuestionario(input.titulo().trim());
        novo.setUrlImagem(original.getUrlImagem());
        novo.setSnAtivo(true);
        novo.setStatusPesquisa(StatusPesquisa.DRAFT);
        Questionario salvo = repository.save(novo);

        List<ItQuestionario> copias = new ArrayList<>();
        for (ItQuestionario origem : itQuestionarioRepository.findByQuestionarioId(origemId)) {
            ItQuestionario item = new ItQuestionario();
            item.setQuestionario(salvo);
            item.setDsItQuestionario(origem.getDsItQuestionario());
            item.setTpItQuestionario(origem.getTpItQuestionario());
            copias.add(item);
        }
        if (!copias.isEmpty()) itQuestionarioRepository.saveAll(copias);
        return mapper.toResponse(salvo);
    }

    public QuestionarioResponse publicar(Long id) {
        return transition(id, StatusPesquisa.DRAFT, StatusPesquisa.PUBLISHED);
    }

    public QuestionarioResponse pausar(Long id) {
        return transition(id, StatusPesquisa.PUBLISHED, StatusPesquisa.PAUSED);
    }

    public QuestionarioResponse retomar(Long id) {
        return transition(id, StatusPesquisa.PAUSED, StatusPesquisa.PUBLISHED);
    }

    public QuestionarioResponse encerrar(Long id) {
        Questionario entity = authorized(id);
        if (entity.getStatusPesquisa() != StatusPesquisa.PUBLISHED
                && entity.getStatusPesquisa() != StatusPesquisa.PAUSED) {
            throw new BusinessException("Somente pesquisas publicadas ou pausadas podem ser encerradas.");
        }
        entity.setStatusPesquisa(StatusPesquisa.CLOSED);
        entity.setSnAtivo(false);
        return mapper.toResponse(repository.save(entity));
    }
}

