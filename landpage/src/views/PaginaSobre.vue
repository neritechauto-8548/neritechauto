<template>
  <main class="pagina-sobre">

    <!-- ── Hero ── -->
    <section class="sobre-hero section-surface section-surface--hero section-surface--pattern">
      <div class="container">
        <header class="sobre-intro aos-init">
          <span class="section-label">Sobre nós</span>
          <h1 class="sobre-intro__title">Nossa missão é simplificar a gestão de oficinas no Brasil</h1>
          <p class="sobre-intro__subtitle">
            Criamos tecnologia prática e honesta para que donos de oficinas mecânicas, funilarias e centros automotivos parem de perder tempo com planilhas ou papel e foquem em crescer.
          </p>
        </header>
      </div>
    </section>

    <!-- ── História ── -->
    <section class="sobre-historia section-surface section-surface--white section-spacer">
      <div class="container historia-inner">
        <div class="historia-copy aos-init">
          <span class="modulo-tag">Como tudo começou</span>
          <h2 class="section-title" style="text-align: left;">Tecnologia real para quem põe a mão na massa</h2>
          <p class="section-subtitle" style="text-align: left; max-width: 100%; margin-bottom: 1.5rem;">
            A NeriTechAuto nasceu da observação direta do pátio e do dia a dia de oficinas mecânicas brasileiras. Vimos proprietários trabalhando 12 horas por dia, divididos entre consertar carros, ligar para distribuidores de peças, enviar fotos pelo WhatsApp e tentar equilibrar o caixa no final do dia em cadernos ou planilhas confusas.
          </p>
          <p class="section-subtitle" style="text-align: left; max-width: 100%;">
            Decidimos criar um sistema diferente dos softwares legados complexos e lentos do mercado. Criamos uma ferramenta moderna, rápida e 100% online, que funciona perfeitamente tanto no celular do mecânico quanto no computador da recepção. Focamos no que traz valor real: ordens de serviço ágeis, controle financeiro transparente e comunicação direta via WhatsApp.
          </p>
        </div>
        <div class="historia-visual aos-init aos-delay-1">
          <div class="mockup-frame sobre-mockup">
            <div class="mockup-frame__bar">
              <span class="mockup-dot mockup-dot--red"></span>
              <span class="mockup-dot mockup-dot--yellow"></span>
              <span class="mockup-dot mockup-dot--green"></span>
              <span class="mockup-frame__url">neritechauto.com.br/sobre</span>
            </div>
            <div class="sobre-mockup-body">
              <div class="smb-logo">N</div>
              <h3>NeriTechAuto</h3>
              <p>O braço direito do dono de oficina brasileira.</p>
              <div class="smb-stat">
                <span class="smb-stat-number">100%</span>
                <span class="smb-stat-label">Em nuvem e seguro</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── Valores ── -->
    <section class="sobre-valores section-surface section-surface--light section-spacer">
      <div class="container">
        <header class="section-header aos-init">
          <span class="section-label">Nossos Valores</span>
          <h2 class="section-title">O que nos guia todos os dias</h2>
          <p class="section-subtitle">
            Desenvolvemos soluções pautadas no respeito à rotina real e ao tempo corrido do setor automotivo.
          </p>
        </header>

        <div class="valores-grid">
          <article
            v-for="(valor, i) in valores"
            :key="valor.title"
            class="valor-card premium-card aos-init"
            :class="`aos-delay-${i + 1}`"
          >
            <div class="valor-icon">{{ valor.icon }}</div>
            <h3>{{ valor.title }}</h3>
            <p>{{ valor.desc }}</p>
          </article>
        </div>
      </div>
    </section>

    <!-- ── CTA Final ── -->
    <section class="final-cta section-surface section-surface--alt" id="cta-sobre">
      <div class="container cta-content aos-init">
        <span class="section-label">Começar agora</span>
        <h2 class="cta-title">Sua oficina mais organizada a partir de hoje</h2>
        <p class="cta-desc">30 dias grátis, sem cartão de crédito. Teste todos os recursos sem compromisso.</p>
        <div class="cta-actions">
          <router-link to="/teste-gratis" class="btn-cta-primary">
            Começar teste grátis
            <span class="cta-arrow">→</span>
          </router-link>
          <a href="/#contato" class="btn-cta-secondary">Falar com a equipe</a>
        </div>
      </div>
    </section>

    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoRodape from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

useScrollAnimation();

const valores = [
  {
    icon: '🇧🇷',
    title: 'Feito para a realidade do Brasil',
    desc: 'Sistema alinhado com o fluxo de emissão fiscal (NF-e e NFS-e), impostos e a dinâmica de auto-peças nacional.',
  },
  {
    icon: '💬',
    title: 'Comunicação transparente',
    desc: 'Aprovação de orçamentos e envio de ordens de serviço simplificadas via WhatsApp para encurtar caminhos.',
  },
  {
    icon: '🤝',
    title: 'Parceria e suporte humano',
    desc: 'Sem respostas automáticas robóticas. Nosso time atende e ajuda você diretamente pelo WhatsApp.',
  },
  {
    icon: '🔒',
    title: 'Relações de confiança',
    desc: 'Não exigimos contratos rígidos ou multas. Assine pelo valor que o sistema entrega à sua oficina.',
  },
];
</script>

<style scoped>
/* ── Hero ── */
.sobre-hero {
  padding: 8rem 0 5rem;
}

.sobre-intro {
  text-align: center;
  max-width: 760px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.sobre-intro__title {
  font-size: clamp(2.25rem, 5vw, 3.25rem);
  font-weight: 800;
  color: var(--midnight-navy);
  line-height: 1.1;
  letter-spacing: -0.04em;
  margin-bottom: 1rem;
}

.sobre-intro__subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.65;
  max-width: 600px;
}

/* ── História ── */
.historia-inner {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 4rem;
  align-items: center;
}

.modulo-tag {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.07);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary);
  font-size: 0.8125rem;
  font-weight: 700;
  padding: 5px 12px;
  border-radius: 99px;
  margin-bottom: 1rem;
  letter-spacing: 0.02em;
}

.sobre-mockup {
  box-shadow: var(--shadow-xl);
}

.sobre-mockup-body {
  background: white;
  padding: 2.5rem 2rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.smb-logo {
  width: 48px;
  height: 48px;
  background: var(--primary-indigo);
  border-radius: 12px;
  color: white;
  font-weight: 800;
  font-size: 1.375rem;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-indigo);
}

.sobre-mockup-body h3 {
  font-size: 1.25rem;
  font-weight: 800;
  color: var(--midnight-navy);
}

.sobre-mockup-body p {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.5;
  max-width: 220px;
}

.smb-stat {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border);
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.smb-stat-number {
  font-size: 2rem;
  font-weight: 800;
  color: var(--primary-emerald);
  font-family: var(--font-heading);
}

.smb-stat-label {
  font-size: 0.75rem;
  color: var(--text-muted);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

/* ── Valores ── */
.valores-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.25rem;
}

.valor-card {
  padding: 2rem 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.valor-icon {
  font-size: 2.25rem;
  margin-bottom: 0.5rem;
}

.valor-card h3 {
  font-size: 1.0625rem;
  font-weight: 800;
  color: var(--midnight-navy);
  font-family: var(--font-body);
}

.valor-card p {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.6;
}

/* ── CTA ── */
.final-cta {
  padding: 6rem 0;
  text-align: center;
  border-top: 1px solid var(--border);
}

.cta-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cta-title {
  font-size: clamp(1.75rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  margin-bottom: 0.875rem;
  max-width: 560px;
}

.cta-desc {
  font-size: 1rem;
  color: var(--text-muted);
  margin-bottom: 2rem;
}

.cta-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn-cta-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: var(--primary);
  color: white !important;
  font-weight: 700;
  font-size: 1rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  transition: all var(--transition-base);
  box-shadow: var(--shadow-indigo);
  text-decoration: none;
}

.btn-cta-primary:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
}

.btn-cta-primary:hover .cta-arrow {
  transform: translateX(4px);
}

.cta-arrow {
  transition: transform 0.2s;
  display: inline-block;
}

.btn-cta-secondary {
  display: inline-flex;
  align-items: center;
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  font-size: 1rem;
  padding: 0.9rem 1.75rem;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
  text-decoration: none;
}

.btn-cta-secondary:hover {
  border-color: var(--primary);
  color: var(--primary) !important;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .historia-inner {
    grid-template-columns: 1fr;
    gap: 3rem;
    text-align: center;
  }
  .historia-copy .section-title,
  .historia-copy .section-subtitle {
    text-align: center !important;
  }
  .valores-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .sobre-hero {
    padding: 7rem 0 3rem;
  }
  .valores-grid {
    grid-template-columns: 1fr;
  }
  .cta-actions {
    flex-direction: column;
    align-items: stretch;
  }
  .btn-cta-primary,
  .btn-cta-secondary {
    justify-content: center;
  }
}
</style>
