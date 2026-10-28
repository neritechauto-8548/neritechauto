package com.neritech.saas.common.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA auditing is enabled only in contexts that load persistence configuration.
 * Keeping this outside SaasApplication avoids creating JPA infrastructure in
 * @WebMvcTest controller slices, which intentionally have no EntityManager.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
