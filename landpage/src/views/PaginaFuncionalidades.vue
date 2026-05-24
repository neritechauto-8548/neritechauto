<template>
  <main class="pagina-funcionalidades">

    <!-- ── Hero ── -->
    <section class="page-hero">
      <div class="container">
        <header class="func-intro aos-init">
          <span class="section-label">Funcionalidades</span>
          <h1 class="func-intro__title">Tudo que sua oficina precisa em um só lugar.</h1>
          <p class="func-intro__subtitle">
            Da abertura da OS até o fechamento do caixa — cada módulo foi projetado para o dia a dia real de uma oficina automotiva.
          </p>
          <div class="func-actions">
            <router-link to="/teste-gratis" class="btn btn-primary btn-lg" id="func-cta-hero">
              Começar teste grátis →
            </router-link>
            <a href="#modulos" class="btn-outline-link">Ver todos os módulos ↓</a>
          </div>
        </header>
      </div>
    </section>

    <!-- ── Módulos detalhados ── -->
    <section id="modulos" class="func-modulos section-surface section-surface--light section-spacer">
      <div class="container">

        <div v-for="(modulo, idx) in modulos" :key="modulo.id" class="modulo-block aos-init" :id="modulo.id">
          <div class="modulo-inner" :class="{ 'modulo-inner--reverse': idx % 2 !== 0 }">

            <!-- Texto -->
            <div class="modulo-text">
              <span class="modulo-tag">{{ modulo.tag }}</span>
              <h2 class="modulo-title">{{ modulo.title }}</h2>
              <p class="modulo-desc">{{ modulo.desc }}</p>
              <ul class="modulo-features">
                <li v-for="f in modulo.features" :key="f">
                  <span class="feature-check">✓</span>
                  {{ f }}
                </li>
              </ul>
            </div>

            <!-- Visual mockup -->
            <div class="modulo-visual">
              <div class="modulo-mockup mockup-frame">
                <div class="mockup-frame__bar">
                  <span class="mockup-dot mockup-dot--red"></span>
                  <span class="mockup-dot mockup-dot--yellow"></span>
                  <span class="mockup-dot mockup-dot--green"></span>
                  <span class="mockup-frame__url">{{ modulo.mockupTitle }}</span>
                </div>
                <div class="modulo-mockup-body">
                  <!-- Header da tela -->
                  <div class="mm-header">
                    <span class="mm-screen-title">{{ modulo.screenTitle }}</span>
                    <span v-if="modulo.badgeText" class="mm-badge">{{ modulo.badgeText }}</span>
                  </div>
                  <!-- Items da tela -->
                  <div class="mm-rows">
                    <div class="mm-row" v-for="(row, i) in modulo.rows" :key="i">
                      <div class="mm-row-left">
                        <span class="mm-dot" :style="{ background: row.color }"></span>
                        <span class="mm-row-label">{{ row.label }}</span>
                      </div>
                      <span class="mm-row-value" :class="row.valueClass">{{ row.value }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </section>

    <!-- ── Integrações rápidas ── -->
    <section class="func-integrations section-surface section-surface--alt section-spacer">
      <div class="container">
        <div class="integrations-header aos-init">
          <span class="section-label">Integrações</span>
          <h2 class="section-title">Conectado com o que você já usa</h2>
          <p class="section-subtitle">O sistema se integra nativamente com as ferramentas do dia a dia da sua oficina.</p>
        </div>
        <div class="integrations-grid aos-init">
          <div class="integration-card" v-for="intg in integrations" :key="intg.name">
            <span class="intg-icon">{{ intg.icon }}</span>
            <span class="intg-name">{{ intg.name }}</span>
            <span class="intg-desc">{{ intg.desc }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ── CTA Final ── -->
    <section class="final-cta section-surface section-surface--alt" id="cta-funcionalidades">
      <div class="container cta-content aos-init">
        <span class="section-label">Comece agora</span>
        <h2 class="cta-title">Pronto para testar todas as funcionalidades?</h2>
        <p class="cta-desc">30 dias grátis, sem cartão de crédito. Configure em minutos e use ainda hoje.</p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary" id="func-cta-final">
            Começar teste grátis
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn-cta-secondary">Falar com a equipe</a>
        </div>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoRodape from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';
useScrollAnimation();

const modulos = [
  {
    id: 'ordem-servico',
    tag: '📋 Ordem de Serviço',
    title: 'OS digital do início ao fim',
    desc: 'Abra, acompanhe e encerre ordens de serviço de forma totalmente digital. Cada OS tem histórico completo: cliente, veículo, peças utilizadas, técnico responsável, tempo de serviço e status em tempo real.',
    features: [
      'Abertura de OS por placa com busca na base nacional',
      'Checklist fotográfico de entrada do veículo',
      'Vínculo automático de peças e serviços à OS',
      'Status em tempo real: Aguardando, Em Andamento, Pronto, Entregue',
      'Histórico completo por cliente e por placa',
      'Impressão e envio da OS por WhatsApp ou e-mail',
    ],
    mockupTitle: 'Ordem de Serviço · OS #0148',
    screenTitle: 'Honda Civic 2019 — ABC-1234',
    badgeText: '🔧 Em andamento',
    rows: [
      { label: 'Troca de pastilha dianteira', value: 'R$ 180,00', color: '#635bff', valueClass: '' },
      { label: 'Disco de freio (par)', value: 'R$ 320,00', color: '#635bff', valueClass: '' },
      { label: 'Mão de obra', value: 'R$ 180,00', color: '#635bff', valueClass: '' },
      { label: 'Total', value: 'R$ 680,00', color: '#10b981', valueClass: 'mm-row-value--bold' },
    ],
  },
  {
    id: 'financeiro',
    tag: '💰 Financeiro',
    title: 'Financeiro integrado, sem planilha',
    desc: 'Contas a pagar e a receber geradas automaticamente a partir das ordens de serviço finalizadas. Tenha visão real do fluxo de caixa, do lucro operacional e das pendências financeiras do dia.',
    features: [
      'Contas a receber geradas ao finalizar a OS',
      'Registro de despesas operacionais (peças, fornecedores, custos fixos)',
      'Fluxo de caixa diário, semanal e mensal',
      'Conciliação de pagamentos: dinheiro, cartão, Pix, boleto',
      'Relatório de lucratividade por OS e por período',
      'Alertas de inadimplência e contas vencidas',
    ],
    mockupTitle: 'Financeiro · Fluxo de Caixa',
    screenTitle: 'Resumo — Maio 2025',
    badgeText: '📈 Atualizado agora',
    rows: [
      { label: 'Entradas do mês', value: 'R$ 12.400', color: '#10b981', valueClass: 'mm-row-value--green' },
      { label: 'Saídas do mês', value: 'R$ 4.200', color: '#ef4444', valueClass: 'mm-row-value--red' },
      { label: 'Saldo operacional', value: 'R$ 8.200', color: '#635bff', valueClass: 'mm-row-value--bold' },
      { label: 'A receber hoje', value: 'R$ 840', color: '#f59e0b', valueClass: '' },
    ],
  },
  {
    id: 'estoque',
    tag: '📦 Estoque',
    title: 'Controle de peças sem complicação',
    desc: 'Cadastre suas peças com código de barras, defina estoque mínimo e receba alertas antes de ficar sem item. A baixa no estoque acontece automaticamente quando a OS é finalizada.',
    features: [
      'Cadastro de peças com código de barras ou referência',
      'Baixa automática de peças ao finalizar a OS',
      'Alertas de estoque mínimo por e-mail ou WhatsApp',
      'Histórico de movimentação por peça',
      'Relatório de giro de estoque e itens parados',
      'Suporte a múltiplos fornecedores por item',
    ],
    mockupTitle: 'Estoque · Peças e Insumos',
    screenTitle: 'Painel de Estoque',
    badgeText: '⚠️ 3 itens em alerta',
    rows: [
      { label: 'Pastilha de freio Bosch', value: '4 unid.', color: '#f59e0b', valueClass: '' },
      { label: 'Filtro de óleo Mann', value: '12 unid.', color: '#10b981', valueClass: '' },
      { label: 'Vela de ignição NGK', value: '2 unid.', color: '#ef4444', valueClass: 'mm-row-value--red' },
      { label: 'Óleo 5W30 Mobil (L)', value: '8 unid.', color: '#10b981', valueClass: '' },
    ],
  },
  {
    id: 'portal-cliente',
    tag: '👤 Portal do Cliente',
    title: 'Seu cliente acompanha tudo pelo celular',
    desc: 'Cada cliente recebe acesso a um portal exclusivo onde visualiza o status do veículo, aprova orçamentos e consulta o histórico de serviços — sem precisar ligar para a oficina.',
    features: [
      'Link exclusivo enviado via WhatsApp após abertura da OS',
      'Aprovação ou rejeição de orçamento com um toque',
      'Acompanhamento de status em tempo real',
      'Histórico completo de serviços do veículo',
      'Notificações automáticas quando o veículo estiver pronto',
      'Avaliação do serviço ao final da OS',
    ],
    mockupTitle: 'Portal do Cliente · Orçamento #0148',
    screenTitle: 'Seu veículo está pronto 🎉',
    badgeText: '✅ Aprovado pelo cliente',
    rows: [
      { label: 'Serviços realizados', value: '3 itens', color: '#10b981', valueClass: '' },
      { label: 'Total aprovado', value: 'R$ 680,00', color: '#635bff', valueClass: 'mm-row-value--bold' },
      { label: 'Status da OS', value: 'Pronto', color: '#10b981', valueClass: 'mm-row-value--green' },
      { label: 'Aprovação', value: 'Via WhatsApp', color: '#25d366', valueClass: '' },
    ],
  },
  {
    id: 'relatorios',
    tag: '📊 Relatórios',
    title: 'Dados para decisões melhores',
    desc: 'Entenda o desempenho da sua oficina com relatórios claros e objetivos. Sem complexidade desnecessária — apenas as informações que realmente importam para o dono de oficina.',
    features: [
      'Relatório de OS por período, técnico e tipo de serviço',
      'Ticket médio e serviços mais realizados',
      'Ranking de clientes por volume de serviços',
      'Performance por mecânico (tempo médio por OS)',
      'Relatório de receita e despesa operacional',
      'Exportação em PDF ou planilha',
    ],
    mockupTitle: 'Relatórios · Desempenho Mensal',
    screenTitle: 'Resumo Operacional — Maio',
    badgeText: null,
    rows: [
      { label: 'OS abertas no mês', value: '47', color: '#635bff', valueClass: '' },
      { label: 'OS finalizadas', value: '44', color: '#10b981', valueClass: 'mm-row-value--green' },
      { label: 'Ticket médio', value: 'R$ 281,00', color: '#635bff', valueClass: '' },
      { label: 'Avaliação média', value: '4.8 ★', color: '#f59e0b', valueClass: '' },
    ],
  },
  {
    id: 'fiscal',
    tag: '🧾 Fiscal',
    title: 'NF-e e NFS-e sem complicação',
    desc: 'Emita notas fiscais eletrônicas diretamente da OS finalizada, com todos os dados do cliente e dos serviços já preenchidos automaticamente. Menos retrabalho, mais conformidade.',
    features: [
      'Emissão de NF-e (produto) integrada à OS',
      'Emissão de NFS-e (serviço) com prefeituras brasileiras',
      'Dados do cliente e CFOP preenchidos automaticamente',
      'Envio automático da nota por e-mail ao cliente',
      'Consulta e cancelamento de notas emitidas',
      'Suporte a Simples Nacional, Lucro Real e Presumido',
    ],
    mockupTitle: 'Fiscal · Nota Fiscal Eletrônica',
    screenTitle: 'NF-e emitida com sucesso',
    badgeText: '✅ Autorizada pela SEFAZ',
    rows: [
      { label: 'Número da NF-e', value: '000.001.047', color: '#635bff', valueClass: '' },
      { label: 'Valor total', value: 'R$ 680,00', color: '#10b981', valueClass: '' },
      { label: 'CFOP', value: '5.102', color: '#635bff', valueClass: '' },
      { label: 'Chave de acesso', value: '••• 8 dígitos •••', color: '#94a3b8', valueClass: '' },
    ],
  },
];

const integrations = [
  { icon: '💬', name: 'WhatsApp Business', desc: 'Envie OS, orçamentos e avisos diretamente pelo WhatsApp.' },
  { icon: '🧾', name: 'SEFAZ', desc: 'Emissão de NF-e e NFS-e com autorização automática.' },
  { icon: '🔍', name: 'Consulta por Placa', desc: 'Busca de dados do veículo pela placa na base nacional.' },
  { icon: '📲', name: 'Pix e Boleto', desc: 'Geração de cobranças integradas ao fechamento da OS.' },
  { icon: '📁', name: 'Google Drive', desc: 'Backup automático de documentos e fotos de vistoria.' },
  { icon: '📧', name: 'E-mail Automático', desc: 'Envio automático de notas, OS e notificações ao cliente.' },
];
</script>

<style scoped>
/* ── Hero ── */
.func-intro {
  text-align: center;
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.func-intro__title {
  font-size: clamp(2.25rem, 5vw, 3.25rem);
  font-weight: 800;
  color: white !important;
  line-height: 1.1;
  letter-spacing: -0.04em;
  margin-bottom: 1rem;
}

.func-intro__subtitle {
  font-size: 1.125rem;
  color: rgba(255,255,255,0.78) !important;
  line-height: 1.65;
  max-width: 560px;
  margin-bottom: 2rem;
}

.func-actions {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  flex-wrap: wrap;
  justify-content: center;
}

/* ── Módulos ── */
.func-modulos { padding: 4rem 0 6rem; }

.modulo-block {
  padding: 5rem 0;
  border-bottom: 1px solid var(--border);
}
.modulo-block:last-child { border-bottom: none; }

.modulo-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
}

.modulo-inner--reverse { direction: rtl; }
.modulo-inner--reverse > * { direction: ltr; }

.modulo-tag {
  display: inline-flex;
  align-items: center;
  background: rgba(99,91,255,0.07);
  border: 1px solid rgba(99,91,255,0.15);
  color: var(--primary);
  font-size: 0.8125rem;
  font-weight: 700;
  padding: 5px 12px;
  border-radius: 99px;
  margin-bottom: 1rem;
  letter-spacing: 0.02em;
}

.modulo-title {
  font-size: clamp(1.5rem, 3vw, 2.125rem);
  font-weight: 800;
  color: var(--midnight-navy);
  line-height: 1.15;
  letter-spacing: -0.03em;
  margin-bottom: 1rem;
}

.modulo-desc {
  font-size: 1rem;
  color: var(--text-muted);
  line-height: 1.7;
  margin-bottom: 1.75rem;
}

.modulo-features {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.modulo-features li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-main);
  line-height: 1.5;
}

.feature-check {
  width: 20px;
  height: 20px;
  background: rgba(99,91,255,0.1);
  color: var(--primary-indigo);
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: 800;
  flex-shrink: 0;
  margin-top: 2px;
}

/* ── Mockup ── */
.modulo-mockup {
  box-shadow: var(--shadow-xl);
}

.modulo-mockup-body {
  background: white;
  padding: 1.25rem;
}

.mm-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.mm-screen-title {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

.mm-badge {
  font-size: 0.75rem;
  font-weight: 600;
  color: #059669;
  background: rgba(5,150,105,0.08);
  border: 1px solid rgba(5,150,105,0.15);
  padding: 3px 10px;
  border-radius: 99px;
}

.mm-rows {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.mm-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: var(--gray-50, #f8fafc);
  border-radius: 8px;
  border: 1px solid var(--border);
}

.mm-row-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.mm-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.mm-row-label {
  font-size: 0.8125rem;
  color: var(--text-main);
  font-weight: 500;
}

.mm-row-value {
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--midnight-navy);
}

.mm-row-value--bold { font-weight: 800; font-size: 0.9375rem; }
.mm-row-value--green { color: #059669; }
.mm-row-value--red { color: #ef4444; }

/* ── Integrações ── */
.func-integrations { background: var(--light-bg); }

.integrations-header {
  text-align: center;
  max-width: 560px;
  margin: 0 auto 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.integrations-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
}

.integration-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  transition: all var(--transition-base);
  box-shadow: var(--shadow-card);
}

.integration-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-border);
}

.intg-icon { font-size: 1.875rem; }

.intg-name {
  font-size: 1rem;
  font-weight: 800;
  color: var(--midnight-navy);
}

.intg-desc {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.55;
}

/* ── CTA ── */
.final-cta { padding: 6rem 0; text-align: center; border-top: 1px solid var(--border); }
.cta-content { display: flex; flex-direction: column; align-items: center; }
.cta-title {
  font-size: clamp(1.75rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  margin-bottom: 0.875rem;
  max-width: 560px;
}
.cta-desc { font-size: 1rem; color: var(--text-muted); margin-bottom: 2rem; }
.cta-actions { display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap; }
.btn-cta-primary {
  display: inline-flex; align-items: center; gap: 8px;
  background: var(--primary); color: white !important; font-weight: 700;
  font-size: 1rem; padding: 0.9rem 1.75rem; border-radius: var(--radius-md);
  transition: all var(--transition-base); box-shadow: var(--shadow-indigo);
  text-decoration: none;
}
.btn-cta-primary:hover { background: var(--primary-dark); transform: translateY(-2px); }
.btn-cta-primary:hover .cta-arrow { transform: translateX(4px); }
.cta-arrow { transition: transform 0.2s; display: inline-block; }
.btn-cta-secondary {
  display: inline-flex; align-items: center;
  background: white; color: var(--midnight-navy) !important; font-weight: 600;
  font-size: 1rem; padding: 0.9rem 1.75rem; border-radius: var(--radius-md);
  border: 1.5px solid var(--border); transition: all var(--transition-base);
  text-decoration: none;
}
.btn-cta-secondary:hover { border-color: var(--primary); color: var(--primary) !important; }

/* ── Responsive ── */
@media (max-width: 1024px) {
  .modulo-inner { grid-template-columns: 1fr; gap: 2.5rem; direction: ltr !important; }
  .modulo-inner--reverse > * { direction: ltr; }
  .integrations-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .func-actions { flex-direction: column; align-items: stretch; }
  .integrations-grid { grid-template-columns: 1fr; }
  .cta-actions { flex-direction: column; align-items: stretch; }
  .btn-cta-primary, .btn-cta-secondary { justify-content: center; }
}
</style>
