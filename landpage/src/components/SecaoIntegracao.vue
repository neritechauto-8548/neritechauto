<template>
  <section class="integrations">
    <div class="container">
      <div class="integrations-inner">
        <!-- Text -->
        <div class="int-text aos-init">
          <span class="section-label">Ecossistema</span>
          <h2 class="int-title">Conectado com o que <span class="text-gradient">seu negócio precisa.</span></h2>
          <p class="int-desc">
            A NeriTech não é uma ilha. Integramos nativamente com as ferramentas que você já usa, automatizando o fluxo do banco até o WhatsApp do seu cliente.
          </p>

          <div class="int-list">
            <div class="int-item aos-init aos-delay-1" v-for="item in integrations" :key="item.title">
              <div class="int-icon-wrap">
                <span>{{ item.icon }}</span>
              </div>
              <div class="int-item-text">
                <strong>{{ item.title }}</strong>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Visual -->
        <div class="int-visual aos-init aos-delay-2">
          <div class="hub-map">
            <!-- Center node -->
            <div class="hub-center">
              <span class="hub-logo">N</span>
              <small>NeriTech</small>
            </div>

            <!-- Orbit nodes -->
            <div class="orbit-node" v-for="(node, i) in orbitNodes" :key="node.label"
              :class="`orbit-pos-${i}`"
              :style="{ animationDelay: node.delay }"
            >
              <div class="orbit-icon">{{ node.icon }}</div>
              <span>{{ node.label }}</span>
            </div>

            <!-- SVG lines -->
            <svg class="hub-lines" viewBox="0 0 400 400" aria-hidden="true">
              <line v-for="line in hubLines" :key="line.id"
                :x1="line.x1" :y1="line.y1" :x2="line.x2" :y2="line.y2"
                :stroke="line.color"
                stroke-width="1.5"
                stroke-dasharray="6 4"
                class="dash-line"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const integrations = [
  { icon: '🏦', title: 'Conciliação Bancária', desc: 'Sincronize extratos e baixe contas automaticamente.' },
  { icon: '📱', title: 'WhatsApp Nativo', desc: 'Aprovação de orçamentos e notificações direto no WhatsApp.' },
  { icon: '📄', title: 'NF-e e NFS-e', desc: 'Emissão de notas fiscais em poucos cliques, integrado à SEFAZ.' },
  { icon: '📦', title: 'Fornecedores', desc: 'Cotação e compra de peças integrada ao controle de estoque.' },
]

const orbitNodes = [
  { icon: '🏦', label: 'Banco',       delay: '0s' },
  { icon: '📱', label: 'WhatsApp',   delay: '0.8s' },
  { icon: '📄', label: 'NF-e',        delay: '1.6s' },
  { icon: '📦', label: 'Estoque',     delay: '2.4s' },
]

const hubLines = [
  { id: 1, x1: 200, y1: 200, x2: 80,  y2: 80,  color: 'rgba(99,91,255,0.4)' },
  { id: 2, x1: 200, y1: 200, x2: 320, y2: 80,  color: 'rgba(0,216,255,0.4)' },
  { id: 3, x1: 200, y1: 200, x2: 80,  y2: 320, color: 'rgba(0,200,83,0.4)' },
  { id: 4, x1: 200, y1: 200, x2: 320, y2: 320, color: 'rgba(139,92,246,0.4)' },
]
</script>

<style scoped>
.integrations {
  padding: var(--spacing-2xl) 0;
  background: white;
  overflow: hidden;
}

.integrations-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
}

/* ── Text ── */
.int-title {
  font-size: clamp(1.75rem, 3.5vw, 2.75rem);
  font-weight: 800;
  margin-bottom: 1rem;
  line-height: 1.15;
}

.int-desc {
  font-size: 1.0625rem;
  color: var(--text-muted);
  line-height: 1.7;
  margin-bottom: 2rem;
}

.int-list {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.int-item {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.int-icon-wrap {
  width: 44px;
  height: 44px;
  background: var(--light-bg);
  border: 1px solid var(--border);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  flex-shrink: 0;
  box-shadow: var(--shadow-xs);
}

.int-item-text strong {
  display: block;
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 3px;
}

.int-item-text p {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.5;
}

/* ── Visual ── */
.int-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.hub-map {
  position: relative;
  width: 380px;
  height: 380px;
}

.hub-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90px;
  height: 90px;
  background: white;
  border-radius: 20px;
  border: 1.5px solid var(--border);
  box-shadow: var(--shadow-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 5;
  gap: 4px;
}

.hub-logo {
  width: 40px;
  height: 40px;
  background: var(--primary-indigo);
  border-radius: 10px;
  font-family: var(--font-heading);
  font-weight: 800;
  font-size: 1.25rem;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hub-center small {
  font-size: 0.6rem;
  font-weight: 700;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

/* Orbit nodes */
.orbit-node {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  animation: float-orbit 4s ease-in-out infinite;
  z-index: 4;
}

.orbit-pos-0 { top: 18px; left: 18px; }
.orbit-pos-1 { top: 18px; right: 18px; }
.orbit-pos-2 { bottom: 18px; left: 18px; }
.orbit-pos-3 { bottom: 18px; right: 18px; }

.orbit-icon {
  width: 56px;
  height: 56px;
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  box-shadow: var(--shadow-md);
}

.orbit-node span:last-child {
  font-size: 0.6875rem;
  font-weight: 700;
  color: var(--text-muted);
}

.hub-lines {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.dash-line {
  animation: march 1.5s linear infinite;
}

@keyframes march {
  from { stroke-dashoffset: 10; }
  to   { stroke-dashoffset: 0; }
}

@keyframes float-orbit {
  0%, 100% { transform: translateY(0px); }
  50%       { transform: translateY(-8px); }
}

/* ── Responsive ── */
@media (max-width: 968px) {
  .integrations-inner {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
  }
  .int-item { text-align: left; }
  .hub-map { width: 300px; height: 300px; }
  .hub-center { width: 70px; height: 70px; }
  .orbit-icon { width: 46px; height: 46px; font-size: 1.25rem; }
  .orbit-pos-0 { top: 10px; left: 10px; }
  .orbit-pos-1 { top: 10px; right: 10px; }
  .orbit-pos-2 { bottom: 10px; left: 10px; }
  .orbit-pos-3 { bottom: 10px; right: 10px; }
}
</style>
