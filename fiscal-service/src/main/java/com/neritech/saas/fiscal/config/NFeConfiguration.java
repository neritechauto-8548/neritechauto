package com.neritech.saas.fiscal.config;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.util.ConfiguracoesUtil;
import br.com.swconsultoria.nfe.util.ProxyUtil;
import br.com.swconsultoria.nfe.wsdl.NFeAutorizacao.NfeAutorizacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.swconsultoria.nfe.domain.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.domain.enuns.AmbienteEnum;
import br.com.swconsultoria.nfe.domain.enuns.EstadosEnum;

import java.io.File;

/**
 * Configura a biblioteca Java_NFe com certificado digital e ambiente SEFAZ.
 */
@Slf4j
@Configuration
public class NFeConfiguration {

    @Value("${nfe.certificado-caminho}")
    private String certificadoCaminho;

    @Value("${nfe.certificado-senha}")
    private String certificadoSenha;

    @Value("${nfe.schemas-caminho}")
    private String schemasCaminho;

    @Value("${nfe.ambiente}")
    private String ambiente;

    @Value("${nfe.uf}")
    private String uf;

    @Value("${nfe.timeout:30}")
    private int timeout;

    @Bean
    public ConfiguracoesNfe configuracoesNfe() {
        try {
            log.info("Inicializando configurações NF-e | Ambiente: {} | UF: {}", ambiente, uf);

            // Carrega o certificado A1
            Certificado certificado = CertificadoService.certificadoPfxBytes(
                    lerArquivo(certificadoCaminho), certificadoSenha
            );

            AmbienteEnum ambienteEnum = "PRODUCAO".equalsIgnoreCase(ambiente)
                    ? AmbienteEnum.PRODUCAO
                    : AmbienteEnum.HOMOLOGACAO;

            EstadosEnum estadoEnum = EstadosEnum.valueOf(uf.toUpperCase());

            ConfiguracoesNfe config = ConfiguracoesUtil.iniciaConfiguracoes(
                    certificado,
                    estadoEnum,
                    ambienteEnum,
                    schemasCaminho
            );

            config.setTimeOut(timeout);

            log.info("Configurações NF-e inicializadas com sucesso.");
            return config;

        } catch (Exception e) {
            log.error("ERRO ao inicializar configurações NF-e: {}", e.getMessage(), e);
            // Retorna configuração nula para permitir startup sem certificado em dev
            return null;
        }
    }

    private byte[] lerArquivo(String caminho) throws Exception {
        File file = new File(caminho);
        if (!file.exists()) {
            throw new IllegalStateException("Certificado não encontrado em: " + caminho);
        }
        return java.nio.file.Files.readAllBytes(file.toPath());
    }
}
