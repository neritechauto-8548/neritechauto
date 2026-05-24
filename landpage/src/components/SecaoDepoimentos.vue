<template>
  <section id="clientes" class="reasons-section">
    <div class="container">
      <div class="reasons-header aos-init">
        <span class="section-label">Por que a NeriTechAuto?</span>
        <h2 class="reasons-title">Construído para quem <span class="text-gradient">vive a rotina de oficina.</span></h2>
        <p class="reasons-subtitle">Cada funcionalidade foi pensada a partir dos problemas reais que donos de oficina enfrentam todo dia.</p>
      </div>

      <div class="reasons-grid">
        <div class="reason-card aos-init" v-for="(r, i) in reasons" :key="r.title" :class="`aos-delay-${i + 1}`">
          <div class="reason-icon">{{ r.icon }}</div>
          <h3>{{ r.title }}</h3>
          <p>{{ r.desc }}</p>
        </div>
      </div>

      <!-- Honest CTA block instead of fake testimonials -->
      <div class="early-access-banner aos-init">
        <div class="eab-left">
          <span class="eab-badge">🚀 Lançamento</span>
          <h3>Seja um dos primeiros a usar</h3>
          <p>Estamos com as primeiras vagas abertas para oficinas que querem digitalizar a gestão com preço especial de lançamento.</p>
        </div>
        <router-link to="/teste-gratis" class="btn btn-primary eab-cta" id="early-access-cta">
          Garantir minha vaga →
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
const reasons = [
  {
    icon: '🔧',
    title: 'Feito para oficina, não para escritório',
    desc: 'Nada de sistemas genéricos adaptados. Cada módulo foi desenhado pensando no fluxo de uma oficina: entrada de veículo, OS, peças, entrega e pagamento.',
  },
  {
    icon: '📱',
    title: 'Funciona em qualquer dispositivo',
    desc: 'Acesse pelo celular, tablet ou computador. Seu mecânico atualiza a OS no pátio e você acompanha tudo do escritório ou de casa.',
  },
  {
    icon: '💬',
    title: 'Suporte humano pelo WhatsApp',
    desc: 'Nada de chatbot ou ticket de suporte. Quando você tiver dúvida, fala com a gente diretamente. Simples assim.',
  },
  {
    icon: '⚡',
    title: 'Configuração rápida, sem complicação',
    desc: 'Em poucas horas sua oficina já está cadastrada, com seus serviços e preços configurados. Sem precisar de técnico de TI.',
  },
  {
    icon: '🔒',
    title: 'Seus dados são seus',
    desc: 'Todas as informações ficam na nuvem com backup automático. Nenhum dado de cliente ou financeiro se perde.',
  },
  {
    icon: '🇧🇷',
    title: 'Sistema brasileiro, para o mercado brasileiro',
    desc: 'NF-e, NFS-e, boleto, Pix — tudo integrado dentro da realidade fiscal e de pagamento do Brasil.',
  },
];
</script>

<style scoped>
.reasons-section {
  padding: var(--spacing-2xl) 0;
  background: var(--light-bg);
}

/* ── Header ── */
.reasons-header {
  text-align: center;
  max-width: 640px;
  margin: 0 auto 3.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.reasons-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 0.75rem;
}

.reasons-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.65;
}

/* ── Grid ── */
.reasons-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
  margin-bottom: 3rem;
}

.reason-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  transition: var(--transition-base);
  box-shadow: var(--shadow-card);
}

.reason-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: rgba(99,91,255,0.15);
}

.reason-icon {
  font-size: 1.75rem;
  width: 48px;
  height: 48px;
  background: rgba(99,91,255,0.06);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.reason-card h3 {
  font-size: 1rem;
  font-weight: 800;
  color: var(--midnight-navy);
  font-family: var(--font-heading);
  line-height: 1.3;
}

.reason-card p {
  font-size: 0.9rem;
  color: var(--text-muted);
  line-height: 1.6;
}

/* ── Early access banner ── */
.early-access-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
  background: var(--midnight-navy);
  border-radius: var(--radius-xl);
  padding: 2.5rem 3rem;
  color: white;
}

.eab-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: rgba(99,91,255,0.3);
  border: 1px solid rgba(99,91,255,0.5);
  color: #a5b4fc;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 99px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.75rem;
}

.eab-left h3 {
  font-size: 1.375rem;
  font-weight: 800;
  color: white;
  margin-bottom: 0.5rem;
  font-family: var(--font-heading);
}

.eab-left p {
  font-size: 0.9375rem;
  color: rgba(255,255,255,0.65);
  line-height: 1.6;
  max-width: 500px;
}

.eab-cta {
  flex-shrink: 0;
  white-space: nowrap;
  font-size: 0.9375rem;
  padding: 0.875rem 1.75rem;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .reasons-grid { grid-template-columns: repeat(2, 1fr); }
  .early-access-banner { flex-direction: column; text-align: center; padding: 2rem; }
  .eab-left { display: flex; flex-direction: column; align-items: center; }
  .eab-left p { text-align: center; }
}

@media (max-width: 640px) {
  .reasons-grid { grid-template-columns: 1fr; }
  .eab-cta { width: 100%; justify-content: center; }
}
</style>
