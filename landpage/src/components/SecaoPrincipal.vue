<template>
  <section class="hero">
    <!-- Glow decorativo de fundo -->
    <div class="hero-glow hero-glow--1" aria-hidden="true"></div>
    <div class="hero-glow hero-glow--2" aria-hidden="true"></div>
    <div class="hero-dots" aria-hidden="true"></div>

    <div class="container hero-container">
      <!-- ── Conteúdo (lado esquerdo) ── -->
      <div class="hero-content">
        <span class="hero-badge aos-init">
          🇧🇷 Sistema feito para oficinas automotivas brasileiras
        </span>

        <h1 class="hero-title aos-init aos-delay-1">
          Chega de papel e planilha.<br>Gerencie sua oficina do jeito certo.
        </h1>

        <p class="hero-subtitle aos-init aos-delay-2">
          OS digital com checklist fotográfico, financeiro integrado, controle de estoque e portal do cliente — tudo em um sistema simples, rápido e feito para o dia a dia da sua oficina.
        </p>

        <div class="hero-actions aos-init aos-delay-3">
          <router-link to="/teste-gratis" class="btn-hero-primary" id="hero-cta-start">
            Começar teste grátis
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
              <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </router-link>
          <a href="#demonstracao" class="btn-hero-secondary" id="hero-cta-demo">
            Ver o sistema funcionando
          </a>
        </div>

        <div class="hero-trust aos-init aos-delay-4">
          <span>✓ 30 dias grátis</span>
          <span>✓ Sem cartão de crédito</span>
          <span>✓ Suporte via Chat</span>
        </div>
      </div>

      <!-- ── Visual/Mockup (lado direito) ── -->
      <div class="hero-visual aos-init aos-delay-2">
        <div class="mockup-wrapper">
          <!-- Bezel do laptop -->
          <div class="mockup-bezel">
            <!-- Barra do browser -->
            <div class="mockup-bar">
              <span class="dot dot--red"></span>
              <span class="dot dot--yellow"></span>
              <span class="dot dot--green"></span>
              <span class="mockup-url">app.neritechauto.com.br</span>
            </div>
            <!-- Dashboard App -->
            <div class="app-dashboard">
              <aside class="app-sidebar">
                <div class="sidebar-logo">N</div>
                <nav class="sidebar-nav">
                  <span class="nav-item nav-item--active">Dashboard</span>
                  <span class="nav-item">Ordens</span>
                  <span class="nav-item">Financeiro</span>
                  <span class="nav-item">Estoque</span>
                </nav>
              </aside>
              <main class="app-main">
                <div class="app-header">
                  <h3>Visão geral</h3>
                  <span class="status-pill">Pátio ativo</span>
                </div>
                <div class="app-stats">
                  <div class="stat-card" v-for="s in stats" :key="s.label">
                    <span class="stat-value">{{ s.value }}</span>
                    <span class="stat-label">{{ s.label }}</span>
                  </div>
                </div>
                <div class="app-table">
                  <div class="table-head">
                    <span>Veículo</span><span>Serviço</span><span>Status</span>
                  </div>
                  <div class="table-row" v-for="row in osRows" :key="row.plate">
                    <span>
                      <strong>{{ row.vehicle }}</strong>
                      <small>{{ row.plate }}</small>
                    </span>
                    <span>{{ row.service }}</span>
                    <span :class="['os-status', `os-status--${row.status}`]">{{ row.statusLabel }}</span>
                  </div>
                </div>
              </main>
            </div>
          </div>
          <!-- Base do laptop -->
          <div class="mockup-base"></div>
        </div>

        <!-- Floating cards -->
        <div class="float-card float-card--1">
          <div class="float-icon float-icon--blue">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"/>
              <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div>
            <span class="float-label">OS em andamento</span>
            <strong>5 veículos no pátio</strong>
          </div>
        </div>

        <div class="float-card float-card--2">
          <div class="float-icon float-icon--green">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M12 2v20M17 7H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <span class="float-label">Financeiro</span>
            <strong>R$ 4.280 a receber</strong>
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
/* ── Section ── */
.hero {
  position: relative;
  background: linear-gradient(135deg, #1e3a8a 0%, #1d4ed8 50%, #2563eb 100%);
  color: white;
  padding: 130px 0 90px;
  overflow: hidden;
}

/* Decoração de fundo */
.hero-glow {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}
.hero-glow--1 {
  width: 600px;
  height: 600px;
  top: -200px;
  right: -100px;
  background: radial-gradient(circle, rgba(96, 165, 250, 0.15) 0%, transparent 65%);
}
.hero-glow--2 {
  width: 400px;
  height: 400px;
  bottom: -100px;
  left: -80px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.12) 0%, transparent 65%);
}
.hero-dots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle, rgba(255,255,255,0.08) 1px, transparent 1px);
  background-size: 32px 32px;
  mask-image: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, transparent 80%);
  -webkit-mask-image: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, transparent 80%);
}

/* ── Layout ── */
.hero-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  align-items: center;
  gap: 4rem;
  position: relative;
  z-index: 1;
}

/* ── Content (left) ── */
.hero-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 6px 14px;
  border-radius: 9999px;
  margin-bottom: 1.5rem;
  backdrop-filter: blur(4px);
}

.hero-title {
  font-size: clamp(2rem, 3.5vw, 3.25rem);
  font-weight: 800;
  color: white !important;
  letter-spacing: -0.04em;
  line-height: 1.1;
  margin-bottom: 1.25rem;
}

.hero-subtitle {
  font-size: 1.0625rem;
  color: rgba(255, 255, 255, 0.75) !important;
  line-height: 1.65;
  margin-bottom: 2rem;
  max-width: 480px;
}

.hero-actions {
  display: flex;
  gap: 0.875rem;
  flex-wrap: wrap;
  margin-bottom: 2rem;
}

.btn-hero-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: white;
  color: #1d4ed8 !important;
  font-weight: 700;
  font-size: 0.9375rem;
  padding: 0.8125rem 1.625rem;
  border-radius: 9999px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
}
.btn-hero-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.3);
  background: #f8fafc;
}

.btn-hero-secondary {
  display: inline-flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  color: white !important;
  font-weight: 600;
  font-size: 0.9375rem;
  padding: 0.8125rem 1.625rem;
  border-radius: 9999px;
  border: 1.5px solid rgba(255, 255, 255, 0.3);
  transition: all 0.25s;
  text-decoration: none;
  backdrop-filter: blur(4px);
}
.btn-hero-secondary:hover {
  background: rgba(255, 255, 255, 0.18);
  border-color: rgba(255, 255, 255, 0.5);
}

.hero-trust {
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
  font-size: 0.8125rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.7);
}
.hero-trust span { display: flex; align-items: center; gap: 5px; }

/* ── Visual / Mockup ── */
.hero-visual {
  position: relative;
}

.mockup-wrapper {
  position: relative;
  filter: drop-shadow(0 30px 60px rgba(0, 0, 0, 0.35));
}

.mockup-bezel {
  background: #1e293b;
  border-radius: 14px 14px 0 0;
  border: 2px solid #334155;
  border-bottom: none;
  overflow: hidden;
}

.mockup-bar {
  height: 36px;
  background: #0f172a;
  border-bottom: 1px solid #1e293b;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 8px;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}
.dot--red    { background: #FF5F57; }
.dot--yellow { background: #FFBD2E; }
.dot--green  { background: #28C840; }

.mockup-url {
  flex: 1;
  margin-left: 6px;
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 6px;
  padding: 4px 12px;
  font-size: 0.625rem;
  color: #64748b;
  font-family: var(--font-body);
}

.mockup-base {
  height: 14px;
  background: linear-gradient(180deg, #334155, #1e293b);
  border-radius: 0 0 14px 14px;
  margin: 0 8%;
}

/* ── App Dashboard ── */
.app-dashboard {
  display: grid;
  grid-template-columns: 110px 1fr;
  min-height: 340px;
  background: #f8fafc;
}

.app-sidebar {
  background: #f1f5f9;
  border-right: 1px solid #e2e8f0;
  padding: 16px 10px;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.sidebar-logo {
  width: 30px;
  height: 30px;
  background: #2563eb;
  color: white;
  border-radius: 8px;
  font-weight: 800;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  font-size: 0.6875rem;
  font-weight: 600;
  color: #64748b;
  padding: 6px 8px;
  border-radius: 6px;
  text-align: left;
  cursor: pointer;
}
.nav-item--active {
  background: #dbeafe;
  color: #1d4ed8;
}

.app-main {
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  background: white;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.app-header h3 {
  font-size: 0.875rem;
  font-weight: 800;
  color: #0f172a;
}

.status-pill {
  font-size: 0.625rem;
  font-weight: 700;
  padding: 3px 10px;
  border-radius: 9999px;
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.app-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.stat-card {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 10px 8px;
  text-align: center;
}
.stat-value {
  display: block;
  font-size: 1rem;
  font-weight: 800;
  color: #0f172a;
}
.stat-label {
  font-size: 0.5rem;
  color: #64748b;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.app-table {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.table-head {
  display: grid;
  grid-template-columns: 1.2fr 1fr 0.8fr;
  padding: 0 10px;
  font-size: 0.5625rem;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}
.table-row {
  display: grid;
  grid-template-columns: 1.2fr 1fr 0.8fr;
  gap: 6px;
  align-items: center;
  padding: 8px 10px;
  background: #f8fafc;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  font-size: 0.6875rem;
}
.table-row strong {
  display: block;
  color: #0f172a;
  font-size: 0.75rem;
}
.table-row small {
  color: #94a3b8;
  font-size: 0.5625rem;
}

.os-status {
  font-size: 0.5625rem;
  font-weight: 700;
  padding: 3px 7px;
  border-radius: 4px;
  width: fit-content;
}
.os-status--active  { background: #dbeafe; color: #1d4ed8; }
.os-status--waiting { background: rgba(245, 158, 11, 0.12); color: #d97706; }
.os-status--done    { background: rgba(5, 150, 105, 0.1); color: #059669; }

/* ── Float cards ── */
.float-card {
  position: absolute;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 10px 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
  z-index: 5;
}
.float-card--1 {
  top: 15%;
  left: -40px;
  animation: float-bob 5s ease-in-out infinite;
}
.float-card--2 {
  bottom: 18%;
  right: -40px;
  animation: float-bob 6s ease-in-out infinite 0.6s;
}

.float-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.float-icon--blue { background: #dbeafe; color: #1d4ed8; }
.float-icon--green { background: rgba(5, 150, 105, 0.1); color: #059669; }

.float-label {
  display: block;
  font-size: 0.5625rem;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}
.float-card strong {
  font-size: 0.75rem;
  color: #0f172a;
  font-weight: 700;
}

@keyframes float-bob {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

/* ── Dark Mode ── */
:global(.p-dark) .hero {
  background: linear-gradient(135deg, #020617 0%, #0f172a 50%, #1e293b 100%) !important;
}

/* ── Responsive ── */
@media (max-width: 1100px) {
  .float-card { display: none; }
}

@media (max-width: 900px) {
  .hero {
    padding: 110px 0 60px;
  }
  .hero-container {
    grid-template-columns: 1fr;
    gap: 3rem;
    text-align: center;
  }
  .hero-content {
    align-items: center;
  }
  .hero-subtitle {
    max-width: 540px;
  }
  .hero-actions {
    justify-content: center;
  }
  .hero-trust {
    justify-content: center;
  }
}

@media (max-width: 600px) {
  .hero {
    padding: 100px 0 50px;
  }
  .hero-title {
    font-size: 2rem;
  }
  .hero-actions {
    flex-direction: column;
    width: 100%;
  }
  .btn-hero-primary,
  .btn-hero-secondary {
    width: 100%;
    justify-content: center;
  }
  .app-sidebar { display: none; }
  .app-dashboard { grid-template-columns: 1fr; }
}
</style>
