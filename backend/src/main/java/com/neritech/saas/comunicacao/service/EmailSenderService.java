package com.neritech.saas.comunicacao.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    /**
     * Envia e-mail de forma assíncrona.
     */
    @Async
    public void sendEmail(String to, String subject, String body) {
        if (fromEmail == null || fromEmail.isBlank()) {
            log.warn("SMTP não configurado. O e-mail para {} não será enviado. Assunto: {}", to, subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para: {}", to);
        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail para {}: {}", to, e.getMessage());
        }
    }
}
