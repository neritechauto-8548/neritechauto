package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import com.neritech.saas.empresa.mapper.EnderecoEmpresaMapper;
import com.neritech.saas.empresa.repository.EnderecoEmpresaRepository;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnderecoEmpresaService {

    private final EnderecoEmpresaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final EnderecoEmpresaMapper mapper;

    public EnderecoEmpresaService(EnderecoEmpresaRepository repository,
            EmpresaRepository empresaRepository,
            EnderecoEmpresaMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public EnderecoEmpresaResponse create(EnderecoEmpresaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Empresa nÃ£o encontrada com ID: " + request.empresaId()));

        EnderecoEmpresa endereco = mapper.toEntity(request);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa saved = repository.save(endereco);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public EnderecoEmpresaResponse findById(Long id) {
        EnderecoEmpresa endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EndereÃ§o nÃ£o encontrado com ID: " + id));
        return mapper.toResponse(endereco);
    }

    @Transactional(readOnly = true)
    public Page<EnderecoEmpresaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<EnderecoEmpresaResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request) {
        EnderecoEmpresa endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EndereÃ§o nÃ£o encontrado com ID: " + id));

        if (!endereco.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Empresa nÃ£o encontrada com ID: " + request.empresaId()));
            endereco.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, endereco);
        EnderecoEmpresa updated = repository.save(endereco);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("EndereÃ§o nÃ£o encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
