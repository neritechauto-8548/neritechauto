<template>
  <section class="hero">
    <div class="hero-mesh" aria-hidden="true">
      <canvas ref="canvasEl" class="hero-canvas"></canvas>
      <div class="hero-mesh-skew"></div>
    </div>

    <div class="container hero-container">
      <div class="hero-copy">
        <p class="hero-eyebrow aos-init">NeriTech Auto</p>

        <h1 class="hero-title aos-init aos-delay-1">
          Software de gestão<br>
          para oficinas crescerem.
        </h1>

        <p class="hero-subtitle aos-init aos-delay-2">
          Tudo em um só lugar: workflow do pátio, orçamentos com aprovação digital,
          checklist com fotos, estoque, financeiro, NF-e e portal do cliente —
          da entrada do veículo até a entrega.
        </p>

        <div class="hero-actions aos-init aos-delay-3">
          <router-link to="/teste-gratis" class="btn-hero-primary" id="hero-cta-start">
            Começar agora
          </router-link>
          <a href="#demonstracao" class="btn-hero-link" id="hero-cta-demo">
            Ver o produto
            <StripeIcon name="arrow" :size="14" />
          </a>
        </div>

        <p class="hero-trust aos-init aos-delay-4">
          180 dias grátis · Sem cartão · Cancele quando quiser
        </p>
      </div>

      <div class="hero-stage aos-init aos-delay-2">
        <div class="stage-card stage-card--main">
          <div class="stage-bar">
            <span class="dot dot--red"></span>
            <span class="dot dot--yellow"></span>
            <span class="dot dot--green"></span>
            <span class="stage-url">app.neritechauto.com.br</span>
          </div>
          <div class="app-shell">
            <aside class="app-side">
              <div class="side-logo">N</div>
              <span class="side-item side-item--on">Pátio</span>
              <span class="side-item">Ordens</span>
              <span class="side-item">Financeiro</span>
              <span class="side-item">Estoque</span>
            </aside>
            <div class="app-body">
              <div class="app-top">
                <span class="app-top-title">Workflow do pátio</span>
                <span class="app-pill">Ao vivo</span>
              </div>
              <div class="app-metrics">
                <div class="metric" v-for="s in stats" :key="s.label">
                  <strong>{{ s.value }}</strong>
                  <span>{{ s.label }}</span>
                </div>
              </div>
              <div class="app-rows">
                <div class="row" v-for="row in osRows" :key="row.plate">
                  <div>
                    <strong>{{ row.vehicle }}</strong>
                    <small>{{ row.plate }}</small>
                  </div>
                  <span>{{ row.service }}</span>
                  <span :class="['tag', `tag--${row.status}`]">{{ row.statusLabel }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="stage-float stage-float--os">
          <div class="float-icon float-icon--indigo">
            <StripeIcon name="camera" :size="16" />
          </div>
          <div>
            <span>Checklist digital</span>
            <strong>12 fotos enviadas</strong>
          </div>
        </div>

        <div class="stage-float stage-float--money">
          <div class="float-icon float-icon--teal">
            <StripeIcon name="message" :size="16" />
          </div>
          <div>
            <span>Orçamento aprovado</span>
            <strong>R$ 1.480 · WhatsApp</strong>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { createStripeGradient } from '../lib/stripeGradient.js';
import StripeIcon from './StripeIcon.vue';

const canvasEl = ref(null);
let gradientApi = null;

const stats = [
  { value: '8', label: 'Na baia' },
  { value: '5', label: 'Aguardando' },
  { value: '3', label: 'Entrega' },
];

const osRows = [
  { vehicle: 'Honda Civic', plate: 'BRA-2E12', service: 'Freios + pastilha', status: 'active', statusLabel: 'Em serviço' },
  { vehicle: 'Toyota Corolla', plate: 'KEL-4910', service: 'Revisão 40 mil', status: 'waiting', statusLabel: 'Orçamento' },
  { vehicle: 'Chevrolet Onix', plate: 'PXT-9182', service: 'Diagnóstico OBD', status: 'done', statusLabel: 'Pronto' },
];

onMounted(() => {
  gradientApi = createStripeGradient(canvasEl.value);
});

onUnmounted(() => {
  gradientApi?.destroy();
});
</script>

<style scoped>
.hero {
  position: relative;
  isolation: isolate;
  padding: 132px 0 0;
  overflow: hidden;
  background: #fff;
  min-height: 720px;
}

.hero-mesh {
  position: absolute;
  left: 0;
  right: 0;
  top: 72px;
  height: min(620px, 78vh);
  z-index: 0;
  pointer-events: none;
  transform: skewY(-8deg);
  transform-origin: 0;
  border-radius: 0 0 40% 0 / 0 0 80px 0;
  overflow: hidden;
}

.hero-canvas {
  position: absolute;
  inset: -12% -4% -8%;
  width: 108%;
  height: 120%;
  transform: skewY(8deg);
  display: block;
}

.hero-mesh-skew {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(255,255,255,0) 55%, rgba(255,255,255,0.85) 88%, #fff 100%);
  pointer-events: none;
}

.hero-container {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: 1fr 1.05fr;
  gap: 3rem;
  align-items: start;
  padding-bottom: 64px;
}

.hero-copy {
  padding-top: 28px;
  max-width: 540px;
}

.hero-eyebrow {
  font-size: 1rem;
  font-weight: 500;
  color: #0A2540;
  margin-bottom: 1rem;
  letter-spacing: -0.02em;
}

.hero-title {
  /* Stripe hero: 56px+ com weight 300 */
  font-size: clamp(2.75rem, 5.5vw, 4.5rem);
  font-weight: 300;
  color: #0A2540 !important;
  letter-spacing: -0.05em;
  line-height: 1.0;
  margin-bottom: 1.5rem;
}

.hero-subtitle {
  /* Stripe: 18-20px no subtítulo do hero */
  font-size: clamp(1.125rem, 1.6vw, 1.3125rem);
  font-weight: 400;
  color: #425466 !important;
  line-height: 1.6;
  margin-bottom: 2.25rem;
  max-width: 480px;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  flex-wrap: wrap;
  margin-bottom: 1.25rem;
}

.btn-hero-primary {
  display: inline-flex;
  align-items: center;
  background: #635BFF;
  color: #fff !important;
  font-weight: 500;
  /* Stripe CTA: 1rem, padding generoso */
  font-size: 1rem;
  padding: 0.8125rem 1.625rem;
  border-radius: 9999px;
  box-shadow: 0 4px 14px rgba(99, 91, 255, 0.35);
  transition: all 0.2s ease;
  text-decoration: none;
  letter-spacing: -0.01em;
}
.btn-hero-primary:hover {
  background: #7A73FF;
  transform: translateY(-1px);
}

.btn-hero-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #635BFF !important;
  font-weight: 500;
  font-size: 1rem;
  text-decoration: none;
  transition: gap 0.2s ease;
  letter-spacing: -0.01em;
}
.btn-hero-link:hover { gap: 10px; }

.hero-trust {
  font-size: 0.875rem;
  color: #8898AA;
  font-weight: 400;
  letter-spacing: -0.01em;
}

.hero-stage {
  position: relative;
  padding: 24px 8px 40px;
}

.stage-card--main {
  background: #fff;
  border-radius: 12px;
  border: 1px solid rgba(50, 50, 93, 0.08);
  box-shadow:
    0 50px 100px -20px rgba(50, 50, 93, 0.25),
    0 30px 60px -30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  transform: perspective(1200px) rotateY(-6deg) rotateX(4deg);
  transition: transform 0.5s ease;
}
.hero-stage:hover .stage-card--main {
  transform: perspective(1200px) rotateY(-3deg) rotateX(2deg);
}

.stage-bar {
  height: 36px;
  background: #F6F9FC;
  border-bottom: 1px solid #E6EBF1;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 7px;
}

.dot { width: 9px; height: 9px; border-radius: 50%; }
.dot--red { background: #FF5F57; }
.dot--yellow { background: #FFBD2E; }
.dot--green { background: #28C840; }

.stage-url {
  flex: 1;
  margin-left: 6px;
  background: #fff;
  border: 1px solid #E6EBF1;
  border-radius: 6px;
  padding: 3px 10px;
  font-size: 0.625rem;
  color: #8898AA;
}

.app-shell {
  display: grid;
  grid-template-columns: 108px 1fr;
  min-height: 320px;
  background: #fff;
}

.app-side {
  background: #F6F9FC;
  border-right: 1px solid #E6EBF1;
  padding: 14px 10px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.side-logo {
  width: 28px;
  height: 28px;
  background: #635BFF;
  color: #fff;
  border-radius: 7px;
  font-weight: 600;
  font-size: 0.8rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 10px;
}

.side-item {
  font-size: 0.6875rem;
  font-weight: 500;
  color: #6B7C93;
  padding: 6px 8px;
  border-radius: 6px;
}
.side-item--on {
  background: #F0EFFF;
  color: #635BFF;
}

.app-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.app-top-title {
  font-size: 0.875rem;
  font-weight: 500;
  color: #0A2540;
}
.app-pill {
  font-size: 0.625rem;
  font-weight: 500;
  padding: 3px 9px;
  border-radius: 9999px;
  background: rgba(13, 148, 136, 0.1);
  color: #0D9488;
}

.app-metrics {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}
.metric {
  background: #F6F9FC;
  border: 1px solid #E6EBF1;
  border-radius: 8px;
  padding: 10px 8px;
  text-align: center;
}
.metric strong {
  display: block;
  font-size: 1rem;
  font-weight: 500;
  color: #0A2540;
  letter-spacing: -0.02em;
}
.metric span {
  font-size: 0.5rem;
  color: #8898AA;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.app-rows {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.row {
  display: grid;
  grid-template-columns: 1.2fr 1fr 0.85fr;
  gap: 6px;
  align-items: center;
  padding: 8px 10px;
  background: #F6F9FC;
  border-radius: 6px;
  border: 1px solid #E6EBF1;
  font-size: 0.6875rem;
  color: #425466;
}
.row strong {
  display: block;
  color: #0A2540;
  font-size: 0.75rem;
  font-weight: 500;
}
.row small {
  color: #8898AA;
  font-size: 0.5625rem;
}

.tag {
  font-size: 0.5625rem;
  font-weight: 500;
  padding: 3px 7px;
  border-radius: 4px;
  width: fit-content;
}
.tag--active  { background: #F0EFFF; color: #635BFF; }
.tag--waiting { background: rgba(255, 176, 32, 0.14); color: #B47A00; }
.tag--done    { background: rgba(13, 148, 136, 0.1); color: #0D9488; }

.stage-float {
  position: absolute;
  background: #fff;
  border: 1px solid rgba(50, 50, 93, 0.08);
  border-radius: 12px;
  padding: 12px 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.12), 0 5px 15px rgba(0, 0, 0, 0.06);
  z-index: 5;
  animation: float-bob 5.5s ease-in-out infinite;
}
.stage-float span {
  display: block;
  font-size: 0.625rem;
  font-weight: 500;
  color: #8898AA;
}
.stage-float strong {
  font-size: 0.8125rem;
  color: #0A2540;
  font-weight: 500;
}
.stage-float--os { top: 8%; left: -28px; }
.stage-float--money { bottom: 12%; right: -20px; animation-delay: 0.7s; }

.float-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.float-icon--indigo { background: #F0EFFF; color: #635BFF; }
.float-icon--teal { background: rgba(13, 148, 136, 0.1); color: #0D9488; }

@keyframes float-bob {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

@media (max-width: 1100px) {
  .stage-float { display: none; }
}

@media (max-width: 900px) {
  .hero { padding-top: 110px; min-height: auto; }
  .hero-mesh { top: 64px; height: 520px; }
  .hero-container { grid-template-columns: 1fr; gap: 2.5rem; }
  .hero-copy { padding-top: 12px; max-width: 100%; }
  .stage-card--main,
  .hero-stage:hover .stage-card--main { transform: none; }
}

@media (max-width: 600px) {
  .hero-title { font-size: 2.15rem; }
  .hero-actions { flex-direction: column; align-items: flex-start; }
  .app-side { display: none; }
  .app-shell { grid-template-columns: 1fr; }
}
</style>
