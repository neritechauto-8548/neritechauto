<template>
  <section id="aprovacao" class="approval section-surface section-surface--light section-spacer">
    <div class="container approval-inner">
      <div class="approval-copy aos-init">
        <span class="section-label">Aprovação Online</span>
        <h2 class="section-title" style="text-align:left;">
          Orçamentos aprovados em segundos
        </h2>
        <p class="section-subtitle" style="text-align:left; margin-bottom:1.5rem;">
          Envie orçamentos detalhados pelo WhatsApp. O cliente visualiza peças, serviços e valores — e aprova com um clique, sem ligações ou idas até a oficina.
        </p>
        <ul class="approval-list">
          <li v-for="item in items" :key="item">
            <span class="feature-check">✓</span>
            {{ item }}
          </li>
        </ul>
        <div class="approval-stat aos-init aos-delay-2">
          <strong>+40%</strong>
          <span>de aprovação média com orçamento digital</span>
        </div>
      </div>

      <div class="approval-visual aos-init aos-delay-1">
        <div class="approval-mockup mockup-frame">
          <div class="mockup-frame__bar">
            <span class="mockup-dot mockup-dot--red"></span>
            <span class="mockup-dot mockup-dot--yellow"></span>
            <span class="mockup-dot mockup-dot--green"></span>
            <span class="mockup-frame__url">Orçamento #1042 · Honda Civic</span>
          </div>
          <div class="approval-body">
            <div class="approval-line" v-for="line in budgetLines" :key="line.item">
              <span>{{ line.item }}</span>
              <span>{{ line.price }}</span>
            </div>
            <div class="approval-total">
              <span>Total</span>
              <strong>R$ 680,00</strong>
            </div>
            <button class="approval-btn" type="button">Aprovar orçamento</button>
            <div class="approval-wa">
              <span>💬</span>
              <span>Cliente aprovou via WhatsApp</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const items = [
  'Link exclusivo e seguro por OS',
  'Detalhamento de peças e mão de obra',
  'Aprovação ou rejeição com registro automático',
  'Integração nativa com WhatsApp Business',
];

const budgetLines = [
  { item: 'Pastilha de freio dianteira', price: 'R$ 180,00' },
  { item: 'Discos de freio (par)', price: 'R$ 320,00' },
  { item: 'Mão de obra — freios', price: 'R$ 180,00' },
];
</script>

<style scoped>
.approval-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.approval-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 1.5rem;
}

.approval-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
}

.approval-stat {
  display: inline-flex;
  flex-direction: column;
  gap: 4px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 1rem 1.25rem;
  box-shadow: var(--shadow-card);
}

.approval-stat strong {
  font-size: 1.75rem;
  font-weight: 800;
  color: var(--primary);
  font-family: var(--font-heading);
  letter-spacing: -0.03em;
}

.approval-stat span {
  font-size: 0.8125rem;
  color: var(--text-muted);
  font-weight: 500;
}

.approval-mockup { box-shadow: var(--shadow-xl); }

.approval-body {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: white;
}

.approval-line {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  padding: 10px 12px;
  background: var(--gray-50);
  border-radius: 8px;
  border: 1px solid var(--border);
  color: var(--text-main);
}

.approval-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-top: 1px solid var(--border);
  font-size: 0.9375rem;
}

.approval-total strong {
  font-size: 1.25rem;
  color: var(--midnight-navy);
  font-family: var(--font-heading);
}

.approval-btn {
  width: 100%;
  padding: 12px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-weight: 700;
  font-size: 0.9375rem;
  font-family: var(--font-body);
  cursor: default;
}

.approval-wa {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.8125rem;
  font-weight: 600;
  color: #059669;
  background: rgba(5, 150, 105, 0.08);
  border: 1px solid rgba(5, 150, 105, 0.15);
  padding: 10px 14px;
  border-radius: var(--radius-md);
}

@media (max-width: 900px) {
  .approval-inner {
    grid-template-columns: 1fr;
    text-align: center;
  }
  .approval-copy .section-title,
  .approval-copy .section-subtitle { text-align: center !important; }
  .approval-list { align-items: flex-start; text-align: left; }
  .approval-stat { align-self: center; }
}
</style>
