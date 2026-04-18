package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaRequest;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaResponse;
import com.neritech.saas.empresa.mapper.AssinaturaEmpresaMapper;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssinaturaEmpresaService {

    private final AssinaturaEmpresaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final PlanoAssinaturaRepository planoRepository;
    private final AssinaturaEmpresaMapper mapper;

    public AssinaturaEmpresaService(AssinaturaEmpresaRepository repository,
            EmpresaRepository empresaRepository,
            PlanoAssinaturaRepository planoRepository,
            AssinaturaEmpresaMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.planoRepository = planoRepository;
        this.mapper = mapper;
    }

    public AssinaturaEmpresaResponse create(AssinaturaEmpresaRequest request) {
        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));
        var plano = planoRepository.findById(request.planoId())
                .orElseThrow(() -> new EntityNotFoundException("Plano nÃ£o encontrado"));

        AssinaturaEmpresa assinatura = mapper.toEntity(request);
        assinatura.setEmpresa(empresa);
        assinatura.setPlano(plano);

        return mapper.toResponse(repository.save(assinatura));
    }

    @Transactional(readOnly = true)
    public AssinaturaEmpresaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<AssinaturaEmpresaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AssinaturaEmpresaResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public AssinaturaEmpresaResponse update(Long id, AssinaturaEmpresaRequest request) {
        AssinaturaEmpresa assinatura = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura nÃ£o encontrada"));

        if (!assinatura.getEmpresa().getId().equals(request.empresaId())) {
            var empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));
            assinatura.setEmpresa(empresa);
        }

        if (!assinatura.getPlano().getId().equals(request.planoId())) {
            var plano = planoRepository.findById(request.planoId())
                    .orElseThrow(() -> new EntityNotFoundException("Plano nÃ£o encontrado"));
            assinatura.setPlano(plano);
        }

        mapper.updateEntityFromRequest(request, assinatura);
        return mapper.toResponse(repository.save(assinatura));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Assinatura nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
