<template>
  <section class="hero">
    <!-- Soft Stripe-style Mesh Gradient Background -->
    <div class="stripe-mesh-bg"></div>
    <div class="grid-overlay"></div>

    <div class="container hero-container">
      <!-- Content Column -->
      <div class="hero-content">
        <!-- ClickUp-style Tag -->
        <div class="hero-pill-badge" style="animation-delay: 0s">
          🔧 Ordem de Serviço & Pátio
        </div>

        <!-- Headline -->
        <h1 class="hero-title" style="animation-delay: 0.08s">
          A plataforma de gestão que <span class="text-gradient-vibrant">organiza sua oficina.</span>
        </h1>

        <!-- Subtitle -->
        <p class="hero-subtitle" style="animation-delay: 0.16s">
          Vistoria digital (DVI), ordens de serviço, orçamentos interativos via WhatsApp e controle de estoque integrado com emissão de notas (NFS-e/NF-e). Do pátio ao financeiro de forma inteligente.
        </p>

        <!-- CTAs -->
        <div class="hero-actions" style="animation-delay: 0.24s">
          <router-link to="/teste-gratis" class="btn-hero-primary" id="hero-cta-start">
            Experimentar 30 Dias Grátis
          </router-link>
          <a href="#showcase" class="btn-hero-secondary" id="hero-cta-demo">
            Conhecer os Recursos
          </a>
        </div>
      </div>

      <!-- Visual Column — ClickUp & Stripe Inspired Light Mockup -->
      <div class="hero-visual" style="animation-delay: 0.32s">
        <div class="mockup-wrapper">
          <div class="mockup-glow"></div>
          <div class="mockup-browser">
            <!-- Browser Top Bar -->
            <div class="browser-bar">
              <div class="browser-dots">
                <span class="dot dot-red"></span>
                <span class="dot dot-yellow"></span>
                <span class="dot dot-green"></span>
              </div>
              <div class="browser-url">app.neritechauto.com.br/ordens</div>
            </div>

            <!-- Light Mode CSS-drawn App Preview (ClickUp structure + UltraCar workshop fields) -->
            <div class="app-preview-container">
              <!-- App Sidebar -->
              <div class="app-preview-sidebar">
                <div class="app-logo">N</div>
                <div class="app-menu">
                  <div class="app-menu-item active">📋 Ordens</div>
                  <div class="app-menu-item">📅 Agenda</div>
                  <div class="app-menu-item">📦 Peças</div>
                  <div class="app-menu-item">💰 Financeiro</div>
                </div>
              </div>

              <!-- App Main Content -->
              <div class="app-preview-main">
                <div class="app-main-header">
                  <h3>Pátio Operacional</h3>
                  <span class="badge-active">5 Veículos Hoje</span>
                </div>

                <!-- ClickUp-style Task List -->
                <div class="app-task-list">
                  <!-- Row 1 -->
                  <div class="app-task-row">
                    <div class="task-vehicle">
                      <strong>Honda Civic</strong>
                      <span>Placa: BRA-2E12</span>
                    </div>
                    <div class="task-service">Revisão de Freios</div>
                    <div class="task-status status-active">Em Andamento</div>
                  </div>
                  <!-- Row 2 -->
                  <div class="app-task-row">
                    <div class="task-vehicle">
                      <strong>Toyota Corolla</strong>
                      <span>Placa: KEL-4910</span>
                    </div>
                    <div class="task-service">Troca de Óleo e Filtros</div>
                    <div class="task-status status-waiting">Aguardando Peça</div>
                  </div>
                  <!-- Row 3 -->
                  <div class="app-task-row">
                    <div class="task-vehicle">
                      <strong>Chevrolet Onix</strong>
                      <span>Placa: PXT-9182</span>
                    </div>
                    <div class="task-service">Diagnóstico de Injeção</div>
                    <div class="task-status status-done">Concluído</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- ClickUp-style Floating Cards with Real Workshop Data -->
          <div class="float-card float-card-1">
            <div class="fc-icon">📸</div>
            <div class="fc-body">
              <span class="fc-label">Checklist de Entrada</span>
              <span class="fc-value">12 Itens Verificados</span>
            </div>
          </div>

          <div class="float-card float-card-2">
            <div class="fc-icon">💬</div>
            <div class="fc-body">
              <span class="fc-label">WhatsApp Integração</span>
              <span class="fc-value">Orçamento Enviado</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding: 140px 0 100px;
  overflow: hidden;
  background-color: #ffffff;
}

/* Stripe-style soft pastel mesh gradient */
.stripe-mesh-bg {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.05) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.05) 0%, transparent 40%),
    radial-gradient(circle at 50% 80%, rgba(139, 92, 246, 0.03) 0%, transparent 50%),
    #ffffff;
  z-index: 1;
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(0, 0, 0, 0.015) 1px, transparent 1px),
                    linear-gradient(90deg, rgba(0, 0, 0, 0.015) 1px, transparent 1px);
  background-size: 40px 40px;
  background-position: center;
  z-index: 2;
  pointer-events: none;
}

.hero-container {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 3;
}

/* ── Content ── */
.hero-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-pill-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.06);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 14px;
  border-radius: 99px;
  margin-bottom: 1.5rem;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.hero-title {
  font-size: clamp(2.5rem, 4.5vw, 4.25rem);
  line-height: 1.08;
  letter-spacing: -0.04em;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1.5rem;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.hero-subtitle {
  font-size: clamp(1rem, 1.8vw, 1.1875rem);
  line-height: 1.6;
  color: var(--text-main);
  margin-bottom: 2.5rem;
  max-width: 560px;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.btn-hero-primary {
  background: var(--primary-indigo);
  color: white !important;
  font-weight: 700;
  font-size: 0.9375rem;
  padding: 0.875rem 2rem;
  border-radius: 99px;
  text-decoration: none;
  box-shadow: var(--shadow-indigo);
  transition: all var(--transition-base);
}

.btn-hero-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(99, 91, 255, 0.4);
}

.btn-hero-secondary {
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  font-size: 0.9375rem;
  padding: 0.875rem 2rem;
  border-radius: 99px;
  border: 1.5px solid var(--border);
  text-decoration: none;
  transition: all var(--transition-base);
}

.btn-hero-secondary:hover {
  background: var(--gray-50);
  border-color: var(--gray-300);
}

/* ── Visual ── */
.hero-visual {
  position: relative;
  display: flex;
  justify-content: center;
  animation: fadeInRight 1s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.mockup-wrapper {
  position: relative;
  width: 105%;
}

.mockup-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 85%;
  height: 70%;
  background: radial-gradient(ellipse, rgba(99, 91, 255, 0.1) 0%, transparent 70%);
  filter: blur(60px);
  z-index: -1;
  pointer-events: none;
}

.mockup-browser {
  background: #ffffff;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow:
    0 40px 100px -20px rgba(10, 37, 64, 0.12),
    0 20px 40px -20px rgba(10, 37, 64, 0.08),
    0 0 0 1px rgba(10, 37, 64, 0.05);
  transform: perspective(2000px) rotateY(-12deg) rotateX(6deg);
  transition: transform 1s cubic-bezier(0.16, 1, 0.3, 1);
}

.mockup-browser:hover {
  transform: perspective(2000px) rotateY(-4deg) rotateX(2deg) translateY(-4px);
}

.browser-bar {
  height: 36px;
  background: #f8fafc;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 12px;
  border-bottom: 1px solid #edf2f7;
}

.browser-dots {
  display: flex;
  gap: 6px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.dot-red    { background: #ff5f57; }
.dot-yellow { background: #ffbd2e; }
.dot-green  { background: #28c840; }

.browser-url {
  flex: 1;
  background: #ffffff;
  border-radius: 6px;
  height: 20px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  font-size: 0.6875rem;
  color: var(--text-muted);
  border: 1px solid #edf2f7;
}

/* ── Light CSS App Mockup (ClickUp structure) ── */
.app-preview-container {
  display: grid;
  grid-template-columns: 100px 1fr;
  background: #ffffff;
  height: 240px;
}

.app-preview-sidebar {
  background: #f8fafc;
  border-right: 1px solid #edf2f7;
  padding: 12px 6px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.app-logo {
  width: 24px;
  height: 24px;
  background: var(--primary-indigo);
  color: white;
  font-weight: 800;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  margin: 0 auto;
}

.app-menu {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.app-menu-item {
  font-size: 0.625rem;
  font-weight: 600;
  color: var(--text-muted);
  padding: 6px;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
}

.app-menu-item.active {
  background: rgba(99, 91, 255, 0.08);
  color: var(--primary-indigo);
}

.app-preview-main {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-main-header h3 {
  font-size: 0.8125rem;
  font-weight: 800;
  color: var(--midnight-navy);
}

.badge-active {
  background: rgba(0, 200, 83, 0.1);
  color: #00c853;
  font-size: 0.5625rem;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 99px;
}

.app-task-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.app-task-row {
  display: grid;
  grid-template-columns: 1.2fr 1.2fr 1fr;
  align-items: center;
  padding: 8px;
  background: #f8fafc;
  border-radius: 6px;
  border: 1px solid #edf2f7;
}

.task-vehicle {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.task-vehicle strong {
  font-size: 0.6875rem;
  color: var(--midnight-navy);
}

.task-vehicle span {
  font-size: 0.5625rem;
  color: var(--text-light);
}

.task-service {
  font-size: 0.625rem;
  font-weight: 500;
  color: var(--text-main);
}

.task-status {
  font-size: 0.5625rem;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 4px;
  text-align: center;
  width: fit-content;
}

.status-active  { background: rgba(99, 91, 255, 0.1); color: var(--primary-indigo); }
.status-waiting { background: rgba(245, 158, 11, 0.1); color: #d97706; }
.status-done    { background: rgba(16, 185, 129, 0.1); color: #10b981; }

/* ── Floating Cards ── */
.float-card {
  position: absolute;
  background: #ffffff;
  border-radius: var(--radius-lg);
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 12px 30px rgba(10, 37, 64, 0.08);
  border: 1px solid #edf2f7;
  z-index: 10;
}

.float-card-1 {
  top: 15%;
  left: -20px;
  animation: float-a 5s ease-in-out infinite;
}

.float-card-2 {
  bottom: 20%;
  right: -10px;
  animation: float-b 6s ease-in-out infinite;
}

.fc-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(99, 91, 255, 0.08);
  color: var(--primary-indigo);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  flex-shrink: 0;
}

.fc-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.fc-label {
  font-size: 0.625rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.fc-value {
  font-size: 0.8125rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

/* ── Animations ── */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(16px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(20px); }
  to   { opacity: 1; transform: translateX(0); }
}

@keyframes float-a {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50%       { transform: translateY(-6px) rotate(0.5deg); }
}

@keyframes float-b {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50%       { transform: translateY(-5px) rotate(-0.5deg); }
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .hero-container {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
    padding-top: 60px;
  }
  .hero-content {
    align-items: center;
  }
  .hero-subtitle {
    margin-left: auto;
    margin-right: auto;
  }
  .hero-visual {
    max-width: 560px;
    margin: 0 auto;
  }
  .mockup-browser {
    transform: none;
  }
  .float-card {
    display: none;
  }
}
</style>
