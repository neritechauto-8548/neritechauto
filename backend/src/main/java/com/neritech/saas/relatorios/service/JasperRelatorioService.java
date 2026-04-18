package com.neritech.saas.relatorios.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperRelatorioService {

    public byte[] gerarRelatorioPdf(String relatorioNome, Map<String, Object> parametros, java.util.Collection<?> dados) {
        try {
            // Carregar o arquivo .jrxml do classpath
            InputStream reportStream = getClass().getResourceAsStream("/reports/" + relatorioNome + ".jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Arquivo de relatório não encontrado: " + relatorioNome);
            }

            // Compilar o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Criar DataSource
            net.sf.jasperreports.engine.JRDataSource dataSource = new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(dados);

            // Preencher o relatório com dados
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar para PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }
}
