package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogAcessoRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private LogAcessoRepository logAcessoRepository;

    @Test
    @DisplayName("Deve listar logs da empresa ordenados por data")
    void deveListarLogsOrdenados() {
        // Arrange
        LogAcesso l1 = new LogAcesso();
        l1.setEmpresaId(1L);
        l1.setTipoEvento(LogAcesso.TipoEvento.LOGIN_SUCCESS);
        l1.setDataEvento(LocalDateTime.now().minusHours(1));
        
        LogAcesso l2 = new LogAcesso();
        l2.setEmpresaId(1L);
        l2.setTipoEvento(LogAcesso.TipoEvento.LOGOUT);
        l2.setDataEvento(LocalDateTime.now());
        
        logAcessoRepository.saveAll(List.of(l1, l2));

        // Act
        List<LogAcesso> logs = logAcessoRepository.findByEmpresaIdOrderByDataEventoDesc(1L);

        // Assert
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getTipoEvento()).isEqualTo(LogAcesso.TipoEvento.LOGOUT);
        assertThat(logs.get(1).getTipoEvento()).isEqualTo(LogAcesso.TipoEvento.LOGIN_SUCCESS);
    }
}
