<template>
  <section class="stats section-surface section-spacer" id="resultados">
    <div class="container">
      <div class="stats-intro aos-init">
        <span class="section-label">Resultados reais</span>
        <h2 class="stats-title">Veja por que oficinas escolhem o NeriTech Auto.</h2>
        <p class="stats-subtitle">
          Resultados que donos de oficina em todo o Brasil estão alcançando com a plataforma.
        </p>
      </div>

      <div class="stats-grid aos-init aos-delay-1">
        <div class="stat-item" v-for="item in items" :key="item.label">
          <strong>{{ item.value }}</strong>
          <span>{{ item.label }}</span>
        </div>
      </div>

      <!-- Trust pillars no estilo Shopmonkey -->
      <div class="trust-pillars aos-init aos-delay-2">
        <div class="trust-pillar" v-for="p in pillars" :key="p.title">
          <div class="trust-pillar__icon">
            <StripeIcon :name="p.icon" :size="20" />
          </div>
          <h3>{{ p.title }}</h3>
          <p>{{ p.desc }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import StripeIcon from './StripeIcon.vue';

const items = [
  { value: '+2h', label: 'economizadas por dia no controle manual' },
  { value: '+40%', label: 'de aprovação com orçamento digital' },
  { value: '100%', label: 'nuvem — PC, tablet e celular' },
  { value: '180', label: 'dias para testar sem cartão de crédito' },
];

const pillars = [
  {
    icon: 'support',
    title: 'Fácil de aprender. Fácil de usar.',
    desc: 'Uma solução intuitiva que retém técnicos e faz clientes voltarem. Do consultor ao mecânico — todos usam.',
  },
  {
    icon: 'rocket',
    title: 'Confiável e veloz.',
    desc: 'Infraestrutura em nuvem com alta disponibilidade. Nada de sistema travando no meio do dia.',
  },
  {
    icon: 'workflow',
    title: 'Feito para oficinas.',
    desc: 'Uma ferramenta que funciona para cada função da sua equipe, tornando todos mais eficientes.',
  },
];
</script>

<style scoped>
.stats {
  background: #0A2540;
  color: #fff;
  position: relative;
  overflow: hidden;
}

.stats::before {
  content: '';
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse 60% 80% at 10% 50%, rgba(99, 91, 255, 0.35) 0%, transparent 55%),
    radial-gradient(ellipse 50% 60% at 90% 30%, rgba(0, 212, 255, 0.2) 0%, transparent 50%),
    radial-gradient(ellipse 40% 50% at 70% 90%, rgba(255, 92, 160, 0.18) 0%, transparent 50%);
  pointer-events: none;
}

.container { position: relative; z-index: 1; }

.stats-intro {
  max-width: 640px;
  margin-bottom: 3rem;
}

.stats-title {
  font-size: clamp(2.125rem, 4vw, 3.125rem);
  font-weight: 300;
  color: #fff !important;
  letter-spacing: -0.04em;
  line-height: 1.05;
  margin-bottom: 0.875rem;
  margin-top: 0.375rem;
}

.stats-subtitle {
  font-size: clamp(1.0625rem, 1.5vw, 1.25rem);
  color: rgba(255, 255, 255, 0.7) !important;
  line-height: 1.65;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  padding-top: 2.5rem;
  margin-bottom: 4rem;
}

.stat-item strong {
  display: block;
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 400;
  letter-spacing: -0.04em;
  color: #fff;
  line-height: 1;
  margin-bottom: 0.5rem;
}

.stat-item span {
  font-size: 0.9375rem;
  color: rgba(255, 255, 255, 0.65);
  font-weight: 400;
}

/* ── Trust Pillars ── */
.trust-pillars {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  padding-top: 2.5rem;
}

.trust-pillar {
  display: flex;
  flex-direction: column;
  gap: 0.625rem;
}

.trust-pillar__icon {
  width: 40px;
  height: 40px;
  background: rgba(99, 91, 255, 0.2);
  border: 1px solid rgba(99, 91, 255, 0.35);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #a5b4fc;
  margin-bottom: 0.375rem;
}

.trust-pillar h3 {
  font-size: 1.0625rem;
  font-weight: 500;
  color: #fff;
  letter-spacing: -0.02em;
}

.trust-pillar p {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.6);
  line-height: 1.55;
}

@media (max-width: 1024px) {
  .trust-pillars { grid-template-columns: 1fr 1fr; }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 2rem 1.5rem;
  }
  .trust-pillars { grid-template-columns: 1fr; }
}
</style>
