<template>
  <section class="advantages">
    <div class="container">
      
      <!-- Premium Stripe-style Light Header -->
      <div class="adv-header aos-init">
        <span class="section-label">Diferenciais</span>
        <h2 class="adv-title">Mais do que um sistema — <span class="text-gradient-vibrant">um parceiro de negócio.</span></h2>
        <p class="adv-subtitle">Diferente dos softwares genéricos e complexos, a NeriTechAuto foi desenhada focando no fluxo real das oficinas brasileiras.</p>
      </div>

      <!-- Stripe-style Clean Cards Grid -->
      <div class="adv-grid">
        <div
          class="adv-card aos-init"
          :class="[`adv-card-accent`, card.accent]"
          v-for="card in cards"
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

      <!-- Clean Comparison Strip (UltraCar & ClickUp Inspired) -->
      <div class="adv-compare aos-init">
        <div class="compare-inner">
          <div class="compare-item" v-for="c in compares" :key="c.them">
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
    iconBg: 'linear-gradient(135deg, rgba(99,91,255,0.08), rgba(139,92,246,0.05))',
    title: 'Suporte Humano via WhatsApp',
    desc: 'Nada de robôs ou filas intermináveis. Nosso time técnico atende diretamente pelo WhatsApp para resolver qualquer dúvida operacional na hora.',
    highlight: 'Suporte humanizado em horário comercial',
    highlightIcon: '⚡',
    accent: '',
  },
  {
    icon: '🚀',
    iconBg: 'linear-gradient(135deg, rgba(0,200,83,0.08), rgba(16,185,129,0.05))',
    title: 'Implantação Descomplicada',
    desc: 'Nossa equipe ajuda você a cadastrar seus primeiros serviços, configurar a tabela de preço de peças e cadastrar seus técnicos em poucos minutos.',
    highlight: 'Pronto para faturar no primeiro dia',
    highlightIcon: '📦',
    accent: '',
  },
  {
    icon: '🛡️',
    iconBg: 'linear-gradient(135deg, rgba(0,216,255,0.08), rgba(8,145,178,0.05))',
    title: 'Sem Multas ou Contrato de Fidelidade',
    desc: 'Acreditamos no valor do nosso produto. Assine mensalmente e cancele quando quiser, sem burocracias ou letras miúdas de fidelidade.',
    highlight: 'Liberdade total e transparência',
    highlightIcon: '🛡️',
    accent: '',
  },
  {
    icon: '🔄',
    iconBg: 'linear-gradient(135deg, rgba(245,158,11,0.08), rgba(217,119,0.05))',
    title: 'Melhorias Contínuas',
    desc: 'Estamos em constante evolução, lançando novas facilidades para otimizar o seu fluxo de ordens de serviço semanalmente.',
    highlight: 'Novos recursos sem cobranças adicionais',
    highlightIcon: '📊',
    accent: '',
  },
];

const compares = [
  { them: 'Sistemas antigos e complexos com instalação local', us: 'Sistema na nuvem moderno e rápido' },
  { them: 'Suporte demorado por e-mail ou tickets burocráticos', us: 'Atendimento humanizado direto no WhatsApp' },
  { them: 'Contratos rígidos com 12 meses de fidelidade', us: 'Assinatura flexível sem fidelidade ou multa' },
  { them: 'Checklists e vistorias feitos em fichas de papel', us: 'Vistoria digital integrada com envio de fotos' },
];
</script>

<style scoped>
.advantages {
  padding: 6rem 0;
  background: #ffffff;
  position: relative;
  overflow: hidden;
  border-top: 1px solid #edf2f7;
  border-bottom: 1px solid #edf2f7;
}

.advantages::before {
  content: '';
  position: absolute;
  top: -150px;
  right: -150px;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(99,91,255,0.03) 0%, transparent 65%);
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

.adv-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
  letter-spacing: -0.03em;
}

.adv-subtitle {
  font-size: 1.1rem;
  color: var(--text-main);
  line-height: 1.6;
}

/* ── Grid ── */
.adv-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.25rem;
  margin-bottom: 4rem;
}

.adv-card {
  background: #ffffff;
  border: 1px solid #edf2f7;
  border-radius: var(--radius-xl);
  padding: 2rem;
  transition: all var(--transition-base);
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  box-shadow: 0 4px 10px rgba(10, 37, 64, 0.02);
}

.adv-card:hover {
  background: #ffffff;
  border-color: rgba(99,91,255,0.25);
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(10, 37, 64, 0.05);
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
  font-weight: 800;
  color: var(--midnight-navy);
  font-family: var(--font-body);
}

.adv-card p {
  font-size: 0.9rem;
  color: var(--text-main);
  line-height: 1.6;
}

.adv-highlight {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #edf2f7;
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-muted);
}

.adv-highlight-icon {
  font-size: 1rem;
}

/* ── Comparison Strip ── */
.adv-compare {
  background: #f8fafc;
  border: 1px solid #edf2f7;
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
  background: #ffffff;
  border: 1px solid #edf2f7;
  box-shadow: 0 2px 4px rgba(10, 37, 64, 0.02);
}

.compare-them {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  color: var(--text-muted);
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
  background: #edf2f7;
  flex-shrink: 0;
}

.compare-us {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--midnight-navy);
}

.compare-check {
  width: 20px;
  height: 20px;
  background: rgba(16, 185, 129, 0.1);
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
