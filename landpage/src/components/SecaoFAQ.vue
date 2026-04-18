<template>
  <section id="faq" class="faq">
    <div class="container">
      <div class="faq-layout">
        <!-- Left: Title -->
        <div class="faq-intro aos-init">
          <span class="section-label">FAQ</span>
          <h2 class="faq-title">Perguntas <span class="text-gradient">frequentes.</span></h2>
          <p class="faq-subtitle">Tudo o que você precisa saber antes de começar. Não encontrou sua dúvida? Fale com nosso time.</p>
          <a href="https://wa.me/5511987654321" target="_blank" rel="noopener" class="btn btn-outline-dark btn-lg" id="faq-wpp-btn">
            💬 Falar no WhatsApp
          </a>
        </div>

        <!-- Right: Accordion -->
        <div class="faq-accordion">
          <div
            class="faq-item aos-init"
            :class="[`aos-delay-${(i % 5) + 1}`, { open: openIndex === i }]"
            v-for="(item, i) in faqs"
            :key="item.q"
          >
            <button class="faq-question" @click="toggle(i)" :aria-expanded="openIndex === i">
              <span>{{ item.q }}</span>
              <span class="faq-chevron">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="6 9 12 15 18 9"></polyline>
                </svg>
              </span>
            </button>
            <div class="faq-answer" :style="{ maxHeight: openIndex === i ? '400px' : '0', padding: openIndex === i ? '0 1.5rem 1.5rem' : '0 1.5rem' }">
              <p>{{ item.a }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const openIndex = ref(0);

const toggle = (i) => {
  openIndex.value = openIndex.value === i ? -1 : i;
};

const faqs = [
  {
    q: 'Quanto tempo leva para começar a usar?',
    a: 'Menos de 5 minutos. Crie sua conta, cadastre sua oficina e comece a abrir ordens de serviço imediatamente. Se precisar de migração de dados do seu sistema anterior, nossa equipe faz para você em até 48 horas — gratuitamente.',
  },
  {
    q: 'Preciso instalar algo no meu computador?',
    a: 'Não! A NeriTech é 100% na nuvem. Você acessa de qualquer navegador, em qualquer dispositivo — computador, tablet ou celular. Sem instalações, sem atualizações manuais.',
  },
  {
    q: 'E se eu já uso outro sistema? Vocês migram meus dados?',
    a: 'Sim, 100% gratuito. Nossa equipe importa seus cadastros de clientes, veículos, produtos e histórico de ordens de serviço. O processo é feito em até 48 horas e não causa nenhuma interrupção no seu dia a dia.',
  },
  {
    q: 'Posso emitir nota fiscal pelo sistema?',
    a: 'Sim! Emitimos NF-e (produtos) e NFS-e (serviços) integrado diretamente à SEFAZ. Tudo com poucos cliques, sem precisar acessar outro sistema. Disponível nos planos Neri Pro e Neri Elite.',
  },
  {
    q: 'O sistema funciona para mais de uma loja?',
    a: 'Sim. O plano Neri Elite oferece o módulo Multilojas, que permite gerenciar várias unidades ou franquias em uma única conta, com visão consolidada ou separada por CNPJ.',
  },
  {
    q: 'Como funciona o suporte?',
    a: 'Nosso suporte é humano e real — respondemos via WhatsApp em tempo médio de menos de 2 minutos durante o horário comercial. Nos planos Pro e Elite, o suporte é prioritário com canal dedicado.',
  },
  {
    q: 'Qual o compromisso de contrato?',
    a: 'Nenhum! A NeriTech não exige fidelidade. Você pode cancelar a qualquer momento, sem multa e sem burocracia. Oferecemos ainda garantia de satisfação de 30 dias com reembolso total.',
  },
  {
    q: 'Meus dados estão seguros?',
    a: 'Absolutamente. Usamos criptografia SSL de ponta a ponta, servidores na AWS com backups diários automáticos, e estamos em total conformidade com a LGPD. Seus dados são seus — sempre.',
  },
];
</script>

<style scoped>
.faq {
  padding: 6rem 0;
  background: var(--light-bg);
}

.faq-layout {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 5rem;
  align-items: start;
}

/* ── Intro ── */
.faq-intro {
  position: sticky;
  top: 120px;
}

.faq-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.faq-subtitle {
  font-size: 1.0625rem;
  color: var(--text-muted);
  line-height: 1.65;
  margin-bottom: 2rem;
}

/* ── Accordion ── */
.faq-accordion {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.faq-item {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all var(--transition-base);
}

.faq-item:hover {
  border-color: rgba(99,91,255,0.2);
}

.faq-item.open {
  border-color: var(--primary-indigo);
  box-shadow: 0 0 0 1px var(--primary-indigo), var(--shadow-sm);
}

.faq-question {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem 1.5rem;
  background: none;
  border: none;
  cursor: pointer;
  font-family: var(--font-body);
  font-size: 1rem;
  font-weight: 600;
  color: var(--midnight-navy);
  text-align: left;
  gap: 1rem;
}

.faq-question:hover {
  color: var(--primary-indigo);
}

.faq-chevron {
  flex-shrink: 0;
  color: var(--text-light);
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  display: flex;
}

.faq-item.open .faq-chevron {
  transform: rotate(180deg);
  color: var(--primary-indigo);
}

.faq-answer {
  overflow: hidden;
  transition: max-height 0.4s cubic-bezier(0.16, 1, 0.3, 1),
              padding 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}

.faq-answer p {
  font-size: 0.9375rem;
  color: var(--text-muted);
  line-height: 1.7;
  border-top: 1px solid var(--border);
  padding-top: 1rem;
}

/* ── Responsive ── */
@media (max-width: 968px) {
  .faq-layout {
    grid-template-columns: 1fr;
    gap: 2rem;
  }
  .faq-intro {
    position: static;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
