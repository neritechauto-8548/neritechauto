package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ModeloDocumento;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import com.neritech.saas.empresa.dto.ModeloDocumentoRequest;
import com.neritech.saas.empresa.dto.ModeloDocumentoResponse;
import com.neritech.saas.empresa.mapper.ModeloDocumentoMapper;
import com.neritech.saas.empresa.repository.ModeloDocumentoRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModeloDocumentoService {

    private final ModeloDocumentoRepository repository;
    private final EmpresaRepository empresaRepository;
    private final ModeloDocumentoMapper mapper;

    public ModeloDocumentoService(ModeloDocumentoRepository repository,
            EmpresaRepository empresaRepository,
            ModeloDocumentoMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public ModeloDocumentoResponse create(ModeloDocumentoRequest request) {
        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

        ModeloDocumento modelo = mapper.toEntity(request);
        modelo.setEmpresa(empresa);
        return mapper.toResponse(repository.save(modelo));
    }

    @Transactional(readOnly = true)
    public ModeloDocumentoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<ModeloDocumentoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ModeloDocumentoResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ModeloDocumentoResponse> findByEmpresaIdAndTipoDocumento(Long empresaId, TipoDocumento tipoDocumento) {
        return repository.findByEmpresaIdAndTipoDocumento(empresaId, tipoDocumento).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ModeloDocumentoResponse update(Long id, ModeloDocumentoRequest request) {
        ModeloDocumento modelo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃ£o encontrado"));

        if (!modelo.getEmpresa().getId().equals(request.empresaId())) {
            var empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));
            modelo.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, modelo);
        return mapper.toResponse(repository.save(modelo));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Modelo nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
