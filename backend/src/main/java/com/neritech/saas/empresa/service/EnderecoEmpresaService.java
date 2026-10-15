package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import com.neritech.saas.common.tenancy.TenantAccess;
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
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class EnderecoEmpresaService {

    private final EnderecoEmpresaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final EnderecoEmpresaMapper mapper;
    private final EmpresaAlteracaoService auditoria;

    public EnderecoEmpresaService(EnderecoEmpresaRepository repository,
            EmpresaRepository empresaRepository,
            EnderecoEmpresaMapper mapper, EmpresaAlteracaoService auditoria) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
        this.auditoria = auditoria;
    }

    public EnderecoEmpresaResponse create(EnderecoEmpresaRequest request) {
        TenantAccess.require(request.empresaId());
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Empresa nÃ£o encontrada com ID: " + request.empresaId()));

        EnderecoEmpresa endereco = mapper.toEntity(request);
        endereco.setEmpresa(empresa);

        EnderecoEmpresa saved = repository.saveAndFlush(endereco);
        auditoria.registrar(request.empresaId(), "enderecos_empresa", saved.getId(),
                saved.getVersao(), Map.of(), camposAuditaveis(saved));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public EnderecoEmpresaResponse findById(Long id) {
        EnderecoEmpresa endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EndereÃ§o nÃ£o encontrado com ID: " + id));
        TenantAccess.require(endereco.getEmpresa().getId());
        return mapper.toResponse(endereco);
    }

    @Transactional(readOnly = true)
    public Page<EnderecoEmpresaResponse> findAll(Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        var all = repository.findByEmpresaId(empresaId).stream().map(mapper::toResponse).toList();
        long start = Math.min(pageable.getOffset(), all.size());
        long end = Math.min(start + pageable.getPageSize(), all.size());
        return new org.springframework.data.domain.PageImpl<>(all.subList((int) start, (int) end), pageable, all.size());
    }

    @Transactional(readOnly = true)
    public List<EnderecoEmpresaResponse> findByEmpresaId(Long empresaId) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request) {
        TenantAccess.require(request.empresaId());
        EnderecoEmpresa endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EndereÃ§o nÃ£o encontrado com ID: " + id));

        TenantAccess.require(endereco.getEmpresa().getId());
        if (!endereco.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Empresa nÃ£o encontrada com ID: " + request.empresaId()));
            endereco.setEmpresa(empresa);
        }

        Map<String, Object> antes = camposAuditaveis(endereco);
        mapper.updateEntityFromRequest(request, endereco);
        EnderecoEmpresa updated = repository.saveAndFlush(endereco);
        auditoria.registrar(request.empresaId(), "enderecos_empresa", updated.getId(),
                updated.getVersao(), antes, camposAuditaveis(updated));
        return mapper.toResponse(updated);
    }

    private static Map<String, Object> camposAuditaveis(EnderecoEmpresa e) {
        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("cep", e.getCep());
        campos.put("logradouro", e.getLogradouro());
        campos.put("numero", e.getNumero());
        campos.put("complemento", e.getComplemento());
        campos.put("bairro", e.getBairro());
        campos.put("cidade", e.getCidade());
        campos.put("estado", e.getEstado());
        return campos;
    }

    public void delete(Long id) {
        EnderecoEmpresa endereco = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));
        TenantAccess.require(endereco.getEmpresa().getId());
        repository.delete(endereco);
    }
}
