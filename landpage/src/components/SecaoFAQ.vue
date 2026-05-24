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
    q: 'Quanto tempo leva para começar a usar?',
    a: 'Menos de 5 minutos. Crie sua conta, cadastre sua oficina e comece a abrir ordens de serviço imediatamente. Se precisar de migração de dados do seu sistema anterior, nossa equipe faz para você em até 48 horas — gratuitamente.',
  },
  {
    q: 'Preciso instalar algo no meu computador?',
    a: 'Não! A NeriTechAuto é 100% na nuvem. Você acessa de qualquer navegador, em qualquer dispositivo — computador, tablet ou celular. Sem instalações, sem atualizações manuais.',
  },
  {
    q: 'E se eu já uso outro sistema? Vocês migram meus dados?',
    a: 'Sim, 100% gratuito. Nossa equipe importa seus cadastros de clientes, veículos, produtos e histórico de ordens de serviço. O processo é feito em até 48 horas e não causa nenhuma interrupção no seu dia a dia.',
  },
  {
    q: 'Posso emitir nota fiscal pelo sistema?',
    a: 'Sim! Emitimos NF-e (produtos) e NFS-e (serviços) integrado diretamente à SEFAZ. Tudo com poucos cliques, sem precisar acessar outro sistema. Disponível nos planos neri pro e neri elite.',
  },
  {
    q: 'Como funciona o suporte?',
    a: 'Nosso suporte é humano e real — respondemos via WhatsApp em tempo médio de menos de 2 minutos durante o horário comercial. Nos planos neri pro e neri elite, o suporte é prioritário com canal dedicado.',
  },
  {
    q: 'O NeriTechAuto serve para qualquer tipo de oficina?',
    a: 'Sim! O NeriTechAuto foi desenhado para ser flexível e atender diferentes perfis de centros automotivos, desde oficinas mecânicas gerais até funilaria e pintura.',
  },
  {
    q: 'Meus dados estão seguros no NeriTechAuto?',
    a: 'Totalmente. Utilizamos a infraestrutura da AWS (Amazon Web Services), a mesma utilizada pelos maiores bancos do mundo. Seus dados contam com backup diário automático e criptografia de ponta a ponta, em total conformidade com a LGPD. Seus dados são seus — sempre.',
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
