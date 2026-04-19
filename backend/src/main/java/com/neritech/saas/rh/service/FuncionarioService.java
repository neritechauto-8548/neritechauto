package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import com.neritech.saas.rh.mapper.FuncionarioMapper;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;

    public Page<FuncionarioResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    public FuncionarioResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("FuncionÃƒÂ¡rio nÃƒÂ£o encontrado"));
    }

    @Transactional
    public FuncionarioResponse create(FuncionarioRequest request) {
        if (repository.existsByEmpresaIdAndMatricula(request.empresaId(), request.matricula())) {
            throw new IllegalArgumentException("Matrícula já existe para esta empresa");
        }
        if (request.cpf() != null && !request.cpf().isBlank() && repository.existsByEmpresaIdAndCpf(request.empresaId(), request.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado para esta empresa");
        }

        Funcionario entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public FuncionarioResponse update(Long id, FuncionarioRequest request) {
        Funcionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FuncionÃƒÂ¡rio nÃƒÂ£o encontrado"));

        mapper.updateEntity(request, entity);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }
        repository.deleteById(id);
    }

    public FuncionarioResponse findByUsuarioId(Long empresaId, Long usuarioId) {
        return repository.findByEmpresaIdAndUsuarioId(empresaId, usuarioId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não vinculado a este usuário"));
    }
}
