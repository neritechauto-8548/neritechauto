package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuditoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogAcessoRepository logAcessoRepository;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("GET /auditoria/logs - Deve listar logs de acesso")
    void deveListarLogsAcesso() throws Exception {
        // Arrange
        LogAcesso log = new LogAcesso();
        log.setId(1L);
        log.setTipoEvento(LogAcesso.TipoEvento.LOGIN_SUCCESS);
        log.setDataEvento(LocalDateTime.now());
        
        when(logAcessoRepository.findByEmpresaIdOrderByDataEventoDesc(anyLong()))
                .thenReturn(List.of(log));

        // Act & Assert
        mockMvc.perform(get("/api/auditoria/logs")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoEvento").value("LOGIN_SUCCESS"));
    }
}
