<template>
  <section class="benefits section-surface section-surface--light section-spacer">
    <div class="container">
      <div class="benefits-grid aos-init">
        <article class="benefit-card" v-for="item in items" :key="item.title">
          <div class="benefit-card__icon" :style="{ background: item.iconBg }">
            <span v-html="item.icon"></span>
          </div>
          <h3>{{ item.title }}</h3>
          <p>{{ item.desc }}</p>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const items = [
  {
    title: 'Ordens de serviço digitais',
    desc: 'Abra, acompanhe e finalize OS com histórico completo por placa e cliente.',
    icon: '<svg width="22" height="22" viewBox="0 0 24 24" fill="none"><rect x="4" y="3" width="16" height="18" rx="2" stroke="currentColor" stroke-width="1.75"/><path d="M8 8h8M8 12h5" stroke="currentColor" stroke-width="1.75" stroke-linecap="round"/></svg>',
    iconBg: 'rgba(37, 99, 235, 0.08)',
  },
  {
    title: 'Financeiro integrado',
    desc: 'Contas a pagar e receber conectadas à conclusão das ordens de serviço.',
    icon: '<svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M12 2v20M17 7H9.5a3.5 3.5 0 100 7h5a3.5 3.5 0 110 7H7" stroke="currentColor" stroke-width="1.75" stroke-linecap="round"/></svg>',
    iconBg: 'rgba(5, 150, 105, 0.08)',
  },
  {
    title: 'Estoque sob controle',
    desc: 'Cadastre peças, receba alertas de estoque mínimo e dê baixa automática na OS.',
    icon: '<svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z" stroke="currentColor" stroke-width="1.75"/></svg>',
    iconBg: 'rgba(14, 165, 233, 0.08)',
  },
  {
    title: 'Portal do cliente',
    desc: 'Seu cliente acompanha o serviço, aprova orçamentos e recebe atualizações.',
    icon: '<svg width="22" height="22" viewBox="0 0 24 24" fill="none"><circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.75"/><path d="M4 20c0-4 3.6-6 8-6s8 2 8 6" stroke="currentColor" stroke-width="1.75" stroke-linecap="round"/></svg>',
    iconBg: 'rgba(99, 102, 241, 0.08)',
  },
];
</script>

<style scoped>
.benefits-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.25rem;
}

.benefit-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 1.75rem;
  transition: all var(--transition-base);
  box-shadow: var(--shadow-card);
}

.benefit-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-border);
}

.benefit-card__icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary);
  margin-bottom: 1.25rem;
}

.benefit-card h3 {
  font-size: 1.0625rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  font-family: var(--font-heading);
}

.benefit-card p {
  font-size: 0.9rem;
  color: var(--text-muted);
  line-height: 1.55;
}

@media (max-width: 1024px) {
  .benefits-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 560px) {
  .benefits-grid { grid-template-columns: 1fr; }
}
</style>
