package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    @Transactional(readOnly = true)
    public List<Permissao> findAll() {
        return permissaoRepository.findAll();
    }

    @Transactional
    public Permissao save(Permissao permissao) {
        return permissaoRepository.save(permissao);
    }
}
