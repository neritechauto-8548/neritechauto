package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.domain.enums.DestinatarioTipo;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.mapper.ComunicacaoEnviadaMapper;
import com.neritech.saas.comunicacao.repository.ComunicacaoEnviadaRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ComunicacaoEnviadaServiceStatusTest {
    private final ComunicacaoEnviadaRepository repository = mock(ComunicacaoEnviadaRepository.class);
    private final ComunicacaoEnviadaMapper mapper = mock(ComunicacaoEnviadaMapper.class);
    private final EmailSenderService email = mock(EmailSenderService.class);
    private final ComunicacaoEnviadaService service = new ComunicacaoEnviadaService(repository, mapper, email);

    @AfterEach void clear() { TenantContext.clear(); }

    private ComunicacaoEnviadaRequest req(TipoComunicacao canal, StatusComunicacao status) {
        return new ComunicacaoEnviadaRequest(7L, null, null, canal,
            DestinatarioTipo.CLIENTE, 18L, "Cliente", "contato@example.com",
            "Assunto", "Mensagem de teste", null, null, status, false,
            null, null, null, null);
    }

    @Test void whatsappCannotPretendToBeSent() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.create(req(TipoComunicacao.WHATSAPP, StatusComunicacao.ENVIADA)))
            .isInstanceOf(BusinessException.class);
        verifyNoInteractions(repository, email, mapper);
    }

    @Test void emailWithWorkingSmtpIsSentButNotDelivered() {
        TenantContext.setCurrentTenant(7L);
        ComunicacaoEnviada entity = new ComunicacaoEnviada();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        service.create(req(TipoComunicacao.EMAIL, StatusComunicacao.ENTREGUE));
        assertThat(entity.getStatus()).isEqualTo(StatusComunicacao.ENVIADA);
        assertThat(entity.getDataEnvio()).isNotNull();
        assertThat(entity.getDataEntrega()).isNull();
    }

    @Test void smtpFailureIsRecordedAsFailure() {
        TenantContext.setCurrentTenant(7L);
        ComunicacaoEnviada entity = new ComunicacaoEnviada();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        doThrow(new IllegalStateException("SMTP indisponível"))
            .when(email).sendEmail(anyString(), anyString(), anyString());
        service.create(req(TipoComunicacao.EMAIL, StatusComunicacao.ENTREGUE));
        assertThat(entity.getStatus()).isEqualTo(StatusComunicacao.FALHOU);
        assertThat(entity.getDataEnvio()).isNull();
        assertThat(entity.getErroEnvio()).contains("SMTP");
        assertThat(entity.getErroEnvio()).doesNotContain("indisponível");
    }

    @Test void updateCannotForgeDeliveredStatus() {
        TenantContext.setCurrentTenant(7L);
        ComunicacaoEnviada existing = new ComunicacaoEnviada();
        existing.setEmpresaId(7L);
        existing.setStatus(StatusComunicacao.FALHOU);
        when(repository.findById(6L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        doAnswer(inv -> {
            ComunicacaoEnviada item = inv.getArgument(1);
            item.setStatus(StatusComunicacao.ENTREGUE);
            return null;
        }).when(mapper).updateEntity(any(), eq(existing));
        service.update(6L, req(TipoComunicacao.EMAIL, StatusComunicacao.ENTREGUE));
        assertThat(existing.getStatus()).isEqualTo(StatusComunicacao.FALHOU);
    }
}
