<template>
  <section id="planos" class="pricing">
    <div class="container">
      <div class="pricing-header aos-init">
        <span class="section-label">Preços</span>
        <h2 class="pricing-title">Preço justo para <span class="text-gradient">escalar seu negócio.</span></h2>
        <p class="pricing-subtitle">Sem surpresas. Cancele quando quiser. Comece grátis por 7 dias.</p>


      </div>

      <div class="pricing-grid">
        <div
          v-for="plan in plans"
          :key="plan.id"
          class="pricing-card"
          :class="{ featured: plan.featured }"
        >
          <!-- Popular badge -->
          <div v-if="plan.featured" class="popular-tag shimmer-badge">
            ⭐ Mais Popular
          </div>

          <div class="plan-top">
            <div class="plan-name">{{ plan.name }}</div>
            <p class="plan-desc">{{ plan.description }}</p>
          </div>

          <div class="plan-price-block">
            <div class="plan-price">
              <span class="currency">R$</span>
              <span class="amount">{{ plan.price }}</span>
              <span class="period">/mês</span>
            </div>
          </div>

          <button
            class="btn btn-plan"
            :class="plan.featured ? 'btn-primary' : 'btn-outline-plan'"
            @click="initiateCheckout(plan.id)"
          >
            Começar grátis por 7 dias →
          </button>

          <div class="plan-divider"></div>

          <div class="plan-features">
            <p class="features-label">O que está incluído:</p>
            <ul>
              <li v-for="f in plan.features" :key="f">
                <span class="check-icon">✓</span>
                {{ f }}
              </li>
            </ul>
          </div>
        </div>
      </div>

      <!-- Bottom trust -->
      <div class="pricing-footer aos-init">
        <div class="trust-row">
          <span class="trust-item">🔒 Pagamento seguro via Stripe</span>
          <span class="trust-item">✓ Cancele a qualquer momento</span>
          <span class="trust-item">🎁 7 dias grátis sem cartão</span>
          <span class="trust-item">💬 Suporte incluído em todos os planos</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';



const plans = ref([
  {
    id: 'price_start',
    name: 'neri start',
    price: '90',
    description: 'Essencial para organizar o fluxo de entrada e saída.',
    featured: false,
    features: [
      'Ordens de Serviço Digitais',
      'Cadastro de Clientes e Veículos',
      'Financeiro Básico (CP/CR)',
      'Checklist de Entrada',
      'Emissão de NF-e Simples',
      'Suporte via E-mail',
    ],
  },
  {
    id: 'price_pro',
    name: 'neri pro',
    price: '140',
    description: 'A gestão completa para oficinas que buscam performance.',
    featured: true,
    features: [
      'Tudo do NeriTechAuto Start',
      'Conciliação Bancária Automática',
      'Emissão de NF-e e NFS-e',
      'Controle de Estoque Inteligente',
      'Relatórios Gerenciais (DRE)',
      'WhatsApp Nativo para Clientes',
      'Suporte prioritário WhatsApp',
    ],
  },
  {
    id: 'price_elite',
    name: 'neri elite',
    price: '230',
    description: 'O nível máximo de controle e inteligência de dados.',
    featured: false,
    features: [
      'Tudo do NeriTechAuto Pro',
      'Dashboards BI Customizados',
      'API para Integrações Externas',
      'Consultoria Mensal de Processos',
    ],
  },
]);

const initiateCheckout = async (planId) => {
  try {
    const publishableKey = import.meta.env.VITE_STRIPE_CHAVE_PUBLICA;
    const priceMap = {
      'price_start': import.meta.env.VITE_STRIPE_PRECO_START,
      'price_pro':   import.meta.env.VITE_STRIPE_PRECO_PRO,
      'price_elite': import.meta.env.VITE_STRIPE_PRECO_ELITE,
    };

    const targetPriceId = priceMap[planId];

    if (!publishableKey || publishableKey.includes('sua_chave_aqui')) {
      alert('Configuração necessária: adicione sua VITE_STRIPE_CHAVE_PUBLICA no arquivo .env');
      return;
    }

    if (!targetPriceId || targetPriceId === 'price_...') {
      alert('Configuração necessária: configure o ID do preço do Stripe no arquivo .env');
      return;
    }

    if (!window.Stripe) {
      alert('Erro: Stripe.js não carregado. Verifique sua conexão.');
      return;
    }

    const stripe = window.Stripe(publishableKey);
    const { error } = await stripe.redirectToCheckout({
      lineItems: [{ price: targetPriceId, quantity: 1 }],
      mode: 'subscription',
      successUrl: window.location.origin + '/success',
      cancelUrl:  window.location.origin + '/cancel',
    });

    if (error) {
      console.error('Stripe error:', error);
      alert(`Erro no Checkout: ${error.message}`);
    }
  } catch (err) {
    console.error('Checkout error:', err);
    alert('Ocorreu um erro ao processar o checkout.');
  }
};
</script>

<style scoped>
.pricing {
  padding: var(--spacing-2xl) 0;
  background: white;
}

/* ── Header ── */
.pricing-header {
  text-align: center;
  max-width: 640px;
  margin: 0 auto var(--spacing-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.pricing-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 0.75rem;
}

.pricing-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  margin-bottom: 2rem;
}



/* ── Grid ── */
.pricing-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
  align-items: start;
  margin-bottom: var(--spacing-xl);
}

/* ── Card ── */
.pricing-card {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 2rem;
  position: relative;
  transition: var(--transition-base);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.pricing-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.pricing-card.featured {
  border-color: var(--primary-indigo);
  box-shadow: 0 0 0 1px var(--primary-indigo), var(--shadow-lg);
  background: linear-gradient(180deg, rgba(99,91,255,0.02) 0%, white 100%);
}

.popular-tag {
  position: absolute;
  top: -14px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--primary-indigo);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 16px;
  border-radius: var(--radius-full);
  white-space: nowrap;
  box-shadow: var(--shadow-indigo);
}

/* Plan info */
.plan-name {
  font-family: var(--font-heading);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 0.375rem;
}

.plan-desc {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.5;
}

/* Price */
.plan-price {
  display: flex;
  align-items: baseline;
  gap: 3px;
  margin-bottom: 0.25rem;
}

.currency {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-muted);
}

.amount {
  font-family: var(--font-heading);
  font-size: 3.25rem;
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  line-height: 1;
}

.period {
  font-size: 0.9375rem;
  color: var(--text-light);
}

.annual-note {
  font-size: 0.75rem;
  color: #059669;
  font-weight: 600;
}

/* CTA */
.btn-plan {
  width: 100%;
  padding: 0.875rem;
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  font-weight: 600;
}

.btn-outline-plan {
  background: transparent;
  color: var(--midnight-navy);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
}

.btn-outline-plan:hover {
  border-color: var(--primary-indigo);
  color: var(--primary-indigo);
  background: var(--primary-indigo-light);
}

/* Features list */
.plan-divider {
  height: 1px;
  background: var(--border);
}

.features-label {
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  margin-bottom: 1rem;
}

.plan-features ul {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.plan-features li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 0.9rem;
  color: var(--text-main);
  line-height: 1.4;
}

.check-icon {
  width: 18px;
  height: 18px;
  background: rgba(99,91,255,0.1);
  color: var(--primary-indigo);
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 0.65rem;
  font-weight: 800;
  flex-shrink: 0;
  margin-top: 1px;
}

.pricing-card.featured .check-icon {
  background: var(--primary-indigo);
  color: white;
}

/* Footer */
.pricing-footer { text-align: center; }

.trust-row {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 2rem;
}

.trust-item {
  font-size: 0.875rem;
  color: var(--text-muted);
  font-weight: 500;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .pricing-grid {
    grid-template-columns: 1fr;
    max-width: 480px;
    margin-left: auto;
    margin-right: auto;
  }
}
</style>
