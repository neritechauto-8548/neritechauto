package com.neritech.saas.empresa.service;

import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", "ID", id));
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> findByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public Page<Empresa> search(String cnpj, String razaoSocial, Pageable pageable) {
        if (cnpj != null && !cnpj.isBlank()) {
            Optional<Empresa> opt = empresaRepository.findByCnpj(cnpj);
            return opt.map(e -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(e), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        }
        if (razaoSocial != null && !razaoSocial.isBlank()) {
            return empresaRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial, pageable);
        }
        return empresaRepository.findAll(pageable);
    }

    @Transactional
    public Empresa create(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa update(Long id, Empresa empresa) {
        Empresa current = findById(id);
        empresa.setId(current.getId());
        return empresaRepository.save(empresa);
    }

    @Transactional
    public void delete(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", "ID", id);
        }
        empresaRepository.deleteById(id);
    }
}
