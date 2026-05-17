<template>
  <section id="piloto-automatico" class="stats-section">
    <div class="container">
      
      <!-- ClickUp & Stripe Style Light Header -->
      <div class="stats-header aos-init">
        <div class="stats-badge">Fluxo de Trabalho</div>
        <h2 class="stats-title">Sua oficina organizada em um <span class="text-gradient-vibrant">único ecossistema.</span></h2>
        <p class="stats-subtitle">Esqueça fichas de papel e planilhas soltas. Centralize o acompanhamento do pátio, controle a entrada e saída de peças e acompanhe o caixa com simplicidade.</p>
      </div>

      <!-- ClickUp-style Feature Cards (Real Workshop Features) -->
      <div class="automation-features-grid">
        <div class="auto-feature-card">
          <div class="af-icon">📋</div>
          <h3>Centralizador de OS</h3>
          <p>Acompanhe o andamento de cada serviço, o técnico responsável e as peças utilizadas em uma visão unificada e organizada.</p>
        </div>

        <div class="auto-feature-card">
          <div class="af-icon">📦</div>
          <h3>Controle de Estoque</h3>
          <p>Cadastre autopeças com código de barras, controle níveis mínimos e dê baixa automática assim que a OS for finalizada.</p>
        </div>

        <div class="auto-feature-card">
          <div class="af-icon">⚖️</div>
          <h3>Fluxo de Caixa Operacional</h3>
          <p>Registre entradas e saídas diretamente a partir da conclusão das ordens de serviço. Contas a pagar e receber integradas.</p>
        </div>
      </div>

      <!-- Stripe-style Light Dashboard Mock (Real interface components) -->
      <div class="dashboard-mock aos-init">
        <!-- Sidebar -->
        <div class="dash-sidebar">
          <div class="dash-logo">N</div>
          <div class="side-menu">
            <div class="side-item active">
              <span class="side-icon">📋</span>
              <span>Ordens de Serviço</span>
            </div>
            <div class="side-item">
              <span class="side-icon">📦</span>
              <span>Estoque de Peças</span>
            </div>
            <div class="side-item">
              <span class="side-icon">💸</span>
              <span>Financeiro</span>
            </div>
            <div class="side-item">
              <span class="side-icon">👥</span>
              <span>Clientes & Pátio</span>
            </div>
          </div>
        </div>

        <!-- Main area -->
        <div class="dash-main">
          <!-- Top bar -->
          <div class="dash-topbar">
            <div class="dash-title">Visão Geral Operacional</div>
            <div class="dash-topbar-right">
              <div class="period-badge">Pátio Ativo</div>
            </div>
          </div>

          <!-- Widgets — ClickUp-style Functional Metas (No fake huge revenues!) -->
          <div class="widgets-grid">
            <div class="widget" v-for="w in widgets" :key="w.label">
              <div class="widget-label">{{ w.label }}</div>
              <div class="widget-value">{{ w.value }}</div>
              <div class="widget-trend">
                {{ w.desc }}
              </div>
            </div>
          </div>

          <!-- Cash Flow Bar Visual -->
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">Movimentação Operacional (Semana)</span>
              <div class="chart-legend">
                <span class="leg"><i style="background: var(--primary-indigo)"></i> Ordens Abertas</span>
                <span class="leg"><i style="background: #00c853"></i> Concluídas</span>
              </div>
            </div>
            <div class="chart-area">
              <div class="bar-group" v-for="(bar, i) in chartData" :key="i">
                <div class="bar bar-in" :style="{ height: bar.in + '%' }"></div>
                <div class="bar bar-out" :style="{ height: bar.out + '%' }"></div>
                <span class="bar-label">{{ bar.label }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
    </div>
  </section>
</template>

<script setup>
const widgets = [
  { label: 'Serviços em Andamento', value: '4 Ativos',  desc: 'Pátio operacional' },
  { label: 'Peças em Alerta Mínimo', value: '2 Itens',  desc: 'Reposição necessária' },
  { label: 'Contas a Receber Hoje',   value: 'R$ 840,00', desc: 'Faturamento de OS prontas' },
];

const chartData = [
  { in: 60, out: 40, label: 'Seg' },
  { in: 80, out: 70, label: 'Ter' },
  { in: 50, out: 50, label: 'Qua' },
  { in: 90, out: 80, label: 'Qui' },
  { in: 70, out: 60, label: 'Sex' },
  { in: 30, out: 30, label: 'Sáb' },
];
</script>

<style scoped>
.stats-section {
  padding: 6rem 0;
  background: #f8fafc;
  position: relative;
  overflow: hidden;
  border-top: 1px solid #edf2f7;
  border-bottom: 1px solid #edf2f7;
}

.stats-section::before {
  content: '';
  position: absolute;
  top: -200px;
  right: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(99, 91, 255, 0.04) 0%, transparent 70%);
  pointer-events: none;
}

.stats-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.08);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 14px;
  border-radius: 99px;
  margin-bottom: 1.25rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.stats-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto 4rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
  letter-spacing: -0.03em;
}

.stats-subtitle {
  font-size: 1.1rem;
  color: var(--text-main);
  line-height: 1.6;
}

/* ── Automation Grid ── */
.automation-features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  margin-bottom: 5rem;
}

.auto-feature-card {
  background: #ffffff;
  border: 1px solid #edf2f7;
  border-radius: var(--radius-xl);
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: all var(--transition-base);
  box-shadow: 0 4px 10px rgba(10, 37, 64, 0.02);
}

.auto-feature-card:hover {
  transform: translateY(-2px);
  border-color: rgba(99, 91, 255, 0.2);
  box-shadow: 0 12px 30px rgba(10, 37, 64, 0.06);
}

.af-icon {
  font-size: 1.75rem;
  margin-bottom: 1.25rem;
  background: rgba(99, 91, 255, 0.06);
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.auto-feature-card h3 {
  font-size: 1.125rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 0.75rem;
}

.auto-feature-card p {
  font-size: 0.9375rem;
  color: var(--text-main);
  line-height: 1.6;
}

/* ── Dashboard Mock ── */
.dashboard-mock {
  display: grid;
  grid-template-columns: 200px 1fr;
  background: #ffffff;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: 
    0 40px 100px -20px rgba(10, 37, 64, 0.08),
    0 20px 40px -20px rgba(10, 37, 64, 0.06),
    0 0 0 1px rgba(10, 37, 64, 0.04);
  min-height: 460px;
}

/* Sidebar */
.dash-sidebar {
  background: #f8fafc;
  border-right: 1px solid #edf2f7;
  padding: 20px 12px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.dash-logo {
  width: 36px;
  height: 36px;
  background: var(--primary-indigo);
  border-radius: 10px;
  color: white;
  font-weight: 800;
  font-size: 1.125rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.side-menu {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.side-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px;
  border-radius: 8px;
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-main);
  cursor: pointer;
  transition: all 0.2s;
}

.side-item.active {
  background: #ffffff;
  color: var(--primary-indigo);
  border: 1px solid #edf2f7;
  box-shadow: 0 2px 4px rgba(10, 37, 64, 0.03);
}

.side-item:hover:not(.active) {
  background: rgba(0, 0, 0, 0.02);
  color: var(--midnight-navy);
}

.side-icon { font-size: 0.875rem; }

/* Main area */
.dash-main {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: #ffffff;
}

.dash-topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dash-title {
  font-weight: 800;
  font-size: 1rem;
  color: var(--midnight-navy);
}

.period-badge {
  background: rgba(0, 200, 83, 0.08);
  border: 1px solid rgba(0, 200, 83, 0.15);
  border-radius: 99px;
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 700;
  color: #00c853;
}

/* Widgets */
.widgets-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}

.widget {
  background: #f8fafc;
  border: 1px solid #edf2f7;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.widget-label {
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-muted);
}

.widget-value {
  font-size: 1.375rem;
  font-weight: 800;
  color: var(--midnight-navy);
  font-family: var(--font-heading);
  letter-spacing: -0.03em;
}

.widget-trend {
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--text-light);
}

/* Chart */
.chart-card {
  border: 1px solid #edf2f7;
  background: #ffffff;
  border-radius: 14px;
  padding: 20px;
  flex: 1;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-title {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

.chart-legend {
  display: flex;
  gap: 14px;
}

.leg {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.75rem;
  color: var(--text-muted);
  font-weight: 600;
}

.leg i {
  width: 8px;
  height: 8px;
  border-radius: 2px;
  display: inline-block;
  flex-shrink: 0;
}

.chart-area {
  height: 120px;
  display: flex;
  align-items: flex-end;
  gap: 12px;
  border-bottom: 1px solid #edf2f7;
}

.bar-group {
  flex: 1;
  display: flex;
  align-items: flex-end;
  gap: 3px;
  position: relative;
}

.bar {
  flex: 1;
  border-radius: 3px 3px 0 0;
  min-height: 4px;
  transition: height 1s cubic-bezier(0.16, 1, 0.3, 1);
}

.bar-in  { background: var(--primary-indigo); opacity: 0.9; }
.bar-out { background: #00c853; opacity: 0.8; }

.bar-label {
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 0.625rem;
  color: var(--text-light);
  white-space: nowrap;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .automation-features-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 968px) {
  .dashboard-mock { grid-template-columns: 1fr; }
  .dash-sidebar { display: none; }
}

@media (max-width: 600px) {
  .widgets-grid { grid-template-columns: 1fr; }
}
</style>
