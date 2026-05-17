<template>
  <section id="financeiro" class="financial section-surface section-surface--white section-spacer">
    <div class="container financial-inner">
      <div class="financial-visual aos-init">
        <div class="financial-mockup mockup-frame">
          <div class="mockup-frame__bar">
            <span class="mockup-dot mockup-dot--red"></span>
            <span class="mockup-dot mockup-dot--yellow"></span>
            <span class="mockup-dot mockup-dot--green"></span>
            <span class="mockup-frame__url">app.neritechauto.com.br/financeiro</span>
          </div>
          <img :src="imgFinancial" alt="Painel financeiro NeriTech Auto" loading="lazy" />
        </div>
        <div class="financial-float">
          <span>↑ 18%</span>
          <small>Faturamento vs. mês anterior</small>
        </div>
      </div>

      <div class="financial-copy aos-init aos-delay-1">
        <span class="section-label">Controle Financeiro</span>
        <h2 class="section-title" style="text-align:left;">
          Saiba exatamente quanto sua oficina lucra
        </h2>
        <p class="section-subtitle" style="text-align:left; margin-bottom:1.5rem;">
          Contas a pagar e receber integradas às ordens de serviço. Sem planilhas paralelas — o financeiro acompanha a operação em tempo real.
        </p>
        <ul class="financial-list">
          <li v-for="item in items" :key="item">
            <span class="feature-check">✓</span>
            {{ item }}
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<script setup>
import imgFinancial from '../assets/images/financial.png';

const items = [
  'Fluxo de caixa operacional',
  'Contas a pagar e receber',
  'Faturamento vinculado às OS',
  'Relatórios gerenciais e DRE simplificado',
  'Emissão de NF-e e NFS-e integrada',
];
</script>

<style scoped>
.financial-inner {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 4rem;
  align-items: center;
}

.financial-visual {
  position: relative;
}

.financial-mockup {
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}

.financial-mockup img {
  width: 100%;
  display: block;
}

.financial-float {
  position: absolute;
  bottom: 12%;
  right: -16px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 12px 16px;
  box-shadow: var(--shadow-lg);
  text-align: center;
}

.financial-float span {
  display: block;
  font-size: 1.25rem;
  font-weight: 800;
  color: #059669;
  font-family: var(--font-heading);
}

.financial-float small {
  font-size: 0.6875rem;
  color: var(--text-muted);
  font-weight: 600;
}

.financial-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.financial-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
}

@media (max-width: 900px) {
  .financial-inner {
    grid-template-columns: 1fr;
  }
  .financial-visual { order: -1; }
  .financial-copy .section-title,
  .financial-copy .section-subtitle { text-align: center !important; }
  .financial-list { align-items: flex-start; }
  .financial-float { display: none; }
}
</style>
