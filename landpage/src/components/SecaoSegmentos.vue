<template>
  <section id="segmentos" class="segments">
    <div class="container">
      <div class="seg-header aos-init">
        <span class="section-label">Para cada tipo de oficina</span>
        <h2 class="seg-title">Escolha o perfil. A plataforma se adapta.</h2>
        <p class="seg-subtitle">Mecânica, centro automotivo ou funilaria — o mesmo sistema cobre workflow, estoque, financeiro e comunicação com o cliente.</p>
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
          <span class="seg-tab-icon"><StripeIcon :name="seg.icon" :size="18" /></span>
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
                Experimente grátis por 180 dias →
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
import StripeIcon from './StripeIcon.vue';

const activeTab = ref(0);

const segments = [
  {
    id: 'oficinas',
    icon: 'wrench',
    title: 'Oficinas mecânicas',
    route: 'oficina',
    description: 'Gerencie o fluxo do veículo da entrada à entrega: OS, checklist com fotos, aprovação de orçamento e histórico por placa — em tempo real.',
    benefits: [
      'Workflow do pátio com status por baia',
      'Checklist digital com fotos e vídeos',
      'Orçamento aprovado via WhatsApp ou link',
      'Histórico completo por placa e cliente',
    ],
    screenTitle: 'Workflow — Oficina',
    menuItems: ['Pátio', 'Ordens', 'Clientes', 'Financeiro', 'Estoque'],
    stats: [
      { value: 'Kanban', label: 'Fluxo de OS' },
      { value: 'WhatsApp', label: 'Aprovações' },
      { value: 'Placa', label: 'Histórico' },
    ]
  },
  {
    id: 'centros',
    icon: 'building',
    title: 'Centros automotivos',
    route: 'centro-automotivo',
    description: 'Operações maiores: múltiplas baias, estoque rigoroso, contas a pagar/receber e emissão fiscal — visão unificada do negócio.',
    benefits: [
      'Estoque com baixa automática na OS',
      'Fluxo de caixa e DRE operacional',
      'NF-e e NFS-e integradas',
      'Relatórios gerenciais em tempo real',
    ],
    screenTitle: 'Dashboard — Centro Automotivo',
    menuItems: ['Dashboard', 'Veículos', 'Estoque', 'NF-e', 'Relatórios'],
    stats: [
      { value: 'Multi-baia', label: 'Produção' },
      { value: 'Estoque', label: 'Reposição' },
      { value: 'Fiscal', label: 'NF-e / NFS-e' },
    ]
  },
  {
    id: 'funilaria',
    icon: 'paint',
    title: 'Funilaria e pintura',
    route: 'funilaria',
    description: 'Controle o reparo com registro fotográfico de avarias, orçamento por etapa e rastreabilidade da entrada à entrega.',
    benefits: [
      'Fotos antes/depois na OS',
      'Orçamento detalhado por etapa',
      'Comunicação clara com o cliente',
      'Controle de materiais e insumos',
    ],
    screenTitle: 'Dashboard — Funilaria',
    menuItems: ['Dashboard', 'Reparos', 'Fotos', 'Clientes', 'Materiais'],
    stats: [
      { value: 'Checklist', label: 'Vistoria' },
      { value: 'Avarias', label: 'Mapeamento' },
      { value: 'Garantia', label: 'Registro' },
    ]
  },
];
</script>

<style scoped>
.segments {
  padding: 6rem 0;
  background: var(--light-bg);
  overflow: hidden;
  position: relative;
}

.segments::before {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background-image: radial-gradient(circle, rgba(147, 197, 253, 0.5) 1px, transparent 1px);
  background-size: 28px 28px;
  opacity: 0.35;
  mask-image: linear-gradient(180deg, transparent, black 30%, black 70%, transparent);
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
  font-size: clamp(2.125rem, 4vw, 3.125rem);
  font-weight: 300;
  letter-spacing: -0.04em;
  line-height: 1.06;
  margin-bottom: 1rem;
}

.seg-subtitle {
  font-size: clamp(1.0625rem, 1.5vw, 1.25rem);
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
  display: inline-flex;
  align-items: center;
  color: inherit;
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
  font-weight: 400;
  color: var(--midnight-navy);
  letter-spacing: -0.035em;
  line-height: 1.15;
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
  font-weight: 600;
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
  font-weight: 600;
  color: var(--midnight-navy);
  letter-spacing: -0.03em;
  font-feature-settings: "tnum" 1;
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
