<template>
  <section id="demonstracao" class="prints section-surface section-surface--white section-spacer">
    <div class="container">
      <header class="section-header aos-init">
        <span class="section-label">Demonstração</span>
        <h2 class="section-title">Veja o sistema por dentro</h2>
        <p class="section-subtitle">
          Interface clara, organizada e pensada para o dia a dia da sua oficina — do dashboard ao portal do cliente.
        </p>
      </header>

      <div class="prints-tabs aos-init aos-delay-1">
        <button
          v-for="(tab, i) in tabs"
          :key="tab.id"
          class="prints-tab"
          :class="{ active: activeTab === i }"
          @click="activeTab = i"
        >
          {{ tab.label }}
        </button>
      </div>

      <div class="prints-showcase aos-init aos-delay-2">
        <transition name="fade-print" mode="out-in">
          <div class="prints-layout" :key="activeTab">
            <div class="prints-copy">
              <h3>{{ tabs[activeTab].title }}</h3>
              <p>{{ tabs[activeTab].desc }}</p>
              <ul class="prints-features">
                <li v-for="f in tabs[activeTab].features" :key="f">
                  <span class="feature-check">✓</span>
                  {{ f }}
                </li>
              </ul>
            </div>
            <div class="prints-visual">
              <div class="mockup-frame prints-mockup">
                <div class="mockup-frame__bar">
                  <span class="mockup-dot mockup-dot--red"></span>
                  <span class="mockup-dot mockup-dot--yellow"></span>
                  <span class="mockup-dot mockup-dot--green"></span>
                  <span class="mockup-frame__url">{{ tabs[activeTab].url }}</span>
                </div>
                <div class="prints-screen">
                  <img
                    v-if="tabs[activeTab].image"
                    :src="tabs[activeTab].image"
                    :alt="tabs[activeTab].title"
                    loading="lazy"
                  />
                  <div v-else class="os-preview">
                    <div class="os-row" v-for="row in osPreview" :key="row.plate">
                      <span class="os-row__vehicle">
                        <strong>{{ row.vehicle }}</strong>
                        <small>{{ row.plate }}</small>
                      </span>
                      <span>{{ row.service }}</span>
                      <span :class="['os-tag', `os-tag--${row.type}`]">{{ row.status }}</span>
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
import imgFinancial from '../assets/images/financial.png';
import imgMobile from '../assets/images/mobile.png';

const activeTab = ref(0);

const osPreview = [
  { vehicle: 'Honda Civic', plate: 'BRA-2E12', service: 'Revisão de freios', status: 'Em andamento', type: 'blue' },
  { vehicle: 'Toyota Corolla', plate: 'KEL-4910', service: 'Troca de óleo', status: 'Aguardando', type: 'amber' },
  { vehicle: 'Chevrolet Onix', plate: 'PXT-9182', service: 'Diagnóstico', status: 'Concluído', type: 'green' },
];

const tabs = [
  {
    id: 'dashboard',
    label: 'Dashboard',
    title: 'Visão geral da operação',
    desc: 'Acompanhe OS abertas, faturamento do mês e alertas de estoque em um painel único e objetivo.',
    url: 'app.neritechauto.com.br/dashboard',
    image: imgFinancial,
    features: ['Indicadores em tempo real', 'Pátio e produção', 'Alertas inteligentes'],
  },
  {
    id: 'os',
    label: 'Ordens de Serviço',
    title: 'Controle completo de OS',
    desc: 'Do orçamento à entrega: status, peças, mão de obra e histórico por veículo em um só lugar.',
    url: 'app.neritechauto.com.br/ordens',
    image: null,
    features: ['Kanban de produção', 'Checklist com fotos', 'Histórico por placa'],
  },
  {
    id: 'financeiro',
    label: 'Financeiro',
    title: 'Fluxo de caixa sem planilhas',
    desc: 'Contas a pagar e receber integradas às ordens de serviço. Saiba exatamente quanto sua oficina fatura.',
    url: 'app.neritechauto.com.br/financeiro',
    image: imgFinancial,
    features: ['Contas a pagar e receber', 'Conciliação simplificada', 'Relatórios gerenciais'],
  },
  {
    id: 'portal',
    label: 'Portal do Cliente',
    title: 'Experiência premium para o cliente',
    desc: 'Seu cliente acompanha o serviço, visualiza fotos e aprova orçamentos pelo celular.',
    url: 'portal.neritechauto.com.br',
    image: imgMobile,
    features: ['Acompanhamento em tempo real', 'Aprovação de orçamento', 'Histórico de serviços'],
  },
];
</script>

<style scoped>
.prints-tabs {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 3rem;
}

.prints-tab {
  padding: 10px 18px;
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

.prints-tab:hover {
  border-color: var(--primary-border);
  color: var(--midnight-navy);
}

.prints-tab.active {
  background: var(--primary);
  border-color: var(--primary);
  color: white;
  box-shadow: var(--shadow-indigo);
}

.prints-layout {
  display: grid;
  grid-template-columns: 0.9fr 1.1fr;
  gap: 4rem;
  align-items: center;
}

.prints-copy h3 {
  font-size: clamp(1.375rem, 2.5vw, 1.875rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.prints-copy p {
  font-size: 1rem;
  color: var(--text-muted);
  line-height: 1.65;
  margin-bottom: 1.5rem;
}

.prints-features {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.prints-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-main);
}

.prints-mockup { box-shadow: var(--shadow-xl); }

.prints-screen {
  background: var(--gray-50);
  min-height: 300px;
  overflow: hidden;
}

.prints-screen img {
  width: 100%;
  height: auto;
  display: block;
  object-fit: cover;
  object-position: top;
}

.os-preview {
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.os-row {
  display: grid;
  grid-template-columns: 1.2fr 1fr 0.95fr;
  gap: 8px;
  align-items: center;
  padding: 12px 14px;
  background: white;
  border: 1px solid var(--border);
  border-radius: 10px;
  font-size: 0.8125rem;
}

.os-row__vehicle strong {
  display: block;
  color: var(--midnight-navy);
}

.os-row__vehicle small {
  color: var(--text-light);
  font-size: 0.75rem;
}

.os-tag {
  font-size: 0.6875rem;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 6px;
  justify-self: end;
}

.os-tag--blue  { background: var(--primary-light); color: var(--primary); }
.os-tag--amber { background: rgba(245, 158, 11, 0.12); color: #D97706; }
.os-tag--green { background: rgba(5, 150, 105, 0.1); color: #059669; }

.fade-print-enter-active,
.fade-print-leave-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}
.fade-print-enter-from,
.fade-print-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

@media (max-width: 900px) {
  .prints-layout {
    grid-template-columns: 1fr;
    gap: 2rem;
    text-align: center;
  }
  .prints-features { align-items: flex-start; text-align: left; }
}
</style>
