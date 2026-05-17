<template>
  <main class="pricing-page">
    <!-- Soft Background Mesh -->
    <div class="stripe-mesh-bg"></div>

    <!-- Header / Hero section -->
    <section class="pricing-hero">
      <div class="container text-center">
        <span class="pricing-badge">Preços Transparentes</span>
        <h1 class="pricing-title">Planos simples, <span class="text-gradient-vibrant">sem taxas surpresas.</span></h1>
        <p class="pricing-subtitle">
          Escolha o plano ideal para acelerar o crescimento da sua oficina. Comece hoje mesmo o seu teste de 30 dias grátis, sem cartão de crédito e sem contratos de fidelidade rígidos.
        </p>
      </div>
    </section>

    <!-- Pricing Tiers Component -->
    <SecaoPrecos />

    <!-- Detailed Value Text section -->
    <section class="pricing-value-proposition">
      <div class="container">
        <div class="value-grid">
          <div class="value-card">
            <div class="v-icon">💎</div>
            <h4>Sem custo de implantação</h4>
            <p>Diferente de sistemas legados, na NeriTechAuto você não paga taxa de adesão ou instalação. Nosso suporte ajuda você a configurar tudo grátis.</p>
          </div>
          <div class="value-card">
            <div class="v-icon">⚡</div>
            <h4>Cancele quando quiser</h4>
            <p>Trabalhamos no modelo SaaS por assinatura. Sem fidelidade forçada. Se o sistema não fizer sua oficina faturar mais, cancele sem multa.</p>
          </div>
          <div class="value-card">
            <div class="v-icon">🔒</div>
            <h4>Seus dados seguros</h4>
            <p>Utilizamos servidores criptografados de alta disponibilidade. Suas ordens de serviço, dados de clientes e financeiro estão 100% protegidos.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Detailed Comparison Component -->
    <SecaoComparativo />

    <!-- Pricing FAQ -->
    <SecaoFAQ />

    <!-- Contact & Footer -->
    <SecaoContato />
    <SecaoRodape />
  </main>
</template>

<script setup>
import { onMounted } from 'vue';
import SecaoPrecos from '../components/SecaoPrecos.vue';
import SecaoComparativo from '../components/SecaoComparativo.vue';
import SecaoFAQ from '../components/SecaoFAQ.vue';
import SecaoContato from '../components/SecaoContato.vue';
import SecaoRodape from '../components/SecaoRodape.vue';

onMounted(() => {
  window.scrollTo({ top: 0, behavior: 'instant' });
});
</script>

<style scoped>
.pricing-page {
  position: relative;
  min-height: 100vh;
  padding-top: 100px;
  background-color: #ffffff;
  overflow: hidden;
}

.stripe-mesh-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.03) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.03) 0%, transparent 40%),
    #ffffff;
}

.pricing-hero {
  position: relative;
  z-index: 1;
  padding: 60px 0 20px;
}

.pricing-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.08);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 14px;
  border-radius: 99px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 1.25rem;
}

.pricing-title {
  font-size: clamp(2.25rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  line-height: 1.1;
  margin-bottom: 1.25rem;
}

.pricing-subtitle {
  font-size: 1.125rem;
  color: var(--text-main);
  max-width: 680px;
  margin: 0 auto;
  line-height: 1.6;
}

/* Value Cards Section */
.pricing-value-proposition {
  position: relative;
  z-index: 1;
  padding: 40px 0 60px;
  background: #f8fafc;
  border-top: 1px solid #edf2f7;
  border-bottom: 1px solid #edf2f7;
}

.value-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.value-card {
  background: #ffffff;
  border: 1px solid #edf2f7;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(10, 37, 64, 0.02);
  transition: transform 0.2s ease;
}

.value-card:hover {
  transform: translateY(-2px);
}

.v-icon {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.value-card h4 {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 0.5rem;
}

.value-card p {
  font-size: 0.875rem;
  color: var(--text-main);
  line-height: 1.5;
}

@media (max-width: 768px) {
  .value-grid {
    grid-template-columns: 1fr;
    gap: 1.25rem;
  }
}
</style>
