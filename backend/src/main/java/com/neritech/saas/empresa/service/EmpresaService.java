package com.neritech.saas.empresa.service;

import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.util.DocumentoValidator;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final PlanoAssinaturaRepository planoAssinaturaRepository;

    public EmpresaService(EmpresaRepository empresaRepository, 
                          AssinaturaEmpresaRepository assinaturaEmpresaRepository, 
                          PlanoAssinaturaRepository planoAssinaturaRepository) {
        this.empresaRepository = empresaRepository;
        this.assinaturaEmpresaRepository = assinaturaEmpresaRepository;
        this.planoAssinaturaRepository = planoAssinaturaRepository;
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
        validateDocumento(empresa.getCnpj());
        Empresa saved = empresaRepository.save(empresa);
        createTrialSubscription(saved);
        return saved;
    }



    private void createTrialSubscription(Empresa empresa) {
        PlanoAssinatura plano = planoAssinaturaRepository.findAll().stream()
                .filter(p -> p.getNivel() != null && p.getNivel() == 1)
                .findFirst()
                .orElse(null);

        if (plano != null) {
            AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
            assinatura.setEmpresa(empresa);
            assinatura.setPlano(plano);
            assinatura.setDataInicio(LocalDate.now());
            assinatura.setDataFim(LocalDate.now().plusDays(plano.getPeriodoTesteDias() != null ? plano.getPeriodoTesteDias() : 15));
            assinatura.setValorMensal(plano.getPrecoMensal());
            assinatura.setStatus(StatusAssinatura.ATIVO); // Or status TRIAL if you have it, based on previous context I'll use ATIVO to simplify access
            assinaturaEmpresaRepository.save(assinatura);
        }
    }

    @Transactional
    public Empresa update(Long id, Empresa empresa) {
        validateDocumento(empresa.getCnpj());
        Empresa current = findById(id);
        empresa.setId(current.getId());
        return empresaRepository.save(empresa);
    }

    private void validateDocumento(String doc) {
        if (doc == null || doc.isBlank()) return;
        if (!DocumentoValidator.isValidCpf(doc) && !DocumentoValidator.isValidCnpj(doc)) {
            throw new IllegalArgumentException("CPF ou CNPJ inválido");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", "ID", id);
        }
        empresaRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Optional<AssinaturaEmpresa> findActiveSubscriptionByEmpresa(Long empresaId) {
        return assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(empresaId);
    }
}
