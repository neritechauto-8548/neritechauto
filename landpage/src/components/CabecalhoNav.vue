<template>
  <header class="header" :class="{ 'scrolled': estaRolar, 'menu-open': menuMobileAberto }">
    <div class="container nav-container">
      <!-- Logo -->
      <LogoMarca to="/" size="md" @click="menuMobileAberto = false" />

      <!-- Desktop Nav (Matches user screenshot exactly) -->
      <nav class="nav-desktop" role="navigation" aria-label="Navegação principal">
        <router-link to="/" class="nav-link">Início</router-link>

        <!-- Dropdown Funcionalidades -->
        <div class="nav-dropdown-item">
          <button class="nav-link nav-dropdown-trigger">
            Funcionalidades
            <svg class="dropdown-chevron" width="10" height="6" viewBox="0 0 10 6" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M1 1L5 5L9 1" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <div class="dropdown-menu">
            <a href="/#showcase" class="dropdown-item">
              <span class="dd-icon">📋</span>
              <div class="dd-text">
                <span class="dd-title">Ordem de Serviço</span>
                <span class="dd-desc">Abertura e controle ágil de OS.</span>
              </div>
            </a>
            <a href="/#piloto-automatico" class="dropdown-item">
              <span class="dd-icon">📊</span>
              <div class="dd-text">
                <span class="dd-title">Pátio Kanban</span>
                <span class="dd-desc">Acompanhe veículos em tempo real.</span>
              </div>
            </a>
            <a href="/#especialistas" class="dropdown-item">
              <span class="dd-icon">📸</span>
              <div class="dd-text">
                <span class="dd-title">Vistoria Digital (DVI)</span>
                <span class="dd-desc">Checklist com fotos no WhatsApp.</span>
              </div>
            </a>
          </div>
        </div>

        <router-link to="/precos" class="nav-link">Preços</router-link>
        
        <router-link to="/blog" class="nav-link">Blog</router-link>

        <!-- Dropdown Fale conosco -->
        <div class="nav-dropdown-item">
          <button class="nav-link nav-dropdown-trigger">
            Fale conosco
            <svg class="dropdown-chevron" width="10" height="6" viewBox="0 0 10 6" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M1 1L5 5L9 1" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <div class="dropdown-menu dropdown-menu-right">
            <a href="https://wa.me/5511999999999" target="_blank" class="dropdown-item">
              <span class="dd-icon">💬</span>
              <div class="dd-text">
                <span class="dd-title">Suporte WhatsApp</span>
                <span class="dd-desc">Fale com um técnico na hora.</span>
              </div>
            </a>
            <a href="/#contato" class="dropdown-item">
              <span class="dd-icon">✉️</span>
              <div class="dd-text">
                <span class="dd-title">Enviar Mensagem</span>
                <span class="dd-desc">Envie um e-mail para nós.</span>
              </div>
            </a>
          </div>
        </div>
      </nav>
      
      <!-- Actions (CTA Button on the right with Login closer) -->
      <div class="nav-actions">
        <a :href="urlSistemaCliente" class="link-login">Login</a>
        <router-link to="/teste-gratis" class="btn-try-free" id="nav-cta-btn">
          Começar Grátis
        </router-link>
      </div>

      <!-- Mobile toggle -->
      <button
        class="menu-toggle"
        @click="menuMobileAberto = !menuMobileAberto"
        :aria-label="menuMobileAberto ? 'Fechar menu' : 'Abrir menu'"
        :aria-expanded="menuMobileAberto"
      >
        <div class="hamburger" :class="{ open: menuMobileAberto }">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </button>
    </div>

    <!-- Mobile Menu Overlay -->
    <Transition name="mobile-slide">
      <div v-if="menuMobileAberto" class="mobile-menu">
        <div class="mobile-nav">
          <router-link to="/" class="mobile-link" @click="menuMobileAberto = false">Início</router-link>
          
          <div class="mobile-section-title">Funcionalidades</div>
          <a href="/#showcase" class="mobile-link indented" @click="menuMobileAberto = false">Ordem de Serviço</a>
          <a href="/#piloto-automatico" class="mobile-link indented" @click="menuMobileAberto = false">Pátio Kanban</a>
          <a href="/#especialistas" class="mobile-link indented" @click="menuMobileAberto = false">Vistoria Digital (DVI)</a>

          <a href="/#planos" class="mobile-link" @click="menuMobileAberto = false">Preços</a>
          
          <router-link to="/blog" class="mobile-link" @click="menuMobileAberto = false">Blog</router-link>

          <div class="mobile-section-title">Fale conosco</div>
          <a href="https://wa.me/5511999999999" target="_blank" class="mobile-link indented" @click="menuMobileAberto = false">Suporte WhatsApp</a>
          <a href="/#contato" class="mobile-link indented" @click="menuMobileAberto = false">Enviar Mensagem</a>
        </div>
        <div class="mobile-actions">
          <a :href="urlSistemaCliente" class="btn-mobile-login">Login</a>
          <router-link to="/teste-gratis" class="btn-mobile-cta" @click="menuMobileAberto = false">
            Começar Grátis →
          </router-link>
        </div>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import LogoMarca from './LogoMarca.vue';

const urlSistemaCliente = import.meta.env.VITE_URL_SISTEMA_CLIENTE || '/login';
const estaRolar = ref(false);
const menuMobileAberto = ref(false);

const handleScroll = () => {
  estaRolar.value = window.scrollY > 40;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true });
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
/* ── Base Header ── */
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  padding: 20px 0;
  z-index: 1000;
  transition: all 0.3s ease;
  background: transparent;
}

.header.scrolled {
  background: rgba(239, 246, 255, 0.92);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  padding: 14px 0;
  box-shadow: 0 1px 0 rgba(37, 99, 235, 0.06), 0 4px 20px rgba(37, 99, 235, 0.04);
}

/* ── Layout ── */
.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

/* ── Desktop Nav ── */
.nav-desktop {
  display: flex;
  align-items: center;
  gap: 1.75rem;
}

.nav-link {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-main) !important;
  text-decoration: none;
  transition: color 0.15s ease;
  font-family: var(--font-body);
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link:hover {
  color: var(--primary) !important;
}

.link-login-nav {
  margin-left: 0.5rem;
}

/* ── Dropdown Styles ── */
.nav-dropdown-item {
  position: relative;
  display: inline-block;
}

.nav-dropdown-trigger {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  outline: none;
}

.dropdown-chevron {
  transition: transform 0.2s ease;
  color: var(--text-muted);
}

.nav-dropdown-item:hover .dropdown-chevron {
  transform: rotate(180deg);
  color: var(--primary);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(10px);
  width: 280px;
  background: #ffffff;
  border: 1px solid #edf2f7;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 20px 40px rgba(10, 37, 64, 0.08);
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
  z-index: 1000;
  margin-top: 12px;
}

.dropdown-menu-right {
  left: auto;
  right: 0;
  transform: translateY(10px);
}

.nav-dropdown-item:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(0);
}

.nav-dropdown-item:hover .dropdown-menu-right {
  transform: translateY(0);
}

.dropdown-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 10px;
  border-radius: 8px;
  text-decoration: none;
  transition: background 0.15s ease;
}

.dropdown-item:hover {
  background: #f8fafc;
}

.dd-icon {
  font-size: 1.25rem;
  line-height: 1;
  margin-top: 2px;
}

.dd-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dd-title {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

.dd-desc {
  font-size: 0.75rem;
  color: var(--text-muted);
  line-height: 1.3;
}

/* ── Nav Actions ── */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex-shrink: 0;
}

.btn-try-free {
  background: var(--primary);
  color: white !important;
  padding: 0.5rem 1.25rem;
  border-radius: 99px;
  font-weight: 700;
  font-size: 0.8125rem;
  box-shadow: var(--shadow-indigo);
  transition: all var(--transition-base);
  text-decoration: none;
}

.btn-try-free:hover {
  transform: translateY(-1px);
  background: var(--primary-dark);
  box-shadow: 0 6px 20px var(--primary-shadow);
}

/* ── Mobile Toggle ── */
.menu-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.hamburger {
  width: 22px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.hamburger span {
  display: block;
  width: 100%;
  height: 2px;
  background: var(--midnight-navy);
  border-radius: 2px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

/* ── Mobile Menu ── */
.mobile-menu {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  z-index: 999;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.mobile-link {
  display: block;
  padding: 10px 0;
  font-size: 1rem;
  font-weight: 600;
  color: var(--midnight-navy) !important;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  text-decoration: none;
}

.mobile-link.indented {
  padding-left: 15px;
  font-size: 0.9375rem;
  color: var(--text-main) !important;
  font-weight: 500;
}

.mobile-section-title {
  font-size: 0.75rem;
  font-weight: 800;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.05em;
  padding-top: 12px;
  padding-bottom: 4px;
}

.mobile-link:hover { color: var(--primary) !important; }

.mobile-actions {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.btn-mobile-login {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0.75rem;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 99px;
  color: var(--midnight-navy) !important;
  font-weight: 600;
  text-decoration: none;
}

.btn-mobile-cta {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0.75rem;
  background: var(--primary);
  border-radius: 99px;
  color: white !important;
  font-weight: 700;
  box-shadow: var(--shadow-indigo);
  text-decoration: none;
}

/* ── Responsive ── */
@media (max-width: 992px) {
  .nav-desktop { display: none !important; }
  .nav-actions { display: none !important; }
  .menu-toggle { display: block !important; }
}
</style>
