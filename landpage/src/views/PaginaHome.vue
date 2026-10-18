<template>
  <main class="home-page">
    <section class="home-hero" aria-labelledby="home-title">
      <div class="container home-hero__layout">
        <div class="home-hero__copy">
          <span class="home-eyebrow">Software de gestão para oficinas</span>
          <h1 id="home-title">Sua oficina organizada, da entrada à entrega.</h1>
          <p>Ordens de serviço, orçamentos, estoque e financeiro na mesma rotina. Você sabe o que está em andamento e sua equipe sabe o próximo passo.</p>
          <div class="home-actions">
            <router-link class="home-button" to="/teste-gratis">Testar grátis por 14 dias <span aria-hidden="true">→</span></router-link>
            <a class="home-text-link" href="#como-funciona">Ver como funciona</a>
          </div>
          <small class="home-hero__note">Sem cartão de crédito para começar.</small>
        </div>
        <div class="home-hero__visual">
          <img class="home-hero__photo" src="/images/home/oficina-mecanico.png" alt="Mecânico inspecionando o motor de um veículo na oficina" width="1536" height="1024" fetchpriority="high">
          <div class="home-preview" aria-label="Prévia ilustrativa do pátio da oficina">
            <div class="home-preview__top"><span class="home-preview__mark">N</span><div><strong>Pátio da oficina</strong><small>Prévia ilustrativa</small></div><span class="home-preview__dots" aria-hidden="true">•••</span></div>
            <div class="home-preview__tabs"><span class="is-active">Em atendimento</span><span>Em serviço</span><span>Pronto</span></div>
            <div class="home-preview__row" v-for="car in previewCars" :key="car.name"><span class="home-preview__line" :class="car.color"></span><div><strong>{{ car.name }}</strong><small>{{ car.service }}</small></div><em>{{ car.status }}</em></div>
          </div>
        </div>
      </div>
    </section>

    <section id="como-funciona" class="home-feature home-feature--first" aria-labelledby="home-operation-title">
      <div class="container home-feature__layout">
        <div class="home-feature__visual home-feature__visual--collage">
          <img src="/images/home/oficina-gestao.png" alt="Responsável pela oficina organizando atendimentos no computador" width="1536" height="1024" loading="lazy">
          <div class="home-feature__inset"><img src="/images/home/oficina-patio.png" alt="Veículos em atendimento no pátio da oficina" width="1536" height="1024" loading="lazy"></div>
        </div>
        <div class="home-feature__copy">
          <span class="home-eyebrow">Operação da oficina</span>
          <h2 id="home-operation-title">Cada veículo no lugar certo. Toda a equipe por dentro.</h2>
          <p class="home-lead">Acompanhe o trabalho desde a chegada do carro, sem depender de anotações soltas ou de perguntar o status de cada OS.</p>
          <ul class="home-feature__list">
            <li><strong>Veja o pátio de uma vez.</strong><span>Identifique o que aguarda atendimento, está em serviço ou já pode ser entregue.</span></li>
            <li><strong>Trabalhe com a OS aberta.</strong><span>Serviços, peças e observações ficam associados ao veículo.</span></li>
            <li><strong>Retome o histórico.</strong><span>Consulte atendimentos anteriores por cliente e veículo.</span></li>
          </ul>
        </div>
      </div>
    </section>

    <section class="home-feature home-feature--soft" aria-labelledby="home-budget-title">
      <div class="container home-feature__layout home-feature__layout--reverse">
        <div class="home-feature__copy">
          <span class="home-eyebrow">Orçamento e aprovação</span>
          <h2 id="home-budget-title">O cliente entende o serviço antes de aprovar.</h2>
          <p class="home-lead">Monte o orçamento com peças e mão de obra, compartilhe para avaliação e mantenha a resposta junto da ordem de serviço.</p>
          <ul class="home-feature__list">
            <li><strong>Detalhe o que será feito.</strong><span>Descreva itens e valores com clareza para a equipe e o cliente.</span></li>
            <li><strong>Envie para avaliação.</strong><span>Compartilhe o orçamento por link e acompanhe o retorno.</span></li>
            <li><strong>Siga com o contexto completo.</strong><span>A aprovação e o trabalho permanecem ligados à OS.</span></li>
          </ul>
        </div>
        <div class="home-feature__visual home-feature__visual--budget">
          <img src="/images/home/oficina-patio.png" alt="Oficina com veículos em serviço nas baias" width="1536" height="1024" loading="lazy">
          <div class="home-budget-card" aria-label="Exemplo ilustrativo de itens de um orçamento">
            <span>Exemplo ilustrativo</span><strong>Orçamento da revisão</strong>
            <div><span>Peças</span><b>Itens discriminados</b></div><div><span>Mão de obra</span><b>Serviços descritos</b></div>
            <div class="home-budget-card__footer">Pronto para avaliação <span aria-hidden="true">→</span></div>
          </div>
        </div>
      </div>
    </section>

    <section class="home-feature" aria-labelledby="home-client-title">
      <div class="container home-feature__layout">
        <div class="home-feature__visual home-feature__visual--client"><img src="/images/home/oficina-cliente.png" alt="Consultor explicando uma inspeção ao cliente ao lado do veículo" width="1536" height="1024" loading="lazy"></div>
        <div class="home-feature__copy">
          <span class="home-eyebrow">Inspeção e relacionamento</span>
          <h2 id="home-client-title">Mostre o que foi encontrado. Guarde o histórico para a próxima visita.</h2>
          <p class="home-lead">Registre a inspeção com observações e imagens. Assim, o cliente entende o diagnóstico e a oficina mantém o contexto do veículo.</p>
          <ul class="home-feature__list">
            <li><strong>Documente a inspeção.</strong><span>Fotos e observações ajudam a explicar o reparo.</span></li>
            <li><strong>Atenda com mais contexto.</strong><span>Consulte o que já foi feito antes de abrir a próxima OS.</span></li>
            <li><strong>Organize o retorno.</strong><span>Use o histórico e a agenda para planejar os próximos atendimentos.</span></li>
          </ul>
        </div>
      </div>
    </section>

    <section class="home-capabilities" aria-labelledby="home-capabilities-title"><div class="container">
      <div class="home-section-heading"><span class="home-eyebrow">O dia a dia em um lugar</span><h2 id="home-capabilities-title">Do pátio ao caixa, a informação segue junto.</h2><p>Serviços, peças e contas fazem parte da mesma operação.</p></div>
      <div class="home-capabilities__grid">
        <article v-for="(item, i) in capabilities" :key="item.title" class="home-capability"><span class="home-capability__number">0{{ i + 1 }} / {{ item.label }}</span><h3>{{ item.title }}</h3><p>{{ item.description }}</p><router-link to="/funcionalidades">Conhecer recursos <span aria-hidden="true">→</span></router-link></article>
      </div>
    </div></section>

    <section id="oficinas" class="home-segments" aria-labelledby="home-segments-title"><div class="container">
      <div class="home-section-heading"><span class="home-eyebrow">Para quem trabalha com veículos</span><h2 id="home-segments-title">Feito para a rotina da sua oficina.</h2><p>Um fluxo de trabalho que começa no atendimento e acompanha o veículo até a entrega.</p></div>
      <div class="home-segments__grid">
        <article v-for="item in segments" :key="item.title" class="home-segment"><img :src="item.image" :alt="item.alt" width="1536" height="1024" loading="lazy"><div><h3>{{ item.title }}</h3><p>{{ item.description }}</p></div></article>
      </div>
    </div></section>

    <section class="home-trial" aria-labelledby="home-trial-title"><div class="container home-trial__layout">
      <div><span class="home-eyebrow">Comece pela sua própria rotina</span><h2 id="home-trial-title">Experimente a NeriTech Auto por 14 dias.</h2><p>Cadastre sua oficina, abra uma OS e veja como o sistema funciona no trabalho de verdade. Sem cartão de crédito para começar.</p></div>
      <router-link class="home-button" to="/teste-gratis">Começar teste grátis <span aria-hidden="true">→</span></router-link>
    </div></section>

    <SecaoFAQ />
    <SecaoContato />
    <SecaoRodape />
  </main>
</template>

<script setup>
import SecaoFAQ from '../components/SecaoFAQ.vue';
import SecaoContato from '../components/SecaoContato.vue';
import SecaoRodape from '../components/SecaoRodape.vue';

const previewCars = [
  { name: 'Honda Civic', service: 'Inspeção inicial', status: 'Recepção', color: '' },
  { name: 'Chevrolet Onix', service: 'Revisão periódica', status: 'Em serviço', color: 'home-preview__line--blue' },
  { name: 'Toyota Corolla', service: 'Conferência final', status: 'Pronto', color: 'home-preview__line--green' },
];
const capabilities = [
  { label: 'Operação', title: 'Ordens de serviço', description: 'Acompanhe etapas, responsáveis, serviços e o histórico de cada veículo.' },
  { label: 'Peças', title: 'Estoque e compras', description: 'Saiba o que há disponível e organize a movimentação de peças da oficina.' },
  { label: 'Gestão', title: 'Financeiro', description: 'Acompanhe contas, entradas e saídas com uma visão mais clara da operação.' },
];
const segments = [
  { title: 'Oficinas mecânicas', description: 'Da revisão ao reparo, acompanhe OS, peças e prazos sem perder o histórico do carro.', image: '/images/home/oficina-mecanico.png', alt: 'Mecânico trabalhando no motor de um veículo' },
  { title: 'Centros automotivos', description: 'Organize o atendimento entre baias, equipe, estoque e gestão financeira.', image: '/images/home/oficina-patio.png', alt: 'Centro automotivo com carros em manutenção' },
  { title: 'Funilaria e pintura', description: 'Registre avarias, detalhe etapas do orçamento e acompanhe a evolução do serviço.', image: '/images/home/oficina-cliente.png', alt: 'Profissional conversando com cliente sobre o veículo' },
];
</script>

<style scoped>
.home-page { color: #17233b; background: #fff; }
.home-page h1, .home-page h2, .home-page h3 { color: #17233b; font-weight: 700; letter-spacing: -0.04em; }
.home-page p { color: #536178; }
.home-eyebrow { display: block; color: #2748ef; font-size: .74rem; line-height: 1.4; font-weight: 700; letter-spacing: .11em; text-transform: uppercase; margin-bottom: 1.1rem; }
.home-button { display: inline-flex; align-items: center; justify-content: center; gap: 1.1rem; width: fit-content; min-height: 48px; padding: .85rem 1.25rem; border-radius: 5px; background: #2748ef; color: #fff !important; font-size: .94rem; font-weight: 700; line-height: 1.2; transition: background .2s ease, transform .2s ease; }
.home-button:hover { background: #1734c9; transform: translateY(-2px); }
.home-button span { font-size: 1.15rem; font-weight: 400; }
.home-hero { background: #f4f7fb; overflow: hidden; }
.home-hero__layout { display: grid; grid-template-columns: minmax(0, .84fr) minmax(0, 1.16fr); align-items: center; gap: 3.75rem; min-height: 680px; padding-top: 116px; padding-bottom: 94px; }
.home-hero__copy { position: relative; z-index: 2; max-width: 550px; }
.home-hero h1 { font-size: clamp(3rem, 4.3vw, 4.65rem); line-height: 1.045; margin-bottom: 1.45rem; }
.home-hero__copy > p { max-width: 480px; font-size: 1.19rem; line-height: 1.57; }
.home-actions { display: flex; align-items: center; gap: 1.35rem; flex-wrap: wrap; margin-top: 2rem; }
.home-text-link { color: #2748ef; font-size: .93rem; font-weight: 700; }
.home-text-link:hover { text-decoration: underline; }
.home-hero__note { display: block; margin-top: 1rem; color: #536178; font-size: .84rem; }
.home-hero__visual { position: relative; min-width: 0; height: 455px; margin-right: calc(-1 * var(--container-padding-x)); }
.home-hero__photo { width: 100%; height: 100%; object-fit: cover; object-position: 55% center; border-radius: 8px 0 0 8px; }
.home-preview { position: absolute; z-index: 2; top: -22px; left: -46px; width: min(77%, 490px); padding: 0 1rem 1rem; background: #fff; border: 1px solid #e2e8f1; border-radius: 13px; box-shadow: 0 18px 55px rgba(11, 31, 66, .2); }
.home-preview__top { display: flex; align-items: center; gap: .65rem; padding: .85rem 0; border-bottom: 1px solid #e9edf4; }
.home-preview__mark { display: grid; place-items: center; width: 27px; height: 27px; border-radius: 6px; background: #2748ef; color: #fff; font-size: .8rem; font-weight: 800; }
.home-preview__top strong, .home-preview__top small { display: block; line-height: 1.15; }
.home-preview__top strong { font-size: .83rem; color: #17233b; }
.home-preview__top small { margin-top: .2rem; color: #6b7890; font-size: .65rem; }
.home-preview__dots { margin-left: auto; color: #9ca9bc; letter-spacing: .15rem; }
.home-preview__tabs { display: flex; gap: 1rem; overflow: hidden; border-bottom: 1px solid #e9edf4; white-space: nowrap; }
.home-preview__tabs span { padding: .75rem 0 .65rem; color: #69768b; font-size: .68rem; font-weight: 650; }
.home-preview__tabs .is-active { color: #2748ef; border-bottom: 2px solid #2748ef; }
.home-preview__row { display: flex; align-items: center; gap: .65rem; padding: .75rem 0; border-bottom: 1px solid #edf0f6; }
.home-preview__row:last-child { border-bottom: 0; padding-bottom: 0; }
.home-preview__line { width: 4px; height: 31px; border-radius: 4px; background: #e9a93e; }
.home-preview__line--blue { background: #2748ef; }
.home-preview__line--green { background: #24836b; }
.home-preview__row div { display: grid; gap: .1rem; }
.home-preview__row strong { color: #17233b; font-size: .74rem; }
.home-preview__row small { color: #6b7890; font-size: .66rem; }
.home-preview__row em { margin-left: auto; color: #546179; font-style: normal; font-size: .66rem; white-space: nowrap; }
.home-feature { padding: clamp(5rem, 9vw, 8rem) 0; }
.home-feature--first { padding-top: clamp(6rem, 10vw, 9rem); }
.home-feature--soft { background: #f6f8fb; }
.home-feature__layout { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); align-items: center; gap: clamp(3rem, 7vw, 7rem); }
.home-feature__layout--reverse .home-feature__copy { order: 0; }
.home-feature__layout--reverse .home-feature__visual { order: 1; }
.home-feature__visual { position: relative; min-width: 0; height: clamp(340px, 36vw, 500px); }
.home-feature__visual > img { width: 100%; height: 100%; object-fit: cover; border-radius: 7px; }
.home-feature__visual--collage > img { width: 88%; height: 88%; object-position: center; }
.home-feature__inset { position: absolute; bottom: 0; right: 0; width: 43%; height: 48%; padding: 7px; background: #fff; border-radius: 9px; box-shadow: 0 14px 35px rgba(19, 32, 55, .13); }
.home-feature__inset img { width: 100%; height: 100%; object-fit: cover; border-radius: 5px; }
.home-feature__copy { max-width: 535px; }
.home-feature h2 { font-size: clamp(2.25rem, 3.35vw, 3.35rem); line-height: 1.1; margin-bottom: 1.25rem; }
.home-lead { font-size: 1.08rem; line-height: 1.65; }
.home-feature__list { display: grid; gap: 1.2rem; margin-top: 2rem; }
.home-feature__list li { position: relative; padding-left: 1.75rem; }
.home-feature__list li::before { content: '✓'; position: absolute; top: .1rem; left: 0; display: grid; place-items: center; width: 18px; height: 18px; border-radius: 50%; background: #2748ef; color: #fff; font-size: .72rem; font-weight: 700; }
.home-feature__list strong, .home-feature__list span { display: block; }
.home-feature__list strong { color: #17233b; font-size: 1rem; line-height: 1.4; }
.home-feature__list span { color: #58667b; font-size: .94rem; line-height: 1.55; margin-top: .2rem; }
.home-feature__visual--budget > img { height: 93%; object-position: center; }
.home-budget-card { position: absolute; right: -18px; bottom: -18px; width: min(78%, 350px); padding: 1.2rem; background: #fff; border: 1px solid #e4e9f2; border-radius: 8px; box-shadow: 0 15px 40px rgba(18, 35, 68, .15); }
.home-budget-card > span { display: block; color: #6b7890; font-size: .68rem; margin-bottom: .35rem; }
.home-budget-card > strong { display: block; color: #17233b; font-size: 1rem; margin-bottom: .85rem; }
.home-budget-card div:not(.home-budget-card__footer) { display: flex; justify-content: space-between; gap: .5rem; padding: .54rem 0; border-top: 1px solid #e9edf4; font-size: .76rem; }
.home-budget-card div span { color: #5d6c81; }
.home-budget-card div b { color: #17233b; font-weight: 650; }
.home-budget-card__footer { display: flex; justify-content: space-between; margin-top: .65rem; padding: .55rem .65rem; border-radius: 4px; background: #edf2ff; color: #2748ef; font-size: .76rem; font-weight: 700; }
.home-feature__visual--client img { object-position: 42% center; }
.home-capabilities { padding: 7rem 0; background: #f6f8fb; }
.home-section-heading { max-width: 700px; margin: 0 auto 3rem; text-align: center; }
.home-section-heading h2 { font-size: clamp(2.3rem, 3.5vw, 3.4rem); line-height: 1.1; }
.home-section-heading p { margin-top: 1rem; font-size: 1.06rem; line-height: 1.55; }
.home-capabilities__grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 1.25rem; }
.home-capability { display: flex; flex-direction: column; align-items: flex-start; min-height: 260px; padding: 2rem; border: 1px solid #e4e9f2; border-radius: 7px; background: #fff; }
.home-capability__number { color: #2748ef; font-size: .72rem; font-weight: 700; letter-spacing: .08em; text-transform: uppercase; }
.home-capability h3 { margin: 2rem 0 .65rem; font-size: 1.45rem; line-height: 1.15; }
.home-capability p { font-size: .94rem; line-height: 1.55; }
.home-capability a { margin-top: auto; padding-top: 1rem; color: #2748ef; font-size: .88rem; font-weight: 700; }
.home-capability a:hover { text-decoration: underline; }
.home-segments { padding: 7rem 0 8rem; }
.home-segments__grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 1.5rem; }
.home-segment { overflow: hidden; border: 1px solid #e5eaf2; border-radius: 7px; background: #fff; }
.home-segment img { width: 100%; height: 220px; object-fit: cover; }
.home-segment:nth-child(1) img { object-position: 66% center; }
.home-segment:nth-child(3) img { object-position: 46% center; }
.home-segment div { padding: 1.35rem 1.4rem 1.6rem; }
.home-segment h3 { font-size: 1.3rem; line-height: 1.2; margin-bottom: .65rem; }
.home-segment p { font-size: .93rem; line-height: 1.55; }
.home-trial { padding: 4.75rem 0; background: #17233b; }
.home-trial__layout { display: flex; align-items: center; justify-content: space-between; gap: 3rem; }
.home-trial__layout > div { max-width: 680px; }
.home-trial .home-eyebrow { color: #8aa0ff; }
.home-trial h2 { color: #fff; font-size: clamp(2.25rem, 3.6vw, 3.4rem); line-height: 1.1; }
.home-trial p { color: #d1d9e8; margin-top: 1rem; font-size: 1rem; line-height: 1.6; }
.home-trial .home-button { flex-shrink: 0; background: #fff; color: #17233b !important; }
.home-trial .home-button:hover { background: #e9edff; }
@media (max-width: 1050px) { .home-hero__layout { gap: 2.2rem; } .home-preview { left: -20px; } .home-feature__layout { gap: 3rem; } }
@media (max-width: 800px) {
  .home-hero__layout { grid-template-columns: 1fr; gap: 2.5rem; padding-top: 115px; padding-bottom: 4rem; }
  .home-hero__copy, .home-hero h1 { max-width: 640px; }
  .home-hero__visual { height: 500px; margin-left: 22px; margin-right: calc(-1 * var(--container-padding-x)); }
  .home-preview { top: 150px; }
  .home-feature__layout { grid-template-columns: 1fr; gap: 2.8rem; }
  .home-feature__visual { height: 420px; }
  .home-feature__layout--reverse .home-feature__visual { order: 0; }
  .home-feature__layout--reverse .home-feature__copy { order: 1; }
  .home-capabilities__grid, .home-segments__grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .home-trial__layout { align-items: flex-start; flex-direction: column; gap: 1.8rem; }
}
@media (max-width: 680px) {
  .home-capabilities__grid, .home-segments__grid { grid-template-columns: 1fr; }
}
@media (max-width: 560px) {
  .home-hero__layout { padding-top: 105px; gap: 2.7rem; }
  .home-hero h1 { font-size: clamp(2.55rem, 10vw, 3.5rem); }
  .home-hero__copy > p { font-size: 1.03rem; }
  .home-actions { align-items: flex-start; flex-direction: column; gap: 1rem; }
  .home-hero__visual { height: 475px; margin-left: 0; }
  .home-preview { top: 155px; left: 0; width: min(89%, 370px); padding: 0 .72rem .7rem; }
  .home-preview__tabs { gap: .65rem; }
  .home-preview__tabs span { font-size: .61rem; }
  .home-preview__row { padding: .48rem 0; }
  .home-preview__row em { display: none; }
  .home-feature, .home-capabilities, .home-segments { padding: 4.8rem 0; }
  .home-feature__visual { height: 320px; }
  .home-feature h2, .home-section-heading h2 { font-size: 2.25rem; }
  .home-budget-card { right: 0; width: min(85%, 315px); }
  .home-capabilities__grid, .home-segments__grid { grid-template-columns: 1fr; }
  .home-capability { min-height: 220px; }
  .home-segment img { height: 210px; }
  .home-trial { padding: 4rem 0; }
}
:global(.p-dark) .home-page :is(h1, h2, h3) { color: #17233b !important; }
:global(.p-dark) .home-page p { color: #536178 !important; }
:global(.p-dark) .home-page .home-eyebrow { color: #2748ef !important; }
:global(.p-dark) .home-page .home-trial h2 { color: #fff !important; }
:global(.p-dark) .home-page .home-trial p { color: #d1d9e8 !important; }
:global(.p-dark) .home-page .home-trial .home-eyebrow { color: #8aa0ff !important; }
</style>
