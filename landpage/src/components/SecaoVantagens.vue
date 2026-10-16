<template>
  <section class="advantages">
    <div class="container">
      <div class="adv-header aos-init">
        <span class="section-label">Feito para a vida da oficina</span>
        <h2 class="adv-title">Simples de usar. Completo de verdade.</h2>
        <p class="adv-subtitle">
          Fácil de aprender, rápido no dia a dia e pensado para cada função da equipe —
          do consultor ao técnico e ao dono.
        </p>
      </div>

      <div class="adv-grid">
        <div
          class="adv-card aos-init"
          v-for="card in cards"
          :key="card.title"
        >
          <div class="adv-icon" :style="{ color: card.color, background: card.iconBg }">
            <StripeIcon :name="card.icon" :size="22" />
          </div>
          <h3>{{ card.title }}</h3>
          <p>{{ card.desc }}</p>
        </div>
      </div>

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
import StripeIcon from './StripeIcon.vue';

const cards = [
  {
    icon: 'support',
    iconBg: 'rgba(99, 91, 255, 0.08)',
    color: '#635BFF',
    title: 'Suporte humano de ponta a ponta',
    desc: 'Time especializado em oficina, no chat — do onboarding às dúvidas do dia a dia. Sem robô, sem ticket eterno.',
  },
  {
    icon: 'rocket',
    iconBg: 'rgba(13, 148, 136, 0.08)',
    color: '#0D9488',
    title: 'Implantação sem dor de cabeça',
    desc: 'Ajudamos a cadastrar serviços, peças e equipe. Migração de dados do sistema antigo incluída, sem custo.',
  },
  {
    icon: 'shield',
    iconBg: 'rgba(0, 212, 255, 0.1)',
    color: '#0891B2',
    title: 'Sem fidelidade e sem multa',
    desc: 'Assinatura mensal transparente. Cancele quando quiser — acreditamos no produto, não no contrato.',
  },
  {
    icon: 'refresh',
    iconBg: 'rgba(255, 176, 32, 0.12)',
    color: '#B47A00',
    title: 'Evolução contínua',
    desc: 'Novos recursos no fluxo de OS, checklist e financeiro — sem taxa extra por atualização.',
  },
];

const compares = [
  { them: 'Sistema local lento e difícil de atualizar', us: '100% nuvem — PC, tablet e celular' },
  { them: 'Orçamento no papel e aprovação por telefone', us: 'Orçamento digital com aprovação no WhatsApp' },
  { them: 'Checklist em ficha e fotos perdidas', us: 'Inspeção digital com fotos na OS' },
  { them: 'Financeiro e fiscal em planilhas separadas', us: 'Caixa, OS e NF-e no mesmo fluxo' },
];
</script>

<style scoped>
.advantages {
  padding: 6rem 0;
  background: #fff;
  position: relative;
  overflow: hidden;
  border-top: 1px solid #E6EBF1;
}

.adv-header {
  text-align: left;
  max-width: 640px;
  margin: 0 0 3.5rem;
}

.adv-title {
  font-size: clamp(2.125rem, 4vw, 3.125rem);
  font-weight: 300;
  letter-spacing: -0.04em;
  line-height: 1.06;
  margin-bottom: 0.875rem;
  color: #0A2540;
}

.adv-subtitle {
  font-size: 1.0625rem;
  color: #6B7C93;
  line-height: 1.55;
}

.adv-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.75rem;
  margin-bottom: 3.5rem;
}

.adv-card {
  padding: 0;
  background: transparent;
  border: none;
  box-shadow: none;
}

.adv-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.1rem;
}

.adv-card h3 {
  font-size: 1.0625rem;
  font-weight: 500;
  letter-spacing: -0.02em;
  margin-bottom: 0.5rem;
  color: #0A2540;
}

.adv-card p {
  font-size: 0.9375rem;
  color: #6B7C93;
  line-height: 1.55;
}

.adv-compare {
  background: #F6F9FC;
  border: 1px solid #E6EBF1;
  border-radius: 12px;
  padding: 1.5rem 1.75rem;
}

.compare-inner {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.compare-item {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 1rem;
  align-items: center;
}

.compare-them,
.compare-us {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.875rem;
  color: #425466;
}

.compare-x {
  color: #8898AA;
  font-weight: 600;
}
.compare-check {
  color: #635BFF;
  font-weight: 600;
}

.compare-divider {
  width: 1px;
  height: 24px;
  background: #E6EBF1;
}

@media (max-width: 1024px) {
  .adv-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .adv-grid { grid-template-columns: 1fr; }
  .compare-item {
    grid-template-columns: 1fr;
    gap: 0.5rem;
  }
  .compare-divider { display: none; }
}
</style>
