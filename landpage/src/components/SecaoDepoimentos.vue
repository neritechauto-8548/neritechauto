<template>
  <section id="clientes" class="testimonials-section section-spacer">
    <div class="container">
      <!-- Header -->
      <div class="test-header aos-init">
        <span class="section-label">Oficinas reais. Resultados reais.</span>
        <h2 class="test-title">
          Veja por que oficinas confiam<br>
          <span class="text-gradient">no NeriTech Auto.</span>
        </h2>
        <p class="test-subtitle">
          Resultados que donos e gestores de oficina em todo o Brasil estão alcançando.
        </p>
      </div>

      <!-- Testimonials Grid -->
      <div class="test-grid">
        <div class="test-card aos-init" v-for="(t, i) in testimonials" :key="t.name" :class="`aos-delay-${i + 1}`">
          <div class="test-stars">
            <span v-for="s in 5" :key="s">★</span>
          </div>
          <blockquote class="test-quote">"{{ t.quote }}"</blockquote>
          <div class="test-author">
            <div class="test-avatar" :style="{ background: t.avatarBg }">{{ t.initials }}</div>
            <div>
              <div class="test-name">{{ t.name }}</div>
              <div class="test-role">{{ t.role }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Featured quote estilo Shopmonkey -->
      <div class="test-featured aos-init">
        <div class="feat-quote-mark">"</div>
        <blockquote class="feat-quote">
          Conseguimos streamlinar e aumentar nossa produtividade diária. Antes levávamos 1h por dia só em papelada de OS. Hoje o mecânico atualiza tudo no celular enquanto está no pátio.
        </blockquote>
        <div class="feat-author">
          <div class="feat-avatar">RO</div>
          <div>
            <div class="feat-name">Ricardo Oliveira</div>
            <div class="feat-role">Dono · Auto Center Oliveira — Campinas, SP</div>
          </div>
        </div>
      </div>

      <!-- CTA Banner estilo Shopmonkey -->
      <div class="early-access-banner aos-init">
        <div class="eab-left">
          <span class="eab-badge">🚀 Lançamento</span>
          <h3>Seja um dos primeiros a usar</h3>
          <p>
            Vagas abertas para oficinas que querem digitalizar a gestão com preço especial de lançamento.
            180 dias grátis, sem cartão de crédito.
          </p>
        </div>
        <router-link to="/teste-gratis" class="btn btn-primary eab-cta" id="early-access-cta">
          Garantir minha vaga →
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
const testimonials = [
  {
    quote: 'Conseguimos agilizar o fluxo de aprovação de orçamentos. Antes mandávamos foto do papel no WhatsApp — hoje o cliente aprova no link e já entra na OS automaticamente.',
    name: 'Marcos Silva',
    role: 'Gerente · MS Mecânica — São Paulo, SP',
    initials: 'MS',
    avatarBg: 'linear-gradient(135deg, #635BFF 0%, #7A73FF 100%)',
  },
  {
    quote: 'O controle de estoque integrado à OS foi o que mais me impressionou. Chega de pegar peça que não tem ou comprar duplicado. O sistema baixa automaticamente.',
    name: 'Ana Paula Ramos',
    role: 'Proprietária · Ramos Auto Center — BH, MG',
    initials: 'AR',
    avatarBg: 'linear-gradient(135deg, #0D9488 0%, #10B981 100%)',
  },
  {
    quote: 'Minha equipe aprendeu em menos de um dia. Sem treinamento longo, sem complicação. O mecânico já atualiza o status da OS pelo celular enquanto trabalha.',
    name: 'Paulo Ferreira',
    role: 'Dono · Ferreira Funilaria — Curitiba, PR',
    initials: 'PF',
    avatarBg: 'linear-gradient(135deg, #DB2777 0%, #F472B6 100%)',
  },
  {
    quote: 'O financeiro integrado ao caixa e às OSs foi um divisor de águas. Agora tenho DRE mensal em tempo real — antes ficava semanas esperando o contador.',
    name: 'Cláudio Mendes',
    role: 'Sócio · Mendes & Cia Multimarcas — Porto Alegre, RS',
    initials: 'CM',
    avatarBg: 'linear-gradient(135deg, #0891B2 0%, #06B6D4 100%)',
  },
  {
    quote: 'O portal do cliente fez toda a diferença na hora de fidelizar. Ele acompanha a OS pelo celular, vê as fotos da inspeção e aprova o serviço extra sem nem ligar.',
    name: 'Fernanda Costa',
    role: 'Gestora · Costa Center Auto — Goiânia, GO',
    initials: 'FC',
    avatarBg: 'linear-gradient(135deg, #B47A00 0%, #F59E0B 100%)',
  },
  {
    quote: 'Não sou de tecnologia e adorei. Simples e direto ao ponto. O suporte foi fundamental — responderam rápido e resolveram tudo sem abrir ticket.',
    name: 'José Almeida',
    role: 'Proprietário · JA Mecânica — Fortaleza, CE',
    initials: 'JA',
    avatarBg: 'linear-gradient(135deg, #7C3AED 0%, #A78BFA 100%)',
  },
];
</script>

<style scoped>
.testimonials-section {
  background: #F6F9FC;
  border-top: 1px solid #E6EBF1;
}

/* ── Header ── */
.test-header {
  text-align: center;
  max-width: 640px;
  margin: 0 auto 3.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.test-title {
  font-size: clamp(2.25rem, 4.5vw, 3.5rem);
  font-weight: 300;
  letter-spacing: -0.04em;
  line-height: 1.04;
  color: #0A2540;
  margin-bottom: 0.875rem;
  margin-top: 0.375rem;
}

.test-subtitle {
  font-size: 1.0625rem;
  color: #6B7C93;
  line-height: 1.65;
}

/* ── Grid ── */
.test-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
  margin-bottom: 2rem;
}

.test-card {
  background: white;
  border: 1px solid #E6EBF1;
  border-radius: 12px;
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  box-shadow: 0 2px 5px rgba(50, 50, 93, 0.05), 0 1px 1.5px rgba(0, 0, 0, 0.03);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.test-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.06);
  border-color: rgba(99, 91, 255, 0.15);
}

.test-stars {
  color: #FFB020;
  font-size: 0.875rem;
  letter-spacing: 1px;
}

.test-quote {
  font-size: 0.9375rem;
  color: #425466;
  line-height: 1.65;
  flex: 1;
  font-style: normal;
}

.test-author {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding-top: 1rem;
  border-top: 1px solid #E6EBF1;
}

.test-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8125rem;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}

.test-name {
  font-size: 0.9rem;
  font-weight: 600;
  color: #0A2540;
}

.test-role {
  font-size: 0.8125rem;
  color: #8898AA;
}

/* ── Featured quote ── */
.test-featured {
  background: white;
  border: 1px solid #E6EBF1;
  border-radius: 12px;
  padding: 2.5rem 3rem;
  margin-bottom: 2rem;
  position: relative;
  box-shadow: 0 2px 5px rgba(50, 50, 93, 0.05);
}

.feat-quote-mark {
  font-size: 6rem;
  line-height: 0;
  color: #E6EBF1;
  font-family: Georgia, serif;
  position: absolute;
  top: 2.25rem;
  left: 2.5rem;
}

.feat-quote {
  font-size: clamp(1.0625rem, 2vw, 1.25rem);
  font-weight: 400;
  color: #0A2540;
  line-height: 1.55;
  letter-spacing: -0.02em;
  margin-bottom: 1.5rem;
  font-style: normal;
  position: relative;
  z-index: 1;
  padding-left: 1rem;
}

.feat-author {
  display: flex;
  align-items: center;
  gap: 0.875rem;
  position: relative;
  z-index: 1;
}

.feat-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #635BFF 0%, #7A73FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}

.feat-name {
  font-size: 0.9375rem;
  font-weight: 600;
  color: #0A2540;
}

.feat-role {
  font-size: 0.8125rem;
  color: #8898AA;
}

/* ── Early access banner ── */
.early-access-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
  background: #0A2540;
  border-radius: 12px;
  padding: 2.5rem 3rem;
  color: white;
}

.eab-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: rgba(99, 91, 255, 0.3);
  border: 1px solid rgba(99, 91, 255, 0.5);
  color: #a5b4fc;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 99px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.75rem;
}

.eab-left h3 {
  font-size: 1.375rem;
  font-weight: 500;
  color: white;
  margin-bottom: 0.5rem;
  letter-spacing: -0.025em;
}

.eab-left p {
  font-size: 0.9375rem;
  color: rgba(255, 255, 255, 0.65);
  line-height: 1.6;
  max-width: 500px;
}

.eab-cta {
  flex-shrink: 0;
  white-space: nowrap;
  font-size: 0.9375rem;
  padding: 0.875rem 1.75rem;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .test-grid { grid-template-columns: repeat(2, 1fr); }
  .early-access-banner { flex-direction: column; text-align: center; padding: 2rem; }
  .eab-left { display: flex; flex-direction: column; align-items: center; }
  .eab-left p { text-align: center; }
}

@media (max-width: 640px) {
  .test-grid { grid-template-columns: 1fr; }
  .eab-cta { width: 100%; justify-content: center; }
  .test-featured { padding: 2rem 1.5rem; }
  .feat-quote-mark { display: none; }
  .feat-quote { padding-left: 0; }
}
</style>
