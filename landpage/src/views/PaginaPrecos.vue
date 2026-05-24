<template>
  <main class="pagina-precos">
    <!-- Hero + planos + garantias em um bloco contínuo -->
    <section class="page-hero precos-top">
      <div class="container">
        <header class="precos-intro aos-init">
          <span class="section-label">Preços</span>
          <h1 class="precos-intro__title">Planos simples, sem taxas surpresas.</h1>
          <p class="precos-intro__subtitle">
            Teste grátis por 30 dias · Sem cartão · Cancele quando quiser
          </p>
          <ul class="precos-trust">
            <li v-for="t in trustItems" :key="t">{{ t }}</li>
          </ul>
        </header>

        <SecaoPrecos :hide-header="true" />

        <div class="valor-grid aos-init">
          <article class="valor-card" v-for="item in valorCards" :key="item.title">
            <span class="valor-card__icon">{{ item.icon }}</span>
            <div>
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </div>
          </article>
        </div>
      </div>
    </section>

    <section class="precos-comparativo section-surface section-surface--light">
      <SecaoComparativo />
    </section>

    <section class="precos-faq section-surface section-surface--white">
      <SecaoFAQ />
    </section>

    <section class="final-cta section-surface section-surface--alt" id="cta-precos">
      <div class="container cta-content aos-init">
        <h2 class="cta-title">Comece seu teste grátis hoje</h2>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary">
            Experimentar 30 dias grátis →
          </router-link>
          <a href="https://wa.me/5511999999999" target="_blank" rel="noopener" class="btn-cta-secondary">
            WhatsApp
          </a>
        </div>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import { onMounted } from 'vue';
import SecaoPrecos from '../components/SecaoPrecos.vue';
import SecaoComparativo from '../components/SecaoComparativo.vue';
import SecaoFAQ from '../components/SecaoFAQ.vue';
import SecaoRodape from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

useScrollAnimation();

const trustItems = [
  '🔒 Pagamento seguro',
  '✓ Sem fidelidade',
  '🎁 30 dias grátis',
  '💬 Suporte incluído',
];

const valorCards = [
  {
    icon: '💎',
    title: 'Sem custo de implantação',
    desc: 'Suporte para configurar sua oficina, sem taxa de adesão.',
  },
  {
    icon: '⚡',
    title: 'Cancele quando quiser',
    desc: 'Assinatura mensal flexível, sem multa.',
  },
  {
    icon: '🔒',
    title: 'Dados na nuvem',
    desc: 'Backup automático e criptografia.',
  },
];

onMounted(() => {
  window.scrollTo({ top: 0, behavior: 'instant' });
});
</script>

<style scoped>
.pagina-precos {
  /* sem padding-top: o page-hero cuida do espaço pelo topo */
}

/* ── Bloco principal compacto ── */
.precos-top {
  /* page-hero já tem padding: 130px 0 90px */
  padding-bottom: 3rem;
}

.precos-intro {
  text-align: center;
  max-width: 640px;
  margin: 0 auto 1.5rem;
}

.precos-intro__title {
  font-size: clamp(1.75rem, 4vw, 2.5rem);
  font-weight: 800;
  color: white !important;
  letter-spacing: -0.04em;
  line-height: 1.12;
  margin-bottom: 0.5rem;
}

.precos-intro__subtitle {
  font-size: 0.9375rem;
  color: rgba(255,255,255,0.78) !important;
  margin-bottom: 1rem;
}

.precos-trust {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem 1rem;
  list-style: none;
}

.precos-trust li {
  font-size: 0.8125rem;
  font-weight: 600;
  color: rgba(255,255,255,0.9);
  background: rgba(255,255,255,0.12);
  border: 1px solid rgba(255,255,255,0.25);
  padding: 6px 12px;
  border-radius: var(--radius-full);
  backdrop-filter: blur(4px);
}

.valor-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.75rem;
  margin-top: 1.25rem;
  padding-top: 1.25rem;
  border-top: 1px solid var(--border);
}

.valor-card {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 1rem 1.125rem;
}

.valor-card__icon {
  font-size: 1.25rem;
  flex-shrink: 0;
  line-height: 1;
}

.valor-card h3 {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 2px;
  font-family: var(--font-heading);
}

.valor-card p {
  font-size: 0.75rem;
  color: var(--text-muted);
  line-height: 1.45;
}

/* ── Overrides: SecaoPrecos embutida ── */
.pagina-precos :deep(.pricing--embedded) {
  padding: 0;
  background: transparent;
}

.pagina-precos :deep(.pricing-grid) {
  margin-bottom: 0;
  gap: 1.25rem;
}

.pagina-precos :deep(.pricing-card) {
  padding: 1.5rem 1.25rem;
  gap: 1rem;
}

.pagina-precos :deep(.amount) {
  font-size: 2.75rem;
}

.pagina-precos :deep(.pricing-footer) {
  margin-top: 1rem;
  padding-top: 0;
}

.pagina-precos :deep(.trust-row) {
  gap: 1rem 1.5rem;
}

.pagina-precos :deep(.trust-item) {
  font-size: 0.8125rem;
}

/* ── Comparativo ── */
.precos-comparativo {
  padding: 0;
}

.pagina-precos :deep(.secao-comparativo) {
  padding: 2.25rem 0;
}

.pagina-precos :deep(.header-comparativo) {
  margin-bottom: 1.25rem;
}

.pagina-precos :deep(.title) {
  font-size: clamp(1.5rem, 3vw, 2rem);
  margin-bottom: 0.5rem;
}

.pagina-precos :deep(.subtitle) {
  font-size: 0.9375rem;
}

.pagina-precos :deep(.tabs-container) {
  margin-bottom: 1.25rem;
}

.pagina-precos :deep(.table-header) {
  padding: 1.25rem 1rem;
}

.pagina-precos :deep(.table-row) {
  padding: 0.75rem 1rem;
}

.pagina-precos :deep(.table-footer) {
  padding: 1rem;
}

.pagina-precos :deep(.category-indicator) {
  font-size: 1.0625rem;
}

/* ── FAQ ── */
.precos-faq {
  padding: 0;
}

.pagina-precos :deep(.faq) {
  padding: 2.25rem 0;
  background: transparent;
}

.pagina-precos :deep(.faq-layout) {
  gap: 2rem;
}

.pagina-precos :deep(.faq-intro) {
  position: static;
}

.pagina-precos :deep(.faq-title) {
  font-size: clamp(1.5rem, 3vw, 2rem);
  margin-bottom: 0.5rem;
}

.pagina-precos :deep(.faq-subtitle) {
  font-size: 0.9375rem;
  margin-bottom: 1rem;
}

/* ── CTA ── */
.final-cta {
  padding: 2.25rem 0;
  text-align: center;
  border-top: 1px solid var(--border);
}

.cta-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.cta-title {
  font-size: clamp(1.375rem, 3vw, 1.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.03em;
}

.cta-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
  justify-content: center;
}

.btn-cta-primary {
  display: inline-flex;
  align-items: center;
  background: var(--primary);
  color: white !important;
  font-weight: 700;
  font-size: 0.9375rem;
  padding: 0.75rem 1.5rem;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-indigo);
  transition: all var(--transition-base);
}

.btn-cta-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-1px);
}

.btn-cta-secondary {
  display: inline-flex;
  align-items: center;
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  font-size: 0.9375rem;
  padding: 0.75rem 1.5rem;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
}

.btn-cta-secondary:hover {
  border-color: var(--primary);
  color: var(--primary) !important;
}

@media (max-width: 768px) {
  .valor-grid { grid-template-columns: 1fr; }
  .pagina-precos :deep(.pricing-grid) { max-width: 100%; }
  .cta-actions { flex-direction: column; width: 100%; max-width: 320px; }
  .btn-cta-primary, .btn-cta-secondary { justify-content: center; width: 100%; }
}
</style>
