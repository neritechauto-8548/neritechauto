package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ConfiguracaoEmail;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailResponse;
import com.neritech.saas.comunicacao.mapper.ConfiguracaoEmailMapper;
import com.neritech.saas.comunicacao.repository.ConfiguracaoEmailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfiguracaoEmailService {

    private final ConfiguracaoEmailRepository repository;
    private final ConfiguracaoEmailMapper mapper;

    @Transactional(readOnly = true)
    public Page<ConfiguracaoEmailResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmailResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o de Email nÃ£o encontrada com id: " + id));
    }

    public ConfiguracaoEmailResponse create(ConfiguracaoEmailRequest request) {
        ConfiguracaoEmail entity = mapper.toEntity(request);
        ConfiguracaoEmail savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ConfiguracaoEmailResponse update(Long id, ConfiguracaoEmailRequest request) {
        ConfiguracaoEmail entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o de Email nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ConfiguracaoEmail updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Configuração de Email não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }

    public void testEmail(String destinatario, ConfiguracaoEmailRequest request) {
        org.springframework.mail.javamail.JavaMailSenderImpl mailSender = new org.springframework.mail.javamail.JavaMailSenderImpl();
        mailSender.setHost(request.servidorSmtp());
        mailSender.setPort(request.portaSmtp() != null ? request.portaSmtp() : 587);
        mailSender.setUsername(request.usuarioSmtp() != null && !request.usuarioSmtp().isBlank() ? request.usuarioSmtp() : request.remetenteEmail());
        mailSender.setPassword(request.senhaSmtp());

        java.util.Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.connectiontimeout", "5000");
        
        if (request.criptografia() == com.neritech.saas.comunicacao.domain.enums.Criptografia.SSL) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", String.valueOf(mailSender.getPort()));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        } else if (request.criptografia() == com.neritech.saas.comunicacao.domain.enums.Criptografia.TLS || request.criptografia() == com.neritech.saas.comunicacao.domain.enums.Criptografia.STARTTLS) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(request.remetenteEmail());
            helper.setTo(destinatario);
            helper.setSubject("NeriTechAuto - Teste de Configuração de E-mail");
            helper.setText("<h1>Email configurado com sucesso!</h1><p>Esta é uma mensagem de teste do NeriTechAuto para validar suas configurações de SMTP.</p>", true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail de teste: " + e.getMessage(), e);
        }
    }
}
