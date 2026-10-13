package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantAccess;
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
        ComunicacaoEnviada entity = mapper.toEntity(request);

        if (request.tipoComunicacao() == com.neritech.saas.comunicacao.domain.enums.TipoComunicacao.EMAIL) {
            try {
                emailSenderService.sendEmail(
                    request.destinatarioContato(),
                    request.assunto() != null ? request.assunto() : "Notificação NeriTechAuto",
                    request.conteudo()
                );
                entity.setStatus(com.neritech.saas.comunicacao.domain.enums.StatusComunicacao.ENVIADA);
                entity.setDataEnvio(java.time.LocalDateTime.now());
            } catch (Exception e) {
                entity.setStatus(com.neritech.saas.comunicacao.domain.enums.StatusComunicacao.FALHOU);
                entity.setErroEnvio(e.getMessage());
            }
        } else if (request.tipoComunicacao() == com.neritech.saas.comunicacao.domain.enums.TipoComunicacao.WHATSAPP) {
            entity.setStatus(com.neritech.saas.comunicacao.domain.enums.StatusComunicacao.ENVIADA);
            entity.setDataEnvio(java.time.LocalDateTime.now());
        }

        ComunicacaoEnviada savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ComunicacaoEnviadaResponse update(Long id, ComunicacaoEnviadaRequest request) {
        TenantAccess.require(request.empresaId());
        ComunicacaoEnviada entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ComunicaÃ§Ã£o nÃ£o encontrada com id: " + id));

        TenantAccess.require(entity.getEmpresaId());
        mapper.updateEntity(request, entity);
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
