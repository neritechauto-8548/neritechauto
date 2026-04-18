<template>
  <section class="logos-section">
    <div class="container">
      <p class="logos-headline">Mais de <strong>500 oficinas</strong> confiam na NeriTech para gerenciar suas operações</p>
    </div>

    <div class="marquee-wrapper">
      <div class="marquee-track">
        <div class="logo-item" v-for="logo in [...logos, ...logos]" :key="logo.name + Math.random()">
          <span class="logo-icon">{{ logo.icon }}</span>
          <span class="logo-name">{{ logo.name }}</span>
        </div>
      </div>
    </div>

    <div class="container">
      <div class="trust-metrics">
        <div class="tm-item" v-for="tm in trustMetrics" :key="tm.label">
          <span class="tm-value">{{ tm.value }}</span>
          <span class="tm-label">{{ tm.label }}</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const logos = [
  { icon: '🔧', name: 'Mendes Auto Center' },
  { icon: '🏎️', name: 'Elite Motors' },
  { icon: '🚗', name: 'Garage PR' },
  { icon: '⚙️', name: 'RO Officina' },
  { icon: '🛠️', name: 'FC Auto Service' },
  { icon: '🏢', name: 'Teixeira & Filhos' },
  { icon: '🔩', name: 'Auto Mecânica JR' },
  { icon: '🚘', name: 'Speed Car Centro' },
  { icon: '💎', name: 'Premium Motors' },
  { icon: '🏗️', name: 'Rede MasterFix' },
]

const trustMetrics = [
  { value: '142.350+', label: 'Ordens de serviço criadas' },
  { value: 'R$ 28M+', label: 'Faturados pela plataforma' },
  { value: '99.9%', label: 'Uptime garantido' },
  { value: '< 2min', label: 'Tempo médio de resposta' },
]
</script>

<style scoped>
.logos-section {
  padding: 3rem 0 4rem;
  background: white;
  border-bottom: 1px solid var(--border);
}

.logos-headline {
  text-align: center;
  font-size: 0.9375rem;
  color: var(--text-muted);
  margin-bottom: 2rem;
  font-weight: 500;
}

.logos-headline strong {
  color: var(--midnight-navy);
}

/* ── Marquee ── */
.logo-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 24px;
  background: var(--light-bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  white-space: nowrap;
  flex-shrink: 0;
}

.logo-icon {
  font-size: 1.25rem;
}

.logo-name {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--midnight-navy);
  letter-spacing: -0.01em;
}

/* ── Trust Metrics ── */
.trust-metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1px;
  margin-top: 3rem;
  background: var(--border);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

.tm-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 1.5rem 1rem;
  background: white;
  text-align: center;
}

.tm-value {
  font-family: var(--font-heading);
  font-size: 1.75rem;
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.03em;
  line-height: 1;
}

.tm-label {
  font-size: 0.8125rem;
  font-weight: 500;
  color: var(--text-muted);
}

/* ── Responsive ── */
@media (max-width: 768px) {
  .trust-metrics {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .trust-metrics {
    grid-template-columns: 1fr 1fr;
  }

  .tm-value {
    font-size: 1.375rem;
  }
}
</style>
