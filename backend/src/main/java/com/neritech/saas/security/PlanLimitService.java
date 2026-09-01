package com.neritech.saas.security;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanLimitService {

    private final PlanAccessService planAccessService;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    /**
     * Garante uma vaga antes de criar ou reativar um usuário.
     *
     * O lock pessimista da empresa serializa alterações de usuários ativos por tenant,
     * evitando que duas requisições concorrentes ultrapassem o limite do plano.
     */
    @Transactional
    public void assertCanActivateUser() {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null) {
            throw new BusinessException("Empresa não identificada para validar o limite de usuários.");
        }

        empresaRepository.findByIdForUpdate(empresaId)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada para validar o limite de usuários."));

        PlanoAssinatura plano = planAccessService.getAccessiblePlan()
                .orElseThrow(() -> new BusinessException(
                        "Não há assinatura ativa para cadastrar ou reativar usuários."
                ));

        Integer limite = plano.getMaxUsuarios();
        if (limite == null || limite <= 0) {
            return;
        }

        long usuariosAtivos = usuarioRepository.countByEmpresaIdAndAtivoTrue(empresaId);
        if (usuariosAtivos >= limite) {
            String nomePlano = plano.getNome() != null ? plano.getNome() : "atual";
            throw new BusinessException(
                    "Limite de usuários ativos do plano " + nomePlano + " atingido (" + limite
                            + "). Faça upgrade do plano ou inative um usuário para liberar uma vaga."
            );
        }
    }
}
