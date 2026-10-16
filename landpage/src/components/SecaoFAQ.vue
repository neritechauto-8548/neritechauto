<template>
  <section id="faq" class="faq">
    <div class="container">
      <div class="faq-layout">
        <!-- Left: Title -->
        <div class="faq-intro aos-init">
          <span class="section-label">FAQ</span>
          <h2 class="faq-title">Perguntas frequentes</h2>
          <p class="faq-subtitle">Tudo o que você precisa saber antes de começar. Não achou a resposta? Fale com o time.</p>
          <a href="https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm" target="_blank" rel="noopener" @click="openChat($event)" class="btn btn-outline-dark btn-lg" id="faq-wpp-btn">
            Abrir chat
          </a>
        </div>

        <!-- Right: Accordion (PrimeVue Accordion Component) -->
        <div class="faq-accordion">
          <Accordion :value="0">
            <AccordionPanel v-for="(item, i) in faqs" :key="i" :value="i">
              <AccordionHeader>{{ item.q }}</AccordionHeader>
              <AccordionContent>
                <p class="faq-answer-text">{{ item.a }}</p>
              </AccordionContent>
            </AccordionPanel>
          </Accordion>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import Accordion from 'primevue/accordion';
import AccordionPanel from 'primevue/accordionpanel';
import AccordionHeader from 'primevue/accordionheader';
import AccordionContent from 'primevue/accordioncontent';

const faqs = [
  {
    q: 'O que é a NeriTech Auto?',
    a: 'É um sistema de gestão para oficinas automotivas que cobre o fluxo completo: da entrada do veículo à entrega — OS, checklist, estoque, financeiro, NF-e e portal do cliente.',
  },
  {
    q: 'Quanto tempo leva para começar a usar?',
    a: 'Em minutos você já abre a primeira OS. Se precisar migrar clientes, veículos e estoque de outro sistema, nossa equipe ajuda — sem custo de implantação.',
  },
  {
    q: 'Preciso instalar algo no computador?',
    a: 'Não. É 100% nuvem: acesse pelo navegador no PC, tablet ou celular. Sem instalação e sem atualização manual.',
  },
  {
    q: 'Posso emitir nota fiscal pelo sistema?',
    a: 'Sim. NF-e (peças) e NFS-e (serviços) integradas à ordem de serviço, sem precisar de outro software fiscal paralelo.',
  },
  {
    q: 'Tem checklist com fotos e aprovação digital?',
    a: 'Sim. Faça a inspeção com fotos e vídeos, envie o orçamento e receba a aprovação do cliente por WhatsApp ou link — com registro na OS.',
  },
  {
    q: 'Serve para oficina pequena e centro automotivo?',
    a: 'Sim. Atende oficinas mecânicas, centros automotivos e funilaria/pintura — do fluxo de uma baia a operações com estoque e fiscal mais robustos.',
  },
  {
    q: 'Como funciona o suporte?',
    a: 'Suporte humano no chat, com time que entende o dia a dia da oficina. Do onboarding às dúvidas operacionais, você fala com gente de verdade.',
  },
];
const openChat = (e) => {
  if (window.Tawk_API && typeof window.Tawk_API.maximize === 'function') {
    e.preventDefault();
    window.Tawk_API.maximize();
  }
};
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
  font-size: clamp(2.125rem, 4vw, 3rem);
  font-weight: 300;
  letter-spacing: -0.04em;
  line-height: 1.06;
  margin-bottom: 1rem;
  color: #0A2540;
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
}

.faq-answer-text {
  font-size: 0.9375rem;
  color: var(--text-muted);
  line-height: 1.7;
}

:deep(.p-accordionheader) {
  font-family: var(--font-body) !important;
  font-weight: 600 !important;
  color: var(--midnight-navy) !important;
  background: var(--white) !important;
  border-color: var(--border) !important;
}

:deep(.p-accordionpanel) {
  border: 1px solid var(--border) !important;
  border-radius: var(--radius-lg) !important;
  margin-bottom: 8px !important;
  overflow: hidden;
}

:deep(.p-accordioncontent) {
  background: var(--white) !important;
  border-color: var(--border) !important;
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
