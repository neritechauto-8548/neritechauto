<template>
  <section id="recursos" class="features">
    <div class="container">
      <div class="features-header aos-init">
        <span class="section-label">Recursos</span>
        <h2 class="features-title">Resultados que você <span class="text-gradient">sente no bolso.</span></h2>
        <p class="features-subtitle">Esqueça sistemas complexos. Nossa plataforma devolve seu tempo para o que realmente importa: atender e lucrar.</p>
      </div>

      <div class="features-grid">
        <div
          class="feature-card aos-init"
          :class="`aos-delay-${(i % 3) + 1}`"
          v-for="(feature, i) in features"
          :key="feature.title"
        >
          <div class="fc-icon-wrap" :style="{ background: feature.iconBg }">
            <span class="fc-emoji">{{ feature.icon }}</span>
          </div>
          <h3 class="fc-title">{{ feature.title }}</h3>
          <p class="fc-desc">{{ feature.desc }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const features = [
  {
    icon: '📋',
    iconBg: 'rgba(99,91,255,0.1)',
    title: 'O.S. Digital',
    desc: 'Abra, gerencie e envie orçamentos via WhatsApp instantaneamente. Histórico completo de cada serviço realizado.',
  },
  {
    icon: '💰',
    iconBg: 'rgba(0,200,83,0.1)',
    title: 'Fluxo de Caixa',
    desc: 'Controle entradas e saídas com conciliação bancária automática. DRE, ticket médio e margem em tempo real.',
  },
  {
    icon: '🧾',
    iconBg: 'rgba(245,158,11,0.1)',
    title: 'NF-e e NFS-e',
    desc: 'Emissão de notas fiscais de serviço e peças integrada à SEFAZ. Sem complicação, sem erro.',
  },
  {
    icon: '🔍',
    iconBg: 'rgba(59,130,246,0.1)',
    title: 'Busca por Placa',
    desc: 'Dados do veículo e histórico completo de revisões puxados automaticamente pelo número da placa.',
  },
  {
    icon: '📸',
    iconBg: 'rgba(236,72,153,0.1)',
    title: 'Checklist com Fotos',
    desc: 'Vistoria detalhada com registro fotográfico. Elimine reclamações e aumente a confiança do cliente.',
  },
  {
    icon: '📦',
    iconBg: 'rgba(139,92,246,0.1)',
    title: 'Gestão de Estoque',
    desc: 'Controle de peças e insumos com alertas de reposição inteligente. Nunca mais fique sem peça crítica.',
  },
  {
    icon: '👥',
    iconBg: 'rgba(20,184,166,0.1)',
    title: 'CRM e Fidelização',
    desc: 'Histórico completo de revisões e avisos automáticos de manutenção preventiva para reter clientes.',
  },
  {
    icon: '📊',
    iconBg: 'rgba(249,115,22,0.1)',
    title: 'Relatórios Gerenciais',
    desc: 'DRE, CMV, produtividade por técnico e ranking de serviços — tomada de decisão baseada em dados reais.',
  },
  {
    icon: '🔄',
    iconBg: 'rgba(0,216,255,0.1)',
    title: 'Multi-empresa',
    desc: 'Gerencie várias unidades ou franquias em uma única conta com visão consolidada e separada por CNPJ.',
  },
]
</script>

<style scoped>
.features {
  padding: var(--spacing-2xl) 0;
  background: var(--light-bg);
}

.features-header {
  text-align: center;
  max-width: 700px;
  margin: 0 auto var(--spacing-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.features-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.features-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.65;
}

/* ── Grid ── */
.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
}

.feature-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 2rem;
  transition: all var(--transition-base);
  cursor: default;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: rgba(99,91,255,0.2);
}

.fc-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.25rem;
}

.fc-emoji {
  font-size: 1.375rem;
}

.fc-title {
  font-size: 1.0625rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 0.5rem;
  font-family: var(--font-body);
}

.fc-desc {
  font-size: 0.9rem;
  color: var(--text-muted);
  line-height: 1.65;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .features-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .features-grid { grid-template-columns: 1fr; }
}
</style>
