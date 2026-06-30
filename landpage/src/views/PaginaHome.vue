<template>
  <main class="pagina-home">
    <SecaoPrincipal />
    <SecaoBeneficios />
    <SecaoPrints />
    <SecaoAprovacao />
    <SecaoSegmentos />
    <SecaoVantagens />
    <SecaoPrecos />
    <SecaoFAQ />
    <SecaoContato />

    <section class="final-cta section-surface section-surface--alt" id="cta-final">
      <div class="container cta-content aos-init">
        <span class="section-label">Começar agora</span>
        <h2 class="cta-title">
          Sua oficina mais organizada em horas, não dias.
        </h2>
        <p class="cta-desc">
          Configure o sistema hoje, cadastre seus clientes e comece a emitir OS digitais ainda esta semana — sem burocracia e sem treinamento complicado.
        </p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary" id="home-cta-main">
            Começar teste grátis
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn-cta-secondary" id="home-cta-contact">
            Falar com a equipe
          </a>
        </div>
        <div class="cta-guarantees">
          <span><svg width="14" height="14" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg> 180 dias grátis</span>
          <span><svg width="14" height="14" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg> Sem cartão de crédito</span>
          <span><svg width="14" height="14" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg> Cancele quando quiser</span>
          <span>🇧🇷 Suporte em português</span>
        </div>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoPrincipal    from '../components/SecaoPrincipal.vue';
import SecaoBeneficios   from '../components/SecaoBeneficios.vue';
import SecaoPrints       from '../components/SecaoPrints.vue';
import SecaoAprovacao    from '../components/SecaoAprovacao.vue';
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

.cta-guarantees {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  flex-wrap: wrap;
  justify-content: center;
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-muted);
}

.cta-guarantees span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.cta-guarantees svg {
  color: var(--primary);
  flex-shrink: 0;
}

@media (max-width: 640px) {
  .cta-actions { flex-direction: column; align-items: stretch; }
  .btn-cta-primary, .btn-cta-secondary { width: 100%; justify-content: center; }
}
</style>
