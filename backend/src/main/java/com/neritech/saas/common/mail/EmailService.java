package com.neritech.saas.common.mail;

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
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:neritechauto@gmail.com}")
    private String fromEmail;

    @Async
    public void sendTrialCredentials(String to, String name, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Seja bem-vindo ao NeriTech! Seu acesso foi criado.");

            String htmlContent = buildTrialEmailTemplate(name, to, password);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Trial credentials email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send trial credentials email to: {}", to, e);
        }
    }

    private String buildTrialEmailTemplate(String name, String email, String password) {
        String loginUrl = "http://localhost:4200/login"; // URL do sistema cliente
        
        return "<html>" +
                "<body style='font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; background-color: #f8fafc; margin: 0; padding: 40px 0;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);'>" +
                "  <!-- Header with Gradient -->" +
                "  <div style='background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%); padding: 40px; text-align: center;'>" +
                "    <h1 style='color: #ffffff; margin: 0; font-size: 28px; font-weight: 700; letter-spacing: -0.025em;'>Bem-vindo à NeriTech</h1>" +
                "    <p style='color: rgba(255,255,255,0.9); margin-top: 10px; font-size: 16px;'>Sua oficina elevada ao próximo nível.</p>" +
                "  </div>" +
                "  " +
                "  <div style='padding: 40px;'>" +
                "    <p style='color: #1e293b; font-size: 18px; font-weight: 600; margin-bottom: 16px;'>Olá " + name + ",</p>" +
                "    <p style='color: #475569; font-size: 16px; line-height: 1.6; margin-bottom: 30px;'>" +
                "      Estamos muito felizes em ter você conosco! Você acaba de dar o primeiro passo para modernizar a sua gestão. " +
                "      Aproveite os seus <strong>7 dias de acesso total gratuito</strong> para explorar todas as nossas ferramentas." +
                "    </p>" +
                "    " +
                "    <div style='background-color: #f1f5f9; border-radius: 8px; padding: 24px; margin-bottom: 35px; border: 1px solid #e2e8f0;'>" +
                "      <p style='margin: 0 0 16px 0; font-size: 12px; color: #64748b; text-transform: uppercase; font-weight: 700; letter-spacing: 0.1em;'>Suas Credenciais de Acesso</p>" +
                "      " +
                "      <div style='margin-bottom: 12px; display: flex; align-items: center;'>" +
                "        <span style='color: #64748b; font-size: 14px; width: 60px; display: inline-block;'>E-mail:</span>" +
                "        <span style='color: #1e293b; font-size: 15px; font-weight: 600;'>" + email + "</span>" +
                "      </div>" +
                "      " +
                "      <div style='display: flex; align-items: center;'>" +
                "        <span style='color: #64748b; font-size: 14px; width: 60px; display: inline-block;'>Senha:</span>" +
                "        <span style='background-color: #ffffff; color: #6366f1; padding: 4px 10px; border-radius: 4px; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, \"Liberation Mono\", \"Courier New\", monospace; font-size: 14px; font-weight: 700; border: 1px solid #cbd5e1;'>" + password + "</span>" +
                "      </div>" +
                "    </div>" +
                "    " +
                "    <div style='text-align: center;'>" +
                "      <a href='" + loginUrl + "' style='display: inline-block; background-color: #6366f1; color: #ffffff; text-decoration: none; padding: 16px 32px; border-radius: 8px; font-weight: 600; font-size: 16px; transition: background-color 0.2s;'>Entrar no Painel Agora</a>" +
                "      <p style='margin-top: 15px; color: #94a3b8; font-size: 13px;'>Link seguro: " + loginUrl + "</p>" +
                "    </div>" +
                "    " +
                "    <hr style='border: 0; border-top: 1px solid #f1f5f9; margin: 40px 0 30px 0;' />" +
                "    " +
                "    <div style='color: #64748b; font-size: 14px; line-height: 1.6;'>" +
                "      <p style='margin-bottom: 8px;'><strong>Próximos passos recomendados:</strong></p>" +
                "      <ul style='padding-left: 20px; margin: 0;'>" +
                "        <li>Complete o cadastro da sua oficina nas configurações.</li>" +
                "        <li>Cadastre o seu primeiro cliente e veículo.</li>" +
                "        <li>Altere sua senha temporária no menu de perfil.</li>" +
                "      </ul>" +
                "    </div>" +
                "  </div>" +
                "  " +
                "  <div style='background-color: #f8fafc; padding: 30px; text-align: center; border-top: 1px solid #f1f5f9;'>" +
                "    <p style='margin: 0; font-size: 12px; color: #94a3b8;'>NeriTech &copy; " + java.time.Year.now().getValue() + " | Todos os direitos reservados.</p>" +
                "    <p style='margin: 5px 0 0 0; font-size: 11px; color: #cbd5e1;'>Você está recebendo este e-mail porque solicitou um teste em neritech.com.br</p>" +
                "  </div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
