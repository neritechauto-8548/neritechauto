<template>
  <section id="planos" class="pricing" :class="{ 'pricing--embedded': hideHeader }">
    <div class="container">
      <div v-if="!hideHeader" class="pricing-header aos-init">
        <span class="section-label">Preços</span>
        <h2 class="pricing-title">Preço justo para <span class="text-gradient">escalar seu negócio.</span></h2>
        <p class="pricing-subtitle">Sem surpresas. Cancele quando quiser. Comece grátis por 30 dias.</p>


      </div>

      <div class="pricing-grid">
        <Card
          v-for="plan in plans"
          :key="plan.id"
          class="pricing-card"
          :class="{ featured: plan.featured }"
        >
          <template #content>
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

            <Button
              class="btn-plan"
              :severity="plan.featured ? 'primary' : 'secondary'"
              :outlined="!plan.featured"
              @click="initiateCheckout(plan.id)"
              rounded
            >
              Começar grátis por 30 dias <i class="pi pi-arrow-right ml-1"></i>
            </Button>

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
          </template>
        </Card>
      </div>

      <!-- Bottom trust -->
      <div v-if="!hideHeader" class="pricing-footer aos-init">
        <div class="trust-row">
          <span class="trust-item">🔒 Pagamento seguro via Stripe</span>
          <span class="trust-item">✓ Cancele a qualquer momento</span>
          <span class="trust-item">🎁 30 dias grátis sem cartão</span>
          <span class="trust-item">💬 Suporte incluído em todos os planos</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';
import Card from 'primevue/card';
import Button from 'primevue/button';

defineProps({
  hideHeader: { type: Boolean, default: false },
});



const plans = ref([
  {
    id: 'price_pro',
    name: 'Neri Pro',
    price: '99,90',
    description: 'Sistema completo de gestão, controle e relacionamento para sua oficina.',
    featured: false,
    features: [
      'Usuários Ilimitados',
      'Suporte via Chat, Telefone e WhatsApp',
      'Gestão de Orçamentos e Ordens de Serviço (OS)',
      'Controle Financeiro e Fluxo de Caixa',
      'Gestão de Estoque e Compras',
      'Relatórios e Dashboards Gerenciais',
      'Cobrança e Relacionamento via WhatsApp',
      '❌ Sem Emissão de Notas Fiscais',
    ],
  },
  {
    id: 'price_elite',
    name: 'Neri Elite',
    price: '199,90',
    description: 'Ideal para oficinas que precisam emitir notas fiscais diretamente pelo sistema.',
    featured: true,
    features: [
      'Todos os recursos do plano Pro',
      'Emissão de Notas Fiscais ilimitadas (NF-e e NFS-e)',
      'NF de Consumidor (NFC-e) e SAT/MF-e',
      'NF de Devolução, Garantia e Retorno',
      'Importação automática de XML de Compra',
      'Manifestação de Destinatário e Busca na SEFAZ',
      'Suporte especializado em Configuração Fiscal',
    ],
  },
]);

const initiateCheckout = async (planId) => {
  try {
    const publishableKey = import.meta.env.VITE_STRIPE_CHAVE_PUBLICA;
    const priceMap = {
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
  background: var(--surface-blue-75);
}

.pricing--embedded {
  padding: 0;
  background: transparent;
}

.pricing--embedded .pricing-grid {
  margin-bottom: 0;
}

.pricing--embedded .pricing-footer {
  margin-top: 1rem;
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
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
  max-width: 900px;
  margin: 0 auto var(--spacing-xl);
  align-items: start;
}

/* ── Card ── */
.pricing-card {
  border: 1.5px solid var(--border) !important;
  border-radius: var(--radius-xl) !important;
  transition: var(--transition-base) !important;
  position: relative;
  background: var(--white) !important;
}

.pricing-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 30px 60px -15px rgba(0,0,0,0.08);
  border-color: var(--primary-border);
}

.pricing-card.featured {
  border-color: var(--primary) !important;
  box-shadow: 0 30px 60px -15px var(--primary-shadow);
  background: linear-gradient(180deg, rgba(37, 99, 235, 0.04) 0%, var(--white) 100%) !important;
}

:deep(.p-card-body) {
  padding: 2.5rem 2rem !important;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.popular-tag {
  position: absolute;
  top: -14px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--primary);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 18px;
  border-radius: 99px;
  white-space: nowrap;
  box-shadow: var(--shadow-indigo);
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

/* Plan info */
.plan-name {
  font-family: var(--font-heading);
  font-size: 1.375rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 0.375rem;
  letter-spacing: -0.02em;
}

.plan-desc {
  font-size: 0.9375rem;
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
  font-size: 3.5rem;
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

.btn-plan {
  width: 100%;
  padding: 0.875rem !important;
  font-size: 0.9375rem !important;
  font-weight: 700 !important;
  font-family: var(--font-body);
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
  background: var(--primary-light);
  color: var(--primary);
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
  background: var(--primary);
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
