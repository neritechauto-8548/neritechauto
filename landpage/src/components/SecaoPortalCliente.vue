<template>
  <section id="portal-cliente" class="portal section-surface section-surface--white section-spacer">
    <div class="container portal-inner">
      <div class="portal-visual aos-init">
        <div class="phone-mockup">
          <div class="phone-mockup__frame">
            <div class="phone-notch"></div>
            <img :src="imgMobile" alt="Portal do cliente NeriTech Auto no celular" loading="lazy" />
          </div>
          <div class="phone-card phone-card--1">
            <span class="phone-card__dot"></span>
            <div>
              <small>Status do serviço</small>
              <strong>Em andamento</strong>
            </div>
          </div>
          <div class="phone-card phone-card--2">
            <span>✓</span>
            <span>Orçamento aprovado</span>
          </div>
        </div>
      </div>

      <div class="portal-copy aos-init aos-delay-1">
        <span class="section-label">Portal do Cliente</span>
        <h2 class="section-title" style="text-align:left; margin-bottom:1rem;">
          Seu cliente acompanha tudo pelo celular
        </h2>
        <p class="section-subtitle" style="text-align:left; margin:0 0 1.5rem;">
          Transparência que gera confiança: status do serviço, fotos, orçamentos e histórico de visitas em um portal moderno e profissional.
        </p>
        <ul class="portal-list">
          <li v-for="item in items" :key="item">
            <span class="feature-check">✓</span>
            {{ item }}
          </li>
        </ul>
        <router-link to="/teste-gratis" class="btn btn-primary" style="margin-top:1.75rem;">
          Experimentar grátis →
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import imgMobile from '../assets/images/mobile.png';

const items = [
  'Acompanhamento do serviço em tempo real',
  'Visualização de fotos e laudos digitais',
  'Histórico completo por veículo',
  'Comunicação profissional com a oficina',
];
</script>

<style scoped>
.portal-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.phone-mockup {
  position: relative;
  max-width: 300px;
  margin: 0 auto;
}

.phone-mockup__frame {
  background: var(--midnight-navy);
  border-radius: 32px;
  padding: 12px;
  box-shadow: var(--shadow-xl);
  border: 3px solid #1E293B;
  overflow: hidden;
}

.phone-notch {
  width: 80px;
  height: 6px;
  background: #334155;
  border-radius: 99px;
  margin: 0 auto 10px;
}

.phone-mockup__frame img {
  width: 100%;
  border-radius: 20px;
  display: block;
}

.phone-card {
  position: absolute;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 10px 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: var(--shadow-lg);
  font-size: 0.8125rem;
}

.phone-card--1 { top: 20%; left: -24px; }
.phone-card--2 {
  bottom: 18%;
  right: -20px;
  color: #059669;
  font-weight: 700;
}

.phone-card__dot {
  width: 8px;
  height: 8px;
  background: var(--primary);
  border-radius: 50%;
  animation: pulse-dot 2s ease infinite;
}

.phone-card small {
  display: block;
  font-size: 0.625rem;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.phone-card strong { color: var(--midnight-navy); }

.portal-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.portal-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-main);
}

@keyframes pulse-dot {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}

@media (max-width: 900px) {
  .portal-inner {
    grid-template-columns: 1fr;
    text-align: center;
  }
  .portal-copy .section-title,
  .portal-copy .section-subtitle { text-align: center !important; }
  .portal-list { align-items: flex-start; text-align: left; }
  .phone-card { display: none; }
}
</style>
