<template>
  <main>
    <SecaoPrincipal />
    <SecaoDestaques />
    <SecaoEstatisticas />
    <SecaoSegmentos />
    <SecaoIntegracao />
    <SecaoRecursos />
    <SecaoVantagens />
    <SecaoPrecos />
    <SecaoFAQ />
    <SecaoContato />

    <!-- Final CTA -->
    <section class="final-cta" id="cta-final">
      <div class="cta-mesh-bg"></div>

      <div class="container cta-content">
        <div class="cta-badge">Comece hoje mesmo</div>
        <h2 class="cta-title">
          Pronto para organizar<br>
          <span class="text-gradient-vibrant">sua oficina?</span>
        </h2>
        <p class="cta-desc">
          Modernize sua gestão, agilize o atendimento ao cliente e controle suas finanças de ponta a ponta com a NeriTechAuto.
          Comece seu teste grátis de 30 dias hoje.
        </p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn btn-cta-primary" id="home-cta-main">
            Começar Grátis por 30 Dias
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn btn-cta-secondary" id="home-cta-contact">
            Falar com um Especialista
          </a>
        </div>
        <p class="cta-micro">Sem cartão de crédito · Configuração rápida · Cancele quando quiser</p>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoPrincipal   from '../components/SecaoPrincipal.vue';
import SecaoDestaques   from '../components/SecaoDestaques.vue';
import SecaoEstatisticas from '../components/SecaoEstatisticas.vue';
import SecaoSegmentos   from '../components/SecaoSegmentos.vue';
import SecaoIntegracao  from '../components/SecaoIntegracao.vue';
import SecaoRecursos    from '../components/SecaoRecursos.vue';
import SecaoVantagens   from '../components/SecaoVantagens.vue';
import SecaoPrecos      from '../components/SecaoPrecos.vue';
import SecaoFAQ         from '../components/SecaoFAQ.vue';
import SecaoContato     from '../components/SecaoContato.vue';
import SecaoRodape      from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

useScrollAnimation();
</script>

<style scoped>
/* ── Final CTA section ── */
.final-cta {
  position: relative;
  padding: 7rem 0;
  overflow: hidden;
  background: #f8fafc;
  border-top: 1px solid #edf2f7;
  text-align: center;
}

.cta-mesh-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.02) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.02) 0%, transparent 40%);
}

.cta-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cta-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(99, 91, 255, 0.08);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 16px;
  border-radius: 99px;
  margin-bottom: 1.5rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.cta-title {
  font-size: clamp(2.5rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  line-height: 1.1;
  letter-spacing: -0.04em;
  margin-bottom: 1.25rem;
}

.cta-desc {
  font-size: 1.125rem;
  color: var(--text-main);
  max-width: 540px;
  line-height: 1.65;
  margin-bottom: 2.5rem;
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
  background: var(--primary-indigo);
  color: white !important;
  font-family: var(--font-body);
  font-weight: 700;
  font-size: 1rem;
  padding: 1rem 2rem;
  border-radius: 99px;
  cursor: pointer;
  border: none;
  text-decoration: none;
  transition: all var(--transition-base);
  box-shadow: var(--shadow-indigo);
}

.btn-cta-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(99, 91, 255, 0.4);
}

.btn-cta-primary:hover .cta-arrow {
  transform: translateX(4px);
}

.cta-arrow {
  transition: transform 0.2s;
  display: inline-block;
}

.btn-cta-secondary {
  display: inline-flex;
  align-items: center;
  background: white;
  color: var(--midnight-navy) !important;
  font-family: var(--font-body);
  font-weight: 600;
  font-size: 1rem;
  padding: 1rem 2rem;
  border-radius: 99px;
  border: 1.5px solid var(--border);
  text-decoration: none;
  cursor: pointer;
  transition: all var(--transition-base);
}

.btn-cta-secondary:hover {
  background: var(--gray-50);
  border-color: var(--gray-300);
}

.cta-micro {
  font-size: 0.8125rem;
  color: var(--text-light);
  font-weight: 500;
}

@media (max-width: 640px) {
  .cta-actions { flex-direction: column; align-items: stretch; }
  .btn-cta-primary, .btn-cta-secondary {
    width: 100%;
    justify-content: center;
  }
}
</style>
