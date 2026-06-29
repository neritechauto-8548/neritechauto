<template>
  <section id="planos" class="pricing" :class="{ 'pricing--embedded': hideHeader }">
    <div class="container">
      <div v-if="!hideHeader" class="pricing-header aos-init">
        <span class="section-label">Preços</span>
        <h2 class="pricing-title">Preço justo para <span class="text-gradient">escalar seu negócio.</span></h2>
        <p class="pricing-subtitle">Sem surpresas. Cancele quando quiser. Comece a usar hoje mesmo.</p>
      </div>

      <div class="pricing-grid">
        <Card
          v-for="plan in plans"
          :key="plan.id"
          class="pricing-card"
          :class="{ featured: plan.featured }"
        >
          <template #content>
            <!-- Badge de Promoção -->
            <div v-if="plan.badge" class="promo-tag">
              {{ plan.badge }}
            </div>
            
            <div class="plan-top">
              <div class="plan-name">{{ plan.name }}</div>
              <p class="plan-desc">{{ plan.description }}</p>
            </div>

            <div class="plan-price-block">
              <div class="plan-price" :class="{ 'price-consult': plan.price === 'Sob Consulta' }">
                <template v-if="plan.price !== 'Sob Consulta'">
                  <span class="currency">R$</span>
                  <span class="amount">{{ plan.price }}</span>
                  <span class="period">/mês</span>
                </template>
                <template v-else>
                  <span class="amount amount--consult">{{ plan.price }}</span>
                </template>
              </div>
              
              <!-- Texto Informativo da Campanha -->
              <div v-if="plan.promoInfo" class="plan-promo-info">
                {{ plan.promoInfo }}
              </div>
            </div>

            <Button
              class="btn-plan"
              :severity="plan.featured ? 'primary' : 'secondary'"
              :outlined="!plan.featured"
              @click="plan.price === 'Sob Consulta' ? contactSales() : initiateCheckout(plan.id)"
              rounded
            >
              {{ plan.buttonLabel }} <i class="pi pi-arrow-right ml-1"></i>
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
          <span class="trust-item">✓ Sem fidelidade, cancele quando quiser</span>
          <span class="trust-item">🎁 6 meses grátis na promoção Basic</span>
          <span class="trust-item">💬 Suporte humanizado incluído</span>
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
    id: 'price_basic',
    name: 'Neri Basic',
    price: '79,90',
    description: 'Sistema essencial de gestão, controle e relacionamento para sua oficina.',
    featured: true,
    badge: '🔥 Campanha Especial',
    promoInfo: 'Promoção por tempo limitado: os próximos 50 assinantes ganham 6 meses GRÁTIS! E depois, mais 6 meses pagando apenas a metade do valor (R$ 39,95/mês).',
    buttonLabel: 'Aproveitar 6 Meses Grátis',
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
    id: 'price_pro',
    name: 'Neri Pro',
    price: 'Sob Consulta',
    description: 'Ideal para oficinas em crescimento que precisam de emissão de notas fiscais e relatórios fiscais avançados.',
    featured: false,
    badge: null,
    promoInfo: null,
    buttonLabel: 'Falar com Consultor',
    features: [
      'Todos os recursos do plano Basic',
      'Emissão de Notas Fiscais ilimitadas (NF-e e NFS-e)',
      'NF de Consumidor (NFC-e) e SAT/MF-e',
      'NF de Devolução, Garantia e Retorno',
      'Importação automática de XML de Compra',
      'Manifestação de Destinatário e Busca na SEFAZ',
      'Suporte especializado em Configuração Fiscal',
      'Módulo Fiscal Avançado (DRE completo)',
    ],
  },
]);

const initiateCheckout = () => {
  // Redireciona para o fluxo de cadastro/teste grátis do sistema
  // onde o cupom ou trial de 6 meses será aplicado.
  window.location.href = '/teste-gratis';
};

const contactSales = () => {
  if (window.Tawk_API && typeof window.Tawk_API.maximize === 'function') {
    window.Tawk_API.maximize();
  } else {
    window.open('https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm', '_blank');
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

.promo-tag {
  position: absolute;
  top: -14px;
  left: 50%;
  transform: translateX(-50%);
  background: #f59e0b; /* Amber 500 */
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 18px;
  border-radius: 99px;
  white-space: nowrap;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.35);
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

.plan-price.price-consult {
  margin-bottom: 0.75rem;
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

.amount--consult {
  font-size: 2.5rem;
  color: var(--midnight-navy);
}

.period {
  font-size: 0.9375rem;
  color: var(--text-light);
}

.plan-promo-info {
  font-size: 0.8125rem;
  font-weight: 600;
  color: #d97706; /* Amber 600 */
  background: #fef3c7; /* Amber 100 */
  padding: 8px 12px;
  border-radius: var(--radius-md);
  line-height: 1.4;
  margin-top: 0.5rem;
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
    gap: 2.5rem;
  }
}
</style>
