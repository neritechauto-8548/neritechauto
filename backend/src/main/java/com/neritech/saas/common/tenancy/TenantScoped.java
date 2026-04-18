package com.neritech.saas.common.tenancy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AnotaÃ§Ã£o para marcar entidades que tÃªm escopo de tenant (empresa)
 * Entidades com esta anotaÃ§Ã£o serÃ£o automaticamente filtradas pelo ID da empresa
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantScoped {
}
