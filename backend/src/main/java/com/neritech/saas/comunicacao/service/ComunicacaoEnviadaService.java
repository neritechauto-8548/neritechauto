package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaResponse;
import com.neritech.saas.comunicacao.mapper.ComunicacaoEnviadaMapper;
import com.neritech.saas.comunicacao.repository.ComunicacaoEnviadaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComunicacaoEnviadaService {

    private final ComunicacaoEnviadaRepository repository;
    private final ComunicacaoEnviadaMapper mapper;
    private final EmailSenderService emailSenderService;

    @Transactional(readOnly = true)
    public Page<ComunicacaoEnviadaResponse> findAll(Long empresaId, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ComunicacaoEnviadaResponse findById(Long id) {
        ComunicacaoEnviada entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comunicação não encontrada com id: " + id));
        TenantAccess.require(entity.getEmpresaId());
        return mapper.toResponse(entity);
    }

    public ComunicacaoEnviadaResponse create(ComunicacaoEnviadaRequest request) {
        TenantAccess.require(request.empresaId());
        // Não simular integração WhatsApp/SMS/PUSH/telefonia, nem afirmar
        // entrega de mensagens sem confirmação real do provedor.
        if (request.tipoComunicacao() != TipoComunicacao.EMAIL) {
            throw new BusinessException("O envio por este canal ainda não possui provedor integrado.");
        }
        if (request.agendadaPara() != null) {
            throw new BusinessException("Agendamento de mensagens ainda não está disponível.");
        }
        ComunicacaoEnviada entity = mapper.toEntity(request);
        // O cliente HTTP não controla o status operacional da mensagem.
        entity.setStatus(StatusComunicacao.FALHOU);
        entity.setDataEnvio(null);
        entity.setErroEnvio(null);
        try {
            emailSenderService.sendEmail(
                request.destinatarioContato(),
                request.assunto() != null ? request.assunto() : "Notificação NERITECH AUTO",
                request.conteudo()
            );
            // SMTP aceitou o envio. Isso não confirma entrega nem leitura.
            entity.setStatus(StatusComunicacao.ENVIADA);
            entity.setDataEnvio(java.time.LocalDateTime.now());
        } catch (Exception e) {
            entity.setStatus(StatusComunicacao.FALHOU);
            // Não persistir mensagens internas do provedor (podem conter dados sensíveis).
            entity.setErroEnvio("Falha no envio SMTP. Confira a configuração e os registros do servidor.");
        }

        return mapper.toResponse(repository.save(entity));
    }

    public ComunicacaoEnviadaResponse update(Long id, ComunicacaoEnviadaRequest request) {
        TenantAccess.require(request.empresaId());
        ComunicacaoEnviada entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ComunicaÃ§Ã£o nÃ£o encontrada com id: " + id));

        TenantAccess.require(entity.getEmpresaId());
        // O status e os timestamps resultam do provedor/servidor, nunca
        // de dados editáveis pelo usuário ou pelo cliente HTTP.
        StatusComunicacao actualStatus = entity.getStatus();
        var actualSentAt = entity.getDataEnvio();
        var actualDeliveryAt = entity.getDataEntrega();
        var actualReadAt = entity.getDataLeitura();
        String actualError = entity.getErroEnvio();
        mapper.updateEntity(request, entity);
        entity.setStatus(actualStatus);
        entity.setDataEnvio(actualSentAt);
        entity.setDataEntrega(actualDeliveryAt);
        entity.setDataLeitura(actualReadAt);
        entity.setErroEnvio(actualError);
        ComunicacaoEnviada updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        ComunicacaoEnviada entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comunicação não encontrada com id: " + id));
        TenantAccess.require(entity.getEmpresaId());
        repository.delete(entity);
    }
}
