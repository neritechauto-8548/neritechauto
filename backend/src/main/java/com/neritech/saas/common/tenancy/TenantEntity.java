package com.neritech.saas.common.tenancy;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * Classe base para entidades com escopo de tenant (empresa).
 * Estende BaseEntity e adiciona o campo empresaId para multi-tenancy.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = Long.class))
@Filter(name = "tenantFilter", condition = "empresa_id = :tenantId")
public abstract class TenantEntity extends BaseEntity {

    @Column(name = "empresa_id", nullable = false, updatable = false)
    private Long empresaId;

    @PrePersist
    public void prePersistTenant() {
        if (this.empresaId == null) {
            Long current = TenantContext.getCurrentTenant();
            if (current == null) {
                throw new IllegalStateException("Cabeçalho X-Tenant-Id é obrigatório para criar entidades");
            }
            this.empresaId = current;
        }
    }

    @PreUpdate
    public void preUpdateTenant() {
        Long current = TenantContext.getCurrentTenant();
        if (current == null) {
            throw new IllegalStateException("Cabeçalho X-Tenant-Id é obrigatório para atualizar entidades");
        }
        if (!java.util.Objects.equals(this.empresaId, current)) {
            throw new IllegalStateException("Não é permitido alterar o empresaId de uma entidade");
        }
    }
}
