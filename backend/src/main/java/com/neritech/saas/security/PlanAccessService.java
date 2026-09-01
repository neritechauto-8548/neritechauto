package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Service("planAccess")
@RequiredArgsConstructor
public class PlanAccessService {

    private static final EnumSet<StatusAssinatura> CANDIDATOS_ACESSO = EnumSet.of(
            StatusAssinatura.ATIVO,
            StatusAssinatura.TESTE,
            StatusAssinatura.ATRASADO
    );

    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;

    public boolean hasLevel(int nivelMinimo) {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null || nivelMinimo < 1) {
            return false;
        }

        return assinaturaEmpresaRepository
                .findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(empresaId, CANDIDATOS_ACESSO)
                .filter(this::possuiAcessoComercial)
                .map(AssinaturaEmpresa::getPlano)
                .map(PlanoAssinatura::getNivel)
                .filter(nivel -> nivel != null && nivel >= nivelMinimo)
                .isPresent();
    }

    public boolean hasFiscalAccess() {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null) {
            return false;
        }

        return assinaturaEmpresaRepository
                .findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(empresaId, CANDIDATOS_ACESSO)
                .filter(this::possuiAcessoComercial)
                .map(AssinaturaEmpresa::getPlano)
                .filter(this::planoPermiteFiscal)
                .isPresent();
    }

    private boolean planoPermiteFiscal(PlanoAssinatura plano) {
        if (Boolean.TRUE.equals(plano.getPossuiIntegracaoNfe())) {
            return true;
        }

        Integer nivel = plano.getNivel();
        return nivel != null && nivel >= 2;
    }

    private boolean possuiAcessoComercial(AssinaturaEmpresa assinatura) {
        if (assinatura.getStatus() == StatusAssinatura.ATIVO) {
            return true;
        }

        LocalDateTime agora = LocalDateTime.now();

        if (assinatura.getStatus() == StatusAssinatura.TESTE) {
            return assinatura.getTrialEndsAt() == null || !assinatura.getTrialEndsAt().isBefore(agora);
        }

        if (assinatura.getStatus() == StatusAssinatura.ATRASADO) {
            return assinatura.getGracePeriodEndsAt() != null
                    && !assinatura.getGracePeriodEndsAt().isBefore(agora);
        }

        return false;
    }
}
