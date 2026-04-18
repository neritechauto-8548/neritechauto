package com.neritech.saas.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FlywayRepairConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                try {
                    log.info("Executando reparo do Flyway para corrigir inconsistências...");
                    
                    // Cria uma nova instância do Flyway para reparo e evitar problemas de pool de conexões
                    FluentConfiguration repairConfig = Flyway.configure()
                        .configuration(flyway.getConfiguration());
                    
                    Flyway repairFlyway = repairConfig.load();
                    repairFlyway.repair();
                    
                    log.info("Reparo do Flyway concluído com sucesso");
                    
                    // Pequeno delay para garantir que as conexões sejam liberadas
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    log.warn("Falha no reparo do Flyway (isso pode ser normal): {}", e.getMessage());
                }
                
                log.info("Executando migração do Flyway...");
                flyway.migrate();
                log.info("Migração do Flyway concluída com sucesso");
            }
        };
    }
}
