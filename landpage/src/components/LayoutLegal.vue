<template>
  <div class="legal-layout">
    <div class="legal-hero">
      <div class="container">
        <span class="legal-badge">Documento Legal</span>
        <h1 class="legal-title">{{ title }}</h1>
        <p class="legal-updated">Última atualização: {{ lastUpdated }}</p>
      </div>
    </div>
    
    <div class="legal-content">
      <div class="container">
        <div class="legal-grid">
          <aside class="legal-sidebar">
            <nav class="legal-nav">
              <slot name="navigation"></slot>
            </nav>
          </aside>
          
          <main class="legal-main">
            <slot name="content"></slot>
            
            <div class="legal-contact">
              <h3>Dúvidas sobre este documento?</h3>
              <p>Entre em contato com nosso Encarregado de Dados (DPO) através do e-mail: <strong>legal@neri<span class="logo-accent">techauto</span>.com.br</strong></p>
            </div>
          </main>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  lastUpdated: String
});
</script>

<style scoped>
.legal-layout {
  background: white;
  min-height: 100vh;
  padding-bottom: 5rem;
}

.legal-hero {
  background: var(--light-bg);
  padding: 120px 0 60px;
  border-bottom: 1px solid var(--border);
  text-align: center;
}

.legal-badge {
  display: inline-block;
  padding: 4px 12px;
  background: rgba(99,91,255,0.1);
  color: var(--primary-indigo);
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 1rem;
}

.legal-title {
  font-size: clamp(2rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
  letter-spacing: -0.03em;
}

.legal-updated {
  font-size: 0.9375rem;
  color: var(--text-muted);
}

.legal-content {
  padding: 4rem 0;
}

.legal-grid {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 5rem;
}

/* Sidebar Nav */
.legal-sidebar {
  position: sticky;
  top: 100px;
  height: max-content;
}

:deep(.legal-nav a) {
  display: block;
  padding: 10px 0;
  color: var(--text-muted);
  font-size: 0.875rem;
  text-decoration: none;
  font-weight: 500;
  border-bottom: 1px solid transparent;
  transition: all 0.2s;
}

:deep(.legal-nav a:hover) {
  color: var(--primary-indigo);
}

/* Main Content Typography */
.legal-main {
  max-width: 800px;
}

:deep(.legal-main h2) {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin: 2.5rem 0 1rem;
}

:deep(.legal-main h3) {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin: 1.5rem 0 0.75rem;
}

:deep(.legal-main p) {
  font-size: 1.0625rem;
  line-height: 1.7;
  color: var(--text-main);
  margin-bottom: 1.25rem;
}

:deep(.legal-main ul), :deep(.legal-main ol) {
  margin-bottom: 1.5rem;
  padding-left: 1.5rem;
}

:deep(.legal-main li) {
  font-size: 1.0625rem;
  line-height: 1.7;
  color: var(--text-main);
  margin-bottom: 0.5rem;
}

.legal-contact {
  margin-top: 4rem;
  padding: 2rem;
  background: var(--light-bg);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
}

.legal-contact h3 {
  margin-top: 0 !important;
}

@media (max-width: 968px) {
  .legal-grid {
    grid-template-columns: 1fr;
    gap: 3rem;
  }
  .legal-sidebar {
    display: none;
  }
}
</style>
