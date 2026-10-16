<template>
  <section class="features section-surface section-surface--white section-spacer" id="funcionalidades">
    <div class="container">

      <!-- ── Bloco 1: Workflow ── -->
      <div class="feature-block aos-init" id="feat-workflow">
        <div class="feature-block__copy">
          <span class="section-label">Workflow do pátio</span>
          <h2 class="feature-block__title">
            Controle o fluxo da oficina de qualquer lugar, em tempo real.
          </h2>
          <div class="feature-points">
            <div class="fp-item" v-for="p in workflow" :key="p.title">
              <div class="fp-icon" :style="{ background: p.iconBg, color: p.color }">
                <StripeIcon :name="p.icon" :size="18" />
              </div>
              <div>
                <h3>{{ p.title }}</h3>
                <p>{{ p.desc }}</p>
              </div>
            </div>
          </div>
        </div>
        <div class="feature-block__visual aos-init aos-delay-1">
          <div class="feat-mockup">
            <div class="feat-mockup__bar">
              <span class="md md--red"></span>
              <span class="md md--yellow"></span>
              <span class="md md--green"></span>
              <span class="feat-mockup__url">app.neritechauto.com.br/patio</span>
            </div>
            <div class="feat-mockup__body">
              <div class="fmb-header">
                <span class="fmb-title">Workflow do Pátio</span>
                <span class="fmb-badge">● Ao vivo</span>
              </div>
              <div class="fmb-kanban">
                <div class="fmb-col" v-for="col in kanban" :key="col.label">
                  <div class="fmb-col-label">{{ col.label }}</div>
                  <div class="fmb-card" v-for="c in col.cards" :key="c.plate">
                    <span class="fmb-plate">{{ c.plate }}</span>
                    <span class="fmb-vehicle">{{ c.vehicle }}</span>
                    <span class="fmb-tech">{{ c.tech }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- ── Bloco 2: Orçamentos & Pagamentos ── -->
      <div class="feature-block feature-block--reverse aos-init" id="feat-orcamentos">
        <div class="feature-block__copy">
          <span class="section-label">Orçamentos & Pagamentos</span>
          <h2 class="feature-block__title">
            Orçamentos rápidos, aprovação digital e pagamento sem complicação.
          </h2>
          <div class="feature-points">
            <div class="fp-item" v-for="p in payments" :key="p.title">
              <div class="fp-icon" :style="{ background: p.iconBg, color: p.color }">
                <StripeIcon :name="p.icon" :size="18" />
              </div>
              <div>
                <h3>{{ p.title }}</h3>
                <p>{{ p.desc }}</p>
              </div>
            </div>
          </div>
        </div>
        <div class="feature-block__visual aos-init aos-delay-1">
          <div class="feat-mockup">
            <div class="feat-mockup__bar">
              <span class="md md--red"></span>
              <span class="md md--yellow"></span>
              <span class="md md--green"></span>
              <span class="feat-mockup__url">Orçamento #1042 · Honda Civic</span>
            </div>
            <div class="feat-mockup__body" style="padding: 1.25rem;">
              <div class="forc-line" v-for="line in orcLines" :key="line.item">
                <span>{{ line.item }}</span>
                <span>{{ line.price }}</span>
              </div>
              <div class="forc-total">
                <span>Total</span>
                <strong>R$ 680,00</strong>
              </div>
              <button class="forc-btn">Aprovar orçamento</button>
              <div class="forc-status">
                <span>💬</span>
                <span>Cliente aprovou via WhatsApp · 14:32</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- ── Bloco 3: CRM & Inspeção ── -->
      <div class="feature-block aos-init" id="feat-crm">
        <div class="feature-block__copy">
          <span class="section-label">CRM & Inspeção Digital</span>
          <h2 class="feature-block__title">
            Mantenha as baias cheias e aumente o ticket médio.
          </h2>
          <div class="feature-points">
            <div class="fp-item" v-for="p in crm" :key="p.title">
              <div class="fp-icon" :style="{ background: p.iconBg, color: p.color }">
                <StripeIcon :name="p.icon" :size="18" />
              </div>
              <div>
                <h3>{{ p.title }}</h3>
                <p>{{ p.desc }}</p>
              </div>
            </div>
          </div>
        </div>
        <div class="feature-block__visual aos-init aos-delay-1">
          <div class="feat-mockup">
            <div class="feat-mockup__bar">
              <span class="md md--red"></span>
              <span class="md md--yellow"></span>
              <span class="md md--green"></span>
              <span class="feat-mockup__url">Inspeção Visual · ABC-1234</span>
            </div>
            <div class="feat-mockup__body" style="padding: 1.25rem; gap: 0.875rem; display:flex; flex-direction:column;">
              <div class="finsp-row" v-for="item in inspection" :key="item.label">
                <span class="finsp-dot" :style="{ background: item.color }"></span>
                <span class="finsp-label">{{ item.label }}</span>
                <span class="finsp-status" :style="{ color: item.color }">{{ item.status }}</span>
              </div>
              <div class="finsp-photo-row">
                <div class="finsp-photo" v-for="i in 3" :key="i">
                  <span>📷</span>
                </div>
              </div>
              <button class="forc-btn">Enviar inspeção ao cliente</button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </section>
</template>

<script setup>
import StripeIcon from './StripeIcon.vue';

const workflow = [
  {
    icon: 'workflow',
    iconBg: 'rgba(99, 91, 255, 0.08)',
    color: '#635BFF',
    title: 'Gerencie o workflow completo da oficina',
    desc: 'Acompanhe cada veículo em tempo real — da recepção à entrega. Status, baia e técnico em um só painel.',
  },
  {
    icon: 'message',
    iconBg: 'rgba(13, 148, 136, 0.08)',
    color: '#0D9488',
    title: 'Reduza a fricção com mensagens integradas',
    desc: 'Comunique-se com clientes e a equipe dentro do sistema — atualizações, perguntas e aprovações, tudo em um lugar.',
  },
  {
    icon: 'chart',
    iconBg: 'rgba(0, 212, 255, 0.1)',
    color: '#0891B2',
    title: 'Relatórios e performance da equipe',
    desc: 'Monitore o desempenho da oficina com relatórios poderosos: horas produtivas, ticket médio e margem por OS.',
  },
];

const payments = [
  {
    icon: 'receipt',
    iconBg: 'rgba(99, 91, 255, 0.08)',
    color: '#635BFF',
    title: 'Orçamentos e notas em poucos cliques',
    desc: 'Envie orçamentos profissionais e NF-e por WhatsApp ou e-mail. Cliente aprova com um toque.',
  },
  {
    icon: 'currency',
    iconBg: 'rgba(255, 92, 160, 0.1)',
    color: '#DB2777',
    title: 'Receba sem dor de cabeça',
    desc: 'Pix, boleto e cartão integrados. O pagamento entra automaticamente no financeiro — sem digitação.',
  },
  {
    icon: 'box',
    iconBg: 'rgba(255, 176, 32, 0.12)',
    color: '#B47A00',
    title: 'Financeiro completo integrado',
    desc: 'Contas a pagar e receber ligadas à OS. Caixa, comissões e DRE em tempo real — sem planilha.',
  },
];

const crm = [
  {
    icon: 'camera',
    iconBg: 'rgba(99, 91, 255, 0.08)',
    color: '#635BFF',
    title: 'Aumente o ARO com inspeções digitais',
    desc: 'Faça upsell com inspeção visual em fotos e vídeos no celular. Mostre o problema, justifique o serviço.',
  },
  {
    icon: 'calendar',
    iconBg: 'rgba(13, 148, 136, 0.08)',
    color: '#0D9488',
    title: 'Agenda que elimina no-shows',
    desc: 'Confirmações e lembretes automáticos por WhatsApp. Reduza faltas e mantenha as baias sempre cheias.',
  },
  {
    icon: 'star',
    iconBg: 'rgba(255, 92, 160, 0.1)',
    color: '#DB2777',
    title: 'Marketing e CRM integrado',
    desc: 'Histórico por placa, campanhas de retorno e gestão de reputação — tudo dentro do sistema.',
  },
];

const kanban = [
  {
    label: 'Aguardando',
    cards: [
      { plate: 'ABC-1234', vehicle: 'Civic 2022', tech: '—' },
      { plate: 'DEF-5678', vehicle: 'Corolla 2020', tech: '—' },
    ],
  },
  {
    label: 'Em Serviço',
    cards: [
      { plate: 'GHI-9012', vehicle: 'HB20 2021', tech: 'Carlos' },
    ],
  },
  {
    label: 'Concluído',
    cards: [
      { plate: 'JKL-3456', vehicle: 'Onix 2023', tech: 'Pedro' },
    ],
  },
];

const orcLines = [
  { item: 'Pastilha de freio dianteira', price: 'R$ 180,00' },
  { item: 'Discos de freio (par)', price: 'R$ 320,00' },
  { item: 'Mão de obra — freios', price: 'R$ 180,00' },
];

const inspection = [
  { label: 'Pastilhas de freio', status: 'Crítico', color: '#EF4444' },
  { label: 'Pneus dianteiros', status: 'Atenção', color: '#F59E0B' },
  { label: 'Fluido de freio', status: 'Ok', color: '#10B981' },
  { label: 'Filtro de ar', status: 'Ok', color: '#10B981' },
];
</script>

<style scoped>
/* ── Feature Blocks ── */
.feature-block {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
  padding: 5rem 0;
  border-bottom: 1px solid #E6EBF1;
}

.feature-block:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.feature-block--reverse {
  direction: rtl;
}

.feature-block--reverse > * {
  direction: ltr;
}

.feature-block__title {
  /* Stripe: 36-40px, weight 300, comprimido */
  font-size: clamp(1.875rem, 3.2vw, 2.875rem);
  font-weight: 300;
  color: #0A2540;
  letter-spacing: -0.04em;
  line-height: 1.06;
  margin-bottom: 2.5rem;
  margin-top: 0.5rem;
}

/* ── Feature Points ── */
.feature-points {
  display: flex;
  flex-direction: column;
  gap: 1.75rem;
}

.fp-item {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.fp-icon {
  width: 38px;
  height: 38px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 2px;
}

.fp-item h3 {
  font-size: 1.0625rem;
  font-weight: 500;
  color: #0A2540;
  letter-spacing: -0.02em;
  margin-bottom: 0.375rem;
}

.fp-item p {
  font-size: 1rem;
  color: #6B7C93;
  line-height: 1.6;
}

/* ── Feat Mockup ── */
.feat-mockup {
  background: white;
  border: 1px solid #E6EBF1;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.06);
}

.feat-mockup__bar {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 14px;
  background: #F6F9FC;
  border-bottom: 1px solid #E6EBF1;
}

.md {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}
.md--red { background: #FF5F57; }
.md--yellow { background: #FFBD2E; }
.md--green { background: #28C840; }

.feat-mockup__url {
  margin-left: 6px;
  font-size: 0.75rem;
  color: #8898AA;
  font-family: var(--font-mono);
}

.feat-mockup__body {
  padding: 1rem;
}

/* ── Kanban ── */
.fmb-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding: 0 0.25rem;
}

.fmb-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: #0A2540;
}

.fmb-badge {
  font-size: 0.75rem;
  color: #10B981;
  font-weight: 500;
}

.fmb-kanban {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.625rem;
}

.fmb-col-label {
  font-size: 0.6875rem;
  font-weight: 600;
  color: #8898AA;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.5rem;
}

.fmb-card {
  background: #F6F9FC;
  border: 1px solid #E6EBF1;
  border-radius: 7px;
  padding: 0.625rem 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-bottom: 0.4rem;
}

.fmb-plate {
  font-size: 0.6875rem;
  font-weight: 700;
  color: #425466;
  font-family: var(--font-mono);
}

.fmb-vehicle {
  font-size: 0.75rem;
  color: #0A2540;
  font-weight: 500;
}

.fmb-tech {
  font-size: 0.6875rem;
  color: #8898AA;
}

/* ── Orçamento mockup lines ── */
.forc-line {
  display: flex;
  justify-content: space-between;
  font-size: 0.8125rem;
  padding: 9px 11px;
  background: #F6F9FC;
  border-radius: 7px;
  border: 1px solid #E6EBF1;
  color: #425466;
  margin-bottom: 0.5rem;
}

.forc-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-top: 1px solid #E6EBF1;
  font-size: 0.9rem;
  margin-bottom: 0.75rem;
  color: #425466;
}

.forc-total strong {
  font-size: 1.15rem;
  font-weight: 600;
  color: #0A2540;
  letter-spacing: -0.02em;
}

.forc-btn {
  width: 100%;
  padding: 10px;
  background: #635BFF;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  font-size: 0.875rem;
  font-family: var(--font-body);
  cursor: default;
  margin-bottom: 0.75rem;
}

.forc-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.8rem;
  font-weight: 600;
  color: #059669;
  background: rgba(5, 150, 105, 0.08);
  border: 1px solid rgba(5, 150, 105, 0.15);
  padding: 9px 12px;
  border-radius: 6px;
}

/* ── Inspeção ── */
.finsp-row {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.875rem;
  padding: 8px 10px;
  background: #F6F9FC;
  border-radius: 7px;
  border: 1px solid #E6EBF1;
}

.finsp-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.finsp-label {
  flex: 1;
  color: #425466;
  font-weight: 500;
}

.finsp-status {
  font-size: 0.75rem;
  font-weight: 600;
}

.finsp-photo-row {
  display: flex;
  gap: 0.5rem;
  margin: 0.25rem 0;
}

.finsp-photo {
  flex: 1;
  height: 52px;
  background: #F0F2F5;
  border: 1px solid #E6EBF1;
  border-radius: 7px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .feature-block {
    grid-template-columns: 1fr;
    gap: 2.5rem;
  }

  .feature-block--reverse {
    direction: ltr;
  }

  .feature-block__visual {
    order: -1;
  }

  .feature-block--reverse .feature-block__visual {
    order: -1;
  }
}

@media (max-width: 560px) {
  .feature-block {
    padding: 3.5rem 0;
  }
}
</style>
