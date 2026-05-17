<template>
  <main class="pagina-home">
    <SecaoPrincipal />
    <SecaoBeneficios />
    <SecaoPrints />
    <SecaoModulos />
    <SecaoPortalCliente />
    <SecaoAprovacao />
    <SecaoFinanceiro />
    <SecaoSegmentos />
    <SecaoVantagens />
    <SecaoPrecos />
    <SecaoFAQ />
    <SecaoContato />

    <section class="final-cta section-surface section-surface--alt" id="cta-final">
      <div class="container cta-content aos-init">
        <span class="section-label">Comece hoje</span>
        <h2 class="cta-title">
          Pronto para organizar sua oficina?
        </h2>
        <p class="cta-desc">
          Modernize sua gestão com uma plataforma profissional, simples e feita para o setor automotivo.
          Teste grátis por 30 dias — sem cartão de crédito.
        </p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary" id="home-cta-main">
            Começar teste grátis
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn-cta-secondary" id="home-cta-contact">
            Falar com especialista
          </a>
        </div>
        <p class="cta-micro">Sem cartão · Configuração rápida · Cancele quando quiser</p>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoPrincipal    from '../components/SecaoPrincipal.vue';
import SecaoBeneficios   from '../components/SecaoBeneficios.vue';
import SecaoPrints       from '../components/SecaoPrints.vue';
import SecaoModulos      from '../components/SecaoModulos.vue';
import SecaoPortalCliente from '../components/SecaoPortalCliente.vue';
import SecaoAprovacao    from '../components/SecaoAprovacao.vue';
import SecaoFinanceiro   from '../components/SecaoFinanceiro.vue';
import SecaoSegmentos    from '../components/SecaoSegmentos.vue';
import SecaoVantagens    from '../components/SecaoVantagens.vue';
import SecaoPrecos       from '../components/SecaoPrecos.vue';
import SecaoFAQ          from '../components/SecaoFAQ.vue';
import SecaoContato      from '../components/SecaoContato.vue';
import SecaoRodape       from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

useScrollAnimation();
</script>

<style scoped>
.final-cta {
  padding: 6rem 0;
  text-align: center;
  border-top: 1px solid var(--border);
}

.cta-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cta-title {
  font-size: clamp(2rem, 4.5vw, 3rem);
  font-weight: 800;
  color: var(--midnight-navy);
  line-height: 1.12;
  letter-spacing: -0.04em;
  margin-bottom: 1rem;
  max-width: 640px;
}

.cta-desc {
  font-size: 1.0625rem;
  color: var(--text-muted);
  max-width: 520px;
  line-height: 1.65;
  margin-bottom: 2rem;
}

.cta-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 1.25rem;
}

.btn-cta-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: var(--primary);
  color: white !important;
  font-weight: 700;
  font-size: 1rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
  box-shadow: var(--shadow-indigo);
}

.btn-cta-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
}

.btn-cta-primary:hover .cta-arrow { transform: translateX(4px); }

.cta-arrow { transition: transform 0.2s; display: inline-block; }

.btn-cta-secondary {
  display: inline-flex;
  align-items: center;
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  font-size: 1rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
}

.btn-cta-secondary:hover {
  border-color: var(--primary);
  color: var(--primary) !important;
  background: var(--primary-light);
}

.cta-micro {
  font-size: 0.8125rem;
  color: var(--text-light);
  font-weight: 500;
}

@media (max-width: 640px) {
  .cta-actions { flex-direction: column; align-items: stretch; }
  .btn-cta-primary, .btn-cta-secondary { width: 100%; justify-content: center; }
}
</style>
