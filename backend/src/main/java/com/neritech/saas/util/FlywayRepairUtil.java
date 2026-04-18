package com.neritech.saas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Utilitário para reparar o histórico do Flyway
 * Execute esta classe uma vez para limpar registros de migração falhados
 */
public class FlywayRepairUtil {
    
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5434/nerisys";
        String username = "postgres";
        String password = "94573730";
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            
            // Deletar registros de migração falhados
            int deleted = stmt.executeUpdate(
                "DELETE FROM flyway_schema_history WHERE version = '173' AND success = false"
            );
            
            System.out.println("✓ Registros de migração falhados removidos: " + deleted);
            System.out.println("✓ Histórico do Flyway reparado com sucesso!");
            System.out.println("Agora você pode reiniciar a aplicação.");
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao reparar o Flyway: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
