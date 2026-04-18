<template>
  <section class="stats-section">
    <div class="container">
      <div class="stats-header aos-init">
        <span class="section-label">Inteligência de Dados</span>
        <h2 class="stats-title">Dados que viram <span class="text-gradient-vibrant">decisões lucrativas.</span></h2>
        <p class="stats-subtitle">Tenha o controle total da sua oficina com relatórios inteligentes e previsibilidade financeira em tempo real.</p>
      </div>

      <!-- Numbers Row -->
      <div class="metrics-row aos-init aos-delay-1">
        <div class="metric-item" v-for="m in metrics" :key="m.label">
          <div class="metric-value">{{ m.value }}</div>
          <div class="metric-label">{{ m.label }}</div>
          <div class="metric-sub">{{ m.sub }}</div>
        </div>
      </div>

      <!-- Dashboard Mock -->
      <div class="dashboard-mock aos-init aos-delay-2">
        <!-- Sidebar -->
        <div class="dash-sidebar">
          <div class="dash-logo">N</div>
          <div class="side-menu">
            <div class="side-item active">
              <span class="side-icon">📊</span>
              <span>Dashboard</span>
            </div>
            <div class="side-item">
              <span class="side-icon">💰</span>
              <span>Financeiro</span>
            </div>
            <div class="side-item">
              <span class="side-icon">🔧</span>
              <span>O.S. / Serviços</span>
            </div>
            <div class="side-item">
              <span class="side-icon">🚗</span>
              <span>Veículos</span>
            </div>
            <div class="side-item">
              <span class="side-icon">👥</span>
              <span>Clientes</span>
            </div>
            <div class="side-item">
              <span class="side-icon">📦</span>
              <span>Estoque</span>
            </div>
          </div>
        </div>

        <!-- Main area -->
        <div class="dash-main">
          <!-- Top bar -->
          <div class="dash-topbar">
            <div class="dash-title">Dashboard · Abril 2026</div>
            <div class="dash-topbar-right">
              <div class="period-badge">Últimos 30 dias</div>
            </div>
          </div>

          <!-- Widgets -->
          <div class="widgets-grid">
            <div class="widget" v-for="w in widgets" :key="w.label">
              <div class="widget-label">{{ w.label }}</div>
              <div class="widget-value">{{ w.value }}</div>
              <div class="widget-trend" :class="w.up ? 'up' : 'down'">
                {{ w.up ? '↑' : '↓' }} {{ w.change }}
              </div>
            </div>
          </div>

          <!-- Chart -->
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">Fluxo de Caixa — Abril 2026</span>
              <div class="chart-legend">
                <span class="leg"><i style="background: var(--primary-indigo)"></i> Entradas</span>
                <span class="leg"><i style="background: var(--primary-cyan)"></i> Saídas</span>
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
const metrics = [
  { value: '+32%',   label: 'Aumento de Receita',   sub: 'média entre clientes ativos' },
  { value: '2h/dia', label: 'Tempo Economizado',     sub: 'em tarefas administrativas' },
  { value: '84%',    label: 'Taxa de Conversão',     sub: 'de orçamentos aprovados' },
  { value: '+500',   label: 'Oficinas Atendidas',    sub: 'e crescendo todo mês' },
]

const widgets = [
  { label: 'Vendas do Mês',   value: 'R$ 42.850', change: '12% vs mês anterior', up: true },
  { label: 'Ticket Médio',    value: 'R$ 1.150',  change: '8% vs mês anterior',  up: true },
  { label: 'OS Abertas Hoje', value: '14',         change: '3 a mais que ontem',  up: true },
]

const chartData = [
  { in: 55, out: 30, label: '1' },
  { in: 70, out: 35, label: '5' },
  { in: 45, out: 28, label: '10' },
  { in: 85, out: 40, label: '15' },
  { in: 60, out: 32, label: '20' },
  { in: 90, out: 45, label: '25' },
  { in: 75, out: 38, label: '30' },
]
</script>

<style scoped>
.stats-section {
  padding: var(--spacing-2xl) 0;
  background: var(--midnight-navy);
  position: relative;
  overflow: hidden;
}

.stats-section::before {
  content: '';
  position: absolute;
  top: -200px;
  left: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(99,91,255,0.15) 0%, transparent 70%);
  pointer-events: none;
}

.stats-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto var(--spacing-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-header .section-label { color: rgba(255,255,255,0.5); }

.stats-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  color: white;
  margin-bottom: 1rem;
}

.stats-subtitle {
  font-size: 1.1rem;
  color: rgba(255,255,255,0.6);
  line-height: 1.65;
}

/* ── Metrics ── */
.metrics-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2px;
  margin-bottom: var(--spacing-xl);
  background: rgba(255,255,255,0.05);
  border-radius: var(--radius-xl);
  border: 1px solid rgba(255,255,255,0.08);
  overflow: hidden;
}

.metric-item {
  padding: 2rem;
  text-align: center;
  border-right: 1px solid rgba(255,255,255,0.06);
}

.metric-item:last-child { border-right: none; }

.metric-value {
  font-family: var(--font-heading);
  font-size: 2.5rem;
  font-weight: 800;
  color: white;
  letter-spacing: -0.04em;
  line-height: 1;
  margin-bottom: 0.5rem;
}

.metric-label {
  font-size: 0.9375rem;
  font-weight: 600;
  color: rgba(255,255,255,0.8);
  margin-bottom: 0.25rem;
}

.metric-sub {
  font-size: 0.75rem;
  color: rgba(255,255,255,0.4);
}

/* ── Dashboard Mock ── */
.dashboard-mock {
  display: grid;
  grid-template-columns: 200px 1fr;
  background: white;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-2xl);
  border: 1px solid rgba(255,255,255,0.06);
  min-height: 460px;
}

/* Sidebar */
.dash-sidebar {
  background: #f8fafc;
  border-right: 1px solid var(--border);
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
  gap: 3px;
}

.side-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 10px;
  border-radius: 8px;
  font-size: 0.8125rem;
  font-weight: 500;
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
}

.side-item.active {
  background: white;
  color: var(--primary-indigo);
  font-weight: 600;
  box-shadow: var(--shadow-sm);
}

.side-item:hover:not(.active) {
  background: rgba(0,0,0,0.04);
  color: var(--midnight-navy);
}

.side-icon { font-size: 0.875rem; }

/* Main area */
.dash-main {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: white;
}

.dash-topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dash-title {
  font-weight: 700;
  font-size: 1rem;
  color: var(--midnight-navy);
}

.period-badge {
  background: var(--light-bg);
  border: 1px solid var(--border);
  border-radius: 99px;
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-muted);
}

/* Widgets */
.widgets-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}

.widget {
  background: #f8fafc;
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.widget-label {
  font-size: 0.75rem;
  font-weight: 600;
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
  font-weight: 600;
}
.widget-trend.up   { color: #059669; }
.widget-trend.down { color: #dc2626; }

/* Chart */
.chart-card {
  border: 1px solid var(--border);
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
  font-weight: 500;
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
  gap: 10px;
  border-bottom: 1px solid var(--border);
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

.bar-in  { background: var(--primary-indigo); opacity: 0.85; }
.bar-out { background: var(--primary-cyan); opacity: 0.7; }

.bar-label {
  position: absolute;
  bottom: -18px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 0.6rem;
  color: var(--text-light);
  white-space: nowrap;
}

/* ── Responsive ── */
@media (max-width: 968px) {
  .metrics-row { grid-template-columns: repeat(2, 1fr); }
  .dashboard-mock { grid-template-columns: 1fr; }
  .dash-sidebar { display: none; }
  .widgets-grid { grid-template-columns: 1fr 1fr; }
}

@media (max-width: 600px) {
  .metrics-row { grid-template-columns: repeat(2, 1fr); }
  .widgets-grid { grid-template-columns: 1fr; }
}
</style>
