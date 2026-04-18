<template>
  <section class="hero section-slanted-hero">
    <!-- Mesh Gradient Background -->
    <div class="stripe-mesh-bg">
      <div class="mesh-vibrant">
        <div class="mesh-cloud-inner"></div>
      </div>
    </div>
    <!-- Slant transition -->
    <div class="slant-bottom"></div>

    <div class="container hero-container">
      <!-- Content Column -->
      <div class="hero-content">
        <!-- Badge -->
        <div class="hero-badge" style="animation-delay: 0s">
          <span class="badge-dot"></span>
          Novo · Checklist Digital com IA
          <span class="badge-arrow">→</span>
        </div>

        <!-- Headline -->
        <h1 class="hero-title" style="animation-delay: 0.08s">
          O sistema #1 para<br>
          oficinas que querem<br>
          <span class="text-gradient-vibrant">lucrar de verdade.</span>
        </h1>

        <!-- Sub -->
        <p class="hero-subtitle" style="animation-delay: 0.16s">
          OS digital, financeiro completo, NF-e, CRM e estoque — tudo em um sistema cloud que seus mecânicos realmente vão usar. Teste grátis por 7 dias.
        </p>

        <!-- CTAs -->
        <div class="hero-actions" style="animation-delay: 0.24s">
          <router-link to="/teste-gratis" class="btn btn-primary btn-lg" id="hero-cta-start">
            Começar grátis por 7 dias
            <span class="btn-arrow">→</span>
          </router-link>
          <a href="/#showcase" class="btn btn-outline-white btn-lg" id="hero-cta-demo">
            Ver como funciona
          </a>
        </div>

        <!-- Social Proof -->
        <div class="hero-social-proof" style="animation-delay: 0.32s">
          <div class="trust-avatars">
            <div class="avatar-stack">
              <img src="https://i.pravatar.cc/80?u=101" alt="Cliente NeriTech" loading="lazy" />
              <img src="https://i.pravatar.cc/80?u=202" alt="Cliente NeriTech" loading="lazy" />
              <img src="https://i.pravatar.cc/80?u=303" alt="Cliente NeriTech" loading="lazy" />
              <img src="https://i.pravatar.cc/80?u=404" alt="Cliente NeriTech" loading="lazy" />
              <img src="https://i.pravatar.cc/80?u=505" alt="Cliente NeriTech" loading="lazy" />
            </div>
            <div class="trust-copy">
              <div class="trust-stars">★★★★★</div>
              <span><strong>+500 oficinas</strong> já transformaram seus resultados</span>
            </div>
          </div>
          <div class="hero-counters">
            <div class="hero-counter">
              <span class="hc-value">142.350+</span>
              <span class="hc-label">OS criadas</span>
            </div>
            <div class="hero-counter-divider"></div>
            <div class="hero-counter">
              <span class="hc-value">R$ 28M+</span>
              <span class="hc-label">Faturados</span>
            </div>
            <div class="hero-counter-divider"></div>
            <div class="hero-counter">
              <span class="hc-value">99.9%</span>
              <span class="hc-label">Uptime</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Visual Column -->
      <div class="hero-visual">
        <div class="mockup-wrapper">
          <div class="mockup-glow"></div>
          <div class="mockup-browser">
            <div class="browser-bar">
              <div class="browser-dots">
                <span class="dot dot-red"></span>
                <span class="dot dot-yellow"></span>
                <span class="dot dot-green"></span>
              </div>
              <div class="browser-url">app.neritech.com.br/dashboard</div>
            </div>
            <img
              src="../assets/gen/dashboard_mockup.png"
              alt="NeriTech Dashboard — Sistema de Gestão para Oficinas"
              class="mockup-img"
              loading="eager"
            />
          </div>

          <!-- Floating cards -->
          <div class="float-card float-card-1">
            <div class="fc-icon" style="background: rgba(0,200,83,0.12); color: #00c853;">📈</div>
            <div class="fc-body">
              <span class="fc-label">Receita do Mês</span>
              <span class="fc-value" style="color: #00c853;">R$ 42.850</span>
            </div>
          </div>

          <div class="float-card float-card-2">
            <div class="fc-icon" style="background: rgba(99,91,255,0.12); color: var(--primary-indigo);">✓</div>
            <div class="fc-body">
              <span class="fc-label">OS Aprovada</span>
              <span class="fc-value" style="color: var(--primary-indigo); font-size: 0.875rem;">Honda Civic · R$ 1.240</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
// Hero animations handled by CSS keyframes
</script>

<style scoped>
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding: 130px 0 160px;
  overflow: hidden;
  background-color: var(--slate-navy);
}

.badge-arrow {
  transition: transform 0.2s;
}
.hero-badge:hover .badge-arrow {
  transform: translateX(3px);
}

.hero-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 1;
}

/* ── Content ── */
.hero-content {
  display: flex;
  flex-direction: column;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(99, 91, 255, 0.15);
  border: 1px solid rgba(99, 91, 255, 0.3);
  padding: 7px 16px;
  border-radius: var(--radius-full);
  font-size: 0.8125rem;
  font-weight: 600;
  color: rgba(255,255,255,0.9);
  margin-bottom: 1.5rem;
  width: fit-content;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.badge-dot {
  width: 7px;
  height: 7px;
  background: var(--primary-cyan);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--primary-cyan);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.6; transform: scale(1.3); }
}

.hero-title {
  font-size: clamp(2.75rem, 5vw, 4.5rem);
  line-height: 1.05;
  letter-spacing: -0.04em;
  font-weight: 800;
  color: white;
  margin-bottom: 1.5rem;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) 0.08s both;
}

.hero-subtitle {
  font-size: clamp(1rem, 2vw, 1.25rem);
  line-height: 1.65;
  color: rgba(255,255,255,0.72);
  margin-bottom: 2.5rem;
  max-width: 520px;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) 0.16s both;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 2.5rem;
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) 0.24s both;
}

.btn-arrow {
  margin-left: 2px;
  transition: transform 0.2s;
  display: inline-block;
}

.btn-primary:hover .btn-arrow { transform: translateX(4px); }

/* ── Social Proof ── */
.hero-social-proof {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(255,255,255,0.1);
  animation: fadeInUp 0.7s cubic-bezier(0.16, 1, 0.3, 1) 0.32s both;
}

.trust-avatars {
  display: flex;
  align-items: center;
  gap: 14px;
}

.avatar-stack {
  display: flex;
}

.avatar-stack img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,0.3);
  margin-left: -10px;
  object-fit: cover;
}

.avatar-stack img:first-child { margin-left: 0; }

.trust-copy {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.trust-stars {
  color: #fbbf24;
  font-size: 0.75rem;
  letter-spacing: 1px;
}

.trust-copy span {
  font-size: 0.8125rem;
  color: rgba(255,255,255,0.75);
  font-weight: 500;
}

.trust-copy strong { color: white; }

.hero-counters {
  display: flex;
  align-items: center;
  gap: 1.25rem;
}

.hero-counter {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.hc-value {
  font-family: var(--font-heading);
  font-size: 1.375rem;
  font-weight: 800;
  color: white;
  letter-spacing: -0.03em;
  line-height: 1;
}

.hc-label {
  font-size: 0.6875rem;
  font-weight: 600;
  color: rgba(255,255,255,0.45);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.hero-counter-divider {
  width: 1px;
  height: 28px;
  background: rgba(255,255,255,0.15);
}

/* ── Visual ── */
.hero-visual {
  position: relative;
  display: flex;
  justify-content: center;
  animation: fadeInRight 1s cubic-bezier(0.16, 1, 0.3, 1) 0.2s both;
}

.mockup-wrapper {
  position: relative;
  width: 110%;
}

.mockup-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 85%;
  height: 70%;
  background: radial-gradient(ellipse, rgba(99,91,255,0.25) 0%, transparent 70%);
  filter: blur(60px);
  z-index: -1;
  pointer-events: none;
}

.mockup-browser {
  background: white;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow:
    0 60px 120px -20px rgba(0,0,0,0.4),
    0 40px 80px -30px rgba(0,0,0,0.3),
    0 0 0 1px rgba(255,255,255,0.1);
  transform: perspective(2000px) rotateY(-16deg) rotateX(8deg);
  transition: transform 1s cubic-bezier(0.16, 1, 0.3, 1);
}

.mockup-browser:hover {
  transform: perspective(2000px) rotateY(-6deg) rotateX(3deg) translateY(-8px);
}

.browser-bar {
  height: 36px;
  background: #f1f3f5;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.browser-dots {
  display: flex;
  gap: 6px;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}
.dot-red    { background: #ff5f57; }
.dot-yellow { background: #ffbd2e; }
.dot-green  { background: #28c840; }

.browser-url {
  flex: 1;
  background: white;
  border-radius: 6px;
  height: 22px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  font-size: 0.6875rem;
  color: var(--text-muted);
  border: 1px solid #e5e7eb;
  font-family: var(--font-body);
}

.mockup-img {
  width: 100%;
  display: block;
}

/* ── Floating Cards ── */
.float-card {
  position: absolute;
  background: white;
  border-radius: var(--radius-lg);
  padding: 14px 18px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: var(--shadow-xl);
  border: 1px solid rgba(0,0,0,0.04);
  z-index: 10;
}

.float-card-1 {
  top: 12%;
  left: -30px;
  animation: float-a 5s ease-in-out infinite;
}

.float-card-2 {
  bottom: 18%;
  right: -20px;
  animation: float-b 6s ease-in-out infinite;
}

.fc-icon {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  flex-shrink: 0;
}

.fc-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.fc-label {
  font-size: 0.6875rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.fc-value {
  font-size: 1rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

/* ── Animations ── */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(30px); }
  to   { opacity: 1; transform: translateX(0); }
}

@keyframes float-a {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50%       { transform: translateY(-12px) rotate(1deg); }
}

@keyframes float-b {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50%       { transform: translateY(-10px) rotate(-1deg); }
}

/* ── Responsive ── */
@media (max-width: 1100px) {
  .mockup-wrapper { width: 100%; }
  .mockup-browser { transform: perspective(2000px) rotateY(-10deg) rotateX(5deg); }
}

@media (max-width: 900px) {
  .hero {
    padding: 120px 0 120px;
    min-height: auto;
  }
  .hero-container {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
  }
  .hero-content { align-items: center; }
  .hero-badge, .hero-subtitle { text-align: center; }
  .hero-actions { justify-content: center; }
  .trust-avatars { justify-content: center; }
  .trust-badges { justify-content: center; }
  .hero-visual { max-width: 560px; margin: 0 auto; }
  .mockup-browser { transform: none; }
  .float-card-1, .float-card-2 { display: none; }
}

@media (max-width: 480px) {
  .hero-actions { flex-direction: column; }
  .hero-actions .btn { width: 100%; justify-content: center; }
}
</style>
