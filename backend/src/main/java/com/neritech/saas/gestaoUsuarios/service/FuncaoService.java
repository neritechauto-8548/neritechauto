package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;

    @Transactional(readOnly = true)
    public List<Funcao> findAll() {
        return funcaoRepository.findAllByEmpresaId(TenantContext.getCurrentTenant());
    }

    @Transactional(readOnly = true)
    public Optional<Funcao> findByNome(String nome) {
        return funcaoRepository.findByNomeAndEmpresaId(nome, TenantContext.getCurrentTenant());
    }

    @Transactional
    public Funcao save(Funcao funcao) {
        return funcaoRepository.save(funcao);
    }
}
