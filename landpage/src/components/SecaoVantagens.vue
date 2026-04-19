<template>
  <section class="advantages">
    <div class="container">
      <div class="adv-header aos-init">
        <span class="section-label">Por que a neritechauto?</span>
        <h2 class="adv-title">Mais do que um sistema — <span class="text-gradient-vibrant">um parceiro de negócio.</span></h2>
        <p class="adv-subtitle">Diferente dos softwares genéricos, a neritechauto foi construída do zero para o setor automotivo brasileiro.</p>
      </div>

      <div class="adv-grid">
        <div
          class="adv-card aos-init"
          :class="[`aos-delay-${i + 1}`, card.accent]"
          v-for="(card, i) in cards"
          :key="card.title"
        >
          <div class="adv-icon" :style="{ background: card.iconBg }">
            <span>{{ card.icon }}</span>
          </div>
          <h3>{{ card.title }}</h3>
          <p>{{ card.desc }}</p>
          <div class="adv-highlight" v-if="card.highlight">
            <span class="adv-highlight-icon">{{ card.highlightIcon }}</span>
            <span>{{ card.highlight }}</span>
          </div>
        </div>
      </div>

      <!-- Comparison strip -->
      <div class="adv-compare aos-init aos-delay-3">
        <div class="compare-inner">
          <div class="compare-item" v-for="c in compares" :key="c.label">
            <div class="compare-them">
              <span class="compare-x">✗</span>
              <span>{{ c.them }}</span>
            </div>
            <div class="compare-divider"></div>
            <div class="compare-us">
              <span class="compare-check">✓</span>
              <span>{{ c.us }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const cards = [
  {
    icon: '🤝',
    iconBg: 'linear-gradient(135deg, rgba(99,91,255,0.15), rgba(139,92,246,0.1))',
    title: 'Suporte Humano Real',
    desc: 'Nada de bots genéricos. Nosso time de especialistas responde via WhatsApp em até 2 minutos. Conhecemos oficinas de verdade.',
    highlight: 'Tempo médio de resposta: 1min 45s',
    highlightIcon: '⚡',
    accent: '',
  },
  {
    icon: '🚀',
    iconBg: 'linear-gradient(135deg, rgba(0,200,83,0.15), rgba(16,185,129,0.1))',
    title: 'Migração 100% Gratuita',
    desc: 'Trazemos todos os seus dados do sistema anterior sem custo extra. Cadastros, histórico de OS, clientes — tudo migrado pela nossa equipe.',
    highlight: 'Migração completa em até 48 horas',
    highlightIcon: '📦',
    accent: '',
  },
  {
    icon: '🔓',
    iconBg: 'linear-gradient(135deg, rgba(0,216,255,0.15), rgba(8,145,178,0.1))',
    title: 'Sem Fidelidade ou Multa',
    desc: 'Cancele quando quiser, sem burocracia. Se não gostar, devolvemos seu dinheiro nos primeiros 30 dias — sem pergunta nenhuma.',
    highlight: 'Garantia de satisfação de 30 dias',
    highlightIcon: '🛡️',
    accent: '',
  },
  {
    icon: '🔄',
    iconBg: 'linear-gradient(135deg, rgba(245,158,11,0.15), rgba(217,119,6,0.1))',
    title: 'Atualizações Constantes',
    desc: 'Novas funcionalidades toda semana, sem custos adicionais. Você acessa melhorias automaticamente — sem downloads, sem espera.',
    highlight: '+120 melhorias lançadas em 2025',
    highlightIcon: '📊',
    accent: '',
  },
];

const compares = [
  { label: 'Suporte', them: 'Chatbot com fila de espera', us: 'WhatsApp direto com especialista' },
  { label: 'Migração', them: 'Custo extra ou faça você mesmo', us: 'Migração gratuita feita por nós' },
  { label: 'Contrato', them: 'Fidelidade de 12 meses', us: 'Cancele quando quiser' },
  { label: 'Atualizações', them: 'Versão defasada por meses', us: 'Updates semanais inclusos' },
];
</script>

<style scoped>
.advantages {
  padding: 6rem 0;
  background: var(--midnight-navy);
  position: relative;
  overflow: hidden;
}

.advantages::before {
  content: '';
  position: absolute;
  top: -150px;
  right: -150px;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(99,91,255,0.12) 0%, transparent 65%);
  pointer-events: none;
}

.advantages::after {
  content: '';
  position: absolute;
  bottom: -100px;
  left: -100px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(0,216,255,0.08) 0%, transparent 60%);
  pointer-events: none;
}

/* ── Header ── */
.adv-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto 3.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.adv-header .section-label {
  color: rgba(255,255,255,0.5);
}

.adv-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  color: white;
  margin-bottom: 1rem;
}

.adv-subtitle {
  font-size: 1.125rem;
  color: rgba(255,255,255,0.6);
  line-height: 1.65;
}

/* ── Grid ── */
.adv-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.25rem;
  margin-bottom: 4rem;
}

.adv-card {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: var(--radius-xl);
  padding: 2rem;
  transition: all var(--transition-base);
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.adv-card:hover {
  background: rgba(255,255,255,0.08);
  border-color: rgba(255,255,255,0.15);
  transform: translateY(-4px);
}

.adv-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.adv-card h3 {
  font-size: 1.125rem;
  font-weight: 700;
  color: white;
  font-family: var(--font-body);
}

.adv-card p {
  font-size: 0.9rem;
  color: rgba(255,255,255,0.6);
  line-height: 1.65;
}

.adv-highlight {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid rgba(255,255,255,0.08);
  font-size: 0.8125rem;
  font-weight: 600;
  color: rgba(255,255,255,0.75);
}

.adv-highlight-icon {
  font-size: 1rem;
}

/* ── Comparison Strip ── */
.adv-compare {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: var(--radius-xl);
  padding: 2rem;
}

.compare-inner {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.25rem;
}

.compare-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border-radius: var(--radius-lg);
  background: rgba(255,255,255,0.03);
}

.compare-them {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  color: rgba(255,255,255,0.45);
  text-decoration: line-through;
}

.compare-x {
  color: #ef4444;
  font-weight: 700;
  font-size: 0.875rem;
}

.compare-divider {
  width: 1px;
  height: 28px;
  background: rgba(255,255,255,0.1);
  flex-shrink: 0;
}

.compare-us {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255,255,255,0.85);
}

.compare-check {
  width: 20px;
  height: 20px;
  background: rgba(16,185,129,0.2);
  color: #10b981;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: 800;
  flex-shrink: 0;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .adv-grid { grid-template-columns: repeat(2, 1fr); }
  .compare-inner { grid-template-columns: 1fr; }
}

@media (max-width: 640px) {
  .adv-grid { grid-template-columns: 1fr; }
  .compare-item { flex-direction: column; text-align: center; gap: 0.75rem; }
  .compare-divider { width: 40px; height: 1px; }
}
</style>
