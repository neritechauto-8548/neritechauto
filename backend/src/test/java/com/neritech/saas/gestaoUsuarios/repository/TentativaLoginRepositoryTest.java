package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TentativaLoginRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private TentativaLoginRepository tentativaLoginRepository;

    @Test
    @DisplayName("Deve contar tentativas falhas recentes")
    void deveContarTentativasFalhas() {
        // Arrange
        TentativaLogin t1 = new TentativaLogin();
        t1.setEmail("test@email.com");
        t1.setEmpresaId(1L);
        t1.setSucesso(false);
        t1.setDataTentativa(LocalDateTime.now());
        
        TentativaLogin t2 = new TentativaLogin();
        t2.setEmail("test@email.com");
        t2.setEmpresaId(1L);
        t2.setSucesso(true);
        t2.setDataTentativa(LocalDateTime.now());
        
        tentativaLoginRepository.saveAll(List.of(t1, t2));

        // Act
        long count = tentativaLoginRepository.countByEmailAndEmpresaIdAndSucessoFalseAndDataTentativaAfter(
                "test@email.com", 1L, LocalDateTime.now().minusMinutes(10));

        // Assert
        assertThat(count).isEqualTo(1);
    }
}
