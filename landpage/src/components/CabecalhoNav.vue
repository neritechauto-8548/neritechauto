<template>
  <header class="header" :class="{ 'scrolled': estaRolar, 'menu-open': menuMobileAberto, 'header--hidden': estaRolar }">
    <div class="container nav-container">
      <!-- Logo -->
      <LogoMarca to="/" size="md" @click="menuMobileAberto = false" />

      <!-- Desktop Nav (Matches user screenshot exactly) -->
      <nav class="nav-desktop" role="navigation" aria-label="Navegação principal">
        <router-link to="/" class="nav-link">Início</router-link>

        <router-link to="/funcionalidades" class="nav-link">Funcionalidades</router-link>

        <router-link to="/precos" class="nav-link">Preços</router-link>
        
        <router-link to="/blog" class="nav-link">Blog</router-link>

        <a href="/#contato" class="nav-link">Fale conosco</a>
      </nav>
      
      <!-- Actions (CTA Button on the right with Login closer) -->
      <div class="nav-actions">
        <button 
          class="btn-theme-toggle" 
          @click="alternarTema" 
          :aria-label="ehModoEscuro ? 'Ativar modo claro' : 'Ativar modo escuro'"
        >
          <i :class="ehModoEscuro ? 'pi pi-sun' : 'pi pi-moon'"></i>
        </button>

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
          
          <router-link to="/funcionalidades" class="mobile-link" @click="menuMobileAberto = false">Funcionalidades</router-link>

          <a href="/#planos" class="mobile-link" @click="menuMobileAberto = false">Preços</a>
          
          <router-link to="/blog" class="mobile-link" @click="menuMobileAberto = false">Blog</router-link>

          <a href="/#contato" class="mobile-link" @click="menuMobileAberto = false">Fale conosco</a>
        </div>
        <div class="mobile-actions">
          <button 
            class="btn-mobile-theme-toggle" 
            @click="alternarTema"
          >
            <i :class="ehModoEscuro ? 'pi pi-sun' : 'pi pi-moon'"></i>
            <span>{{ ehModoEscuro ? 'Modo Claro' : 'Modo Escuro' }}</span>
          </button>

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
const ehModoEscuro = ref(false);

const handleScroll = () => {
  estaRolar.value = window.scrollY > 40;
};

const alternarTema = () => {
  const isDark = document.documentElement.classList.toggle('p-dark');
  ehModoEscuro.value = isDark;
  localStorage.setItem('theme', isDark ? 'dark' : 'light');
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true });
  
  const temaSalvo = localStorage.getItem('theme');
  if (temaSalvo === 'dark' || (!temaSalvo && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
    document.documentElement.classList.add('p-dark');
    ehModoEscuro.value = true;
  } else {
    document.documentElement.classList.remove('p-dark');
    ehModoEscuro.value = false;
  }
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
/* ── Base Header ── */
.header {
  position: fixed;
  top: 16px;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1000;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1), transform 0.35s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.35s ease;
  background: transparent;
  padding: 0;
}

/* Esconde ao rolar para baixo */
.header--hidden {
  transform: translateY(calc(-100% - 32px));
  opacity: 0;
  pointer-events: none;
}

.header.scrolled {
  top: 10px;
  padding: 0;
}

/* ── Layout ── */
.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  background: rgba(255, 255, 255, 0.0);
  backdrop-filter: blur(0px);
  -webkit-backdrop-filter: blur(0px);
  border: 1px solid transparent;
  border-radius: 99px;
  padding: 10px 24px;
  box-shadow: none;
  transition: all 0.35s ease;
  max-width: 1200px;
  margin: 0 auto;
}

.header.scrolled .nav-container {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  box-shadow: 0 8px 30px -8px rgba(37, 99, 235, 0.12);
  border-color: rgba(37, 99, 235, 0.12);
  padding: 8px 24px;
}

:global(.p-dark) .header.scrolled .nav-container {
  background: rgba(15, 23, 42, 0.9);
  border-color: var(--p-surface-700);
  box-shadow: 0 8px 30px -8px rgba(0, 0, 0, 0.3);
}

/* ── Desktop Nav ── */
.nav-desktop {
  display: flex;
  align-items: center;
  gap: 1.75rem;
}

/* No topo: links e logo brancos (hero azul) */
.nav-link {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9) !important;
  text-decoration: none;
  transition: color 0.2s ease;
  font-family: var(--font-body);
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link:hover {
  color: white !important;
  opacity: 0.85;
}

/* Depois do scroll: links escuros */
.header.scrolled .nav-link {
  color: var(--midnight-navy) !important;
}

.header.scrolled .nav-link:hover {
  color: var(--primary) !important;
  opacity: 1;
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
  gap: 1.25rem;
  flex-shrink: 0;
}

/* Login link: branco no topo, escuro com scroll */
.link-login {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9) !important;
  text-decoration: none;
  transition: all 0.2s;
}
.link-login:hover { color: white !important; }
.header.scrolled .link-login { color: var(--midnight-navy) !important; }
.header.scrolled .link-login:hover { color: var(--primary) !important; }

/* Btn Começar Grátis: outline branco no topo, sólido com scroll */
.btn-try-free {
  background: rgba(255, 255, 255, 0.15);
  color: white !important;
  padding: 0.5rem 1.25rem;
  border-radius: 99px;
  font-weight: 700;
  font-size: 0.8125rem;
  border: 1.5px solid rgba(255, 255, 255, 0.5);
  transition: all var(--transition-base);
  text-decoration: none;
  backdrop-filter: blur(4px);
}

.btn-try-free:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: white;
  transform: translateY(-1px);
}

.header.scrolled .btn-try-free {
  background: var(--primary);
  border-color: transparent;
  box-shadow: var(--shadow-indigo);
}

.header.scrolled .btn-try-free:hover {
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
  background: white;
  border-radius: 2px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.header.scrolled .hamburger span { background: var(--midnight-navy); }

.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

/* ── Mobile Menu ── */
.mobile-menu {
  position: absolute;
  top: 100%;
  left: 5%;
  width: 90%;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid var(--border);
  border-radius: 20px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  z-index: 999;
  box-shadow: var(--shadow-lg);
  margin-top: 8px;
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

.btn-theme-toggle {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  color: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
  transition: background 0.2s, color 0.2s;
}

.btn-theme-toggle:hover {
  background: rgba(255, 255, 255, 0.15);
  color: white;
}

.header.scrolled .btn-theme-toggle {
  color: var(--text-main);
}

.header.scrolled .btn-theme-toggle:hover {
  background: var(--p-surface-100);
  color: var(--primary);
}

:global(.p-dark) .btn-theme-toggle:hover {
  background: var(--p-surface-800);
}

.btn-mobile-theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 0.75rem;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 99px;
  background: transparent;
  color: var(--midnight-navy);
  font-weight: 600;
  cursor: pointer;
  margin-bottom: 0.5rem;
}

:global(.p-dark) .btn-mobile-theme-toggle {
  border-color: var(--p-surface-800);
  color: var(--p-surface-50);
}

/* ── Responsive ── */
@media (max-width: 992px) {
  .nav-desktop { display: none !important; }
  .nav-actions { display: none !important; }
  .menu-toggle { display: block !important; }
}
</style>
