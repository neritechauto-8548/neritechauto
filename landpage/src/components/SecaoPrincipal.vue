<template>
  <section class="hero section-surface section-surface--hero section-surface--pattern">
    <div class="hero-shape hero-shape--1" aria-hidden="true"></div>
    <div class="hero-shape hero-shape--2" aria-hidden="true"></div>

    <div class="container hero-container">
      <div class="hero-content">
        <span class="hero-badge aos-init">Sistema de gestão para oficinas</span>

        <h1 class="hero-title aos-init aos-delay-1">
          Controle total da sua oficina em um único sistema.
        </h1>

        <p class="hero-subtitle aos-init aos-delay-2">
          Ordens de serviço, financeiro, estoque e portal do cliente em uma plataforma moderna, simples e profissional.
        </p>

        <div class="hero-actions aos-init aos-delay-3">
          <router-link to="/teste-gratis" class="btn-hero-primary" id="hero-cta-start">
            Teste grátis
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true"><path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round"/></svg>
          </router-link>
          <a href="#demonstracao" class="btn-hero-secondary" id="hero-cta-demo">
            Ver demonstração
          </a>
        </div>

        <p class="hero-trust aos-init aos-delay-4">
          <span>✓ 30 dias grátis</span>
          <span>✓ Sem cartão</span>
          <span>✓ Suporte humanizado</span>
        </p>
      </div>

      <div class="hero-visual aos-init aos-delay-2">
        <div class="mockup-stage">
          <div class="mockup-laptop">
            <div class="mockup-laptop__bezel">
              <div class="mockup-frame">
                <div class="mockup-frame__bar">
                  <span class="mockup-dot mockup-dot--red"></span>
                  <span class="mockup-dot mockup-dot--yellow"></span>
                  <span class="mockup-dot mockup-dot--green"></span>
                  <span class="mockup-frame__url">app.neritechauto.com.br</span>
                </div>
                <div class="app-dashboard">
                  <aside class="app-sidebar">
                    <div class="app-sidebar__logo">N</div>
                    <nav class="app-sidebar__nav">
                      <span class="nav-item active">Dashboard</span>
                      <span class="nav-item">Ordens</span>
                      <span class="nav-item">Financeiro</span>
                      <span class="nav-item">Estoque</span>
                    </nav>
                  </aside>
                  <main class="app-main">
                    <div class="app-main__header">
                      <h3>Visão geral</h3>
                      <span class="pill pill--green">Pátio ativo</span>
                    </div>
                    <div class="app-stats">
                      <div class="stat-card" v-for="s in stats" :key="s.label">
                        <span class="stat-card__value">{{ s.value }}</span>
                        <span class="stat-card__label">{{ s.label }}</span>
                      </div>
                    </div>
                    <div class="app-table">
                      <div class="table-row table-row--head">
                        <span>Veículo</span><span>Serviço</span><span>Status</span>
                      </div>
                      <div class="table-row" v-for="row in osRows" :key="row.plate">
                        <span><strong>{{ row.vehicle }}</strong><small>{{ row.plate }}</small></span>
                        <span>{{ row.service }}</span>
                        <span :class="['status', `status--${row.status}`]">{{ row.statusLabel }}</span>
                      </div>
                    </div>
                  </main>
                </div>
              </div>
            </div>
            <div class="mockup-laptop__base"></div>
          </div>

          <div class="float-card float-card--1">
            <div class="float-card__icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="2"/></svg>
            </div>
            <div>
              <span class="float-card__label">OS em andamento</span>
              <strong>5 veículos no pátio</strong>
            </div>
          </div>

          <div class="float-card float-card--2">
            <div class="float-card__icon float-card__icon--green">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M12 2v20M17 7H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>
            </div>
            <div>
              <span class="float-card__label">Financeiro</span>
              <strong>R$ 4.280 a receber</strong>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const stats = [
  { value: '12', label: 'OS abertas' },
  { value: 'R$ 8.4k', label: 'Faturamento mês' },
  { value: '3', label: 'Alertas estoque' },
];

const osRows = [
  { vehicle: 'Honda Civic', plate: 'BRA-2E12', service: 'Revisão de freios', status: 'active', statusLabel: 'Em andamento' },
  { vehicle: 'Toyota Corolla', plate: 'KEL-4910', service: 'Troca de óleo', status: 'waiting', statusLabel: 'Aguardando' },
  { vehicle: 'Chevrolet Onix', plate: 'PXT-9182', service: 'Diagnóstico', status: 'done', statusLabel: 'Concluído' },
];
</script>

<style scoped>
.hero {
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding: 128px 0 88px;
}

.hero-shape {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;
}

.hero-shape--1 {
  width: 480px;
  height: 480px;
  top: -120px;
  right: -80px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.06) 0%, transparent 70%);
}

.hero-shape--2 {
  width: 320px;
  height: 320px;
  bottom: 10%;
  left: -60px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.04) 0%, transparent 70%);
}

.hero-container {
  display: grid;
  grid-template-columns: 1fr 1.05fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 1;
}

.hero-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-badge {
  display: inline-flex;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--primary);
  background: var(--primary-light);
  border: 1px solid var(--primary-border);
  padding: 6px 14px;
  border-radius: var(--radius-full);
  margin-bottom: 1.5rem;
}

.hero-title {
  font-size: clamp(2.25rem, 4.8vw, 3.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  line-height: 1.08;
  margin-bottom: 1.25rem;
  max-width: 560px;
}

.hero-subtitle {
  font-size: clamp(1rem, 1.8vw, 1.125rem);
  color: var(--text-muted);
  line-height: 1.65;
  margin-bottom: 2rem;
  max-width: 520px;
}

.hero-actions {
  display: flex;
  gap: 0.875rem;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
}

.btn-hero-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: var(--primary);
  color: white !important;
  font-weight: 700;
  font-size: 0.9375rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-indigo);
  transition: all var(--transition-base);
}

.btn-hero-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 10px 28px var(--primary-shadow);
}

.btn-hero-secondary {
  display: inline-flex;
  align-items: center;
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  font-size: 0.9375rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
}

.btn-hero-secondary:hover {
  border-color: var(--primary);
  color: var(--primary) !important;
  background: var(--primary-light);
}

.hero-trust {
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-muted);
}

.hero-trust span { display: flex; align-items: center; gap: 4px; }

/* ── Mockup ── */
.hero-visual { position: relative; }

.mockup-stage {
  position: relative;
  padding: 0 1rem;
}

.mockup-laptop {
  position: relative;
}

.mockup-laptop__bezel {
  padding: 10px 10px 0;
  background: #E2E8F0;
  border-radius: 14px 14px 0 0;
  border: 1px solid var(--border);
  border-bottom: none;
}

.mockup-laptop__base {
  height: 14px;
  background: linear-gradient(180deg, #CBD5E1, #94A3B8);
  border-radius: 0 0 12px 12px;
  margin: 0 8%;
}

.mockup-frame {
  border-radius: 8px 8px 0 0;
  box-shadow: none;
}

.app-dashboard {
  display: grid;
  grid-template-columns: 88px 1fr;
  min-height: 280px;
  background: white;
}

.app-sidebar {
  background: var(--gray-50);
  border-right: 1px solid var(--border);
  padding: 12px 8px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.app-sidebar__logo {
  width: 28px;
  height: 28px;
  background: var(--primary);
  color: white;
  border-radius: 8px;
  font-weight: 800;
  font-size: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.app-sidebar__nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  font-size: 0.5625rem;
  font-weight: 600;
  color: var(--text-muted);
  padding: 6px 4px;
  border-radius: 6px;
  text-align: center;
}

.nav-item.active {
  background: var(--primary-light);
  color: var(--primary);
}

.app-main {
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-main__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-main__header h3 {
  font-size: 0.8125rem;
  font-weight: 800;
}

.pill {
  font-size: 0.5625rem;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 99px;
}

.pill--green {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.app-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.stat-card {
  background: var(--gray-50);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 8px;
  text-align: center;
}

.stat-card__value {
  display: block;
  font-size: 0.75rem;
  font-weight: 800;
  color: var(--midnight-navy);
  font-family: var(--font-heading);
}

.stat-card__label {
  font-size: 0.5rem;
  color: var(--text-muted);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.app-table { display: flex; flex-direction: column; gap: 6px; }

.table-row {
  display: grid;
  grid-template-columns: 1.2fr 1fr 0.8fr;
  gap: 6px;
  align-items: center;
  padding: 7px 8px;
  background: var(--gray-50);
  border-radius: 6px;
  border: 1px solid var(--border);
  font-size: 0.5625rem;
}

.table-row--head {
  background: transparent;
  border: none;
  padding: 0 8px;
  font-weight: 700;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  font-size: 0.5rem;
}

.table-row strong {
  display: block;
  color: var(--midnight-navy);
  font-size: 0.625rem;
}

.table-row small {
  color: var(--text-light);
  font-size: 0.5rem;
}

.status {
  font-size: 0.5rem;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 4px;
  width: fit-content;
}

.status--active  { background: var(--primary-light); color: var(--primary); }
.status--waiting { background: rgba(245, 158, 11, 0.12); color: #D97706; }
.status--done    { background: rgba(5, 150, 105, 0.1); color: #059669; }

.float-card {
  position: absolute;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 12px 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: var(--shadow-lg);
  z-index: 5;
}

.float-card--1 { top: 8%; left: -8px; animation: float-soft 5s ease-in-out infinite; }
.float-card--2 { bottom: 18%; right: -12px; animation: float-soft 6s ease-in-out infinite 0.5s; }

.float-card__icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: var(--primary-light);
  color: var(--primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.float-card__icon--green {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.float-card__label {
  display: block;
  font-size: 0.625rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.float-card strong {
  font-size: 0.8125rem;
  color: var(--midnight-navy);
}

@keyframes float-soft {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

@media (max-width: 1024px) {
  .hero-container {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
  }
  .hero-content { align-items: center; }
  .hero-title, .hero-subtitle { max-width: 100%; }
  .hero-trust { justify-content: center; }
  .hero-actions { justify-content: center; }
  .float-card { display: none; }
}

@media (max-width: 640px) {
  .hero { padding-top: 110px; }
  .hero-actions { flex-direction: column; width: 100%; }
  .btn-hero-primary, .btn-hero-secondary { width: 100%; justify-content: center; }
  .app-stats { grid-template-columns: 1fr; }
}
</style>
