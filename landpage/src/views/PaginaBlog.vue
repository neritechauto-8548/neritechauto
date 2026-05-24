<template>
  <main class="blog-page">
    <!-- Seção de Artigo Completo -->
    <div v-if="idArtigoAtivo">
      <section class="page-hero blog-detail-section">
        <div class="container">
          <div class="article-inner aos-init">
            <router-link to="/blog" class="btn-back">
              <span class="arrow-back">←</span> Voltar para o Blog
            </router-link>

            <article v-if="artigoAtivo" class="full-article premium-card aos-init aos-delay-1">
              <header class="article-header">
                <span class="section-label">{{ artigoAtivo.category }}</span>
                <h1 class="article-title">{{ artigoAtivo.title }}</h1>
                <div class="article-meta">
                  <span class="article-date">📅 {{ artigoAtivo.date }}</span>
                  <span class="meta-separator">•</span>
                  <span class="article-read-time">📖 {{ artigoAtivo.readTime }} de leitura</span>
                </div>
              </header>

              <div class="article-image-wrapper mockup-frame">
                <div class="mockup-frame__bar">
                  <span class="mockup-dot mockup-dot--red"></span>
                  <span class="mockup-dot mockup-dot--yellow"></span>
                  <span class="mockup-dot mockup-dot--green"></span>
                  <div class="mockup-frame__url">neritechauto.com.br/blog/{{ artigoAtivo.id }}</div>
                </div>
                <img :src="artigoAtivo.image" :alt="artigoAtivo.title" class="article-hero-image">
              </div>

              <!-- Dynamic Rich Content for the Post -->
              <div class="article-body text-main" v-html="artigoAtivo.content"></div>
            </article>

            <div v-else class="text-center article-not-found premium-card aos-init aos-delay-1">
              <span class="section-label">Erro 404</span>
              <h2 class="section-title">Artigo não encontrado</h2>
              <p class="section-subtitle">O artigo que você está procurando não existe ou foi movido.</p>
              <router-link to="/blog" class="btn btn-primary">Voltar para o Blog</router-link>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- Lista de Posts Padrão (se não houver ID na rota) -->
    <div v-else>
      <!-- Hero Section -->
      <section class="page-hero blog-hero">
        <div class="container">
          <header class="section-header aos-init">
            <span class="section-label">Central de Inteligência</span>
            <h1 class="section-title" style="color: white !important;">Central de <span style="color: rgba(255,255,255,0.85);">Conhecimento</span></h1>
            <p class="section-subtitle" style="color: rgba(255,255,255,0.75) !important;">
              Insights estratégicos, finanças e tecnologia para oficinas que buscam o topo do mercado.
            </p>
          </header>
          
          <div class="category-filters aos-init aos-delay-1">
            <button 
              v-for="cat in categories" 
              :key="cat"
              class="filter-btn"
              :class="{ 'active': activeCategory === cat }"
              @click="activeCategory = cat"
            >
              {{ cat }}
            </button>
          </div>
        </div>
      </section>

      <!-- Grid de Posts -->
      <section class="section-surface section-surface--light blog-content-section">
        <div class="container">
          <div class="blog-grid">
            <article 
              v-for="(post, index) in filteredPosts" 
              :key="post.id" 
              class="blog-card premium-card aos-init"
              :class="'aos-delay-' + ((index % 3) + 1)"
            >
              <div class="post-image-wrapper mockup-frame">
                <div class="mockup-frame__bar">
                  <span class="mockup-dot mockup-dot--red"></span>
                  <span class="mockup-dot mockup-dot--yellow"></span>
                  <span class="mockup-dot mockup-dot--green"></span>
                </div>
                <img :src="post.image" :alt="post.title" class="post-image">
                <span class="post-label section-label">{{ post.category }}</span>
              </div>
              <div class="post-body">
                <span class="post-date">📅 {{ post.date }}</span>
                <h2 class="post-title">{{ post.title }}</h2>
                <p class="post-excerpt text-muted">{{ post.excerpt }}</p>
                <div class="post-footer">
                  <span class="post-read-time">📖 {{ post.readTime }} de leitura</span>
                  <router-link :to="`/blog/${post.id}`" class="post-link">
                    Ler matéria <span class="arrow">→</span>
                  </router-link>
                </div>
              </div>
            </article>
          </div>
        </div>
      </section>


    </div>

    <SecaoRodape />
  </main>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue';
import { useRoute } from 'vue-router';
import SecaoRodape from '../components/SecaoRodape.vue';
import { useScrollAnimation } from '../composables/useScrollAnimation.js';

const route = useRoute();
const categories = ['Todos', 'Gestão', 'Mercado', 'Finanças', 'Tecnologia'];
const activeCategory = ref('Todos');

const idArtigoAtivo = computed(() => route.params.id);

// Inicializa a animação de scroll no mount inicial
useScrollAnimation();

onMounted(() => {
  window.scrollTo({ top: 0, behavior: 'instant' });
});

// Garante que as animações AOS reiniciem e a tela suba ao navegar
watch(
  () => route.path,
  () => {
    window.scrollTo({ top: 0, behavior: 'instant' });
    nextTick(() => {
      // Remove a visibilidade para animar novamente
      document.querySelectorAll('.aos-init').forEach((el) => {
        el.classList.remove('aos-visible');
      });
      // Registra novamente os novos elementos renderizados no IntersectionObserver
      setTimeout(() => {
        const observer = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                entry.target.classList.add('aos-visible');
              }
            });
          },
          { threshold: 0.1, rootMargin: '0px 0px -40px 0px' }
        );
        document.querySelectorAll('.aos-init').forEach((el) => {
          observer.observe(el);
        });
      }, 100);
    });
  }
);

const posts = ref([
  {
    id: 'checklist-vistoria-evitar-processos',
    title: 'Como o Checklist de Entrada Evita Prejuízos de até R$ 5.000 por Mês',
    excerpt: 'A maioria dos desentendimentos com clientes ocorre devido a avarias já existentes na lataria ou rodas. Veja como um termo de vistoria digital com fotos elimina reclamações indesejadas e constrói confiança profissional imediata.',
    category: 'Gestão',
    date: '17 Mai, 2026',
    readTime: '6 min',
    image: '/images/blog_post_customer_experience_1773595298748.png',
    content: `
      <p>Imagine a cena: um cliente deixa o carro para trocar as pastilhas de freio. No dia seguinte, ao retirar o veículo, ele aponta indignado para um risco profundo na porta traseira esquerda e jura de pé junto que o carro entrou intacto. Como dono de oficina, você se vê em uma encruzilhada: arca com um retoque de pintura de R$ 600 ou enfrenta um processo desgastante no Juizado Especial Cível (JEC)?</p>
      <p>Esse cenário é extremamente comum e representa um ralo silencioso de lucro nas oficinas brasileiras. É aí que a <strong>Vistoria Digital (DVI)</strong> entra como a melhor blindagem operacional e jurídica para a sua empresa.</p>
      <h3>O poder da prova visual inquestionável</h3>
      <p>Com o checklist de entrada fotográfico da NeriTechAuto, a sua equipe anexa fotos de alta resolução de todos os ângulos do veículo diretamente na Ordem de Serviço, antes mesmo de levá-lo para a baia de trabalho. Ao salvar, um link contendo as fotos e o termo de vistoria assinado é enviado automaticamente via WhatsApp para o cliente. Isso impede qualquer questionamento de má-fé.</p>
      <ul>
        <li><strong>Prevenção imediata:</strong> 98% dos clientes desistem de reclamar quando confrontados com as fotos originais da entrada do veículo.</li>
        <li><strong>Termos Legais Integrados:</strong> Emita laudos com validade jurídica de avarias pré-existentes nas rodas, para-choques e painel.</li>
        <li><strong>Profissionalismo Elevado:</strong> O cliente percebe que está deixando o carro em uma concessionária tecnológica, e não em uma oficina amadora.</li>
      </ul>
      <p>Essa simples mudança no processo de entrada economiza em média de 3 a 5 disputas mensais por oficina, convertendo-se em um ganho líquido de até R$ 5.000 mensais que antes seriam jogados no lixo com reparos não cobrados.</p>
    `
  },
  {
    id: 'calculo-custo-hora-oficina',
    title: 'A Fórmula Prática do Custo-Hora: Sua Oficina Está Cobrando Certo?',
    excerpt: 'Muitos proprietários precificam serviços baseados apenas no mercado local ou em concorrentes diretos. Aprenda a calcular o custo real de cada baia produtiva para proteger sua margem líquida e ajustar seu valor de mão de obra.',
    category: 'Finanças',
    date: '12 Mai, 2026',
    readTime: '8 min',
    image: '/images/blog_post_management_dashboard_1773595279284.png',
    content: `
      <p>Você sabe exatamente quanto custa manter a sua oficina aberta por minuto? A precificação errada é a principal causa da mortalidade de oficinas mecânicas. Cobrar "pelo preço do vizinho" ou apenas somando o valor das peças com uma margem arbitrária é uma receita para o prejuízo.</p>
      <p>Para obter lucro real, você precisa dominar o cálculo do <strong>Custo-Hora Produtivo</strong>. Veja o passo a passo simplificado para aplicar hoje mesmo:</p>
      <h3>A Fórmula do Custo-Hora</h3>
      <p>1. <strong>Some todas as despesas fixas da oficina:</strong> Aluguel, água, energia, salários da administração, software, impostos fixos e ferramentas. Digamos que a soma seja R$ 20.000/mês.</p>
      <p>2. <strong>Calcule a capacidade produtiva em horas:</strong> Se você tem 3 mecânicos trabalhando 220 horas por mês cada, sua capacidade total é de 660 horas produtivas por mês.</p>
      <p>3. <strong>Divida a despesa pelas horas produtivas:</strong> R$ 20.000 / 660 = R$ 30,30 por hora. Esse é o seu custo base! Qualquer hora vendida abaixo disso representa prejuízo direto no seu caixa.</p>
      <p>A partir deste custo base, você adiciona a margem de lucro desejada e o custo específico do mecânico alocado para obter a sua taxa de mão de obra real. O módulo financeiro da NeriTechAuto calcula automaticamente a eficiência das suas baias e ajuda você a ajustar esses valores em tempo real para manter sua oficina lucrativa.</p>
    `
  },
  {
    id: 'tendencias-veiculos-eletricos-2026',
    title: 'O Futuro das Oficinas: Como se preparar para os Veículos Elétricos',
    excerpt: 'O mercado automobilístico está mudando mais rápido do que nunca com a chegada massiva dos elétricos e híbridos leves. Descubra os equipamentos básicos de segurança elétrica e treinamentos necessários para não ficar para trás.',
    category: 'Mercado',
    date: '08 Mai, 2026',
    readTime: '10 min',
    image: '/images/blog_post_electric_car_1773595259767.png',
    content: `
      <p>O pátio das oficinas no Brasil já começou a mudar. A circulação de veículos híbridos e 100% elétricos não é mais exclusividade de grandes centros ou concessionárias autorizadas. Se um Toyota Corolla Hybrid ou um BYD Dolphin entrar na sua oficina hoje com uma pane no sistema elétrico ou precisando de manutenção de suspensão, sua equipe está pronta para atendê-lo com segurança?</p>
      <p>Trabalhar com sistemas de alta tensão (que podem passar de 400 Volts) exige uma mudança drástica de mentalidade, ferramentas e equipamentos de proteção individual (EPIs).</p>
      <h3>O que sua oficina precisa para iniciar:</h3>
      <ul>
        <li><strong>Luvas Isolantes de Classe 00 (até 500V) ou Classe 0 (até 1000V):</strong> Item obrigatório para qualquer manuseio no cofre do motor ou baterias tracionárias.</li>
        <li><strong>Ferramentas Isoladas VDE:</strong> Chaves e alicates certificados para suportar alta tensão sem conduzir corrente para o mecânico.</li>
        <li><strong>Área de Isolamento no Pátio:</strong> Demarque uma baia específica com fitas de sinalização e placas de perigo de alta tensão para evitar acidentes com outros funcionários.</li>
      </ul>
      <p>A NeriTechAuto já está preparada para o futuro, permitindo que você adicione checklists específicos de desenergização e segurança veicular (como a remoção do plugue de segurança de alta tensão) para garantir que seus técnicos trabalhem 100% protegidos.</p>
    `
  }
]);

const artigoAtivo = computed(() => {
  return posts.value.find(post => post.id === idArtigoAtivo.value);
});

const filteredPosts = computed(() => {
  if (activeCategory.value === 'Todos') return posts.value;
  return posts.value.filter(post => post.category === activeCategory.value);
});


</script>

<style scoped>
.blog-page {
  position: relative;
  min-height: 100vh;
}

/* Detail Section Layout */
.blog-detail-section {
  padding: 130px 0 6rem;
}

.article-inner {
  max-width: 740px;
  margin: 0 auto;
}

.btn-back {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: white;
  color: var(--midnight-navy) !important;
  font-weight: 700;
  font-size: 0.875rem;
  padding: 0.6rem 1.25rem;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--border);
  transition: all var(--transition-base);
  margin-bottom: 2rem;
  box-shadow: var(--shadow-xs);
}

.btn-back:hover {
  border-color: var(--primary);
  color: var(--primary) !important;
  background: var(--primary-light);
  transform: translateX(-4px);
  box-shadow: var(--shadow-sm);
}

.arrow-back {
  transition: transform var(--transition-fast);
}

.btn-back:hover .arrow-back {
  transform: translateX(-2px);
}

.full-article {
  padding: 3.5rem;
  margin-top: 1rem;
}

.article-header {
  margin-bottom: 2.5rem;
  text-align: left;
}

.article-header .section-label {
  margin-bottom: 1.25rem;
  background: var(--primary-light) !important;
  border: 1px solid var(--primary-border) !important;
  color: var(--primary) !important;
}

:global(.p-dark) .article-header .section-label {
  background: rgba(255, 255, 255, 0.12) !important;
  border-color: rgba(255, 255, 255, 0.25) !important;
  color: rgba(255, 255, 255, 0.95) !important;
}

.article-title {
  font-size: clamp(2rem, 4.5vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy) !important;
  line-height: 1.2;
  letter-spacing: -0.035em;
  margin-bottom: 1.25rem;
  font-family: var(--font-heading);
}

:global(.p-dark) .article-title {
  color: var(--p-surface-50) !important;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 0.875rem;
  color: var(--text-muted);
  font-weight: 600;
}

.meta-separator {
  color: var(--border);
}

.article-image-wrapper {
  margin-bottom: 3rem;
  box-shadow: var(--shadow-lg);
}

.article-hero-image {
  width: 100%;
  height: 440px;
  object-fit: cover;
}

.article-body {
  font-size: 1.125rem;
  color: #334155;
  line-height: 1.85;
}

.article-body :deep(p) {
  margin-bottom: 1.75rem;
  color: #334155 !important;
}

.article-body :deep(h2) {
  font-size: 1.75rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-top: 2.75rem;
  margin-bottom: 1.25rem;
  letter-spacing: -0.035em;
  font-family: var(--font-heading);
}

.article-body :deep(h3) {
  font-size: 1.45rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-top: 2.5rem;
  margin-bottom: 1.25rem;
  letter-spacing: -0.03em;
  font-family: var(--font-heading);
}

.article-body :deep(ul), .article-body :deep(ol) {
  margin-bottom: 1.75rem;
  padding-left: 1.75rem;
}

.article-body :deep(li) {
  margin-bottom: 0.75rem;
  line-height: 1.75;
  color: #334155;
}

.article-body :deep(strong) {
  color: var(--midnight-navy);
  font-weight: 700;
}

.article-body :deep(blockquote) {
  border-left: 4px solid var(--primary);
  padding-left: 1.5rem;
  font-style: italic;
  color: var(--text-muted);
  margin: 2rem 0;
  font-size: 1.2rem;
  line-height: 1.6;
}

/* ── Dark Mode Reading Enhancements ── */
:global(.p-dark) .article-body {
  color: var(--p-surface-200) !important;
}
:global(.p-dark) .article-body :deep(p),
:global(.p-dark) .article-body :deep(li) {
  color: var(--p-surface-200) !important;
}
:global(.p-dark) .article-body :deep(h2),
:global(.p-dark) .article-body :deep(h3),
:global(.p-dark) .article-body :deep(strong) {
  color: var(--p-surface-50) !important;
}
:global(.p-dark) .article-body :deep(blockquote) {
  color: var(--p-surface-400) !important;
  border-left-color: var(--p-primary-500);
}

.article-not-found {
  padding: 5rem 3rem;
  text-align: center;
  margin-top: 1rem;
}

.article-not-found .section-title {
  margin-top: 1rem;
  font-size: clamp(1.75rem, 4vw, 2.25rem);
}

.article-not-found p {
  margin-bottom: 2rem;
}

/* Hero Section */
.blog-hero {
  padding: 130px 0 3rem;
  border-bottom: 1px solid var(--border);
}

.category-filters {
  display: flex;
  justify-content: center;
  gap: 0.75rem;
  flex-wrap: wrap;
  margin-top: 1rem;
}

.filter-btn {
  padding: 8px 24px;
  border-radius: var(--radius-full);
  background: white;
  border: 1.5px solid var(--border);
  color: var(--text-main);
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all var(--transition-base);
}

.filter-btn:hover, .filter-btn.active {
  background: var(--primary);
  border-color: var(--primary);
  color: white;
  box-shadow: var(--shadow-indigo);
}

/* Grid & Cards Section */
.blog-content-section {
  padding: 5rem 0 6rem;
}

.blog-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.blog-card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.blog-card .post-image-wrapper.mockup-frame {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
  border-left: 0;
  border-right: 0;
  border-top: 0;
  box-shadow: none;
  height: 220px;
}

.post-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
}

.blog-card:hover .post-image {
  transform: scale(1.05);
}

.post-label {
  position: absolute;
  top: 48px; /* space below mockup bar */
  right: 16px;
  z-index: 2;
  box-shadow: var(--shadow-sm);
  background: var(--primary) !important;
  color: white !important;
  border-color: rgba(255,255,255,0.2) !important;
}

.post-body {
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.post-date {
  font-size: 0.8125rem;
  color: var(--text-muted);
  font-weight: 600;
  margin-bottom: 0.75rem;
}

.post-title {
  font-size: 1.25rem;
  font-weight: 800;
  line-height: 1.35;
  margin-bottom: 1rem;
  color: var(--midnight-navy);
  letter-spacing: -0.025em;
  font-family: var(--font-heading);
}

.post-excerpt {
  font-size: 0.875rem;
  line-height: 1.6;
  margin-bottom: 1.75rem;
  flex-grow: 1;
}

.post-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid var(--border);
  padding-top: 1.25rem;
  margin-top: auto;
}

.post-read-time {
  font-size: 0.75rem;
  color: var(--text-muted);
  font-weight: 600;
}

.post-link {
  font-weight: 700;
  font-size: 0.8125rem;
  color: var(--primary);
  display: flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  transition: gap var(--transition-fast);
}

.post-link:hover {
  gap: 8px;
}

/* Newsletter Section */
.newsletter-section {
  padding: 5rem 0;
}

.newsletter-content {
  max-width: 680px;
  margin: 0 auto;
  text-align: center;
  padding: 4rem 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: var(--shadow-xl);
}

.newsletter-content .section-title {
  margin-top: 1.25rem;
  margin-bottom: 0.75rem;
  font-size: clamp(1.75rem, 4vw, 2.25rem);
}

.newsletter-content .section-subtitle {
  margin-bottom: 2.25rem;
}

.newsletter-form {
  display: flex;
  max-width: 500px;
  width: 100%;
  margin: 0 auto;
  gap: 0.75rem;
}

.newsletter-form input {
  flex-grow: 1;
}

@media (max-width: 1024px) {
  .blog-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .blog-grid { grid-template-columns: 1fr; }
  .blog-content-section { padding: 3.5rem 0 4.5rem; }
  .newsletter-section { padding: 3.5rem 0; }
  .newsletter-content { padding: 3rem 1.5rem; }
  .newsletter-form { flex-direction: column; gap: 0.75rem; }
  .newsletter-form button { width: 100%; }
  .full-article { padding: 2rem 1.5rem; }
  .article-hero-image { height: 280px; }
}
</style>
