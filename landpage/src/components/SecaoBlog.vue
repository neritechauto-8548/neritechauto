<template>
  <section id="blog" class="blog-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">Radar <span class="text-gradient">NeriTech</span></h2>
        <p class="section-subtitle">Fique por dentro do mercado automobilístico e receba dicas de gestão de elite.</p>
      </div>

      <div class="blog-preview-grid">
        <article v-for="post in posts" :key="post.id" class="blog-preview-card">
          <div class="preview-img-wrapper">
            <img :src="post.image" :alt="post.title" class="preview-img">
            <span class="category-tag">{{ post.category }}</span>
          </div>
          <div class="preview-body">
            <h3 class="preview-title">{{ post.title }}</h3>
            <p class="preview-excerpt">{{ post.excerpt }}</p>
            <router-link to="/blog" class="read-more">Ver no Blog <span>→</span></router-link>
          </div>
        </article>
      </div>

      <div class="section-footer">
        <router-link to="/blog" class="btn btn-primary">Ver Todas as Matérias</router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
const posts = [
  {
    id: 1,
    title: 'O Futuro das Oficinas: Como se preparar para os Elétricos',
    excerpt: 'O mercado automobilístico está mudando. Descubra as ferramentas necessárias para não ficar para trás.',
    category: 'Mercado',
    image: '/images/blog_post_electric_car_1773595259767.png'
  },
  {
    id: 2,
    title: '3 Estratégias Reais para Dobrar o Lucro da sua Oficina',
    excerpt: 'Esqueça fórmulas mágicas. Vamos analisar dados Reais de como a otimização impacta seu bolso.',
    category: 'Finanças',
    image: '/images/blog_post_management_dashboard_1773595279284.png'
  }
];
</script>

<style scoped>
.blog-section {
  padding: var(--spacing-xl) 0; /* Reduced from 2xl/3xl */
  background: white;
}

.section-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.section-title {
  font-size: 3rem;
  margin-bottom: var(--spacing-sm);
}

.blog-preview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
}

.blog-preview-card {
  display: flex;
  background: white;
  border-radius: var(--radius-xl);
  overflow: hidden;
  border: 1px solid var(--border);
  transition: var(--transition-base);
}

.blog-preview-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary-indigo);
}

.preview-img-wrapper {
  width: 40%;
  position: relative;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.category-tag {
  position: absolute;
  top: 12px;
  left: 12px;
  background: var(--primary-indigo);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.7rem;
  font-weight: 800;
  text-transform: uppercase;
}

.preview-body {
  width: 60%;
  padding: var(--spacing-lg);
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.preview-title {
  font-size: 1.25rem;
  margin-bottom: var(--spacing-xs);
  line-height: 1.3;
}

.preview-excerpt {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin-bottom: var(--spacing-md);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.read-more {
  font-weight: 700;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 4px;
}

.section-footer {
  text-align: center;
}

@media (max-width: 900px) {
  .blog-preview-grid { grid-template-columns: 1fr; }
  .blog-preview-card { flex-direction: column; }
  .preview-img-wrapper, .preview-body { width: 100%; }
  .preview-img-wrapper { height: 200px; }
}
</style>
