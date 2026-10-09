<template>
  <main class="pagina-home">
    <SecaoPrincipal />
    <SecaoBeneficios />
    <SecaoEstatisticasStripe />
    <SecaoSegmentos />
    <SecaoDepoimentos />
    <SecaoVantagens />
    <SecaoPrecos />
    <SecaoFAQ />
    <SecaoContato />

    <section class="final-cta" id="cta-final">
      <div class="container cta-content aos-init">
        <h2 class="cta-title">
          Pronto para profissionalizar a gestão da oficina?
        </h2>
        <p class="cta-desc">
          Comece grátis, configure o fluxo do pátio e envie o primeiro orçamento digital ainda esta semana.
        </p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary" id="home-cta-main">
            Começar agora
          </router-link>
          <a href="/#contato" class="btn-cta-secondary" id="home-cta-contact">
            Falar com a equipe
          </a>
        </div>
        <div class="cta-guarantees">
          <span>180 dias grátis</span>
          <span>Sem cartão de crédito</span>
          <span>Cancele quando quiser</span>
          <span>Suporte em português</span>
        </div>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoPrincipal from '../components/SecaoPrincipal.vue';
import SecaoBeneficios from '../components/SecaoBeneficios.vue';
import SecaoEstatisticasStripe from '../components/SecaoEstatisticasStripe.vue';
import SecaoSegmentos from '../components/SecaoSegmentos.vue';
import SecaoDepoimentos from '../components/SecaoDepoimentos.vue';
import SecaoVantagens from '../components/SecaoVantagens.vue';
import SecaoPrecos from '../components/SecaoPrecos.vue';
import SecaoFAQ from '../components/SecaoFAQ.vue';
import SecaoContato from '../components/SecaoContato.vue';
import SecaoRodape from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

useScrollAnimation();
</script>

<style scoped>
.final-cta {
  padding: 6.5rem 0;
  text-align: center;
  background: #F6F9FC;
  border-top: 1px solid #E6EBF1;
}

.cta-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cta-title {
  /* Stripe: CTA heading grande, leve, impactante */
  font-size: clamp(2.25rem, 4.5vw, 3.5rem);
  font-weight: 300;
  color: #0A2540;
  line-height: 1.04;
  letter-spacing: -0.045em;
  margin-bottom: 1.125rem;
  max-width: 680px;
}

.cta-desc {
  font-size: clamp(1.0625rem, 1.5vw, 1.25rem);
  color: #6B7C93;
  max-width: 520px;
  line-height: 1.65;
  margin-bottom: 2.25rem;
}

.cta-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
}

.btn-cta-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: #635BFF;
  color: white !important;
  font-weight: 500;
  font-size: 0.9375rem;
  padding: 0.75rem 1.5rem;
  border-radius: 9999px;
  transition: all 0.2s ease;
  box-shadow: 0 4px 14px rgba(99, 91, 255, 0.35);
}

.btn-cta-primary:hover {
  background: #7A73FF;
  transform: translateY(-1px);
}

.btn-cta-secondary {
  display: inline-flex;
  align-items: center;
  background: transparent;
  color: #0A2540 !important;
  font-weight: 500;
  font-size: 0.9375rem;
  padding: 0.75rem 1.5rem;
  border-radius: 9999px;
  border: 1px solid #E6EBF1;
  transition: all 0.2s ease;
}

.btn-cta-secondary:hover {
  border-color: #635BFF;
  color: #635BFF !important;
}

.cta-guarantees {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex-wrap: wrap;
  justify-content: center;
  font-size: 0.8125rem;
  font-weight: 400;
  color: #8898AA;
}

@media (max-width: 640px) {
  .cta-actions { flex-direction: column; align-items: stretch; }
  .btn-cta-primary, .btn-cta-secondary { width: 100%; justify-content: center; }
}
</style>
