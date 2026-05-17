<template>
  <main class="blog-page">
    <!-- Soft Background Mesh -->
    <div class="stripe-mesh-bg"></div>

    <!-- Seção de Artigo Completo -->
    <div v-if="idArtigoAtivo" class="article-detail-container">
      <div class="container article-inner">
        <router-link to="/blog" class="btn-back">
          ← Voltar para o Blog
        </router-link>

        <article v-if="artigoAtivo" class="full-article">
          <header class="article-header">
            <span class="article-category">{{ artigoAtivo.category }}</span>
            <h1 class="article-title">{{ artigoAtivo.title }}</h1>
            <div class="article-meta">
              <span class="article-date">Publicado em {{ artigoAtivo.date }}</span>
              <span class="meta-separator">•</span>
              <span class="article-read-time">📖 {{ artigoAtivo.readTime }} de leitura</span>
            </div>
          </header>

          <div class="article-image-wrapper">
            <img :src="artigoAtivo.image" :alt="artigoAtivo.title" class="article-hero-image">
          </div>

          <!-- Dynamic Rich Content for the Post -->
          <div class="article-body" v-html="artigoAtivo.content"></div>
        </article>

        <div v-else class="text-center article-not-found">
          <h2>Artigo não encontrado</h2>
          <p>O artigo que você está procurando não existe ou foi movido.</p>
          <router-link to="/blog" class="btn btn-primary">Voltar para o Blog</router-link>
        </div>
      </div>
    </div>

    <!-- Lista de Posts Padrão (se não houver ID na rota) -->
    <div v-else>
      <section class="blog-hero">
        <div class="container text-center">
          <span class="blog-badge">Central de Inteligência</span>
          <h1 class="blog-title">Central de <span class="text-gradient-vibrant">Conhecimento</span></h1>
          <p class="blog-subtitle">Insights estratégicos, finanças e tecnologia para oficinas que buscam o topo do mercado.</p>
          
          <div class="category-filters">
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

      <section class="blog-content">
        <div class="container">
          <div class="blog-grid">
            <article 
              v-for="post in filteredPosts" 
              :key="post.id" 
              class="blog-card"
            >
              <div class="post-image-wrapper">
                <img :src="post.image" :alt="post.title" class="post-image">
                <span class="post-label">{{ post.category }}</span>
              </div>
              <div class="post-body">
                <span class="post-date">{{ post.date }}</span>
                <h2 class="post-title">{{ post.title }}</h2>
                <p class="post-excerpt">{{ post.excerpt }}</p>
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

      <!-- Newsletter Section (Premium Light Theme) -->
      <section class="newsletter">
        <div class="container newsletter-content">
          <span class="news-badge">Newsletter</span>
          <h3>Receba o Radar NeriTech</h3>
          <p class="news-desc">Notícias do mercado e dicas de gestão diretamente no seu e-mail para acelerar sua oficina.</p>
          <form class="newsletter-form" @submit.prevent="inscreverNewsletter">
            <input type="email" v-model="emailNewsletter" placeholder="Seu melhor e-mail" required>
            <button type="submit" class="btn btn-primary">Inscrever-se</button>
          </form>
        </div>
      </section>
    </div>

    <SecaoRodape />
  </main>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import SecaoRodape from '../components/SecaoRodape.vue';

const route = useRoute();
const emailNewsletter = ref('');
const categories = ['Todos', 'Gestão', 'Mercado', 'Finanças', 'Tecnologia'];
const activeCategory = ref('Todos');

const idArtigoAtivo = computed(() => route.params.id);

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

const inscreverNewsletter = () => {
  if (emailNewsletter.value) {
    alert('Obrigado! Seu e-mail foi cadastrado com sucesso no Radar NeriTech.');
    emailNewsletter.value = '';
  }
};
</script>

<style scoped>
.blog-page {
  position: relative;
  min-height: 100vh;
  padding-top: 100px;
  background-color: #ffffff;
  overflow: hidden;
}

.stripe-mesh-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.03) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.03) 0%, transparent 40%),
    #ffffff;
}

/* Seção de Artigo Detalhado */
.article-detail-container {
  position: relative;
  z-index: 1;
  padding: 40px 0 80px;
}

.article-inner {
  max-width: 800px;
  margin: 0 auto;
}

.btn-back {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: var(--primary-indigo);
  text-decoration: none;
  font-weight: 700;
  font-size: 0.9375rem;
  margin-bottom: 2rem;
  transition: transform 0.2s ease;
}

.btn-back:hover {
  transform: translateX(-4px);
}

.full-article {
  background: white;
  border: 1px solid #edf2f7;
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 10px 30px rgba(10, 37, 64, 0.02);
}

.article-header {
  margin-bottom: 2.5rem;
}

.article-category {
  display: inline-block;
  background: rgba(99, 91, 255, 0.08);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  padding: 4px 12px;
  border-radius: 6px;
  letter-spacing: 0.05em;
  margin-bottom: 1rem;
}

.article-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  line-height: 1.2;
  letter-spacing: -0.03em;
  margin-bottom: 1rem;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 0.875rem;
  color: var(--text-muted);
  font-weight: 500;
}

.meta-separator {
  color: #cbd5e1;
}

.article-image-wrapper {
  height: 400px;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 2.5rem;
}

.article-hero-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-body {
  font-size: 1.125rem;
  color: var(--text-main);
  line-height: 1.8;
}

.article-body :deep(p) {
  margin-bottom: 1.5rem;
}

.article-body :deep(h3) {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-top: 2rem;
  margin-bottom: 1rem;
  letter-spacing: -0.02em;
}

.article-body :deep(ul) {
  margin-bottom: 1.5rem;
  padding-left: 1.5rem;
}

.article-body :deep(li) {
  margin-bottom: 0.5rem;
}

.article-body :deep(strong) {
  color: var(--midnight-navy);
  font-weight: 700;
}

.article-not-found {
  padding: 6rem 0;
  background: white;
  border: 1px solid #edf2f7;
  border-radius: 24px;
}

.article-not-found h2 {
  font-size: 2rem;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
}

.article-not-found p {
  color: var(--text-main);
  margin-bottom: 2rem;
}

/* Lista de Posts / Hero */
.blog-hero {
  position: relative;
  z-index: 1;
  padding: 60px 0 20px;
}

.blog-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.08);
  border: 1px solid rgba(99, 91, 255, 0.15);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 6px 14px;
  border-radius: 99px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 1.25rem;
}

.blog-title {
  font-size: clamp(2.25rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  line-height: 1.1;
  margin-bottom: 1.25rem;
}

.blog-subtitle {
  font-size: 1.125rem;
  color: var(--text-main);
  max-width: 680px;
  margin: 0 auto 2.5rem;
  line-height: 1.6;
}

.category-filters {
  display: flex;
  justify-content: center;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 24px;
  border-radius: 99px;
  background: #f1f5f9;
  border: 1px solid #edf2f7;
  color: var(--text-main);
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-btn:hover, .filter-btn.active {
  background: var(--primary-indigo);
  border-color: var(--primary-indigo);
  color: white;
}

.blog-content {
  position: relative;
  z-index: 1;
  padding: 60px 0 80px;
}

.blog-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.blog-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(10, 37, 64, 0.02);
  transition: all 0.3s ease;
  border: 1px solid #edf2f7;
  display: flex;
  flex-direction: column;
}

.blog-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(10, 37, 64, 0.06);
}

.post-image-wrapper {
  position: relative;
  height: 220px;
  overflow: hidden;
}

.post-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.blog-card:hover .post-image {
  transform: scale(1.05);
}

.post-label {
  position: absolute;
  top: 16px;
  right: 16px;
  background: var(--primary-indigo);
  color: white;
  padding: 4px 12px;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.post-body {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.post-date {
  font-size: 0.8125rem;
  color: var(--text-muted);
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.post-title {
  font-size: 1.25rem;
  font-weight: 700;
  line-height: 1.35;
  margin-bottom: 0.75rem;
  color: var(--midnight-navy);
}

.post-excerpt {
  font-size: 0.875rem;
  color: var(--text-main);
  line-height: 1.6;
  margin-bottom: 1.5rem;
  flex-grow: 1;
}

.post-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid #f1f5f9;
  padding-top: 1rem;
  margin-top: auto;
}

.post-read-time {
  font-size: 0.75rem;
  color: var(--text-muted);
  font-weight: 500;
}

.post-link {
  font-weight: 700;
  font-size: 0.8125rem;
  color: var(--primary-indigo);
  display: flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  transition: gap 0.2s ease;
}

.post-link:hover {
  gap: 8px;
}

/* Newsletter section style */
.newsletter {
  position: relative;
  z-index: 1;
  padding: 80px 0;
  background: #f8fafc;
  border-top: 1px solid #edf2f7;
  text-align: center;
}

.news-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(99, 91, 255, 0.06);
  color: var(--primary-indigo);
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 99px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 1rem;
}

.newsletter-content h3 {
  font-size: clamp(1.75rem, 4vw, 2.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 0.5rem;
  letter-spacing: -0.03em;
}

.news-desc {
  font-size: 1.0625rem;
  color: var(--text-main);
  max-width: 520px;
  margin: 0 auto 2rem;
  line-height: 1.5;
}

.newsletter-form {
  display: flex;
  max-width: 500px;
  margin: 0 auto;
  gap: 0.75rem;
}

.newsletter-form input {
  flex-grow: 1;
  padding: 12px 20px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: #ffffff;
  color: var(--midnight-navy);
  font-size: 0.875rem;
}

.newsletter-form input:focus {
  outline: none;
  border-color: var(--primary-indigo);
}

.newsletter-form button {
  background: var(--primary-indigo);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 700;
  font-size: 0.875rem;
  cursor: pointer;
  box-shadow: var(--shadow-indigo);
  transition: all 0.2s ease;
}

.newsletter-form button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 18px rgba(99, 91, 255, 0.35);
}

@media (max-width: 1024px) {
  .blog-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
  .blog-grid { grid-template-columns: 1fr; }
  .newsletter-form { flex-direction: column; }
  .newsletter-form button { width: 100%; }
  .full-article { padding: 1.5rem; }
  .article-image-wrapper { height: 240px; }
}
</style>
