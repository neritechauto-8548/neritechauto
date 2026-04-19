package com.neritech.saas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NeriTech API")
                        .description("API do sistema NeriTech para gestão de oficinas")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Suporte NeriTech")
                                .email("suporte@neritech.com.br")
                                .url("https://neritech.com.br"))
                        .license(new License()
                                .name("Proprietário")
                                .url("https://neritech.com.br/licenca")))
                .servers(Arrays.asList(
                        new Server().url("/api").description("Servidor de Produção"),
                        new Server().url("http://localhost:8080/api").description("Servidor Local")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("BearerAuth")
                        .addList("TenantId"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes("TenantId", new SecurityScheme()
                                .name("X-Tenant-Id")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)));
    }

    @Bean
    public GroupedOpenApi clientesOpenApi() {
        return GroupedOpenApi.builder()
                .group("Gestão de Clientes")
                .pathsToMatch(
                        "/v1/clientes/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi allEndpointsOpenApi() {
        return GroupedOpenApi.builder()
                .group("Todos os Endpoints")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi veiculosOpenApi() {
        return GroupedOpenApi.builder()
                .group("Veículos")
                .pathsToMatch(
                        "/v1/veiculos/**",
                        "/v1/modelos-veiculos/**",
                        "/v1/tipos-combustivel/**",
                        "/v1/anos-modelo/**",
                        "/v1/documentos-veiculos/**",
                        "/v1/fotos-veiculos/**",
                        "/v1/historico-quilometragem/**",
                        "/v1/manutencoes-preventivas/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi produtosServicosOpenApi() {
        return GroupedOpenApi.builder()
                .group("Produtos e Serviços")
                .pathsToMatch(
                        "/v1/produtos/**",
                        "/v1/categorias-produtos/**",
                        "/v1/servicos/**",
                        "/v1/categorias-servicos/**",
                        "/v1/servicos-precos/**",
                        "/v1/tabelas-precos/**",
                        "/v1/tempos-servicos/**",
                        "/v1/unidades-medida/**",
                        "/v1/kits-servicos/**",
                        "/v1/fornecedores/**",
                        "/v1/produtos-fornecedores/**",
                        "/v1/especialidades-servicos/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi estoqueOpenApi() {
        return GroupedOpenApi.builder()
                .group("Estoque")
                .pathsToMatch(
                        "/v1/estoques/**",
                        "/v1/lotes-produtos/**",
                        "/v1/reservas-estoque/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi garantiasOpenApi() {
        return GroupedOpenApi.builder()
                .group("Garantias")
                .pathsToMatch(
                        "/v1/garantias/**",
                        "/v1/garantias/tipos/**",
                        "/v1/garantias/resolucoes/**",
                        "/v1/garantias/reclamacoes/**",
                        "/v1/garantias/itens/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi ordensServicoOpenApi() {
        return GroupedOpenApi.builder()
                .group("Ordens de Serviço")
                .pathsToMatch(
                        "/v1/ordens-servico/**",
                        "/v1/ordens-servico/checklists/**",
                        "/v1/ordens-servico/it-checklist/**",
                        "/v1/orcamentos/**",
                        "/v1/status-os/**",
                        "/v1/itens-os-produtos/**",
                        "/v1/itens-os-servicos/**",
                        "/v1/diagnosticos/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi financeiroOpenApi() {
        return GroupedOpenApi.builder()
                .group("Financeiro")
                .pathsToMatch(
                        "/v1/financeiro/contas-receber/**",
                        "/v1/financeiro/contas-pagar/**",
                        "/v1/financeiro/contas-bancarias/**",
                        "/v1/financeiro/fluxo-caixa/**",
                        "/v1/financeiro/faturas/**",
                        "/v1/financeiro/itens-fatura/**",
                        "/v1/financeiro/condicoes-pagamento/**",
                        "/v1/financeiro/formas-pagamento/**",
                        "/v1/financeiro/centros-custo/**",
                        "/v1/financeiro/comissoes/**",
                        "/v1/financeiro/conciliacoes/**",
                        "/v1/financeiro/lancamentos-contabeis/**",
                        "/v1/financeiro/pagamentos/**",
                        "/v1/financeiro/parcelas-pagamento/**",
                        "/v1/financeiro/plano-contas/**",
                        "/v1/financeiro/nfe/**",
                        "/v1/financeiro/impostos/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi fiscalOpenApi() {
        return GroupedOpenApi.builder()
                .group("Fiscal")
                .pathsToMatch(
                        "/v1/fiscal/sped/**",
                        "/v1/fiscal/regimes-tributarios/**",
                        "/v1/fiscal/ncm/**",
                        "/v1/fiscal/cst/**",
                        "/v1/fiscal/configuracoes-nfe/**",
                        "/v1/fiscal/cfop/**",
                        "/v1/fiscal/certificados-digitais/**",
                        "/v1/fiscal/aliquotas-impostos/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi relatoriosOpenApi() {
        return GroupedOpenApi.builder()
                .group("Relatórios")
                .pathsToMatch(
                        "/v1/relatorios/**",
                        "/v1/metricas/**",
                        "/v1/logs-alteracoes/**",
                        "/v1/logs-sistema/**",
                        "/v1/kpis/**",
                        "/v1/exportacoes/**",
                        "/v1/backups/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi comunicacaoOpenApi() {
        return GroupedOpenApi.builder()
                .group("Comunicação")
                .pathsToMatch(
                        "/v1/comunicacao/templates/**",
                        "/v1/comunicacao/notificacoes/**",
                        "/v1/comunicacao/listas/**",
                        "/v1/comunicacao/config/whatsapp/**",
                        "/v1/comunicacao/config/sms/**",
                        "/v1/comunicacao/config/email/**",
                        "/v1/comunicacao/questionarios/**",
                        "/v1/comunicacao/it-checklist/**",
                        "/v1/comunicacao/campanhas/**",
                        "/v1/comunicacao/envios/**",
                        "/v1/comunicacao/alertas/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi integracaoOpenApi() {
        return GroupedOpenApi.builder()
                .group("Integração")
                .pathsToMatch(
                        "/v1/integracao/webhooks/**",
                        "/v1/integracao/tokens/**",
                        "/v1/integracao/sincronizacoes/**",
                        "/v1/integracao/mapeamentos/**",
                        "/v1/integracao/logs/**",
                        "/v1/integracao/integracoes/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi agendamentosOpenApi() {
        return GroupedOpenApi.builder()
                .group("Agendamentos")
                .pathsToMatch(
                        "/v1/agendamentos/**",
                        "/v1/agendamentos/tipos/**",
                        "/v1/agendamentos/recursos/**",
                        "/v1/agendamentos/reagendamentos/**",
                        "/v1/agendamentos/bloqueios/**",
                        "/v1/agendamentos/disponibilidade/**",
                        "/v1/agendamentos/lembretes/**",
                        "/v1/agendamentos/lista-espera/**",
                        "/v1/agendamentos/no-shows/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi rhOpenApi() {
        return GroupedOpenApi.builder()
                .group("RH")
                .pathsToMatch(
                        "/v1/rh/funcionarios/**",
                        "/v1/rh/departamentos/**",
                        "/v1/rh/especialidades/**",
                        "/v1/rh/cargos/**",
                        "/v1/rh/comissoes/**",
                        "/v1/rh/treinamentos/**",
                        "/v1/rh/horarios-trabalho/**",
                        "/v1/rh/funcionarios-especialidades/**",
                        "/v1/rh/documentos/**",
                        "/v1/rh/escalas-trabalho/**",
                        "/v1/rh/avaliacoes/**",
                        "/v1/rh/faltas-atrasos/**",
                        "/v1/rh/mecanicos/**",
                        "/v1/rh/certificacoes/**",
                        "/v1/rh/ferias/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi empresaOpenApi() {
        return GroupedOpenApi.builder()
                .group("Empresa")
                .pathsToMatch(
                        "/v1/enderecos-empresa/**",
                        "/v1/setores-empresa/**",
                        "/v1/localizacoes-empresa/**",
                        "/v1/departamentos-contabio-empresa/**",
                        "/v1/modelos-documentos/**",
                        "/v1/configuracoes-oficina/**",
                        "/v1/configuracoes-empresa/**",
                        "/v1/assinaturas-empresas/**",
                        "/v1/configuracoes-fiscais/**",
                        "/v1/planos-assinatura/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi gestaoUsuariosOpenApi() {
        return GroupedOpenApi.builder()
                .group("Gestão de Usuários")
                .pathsToMatch(
                        "/auth/**",
                        "/usuarios/**",
                        "/funcoes/**",
                        "/permissoes/**",
                        "/auditoria/**"
                )
                .build();
    }
}
