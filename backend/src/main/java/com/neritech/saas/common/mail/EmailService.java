package com.neritech.saas.common.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.host:}")
    private String smtpHost;

    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public void sendTrialWelcome(String to, String name, String workshop) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Boas-vindas à NERITECH AUTO");
            String safeName = org.springframework.web.util.HtmlUtils.htmlEscape(name);
            String safeWorkshop = org.springframework.web.util.HtmlUtils.htmlEscape(workshop);
            String loginUrl = frontendUrl + "/auth/login";
            helper.setText("<html><body style='margin:0;background:#f4f6fa;font-family:Arial,sans-serif;color:#14233e'>"
                    + "<div style='max-width:560px;margin:30px auto;background:white;border:1px solid #dfe6f1'>"
                    + "<div style='padding:28px;background:#172a50;color:white;font-size:22px;font-weight:bold'>NERITECH AUTO</div>"
                    + "<div style='padding:30px'><h1 style='font-size:24px'>Bem-vindo à NERITECH AUTO</h1>"
                    + "<p>Olá " + safeName + ", o cadastro da oficina " + safeWorkshop + " foi concluído.</p>"
                    + "<p>Acesse com o e-mail informado e a senha que você definiu no cadastro.</p>"
                    + "<p><a href='" + loginUrl + "' style='display:inline-block;padding:13px 20px;background:#3154ff;color:white;text-decoration:none'>Acessar minha oficina</a></p>"
                    + "<p>Por segurança, nunca compartilhe sua senha. Se precisar, use a opção de recuperação de acesso na tela de login.</p>"
                    + "</div><div style='padding:18px 30px;background:#f4f6fa;color:#62718b;font-size:12px'>NERITECH AUTO</div></div></body></html>", true);
            mailSender.send(message);
            log.info("Trial welcome email accepted by SMTP transport");
        } catch (Exception e) {
            throw new org.springframework.mail.MailSendException("Trial welcome delivery unavailable", e);
        }
    }

    /** Synchronous delivery for recovery so outages cannot be silently swallowed. */
    public void sendPasswordResetLink(String to, String name, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperação de Acesso - NeriTechAuto");

            String htmlContent = buildResetEmailTemplate(name, token);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email accepted by SMTP transport");
        } catch (Exception e) {
            // Never include the raw reset link/token in HTTP responses or logs.
            throw new org.springframework.mail.MailSendException("Password reset delivery unavailable", e);
        }
    }

    /** Entrega síncrona: o cadastro do convite deve falhar se o provedor não aceitar a mensagem. */
    public void sendUserInvitationLink(String to, String name, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Convite para acessar o NeriTech Auto");
            String url = frontendUrl + "/auth/reset-password#token=" + token;
            helper.setText("<p>Olá " + org.springframework.web.util.HtmlUtils.htmlEscape(name) + ",</p><p>Você foi convidado para acessar o NERITECH AUTO.</p>"
                    + "<p><a href='" + url + "'>Definir senha e aceitar convite</a></p>"
                    + "<p>Este convite é de uso único e expira em 48 horas.</p>", true);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new org.springframework.mail.MailSendException("Invitation delivery unavailable", ex);
        }
    }

    public boolean isPasswordResetConfigured() {
        return smtpHost != null && !smtpHost.isBlank()
                && fromEmail != null && !fromEmail.isBlank();
    }

    private String buildResetEmailTemplate(String name, String token) {
        // A URL fragment is not sent to the web server or Referer header.
        String resetUrl = frontendUrl + "/auth/reset-password#token=" + token;
        
        return "<html>" +
                "<body style='font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; background-color: #f8fafc; margin: 0; padding: 40px 0;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);'>" +
                "  <div style='background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%); padding: 40px; text-align: center;'>" +
                "    <h1 style='color: #ffffff; margin: 0; font-size: 28px; font-weight: 700; letter-spacing: -0.025em;'>Recuperação de Senha</h1>" +
                "    <p style='color: rgba(255,255,255,0.9); margin-top: 10px; font-size: 16px;'>NeriTechAuto - Sempre com você.</p>" +
                "  </div>" +
                "  " +
                "  <div style='padding: 40px;'>" +
                "    <p style='color: #1e293b; font-size: 18px; font-weight: 600; margin-bottom: 16px;'>Olá " + name + ",</p>" +
                "    <p style='color: #475569; font-size: 16px; line-height: 1.6; margin-bottom: 30px;'>" +
                "      Recebemos uma solicitação para redefinir a senha da sua conta NeriTechAuto. " +
                "      Se você não solicitou essa alteração, pode ignorar este e-mail com segurança." +
                "    </p>" +
                "    " +
                "    <div style='text-align: center; margin: 40px 0;'>" +
                "      <a href='" + resetUrl + "' style='display: inline-block; background-color: #6366f1; color: #ffffff; text-decoration: none; padding: 18px 36px; border-radius: 8px; font-weight: 600; font-size: 16px; box-shadow: 0 4px 6px -1px rgba(99, 102, 241, 0.4);'>Redefinir Minha Senha</a>" +
                "    </div>" +
                "    " +
                "    <p style='color: #64748b; font-size: 14px; text-align: center;'>" +
                "      Este link é válido por 30 minutos. Após esse período, você precisará solicitar uma nova recuperação." +
                "    </p>" +
                "    " +
                "    <hr style='border: 0; border-top: 1px solid #f1f5f9; margin: 40px 0 30px 0;' />" +
                "    " +
                "    <p style='color: #94a3b8; font-size: 12px; line-height: 1.5; text-align: center;'>" +
                "      Se o botão acima não funcionar, copie e cole o seguinte link no seu navegador:<br>" +
                "      <span style='color: #6366f1; word-break: break-all;'>" + resetUrl + "</span>" +
                "    </p>" +
                "  </div>" +
                "  " +
                "  <div style='background-color: #f8fafc; padding: 30px; text-align: center; border-top: 1px solid #f1f5f9;'>" +
                "    <p style='margin: 0; font-size: 12px; color: #94a3b8;'>NeriTechAuto &copy; " + java.time.Year.now().getValue() + " | Todos os direitos reservados.</p>" +
                "  </div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
