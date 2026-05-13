package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;
    private final PermissaoRepository permissaoRepository;

    @Transactional(readOnly = true)
    public List<Funcao> findAll() {
        return funcaoRepository.findAllByEmpresaId(TenantContext.getCurrentTenant());
    }

    @Transactional(readOnly = true)
    public Optional<Funcao> findByNome(String nome) {
        return funcaoRepository.findByNomeAndEmpresaId(nome, TenantContext.getCurrentTenant());
    }

    @Transactional(readOnly = true)
    public Funcao findById(Long id) {
        return funcaoRepository.findByIdAndEmpresaId(id, TenantContext.getCurrentTenant())
                .orElseThrow(() -> new ResourceNotFoundException("Função", "ID", id));
    }

    @Transactional
    public Funcao save(Funcao funcao) {
        syncPermissoes(funcao);
        return funcaoRepository.save(funcao);
    }

    @Transactional
    public Funcao update(Long id, Funcao dadosAtualizados) {
        Funcao funcaoExistente = findById(id);
        
        funcaoExistente.setNome(dadosAtualizados.getNome());
        funcaoExistente.setDescricao(dadosAtualizados.getDescricao());
        funcaoExistente.setAtivo(dadosAtualizados.getAtivo());
        
        // Sincronizar permissões
        funcaoExistente.getPermissoes().clear();
        if (dadosAtualizados.getPermissoes() != null) {
            funcaoExistente.getPermissoes().addAll(dadosAtualizados.getPermissoes());
        }
        syncPermissoes(funcaoExistente);
        
        return funcaoRepository.save(funcaoExistente);
    }

    @Transactional
    public void delete(Long id) {
        Funcao funcao = findById(id);
        if (funcao.getSistema() != null && funcao.getSistema()) {
            throw new IllegalArgumentException("Não é possível excluir uma função do sistema.");
        }
        funcaoRepository.delete(funcao);
    }

    private void syncPermissoes(Funcao funcao) {
        if (funcao.getPermissoes() != null && !funcao.getPermissoes().isEmpty()) {
            List<Long> permissaoIds = funcao.getPermissoes().stream()
                    .map(Permissao::getId)
                    .collect(Collectors.toList());
            Set<Permissao> permissoesGerenciadas = permissaoRepository.findAllById(permissaoIds)
                    .stream()
                    .collect(Collectors.toSet());
            funcao.setPermissoes(permissoesGerenciadas);
        }
    }
}
