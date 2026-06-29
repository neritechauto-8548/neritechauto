<template>
  <footer class="footer">
    <div class="container footer-top">
      <!-- Brand -->
      <div class="footer-brand">
        <div class="footer-logo">
          <div class="logo-mark">N</div>
          <span class="logo-wordmark">Neri<span class="logo-accent">TechAuto</span></span>
        </div>
        <p class="brand-desc">
          Sistema de gestão para oficinas mecânicas, centros automotivos e funilarias. Feito para simplificar o seu dia a dia.
        </p>
        <div class="social-links">
          <a href="https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm" target="_blank" rel="noopener" @click="openChat($event)" class="social-link" aria-label="Chat Online (Tawk.to)">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 9h12v2H6V9zm8 5H6v-2h8v2zm4-6H6V6h12v2z"/></svg>
          </a>
          <a href="https://instagram.com/neritechauto" target="_blank" rel="noopener" class="social-link" aria-label="Instagram">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zM12 0C8.741 0 8.333.014 7.053.072 2.695.272.273 2.69.073 7.052.014 8.333 0 8.741 0 12c0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98C8.333 23.986 8.741 24 12 24c3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98C15.668.014 15.259 0 12 0zm0 5.838a6.162 6.162 0 100 12.324 6.162 6.162 0 000-12.324zM12 16a4 4 0 110-8 4 4 0 010 8zm6.406-11.845a1.44 1.44 0 100 2.881 1.44 1.44 0 000-2.881z"/></svg>
          </a>
        </div>
      </div>

      <!-- Links -->
      <div class="footer-links-grid">
        <div class="footer-col">
          <h4>Produto</h4>
          <router-link to="/funcionalidades">Funcionalidades</router-link>
          <router-link to="/precos">Preços</router-link>
        </div>
        <div class="footer-col">
          <h4>Empresa</h4>
          <router-link to="/sobre">Sobre a NeriTechAuto</router-link>
        </div>
        <div class="footer-col">
          <h4>Suporte</h4>
          <a href="https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm" target="_blank" @click="openChat($event)">Suporte via Chat Online</a>
          <a href="/#faq">Perguntas Frequentes</a>
        </div>
        <div class="footer-col">
          <h4>Legal</h4>
          <router-link to="/politica-de-privacidade">Política de Privacidade</router-link>
          <router-link to="/termos-de-uso">Termos de Uso</router-link>
          <router-link to="/politica-de-cookies">Política de Cookies</router-link>
          <router-link to="/dpa">DPA</router-link>
        </div>
      </div>
    </div>



    <div class="container footer-bottom">
      <p class="copyright">© {{ currentYear }} NeriTechAuto Sistemas. Todos os direitos reservados.</p>
      <div class="footer-badges">
        <span class="badge-trust">
          <svg width="14" height="14" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg>
          Conexão segura SSL
        </span>
        <span class="badge-trust">
          <svg width="14" height="14" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg>
          LGPD Compliant
        </span>
        <span class="badge-trust">🇧🇷 Feito no Brasil</span>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue';
const currentYear = ref(new Date().getFullYear());

const openChat = (e) => {
  if (window.Tawk_API && typeof window.Tawk_API.maximize === 'function') {
    e.preventDefault();
    window.Tawk_API.maximize();
  }
};
</script>

<style scoped>
.footer {
  background: #0b0f19;
  color: rgba(255,255,255,0.7);
}

/* ── Top ── */
.footer-top {
  display: grid;
  grid-template-columns: 1.2fr 2fr;
  gap: 5rem;
  padding: var(--spacing-2xl) 0 var(--spacing-xl);
  border-bottom: 1px solid rgba(255,255,255,0.07);
}

/* Brand */
.footer-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 1rem;
}

.logo-mark {
  width: 32px;
  height: 32px;
  background: var(--primary-indigo);
  border-radius: 8px;
  color: white;
  font-weight: 800;
  font-size: 1rem;
  font-family: var(--font-heading);
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-wordmark {
  font-family: var(--font-heading);
  font-size: 1.25rem;
  font-weight: 800;
  color: white;
  letter-spacing: -0.04em;
}

.logo-accent { color: var(--primary-cyan); }

.brand-desc {
  font-size: 0.9rem;
  line-height: 1.65;
  max-width: 300px;
  margin-bottom: 1.5rem;
}

.social-links {
  display: flex;
  gap: 10px;
}

.social-link {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  border: 1px solid rgba(255,255,255,0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255,255,255,0.6);
  transition: all var(--transition-base);
  background: rgba(255,255,255,0.04);
}

.social-link:hover {
  border-color: rgba(255,255,255,0.3);
  color: white;
  background: rgba(255,255,255,0.08);
}

/* Links grid */
.footer-links-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.footer-col h4 {
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(255,255,255,0.9);
  margin-bottom: 1.25rem;
}

.footer-col a {
  display: block;
  font-size: 0.875rem;
  color: rgba(255,255,255,0.55);
  margin-bottom: 0.75rem;
  transition: color var(--transition-fast);
  text-decoration: none;
}

.footer-col a:hover {
  color: rgba(255,255,255,0.9);
}

/* ── Bottom ── */
.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem 0;
  flex-wrap: wrap;
  gap: 1rem;
}

.copyright {
  font-size: 0.8125rem;
  color: rgba(255,255,255,0.35);
}

.footer-badges {
  display: flex;
  gap: 1.25rem;
  flex-wrap: wrap;
}

.badge-trust {
  font-size: 0.75rem;
  font-weight: 500;
  color: rgba(255,255,255,0.45);
  display: flex;
  align-items: center;
  gap: 5px;
}

.badge-trust svg {
  color: rgba(255,255,255,0.35);
}



/* ── Responsive ── */
@media (max-width: 1024px) {
  .footer-top { grid-template-columns: 1fr; gap: 3rem; }
  .footer-links-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .footer-links-grid { grid-template-columns: 1fr 1fr; }
  .footer-bottom { flex-direction: column; text-align: center; }
  .footer-badges { justify-content: center; flex-wrap: wrap; }
}
</style>
