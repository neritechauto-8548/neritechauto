package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.orcamento.dto.OrcamentoCommercialPermissionsResponse;
import com.neritech.saas.orcamento.repository.OrcamentoDiscountAuthorityLimitRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrcamentoCommercialAuthorityService {

    public record AuthoritySnapshot(
            Long actorId,
            boolean canEditPrice,
            boolean canApplyDiscount,
            boolean canApproveDiscount,
            boolean canViewCost,
            BigDecimal discountAuthorityPercent) {

        public OrcamentoCommercialPermissionsResponse toResponse() {
            return new OrcamentoCommercialPermissionsResponse(
                    canEditPrice,
                    canEditPrice,
                    canApplyDiscount,
                    canApproveDiscount,
                    canViewCost,
                    discountAuthorityPercent);
        }
    }

    private final UsuarioRepository usuarioRepository;
    private final OrcamentoDiscountAuthorityLimitRepository limitRepository;

    public OrcamentoCommercialAuthorityService(
            UsuarioRepository usuarioRepository,
            OrcamentoDiscountAuthorityLimitRepository limitRepository) {
        this.usuarioRepository = usuarioRepository;
        this.limitRepository = limitRepository;
    }

    @Transactional(readOnly = true)
    public AuthoritySnapshot current() {
        Long tenantId = TenantContext.getCurrentTenant();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (tenantId == null || authentication == null || !authentication.isAuthenticated()) {
            return anonymous();
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
        Usuario actor = usuarioRepository
                .findByEmailIgnoreCaseAndEmpresaId(authentication.getName(), tenantId)
                .orElse(null);
        if (actor == null) return anonymous();

        boolean canApprove = authorities.contains("ORCAMENTO_DESCONTO_APROVAR");
        BigDecimal limit = canApprove
                ? new BigDecimal("100.0000")
                : limitRepository.findMaximumForUser(tenantId, actor.getId())
                        .orElse(BigDecimal.ZERO)
                        .max(BigDecimal.ZERO)
                        .min(new BigDecimal("100"))
                        .setScale(4, RoundingMode.HALF_UP);

        return new AuthoritySnapshot(
                actor.getId(),
                authorities.contains("ORCAMENTO_PRECO_EDITAR"),
                authorities.contains("ORCAMENTO_DESCONTO_APLICAR") || canApprove,
                canApprove,
                authorities.contains("ORCAMENTO_CUSTO_VISUALIZAR"),
                limit);
    }

    public AuthoritySnapshot requireAuthenticated() {
        AuthoritySnapshot current = current();
        if (current.actorId() == null) {
            throw new AccessDeniedException("Identidade autenticada obrigatoria para ajuste comercial.");
        }
        return current;
    }

    private AuthoritySnapshot anonymous() {
        return new AuthoritySnapshot(null, false, false, false, false, BigDecimal.ZERO.setScale(4));
    }
}

