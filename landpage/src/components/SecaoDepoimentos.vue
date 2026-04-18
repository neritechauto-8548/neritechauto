<template>
  <section id="clientes" class="testimonials">
    <div class="container">
      <div class="test-header aos-init">
        <span class="section-label">Depoimentos</span>
        <h2 class="test-title">O que dizem os líderes <span class="text-gradient">do setor.</span></h2>
        <p class="test-subtitle">A NeriTech sustenta a operação das oficinas que mais crescem no Brasil.</p>
      </div>

      <!-- Stars row -->
      <div class="global-rating aos-init aos-delay-1">
        <div class="rating-stars">★★★★★</div>
        <div class="rating-info">
          <strong>4.9/5</strong> de satisfação · <span>baseado em +200 avaliações</span>
        </div>
      </div>

      <!-- Cards grid -->
      <div class="test-grid">
        <div
          class="test-card aos-init"
          :class="`aos-delay-${(i % 3) + 1}`"
          v-for="(t, i) in testimonials"
          :key="t.name"
        >
          <div class="test-stars">★★★★★</div>
          <p class="test-text">"{{ t.text }}"</p>
          <div class="test-author">
            <div class="test-avatar" :style="{ background: t.avatarBg }">{{ t.initials }}</div>
            <div class="test-meta">
              <span class="test-name">{{ t.name }}</span>
              <span class="test-role">{{ t.role }} · {{ t.company }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Logos Marquee -->
      <div class="logos-row aos-init">
        <p class="logos-label">Empresas que confiam na NeriTech</p>
        <div class="marquee-wrapper logos-marquee">
          <div class="marquee-track">
            <div class="logo-pill" v-for="l in [...logos, ...logos]" :key="l + Math.random()">{{ l }}</div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const testimonials = [
  {
    name: 'Carlos Mendes',
    role: 'Proprietário',
    company: 'Mendes Auto Center',
    initials: 'CM',
    avatarBg: 'linear-gradient(135deg, #635bff, #a855f7)',
    text: 'A integração da busca por placa e o faturamento automático nos salvou horas de trabalho manual por dia. Nunca mais voltamos para planilhas.',
  },
  {
    name: 'Ricardo Oliveira',
    role: 'Gerente',
    company: 'RO Officina',
    initials: 'RO',
    avatarBg: 'linear-gradient(135deg, #0891b2, #06b6d4)',
    text: 'Finalmente um sistema que meus mecânicos realmente usam. A interface é limpa, rápida e foca no que importa. Zero treinamento necessário.',
  },
  {
    name: 'Ana Silva',
    role: 'Diretora',
    company: 'Elite Motors',
    initials: 'AS',
    avatarBg: 'linear-gradient(135deg, #ec4899, #f43f5e)',
    text: 'O controle financeiro do NeriTech é o mais preciso que já usei. Tenho visão real do meu lucro, ticket médio e margem — tudo em tempo real.',
  },
  {
    name: 'Marcos Teixeira',
    role: 'Sócio',
    company: 'Teixeira & Filhos',
    initials: 'MT',
    avatarBg: 'linear-gradient(135deg, #059669, #10b981)',
    text: 'Reduzi o tempo de emissão de NF-e de 15 minutos para menos de 2. O checklist com foto eliminou todas as reclamações de clientes.',
  },
  {
    name: 'Fernanda Costa',
    role: 'Proprietária',
    company: 'FC Auto Service',
    initials: 'FC',
    avatarBg: 'linear-gradient(135deg, #d97706, #f59e0b)',
    text: 'Em 3 meses usando o NeriTech, aumentei minha receita em 28%. O controle de estoque evitou que eu perdesse serviços por falta de peça.',
  },
  {
    name: 'Paulo Rodrigues',
    role: 'Dono',
    company: 'Garage PR',
    initials: 'PR',
    avatarBg: 'linear-gradient(135deg, #7c3aed, #8b5cf6)',
    text: 'O suporte da NeriTech é impecável. Qualquer dúvida, resposta em minutos via WhatsApp. Sinto que tenho um parceiro de negócio, não só um software.',
  },
]

const logos = [
  'Mendes Auto Center',
  'Elite Motors',
  'Garage PR',
  'RO Officina',
  'FC Auto Service',
  'Teixeira & Filhos',
]
</script>

<style scoped>
.testimonials {
  padding: var(--spacing-2xl) 0;
  background: var(--light-bg);
}

/* ── Header ── */
.test-header {
  text-align: center;
  max-width: 640px;
  margin: 0 auto 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.test-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 0.75rem;
}

.test-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
}

/* Global rating */
.global-rating {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: var(--spacing-xl);
  padding: 16px 28px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  width: fit-content;
  margin-left: auto;
  margin-right: auto;
  box-shadow: var(--shadow-sm);
}

.rating-stars {
  color: #fbbf24;
  font-size: 1.125rem;
  letter-spacing: 2px;
}

.rating-info {
  font-size: 0.9375rem;
  color: var(--text-muted);
}

.rating-info strong {
  color: var(--midnight-navy);
}

/* ── Cards ── */
.test-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
  margin-bottom: var(--spacing-xl);
}

.test-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  transition: var(--transition-base);
}

.test-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: rgba(99,91,255,0.15);
}

.test-stars {
  color: #fbbf24;
  font-size: 0.875rem;
  letter-spacing: 2px;
}

.test-text {
  font-size: 0.9375rem;
  color: var(--midnight-navy);
  line-height: 1.7;
  flex: 1;
  font-style: italic;
}

.test-author {
  display: flex;
  align-items: center;
  gap: 0.875rem;
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid var(--border);
}

.test-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.875rem;
  color: white;
  flex-shrink: 0;
}

.test-meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.test-name {
  font-weight: 700;
  font-size: 0.9375rem;
  color: var(--midnight-navy);
}

.test-role {
  font-size: 0.8125rem;
  color: var(--text-muted);
}

/* ── Logos ── */
.logos-row {
  text-align: center;
}

.logos-label {
  font-size: 0.8125rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-light);
  margin-bottom: 1.25rem;
}

.logos-marquee {
  margin-top: 0.5rem;
}

.logo-pill {
  padding: 8px 20px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-muted);
  box-shadow: var(--shadow-xs);
  white-space: nowrap;
  flex-shrink: 0;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .test-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .test-grid { grid-template-columns: 1fr; }
  .global-rating { flex-direction: column; gap: 0.5rem; }
}
</style>
