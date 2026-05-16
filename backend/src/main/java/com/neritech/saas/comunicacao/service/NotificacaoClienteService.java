package com.neritech.saas.comunicacao.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.repository.TemplateComunicacaoRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoClienteService {

    private final TemplateComunicacaoRepository templateRepository;
    private final com.neritech.saas.cliente.repository.ClienteRepository clienteRepository;
    private final EmailSenderService emailSenderService;

    /**
     * Envia notificação automática para o cliente baseada em um template.
     */
    public void enviarNotificacaoOS(OrdemServico os, Long templateId) {
        if (templateId == null) return;

        templateRepository.findById(templateId).ifPresent(template -> {
            Cliente cliente = clienteRepository.findById(os.getClienteId()).orElse(null);
            if (cliente == null) return;

            String conteudo = processarTemplate(template.getConteudo(), os, cliente);
            String assunto = processarTemplate(template.getAssunto() != null ? template.getAssunto() : "Notificação NeriTech", os, cliente);
            
            // Disparo por tipo de canal
            if (template.getTipoTemplate() == com.neritech.saas.comunicacao.domain.enums.TipoTemplate.EMAIL) {
                if (cliente.getEmail() != null && !cliente.getEmail().isBlank()) {
                    emailSenderService.sendEmail(cliente.getEmail(), assunto, conteudo);
                } else {
                    log.warn("Cliente {} não possui e-mail cadastrado para receber notificação.", cliente.getNomeCompleto());
                }
            } else {
                // Outros canais (WhatsApp, SMS) - Log por enquanto
                log.info("DISPARO DE NOTIFICAÇÃO ({}) PARA CLIENTE {}: {}", 
                        template.getTipoTemplate(), cliente.getNomeCompleto(), conteudo);
            }
            
            registrarEnvio(os, cliente, template, conteudo);
        });
    }

    private String processarTemplate(String template, OrdemServico os, Cliente cliente) {
        Map<String, String> variaveis = new HashMap<>();
        variaveis.put("{{CLIENTE}}", cliente.getNomeCompleto() != null ? cliente.getNomeCompleto() : cliente.getRazaoSocial());
        variaveis.put("{{OS_NUMERO}}", os.getNumeroOS());
        variaveis.put("{{STATUS}}", os.getStatus() != null ? os.getStatus().getNome() : "Atualizado");
        variaveis.put("{{VALOR_TOTAL}}", os.getValorTotal() != null ? os.getValorTotal().toString() : "0.00");
        
        String resultado = template;
        for (Map.Entry<String, String> entry : variaveis.entrySet()) {
            resultado = resultado.replace(entry.getKey(), entry.getValue());
        }
        return resultado;
    }

    private void registrarEnvio(OrdemServico os, Cliente cliente, TemplateComunicacao template, String conteudo) {
        // Implementar persistência na tabela de comunicações enviadas futuramente
        log.info("Notificação registrada para auditoria. OS: {}", os.getNumeroOS());
    }
}
