package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

/** Records failed authentication outside the rollback-only login transaction. */
@Service
@RequiredArgsConstructor
public class LoginFailureRecorder {
    private final TentativaLoginRepository tentativaLoginRepository;
    private final LogAcessoRepository logAcessoRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(LoginRequest request, Long tenantId) {
        String key = request.getEmail().trim().toLowerCase(Locale.ROOT);
        tentativaLoginRepository.saveAndFlush(TentativaLogin.builder()
                .email(key)
                .ipAddress(request.getIpAddress() == null ? "unknown" : request.getIpAddress())
                .sucesso(false)
                .dataTentativa(LocalDateTime.now())
                .empresaId(tenantId)
                .build());
        if (tenantId != null) {
            logAcessoRepository.saveAndFlush(LogAcesso.builder()
                    .tipoEvento(LogAcesso.TipoEvento.LOGIN_FAIL)
                    .detalhes("Credenciais inválidas")
                    .ipAddress(request.getIpAddress())
                    .userAgent(request.getUserAgent())
                    .dataEvento(LocalDateTime.now())
                    .empresaId(tenantId)
                    .build());
        }
    }
}
