<template>
  <section id="segmentos" class="segments">
    <div class="container">
      <div class="seg-header aos-init">
        <span class="section-label">Para quem é</span>
        <h2 class="seg-title">Independente do seu <span class="text-gradient">segmento automotivo.</span></h2>
        <p class="seg-subtitle">Não importa se você tem 1 baia ou 50 — a neritechauto se adapta ao seu fluxo e escala com o crescimento da sua empresa.</p>
      </div>

      <!-- Tabs -->
      <div class="seg-tabs aos-init aos-delay-1">
        <button
          v-for="(seg, i) in segments"
          :key="seg.id"
          class="seg-tab"
          :class="{ active: activeTab === i }"
          @click="activeTab = i"
        >
          <span class="seg-tab-icon">{{ seg.icon }}</span>
          <span class="seg-tab-label">{{ seg.title }}</span>
        </button>
      </div>

      <!-- Active Content -->
      <div class="seg-content aos-init aos-delay-2">
        <transition name="seg-fade" mode="out-in">
          <div class="seg-panel" :key="activeTab">
            <div class="seg-panel-text">
              <h3>{{ segments[activeTab].title }}</h3>
              <p class="seg-panel-desc">{{ segments[activeTab].description }}</p>
              <ul class="seg-benefits">
                <li v-for="b in segments[activeTab].benefits" :key="b">
                  <span class="seg-check">✓</span>
                  {{ b }}
                </li>
              </ul>
              <router-link to="/teste-gratis" class="btn btn-primary" id="seg-cta-btn">
                Experimente grátis por 7 dias →
              </router-link>
            </div>
            <div class="seg-panel-visual">
              <div class="seg-mockup">
                <div class="seg-mockup-bar">
                  <span class="sm-dot sm-red"></span>
                  <span class="sm-dot sm-yellow"></span>
                  <span class="sm-dot sm-green"></span>
                  <span class="sm-url">app.neritechauto.com.br/{{ segments[activeTab].route }}</span>
                </div>
                <div class="seg-mockup-body">
                  <div class="smb-sidebar">
                    <div class="smb-logo">N</div>
                    <div class="smb-menu">
                      <div class="smb-item" v-for="mi in segments[activeTab].menuItems" :key="mi" :class="{ 'smb-active': mi === segments[activeTab].menuItems[0] }">{{ mi }}</div>
                    </div>
                  </div>
                  <div class="smb-main">
                    <div class="smb-header">{{ segments[activeTab].screenTitle }}</div>
                    <div class="smb-cards">
                      <div class="smb-stat" v-for="s in segments[activeTab].stats" :key="s.label">
                        <span class="smb-stat-value">{{ s.value }}</span>
                        <span class="smb-stat-label">{{ s.label }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const activeTab = ref(0);

const segments = [
  {
    id: 'oficinas',
    icon: '🔧',
    title: 'Oficinas Mecânicas',
    route: 'oficina',
    description: 'Gerencie ordens de serviço, controle a produtividade dos mecânicos e acompanhe o faturamento em tempo real. Da entrada do veículo à entrega, tudo digitalizado.',
    benefits: [
      'OS digital com checklist fotográfico',
      'Controle de produtividade por técnico',
      'Aprovação de orçamento via WhatsApp',
      'Histórico completo por placa',
    ],
    screenTitle: 'Dashboard — Oficina Central',
    menuItems: ['Dashboard', 'Ordens de Serviço', 'Clientes', 'Financeiro', 'Estoque'],
    stats: [
      { value: '23', label: 'OS Abertas' },
      { value: 'R$ 18.4k', label: 'Faturamento' },
      { value: '94%', label: 'Aprovação' },
    ]
  },
  {
    id: 'centros',
    icon: '🏢',
    title: 'Centros Automotivos',
    route: 'centro-automotivo',
    description: 'Para operações maiores que precisam de controle rigoroso de estoque, múltiplas baias e gestão financeira avançada. Visão 360° do seu negócio.',
    benefits: [
      'Backup automático e seguro na nuvem',
      'Gestão avançada de estoque com curva ABC',
      'DRE e fluxo de caixa em tempo real',
      'Emissão de NF-e e NFS-e integrada',
    ],
    screenTitle: 'Dashboard — Centro Automotivo',
    menuItems: ['Dashboard', 'Veículos', 'Estoque', 'NF-e', 'Relatórios'],
    stats: [
      { value: '5', label: 'Usuários Ilimitados' },
      { value: 'R$ 142k', label: 'Receita/mês' },
      { value: '12', label: 'Técnicos' },
    ]
  },
  {
    id: 'funilaria',
    icon: '🎨',
    title: 'Funilaria e Pintura',
    route: 'funilaria',
    description: 'Controle todo o fluxo de reparo e pintura com registro fotográfico detalhado. Do orçamento à entrega, com rastreabilidade total.',
    benefits: [
      'Registro de avarias com fotos antes/depois',
      'Orçamentos detalhados por etapa',
      'Comunicação direta com seguradoras',
      'Controle de materiais e tintas',
    ],
    screenTitle: 'Dashboard — Funilaria Express',
    menuItems: ['Dashboard', 'Reparos', 'Fotos', 'Seguros', 'Materiais'],
    stats: [
      { value: '8', label: 'Em Reparo' },
      { value: 'R$ 32k', label: 'Faturamento' },
      { value: '4.8★', label: 'Avaliação' },
    ]
  },
  {
    id: 'autopecas',
    icon: '📦',
    title: 'Auto Peças',
    route: 'autopecas',
    description: 'Gestão completa de estoque com curva ABC, alertas de reposição e integração com fornecedores. Nunca mais perca uma venda por falta de peça.',
    benefits: [
      'Estoque com curva ABC inteligente',
      'Cotação automática com fornecedores',
      'PDV integrado ao financeiro',
      'Relatórios de giro e margem por peça',
    ],
    screenTitle: 'Dashboard — Auto Peças Prime',
    menuItems: ['Dashboard', 'Estoque', 'Vendas', 'Fornecedores', 'PDV'],
    stats: [
      { value: '4.200', label: 'SKUs' },
      { value: 'R$ 89k', label: 'Vendas/mês' },
      { value: '18%', label: 'Margem' },
    ]
  },
];
</script>

<style scoped>
.segments {
  padding: 6rem 0;
  background: var(--light-bg);
  overflow: hidden;
}

.seg-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.seg-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.seg-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.65;
}

/* ── Tabs ── */
.seg-tabs {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-bottom: 3rem;
  flex-wrap: wrap;
}

.seg-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: var(--radius-full);
  border: 1.5px solid var(--border);
  background: white;
  font-family: var(--font-body);
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-muted);
  cursor: pointer;
  transition: all var(--transition-base);
}

.seg-tab:hover {
  border-color: rgba(99,91,255,0.3);
  color: var(--midnight-navy);
}

.seg-tab.active {
  background: var(--primary-indigo);
  border-color: var(--primary-indigo);
  color: white;
  box-shadow: var(--shadow-indigo);
}

.seg-tab-icon {
  font-size: 1.125rem;
}

/* ── Panel ── */
.seg-panel {
  display: grid;
  grid-template-columns: 1fr 1.3fr;
  gap: 4rem;
  align-items: center;
}

.seg-panel-text h3 {
  font-size: clamp(1.5rem, 3vw, 2rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
}

.seg-panel-desc {
  font-size: 1.0625rem;
  color: var(--text-muted);
  line-height: 1.7;
  margin-bottom: 1.5rem;
}

.seg-benefits {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 2rem;
}

.seg-benefits li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-main);
}

.seg-check {
  width: 22px;
  height: 22px;
  background: rgba(99,91,255,0.1);
  color: var(--primary-indigo);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 800;
  flex-shrink: 0;
}

/* ── Mockup ── */
.seg-mockup {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}

.seg-mockup-bar {
  height: 36px;
  background: #f1f3f5;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 8px;
  border-bottom: 1px solid #e5e7eb;
}

.sm-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}
.sm-red    { background: #ff5f57; }
.sm-yellow { background: #ffbd2e; }
.sm-green  { background: #28c840; }

.sm-url {
  flex: 1;
  margin-left: 4px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 2px 10px;
  font-size: 0.6875rem;
  color: var(--text-muted);
}

.seg-mockup-body {
  display: grid;
  grid-template-columns: 160px 1fr;
  min-height: 280px;
}

.smb-sidebar {
  background: #f8fafc;
  border-right: 1px solid var(--border);
  padding: 16px 10px;
}

.smb-logo {
  width: 32px;
  height: 32px;
  background: var(--primary-indigo);
  border-radius: 8px;
  color: white;
  font-weight: 800;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.smb-menu {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.smb-item {
  padding: 7px 10px;
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--text-muted);
  border-radius: 6px;
  cursor: default;
}

.smb-item.smb-active {
  background: white;
  color: var(--primary-indigo);
  font-weight: 600;
  box-shadow: var(--shadow-xs);
}

.smb-main {
  padding: 20px;
}

.smb-header {
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 16px;
}

.smb-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.smb-stat {
  background: var(--light-bg);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  text-align: center;
}

.smb-stat-value {
  font-family: var(--font-heading);
  font-size: 1.375rem;
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.03em;
}

.smb-stat-label {
  font-size: 0.6875rem;
  color: var(--text-muted);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

/* ── Transitions ── */
.seg-fade-enter-active, .seg-fade-leave-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}
.seg-fade-enter-from { opacity: 0; transform: translateY(10px); }
.seg-fade-leave-to   { opacity: 0; transform: translateY(-10px); }

/* ── Responsive ── */
@media (max-width: 1024px) {
  .seg-panel {
    grid-template-columns: 1fr;
    gap: 2rem;
  }
  .seg-panel-text { order: 2; text-align: center; }
  .seg-panel-visual { order: 1; }
  .seg-benefits { align-items: center; }
  .seg-mockup-body { grid-template-columns: 1fr; }
  .smb-sidebar { display: none; }
}

@media (max-width: 640px) {
  .seg-tabs { gap: 4px; }
  .seg-tab { padding: 8px 14px; font-size: 0.8125rem; }
  .seg-tab-label { display: none; }
  .seg-tab-icon { font-size: 1.375rem; }
  .smb-cards { grid-template-columns: 1fr; }
}
</style>
