<template>
  <main>
    <SecaoPrincipal />
    <SecaoLogos />
    <SecaoDestaques />
    <SecaoEstatisticas />
    <SecaoSegmentos />
    <SecaoIntegracao />
    <SecaoRecursos />
    <SecaoVantagens />
    <SecaoPrecos />
    <SecaoDepoimentos />
    <SecaoFAQ />
    <SecaoContato />

    <!-- Final CTA -->
    <section class="final-cta" id="cta-final">
      <div class="cta-mesh-bg">
        <div class="mesh-vibrant">
          <div class="mesh-cloud-inner"></div>
        </div>
      </div>

      <div class="container cta-content">
        <div class="cta-badge">Comece hoje mesmo</div>
        <h2 class="cta-title">
          Pronto para transformar<br>
          <span class="text-gradient-vibrant">sua oficina?</span>
        </h2>
        <p class="cta-desc">
          Junte-se a mais de 500 oficinas que modernizaram sua gestão com a neritechauto.
          7 dias grátis, sem cartão de crédito.
        </p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn btn-cta-primary" id="home-cta-main">
            Começar Grátis por 7 Dias
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn btn-cta-secondary" id="home-cta-contact">
            Falar com um Especialista
          </a>
        </div>
        <p class="cta-micro">Sem cartão de crédito · Configuração em menos de 5 minutos · Cancele quando quiser</p>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoPrincipal   from '../components/SecaoPrincipal.vue';
import SecaoLogos       from '../components/SecaoLogos.vue';
import SecaoDestaques   from '../components/SecaoDestaques.vue';
import SecaoEstatisticas from '../components/SecaoEstatisticas.vue';
import SecaoSegmentos   from '../components/SecaoSegmentos.vue';
import SecaoIntegracao  from '../components/SecaoIntegracao.vue';
import SecaoRecursos    from '../components/SecaoRecursos.vue';
import SecaoVantagens   from '../components/SecaoVantagens.vue';
import SecaoPrecos      from '../components/SecaoPrecos.vue';
import SecaoDepoimentos from '../components/SecaoDepoimentos.vue';
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
  background: var(--slate-navy);
  text-align: center;
}

.cta-mesh-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
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
  background: rgba(99,91,255,0.2);
  border: 1px solid rgba(99,91,255,0.4);
  color: rgba(255,255,255,0.85);
  font-size: 0.8125rem;
  font-weight: 600;
  padding: 6px 16px;
  border-radius: 99px;
  margin-bottom: 1.5rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.cta-title {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 800;
  color: white;
  line-height: 1.1;
  letter-spacing: -0.04em;
  margin-bottom: 1.25rem;
}

.cta-desc {
  font-size: 1.1875rem;
  color: rgba(255,255,255,0.72);
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
  background: white;
  color: var(--midnight-navy);
  font-family: var(--font-body);
  font-weight: 700;
  font-size: 1rem;
  padding: 1rem 2rem;
  border-radius: var(--radius-md);
  cursor: pointer;
  border: none;
  text-decoration: none;
  transition: all var(--transition-base);
  box-shadow: 0 8px 24px rgba(0,0,0,0.2);
}

.btn-cta-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.25);
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
  background: transparent;
  color: rgba(255,255,255,0.85);
  font-family: var(--font-body);
  font-weight: 600;
  font-size: 1rem;
  padding: 1rem 2rem;
  border-radius: var(--radius-md);
  border: 1.5px solid rgba(255,255,255,0.25);
  text-decoration: none;
  cursor: pointer;
  transition: all var(--transition-base);
}

.btn-cta-secondary:hover {
  border-color: rgba(255,255,255,0.5);
  background: rgba(255,255,255,0.06);
  color: white;
}

.cta-micro {
  font-size: 0.8125rem;
  color: rgba(255,255,255,0.4);
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
