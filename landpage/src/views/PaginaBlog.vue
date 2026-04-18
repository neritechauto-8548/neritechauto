<template>
  <div class="blog-view">
    <section class="blog-hero section-dark">
      <div class="container">
        <h1 class="blog-title">Central de <span class="text-gradient-vibrant">Conhecimento</span></h1>
        <p class="blog-subtitle">Insights estratégicos para oficinas que buscam o topo do mercado.</p>
        
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
              <router-link :to="`/blog/${post.id}`" class="post-link">
                Ler matéria completa <span class="arrow">→</span>
              </router-link>
            </div>
          </article>
        </div>
      </div>
    </section>

    <!-- Newsletter Section -->
    <section class="newsletter section-dark">
      <div class="container newsletter-content">
        <h3>Receba o Radar NeriTech</h3>
        <p>Notícias do mercado e dicas de gestão diretamente no seu e-mail.</p>
        <form class="newsletter-form" @submit.prevent>
          <input type="email" placeholder="Seu melhor e-mail" required>
          <button type="submit" class="btn btn-primary">Inscrever-se</button>
        </form>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const categories = ['Todos', 'Gestão', 'Mercado', 'Finanças', 'Tecnologia'];
const activeCategory = ref('Todos');

const posts = ref([
  {
    id: 'tendencias-veiculos-eletricos-2026',
    title: 'O Futuro das Oficinas: Como se preparar para os Elétricos',
    excerpt: 'O mercado automobilístico está mudando mais rápido do que nunca. Descubra as ferramentas e treinamentos necessários para não ficar para trás.',
    category: 'Mercado',
    date: '15 Mar, 2026',
    image: '/images/blog_post_electric_car_1773595259767.png'
  },
  {
    id: 'gestao-financeira-lucratividade',
    title: '3 Estratégias Reais para Dobrar o Lucro da sua Oficina',
    excerpt: 'Esqueça fórmulas mágicas. Vamos analisar dados Reais de como a otimização de processos e controle de estoque impactam seu bolso.',
    category: 'Finanças',
    date: '12 Mar, 2026',
    image: '/images/blog_post_management_dashboard_1773595279284.png'
  },
  {
    id: 'experiencia-do-cliente-fidelizacao',
    title: 'Fidelização: Transformando Clientes em Fãs da sua Marca',
    excerpt: 'A primeira impressão é a que fica, mas é o pós-venda que traz o cliente de volta. Aprenda como a NeriTech ajuda no encantamento.',
    category: 'Gestão',
    date: '10 Mar, 2026',
    image: '/images/blog_post_customer_experience_1773595298748.png'
  }
]);

const filteredPosts = computed(() => {
  if (activeCategory.value === 'Todos') return posts.value;
  return posts.value.filter(post => post.category === activeCategory.value);
});
</script>

<style scoped>
.blog-view {
  padding-top: 80px;
}

.blog-hero {
  padding: var(--spacing-3xl) 0 var(--spacing-xl);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.blog-title {
  font-size: 4rem;
  margin-bottom: var(--spacing-sm);
}

.blog-subtitle {
  font-size: 1.5rem;
  color: var(--text-light);
  max-width: 700px;
  margin: 0 auto var(--spacing-xl);
}

.category-filters {
  display: flex;
  justify-content: center;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 24px;
  border-radius: 99px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: var(--white);
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-base);
}

.filter-btn:hover, .filter-btn.active {
  background: var(--primary-violet);
  border-color: var(--primary-violet);
}

.blog-content {
  padding: var(--spacing-3xl) 0;
  background: var(--light-bg);
}

.blog-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-xl);
}

.blog-card {
  background: white;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: var(--transition-base);
  border: 1px solid var(--border);
  display: flex;
  flex-direction: column;
}

.blog-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-xl);
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
  transform: scale(1.1);
}

.post-label {
  position: absolute;
  top: 16px;
  right: 16px;
  background: var(--primary-violet);
  color: white;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
}

.post-body {
  padding: var(--spacing-lg);
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.post-date {
  font-size: 0.8125rem;
  color: var(--text-muted);
  font-weight: 600;
  margin-bottom: var(--spacing-xs);
}

.post-title {
  font-size: 1.375rem;
  line-height: 1.3;
  margin-bottom: var(--spacing-sm);
  color: var(--midnight-navy);
}

.post-excerpt {
  font-size: 0.9375rem;
  color: var(--text-muted);
  line-height: 1.6;
  margin-bottom: var(--spacing-lg);
  flex-grow: 1;
}

.post-link {
  font-weight: 700;
  font-size: 0.875rem;
  color: var(--primary-indigo);
  display: flex;
  align-items: center;
  gap: 8px;
}

.newsletter {
  padding: var(--spacing-3xl) 0;
  text-align: center;
}

.newsletter-content h3 {
  font-size: 2.5rem;
  margin-bottom: var(--spacing-xs);
}

.newsletter-content p {
  color: var(--text-light);
  margin-bottom: var(--spacing-xl);
}

.newsletter-form {
  display: flex;
  max-width: 500px;
  margin: 0 auto;
  gap: var(--spacing-sm);
}

.newsletter-form input {
  flex-grow: 1;
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
}

@media (max-width: 1024px) {
  .blog-grid { grid-template-columns: repeat(2, 1fr); }
  .blog-title { font-size: 3rem; }
}

@media (max-width: 768px) {
  .blog-grid { grid-template-columns: 1fr; }
  .blog-title { font-size: 2.5rem; }
  .newsletter-form { flex-direction: column; }
}
</style>
