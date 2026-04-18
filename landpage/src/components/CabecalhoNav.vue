<template>
  <header class="header" :class="{ 'scrolled': estaRolar, 'menu-open': menuMobileAberto }">
    <div class="container nav-container">
      <!-- Logo -->
      <router-link to="/" class="logo" @click="menuMobileAberto = false">
        <span class="logo-mark">N</span>
        <span class="logo-wordmark">neri<span class="logo-accent">tech</span></span>
      </router-link>

      <!-- Desktop Nav -->
      <nav class="nav-desktop" role="navigation" aria-label="Navegação principal">
        <router-link to="/" class="nav-link">Início</router-link>

        <div class="nav-item-dropdown">
          <button class="nav-link dropdown-trigger" aria-expanded="false">
            Produto <span class="chevron">▾</span>
          </button>
          <div class="dropdown-menu">
            <div class="dropdown-grid">
              <a href="/#showcase" class="dropdown-item">
                <div class="dd-icon">🛠️</div>
                <div class="dd-text">
                  <strong>Gestão de OS</strong>
                  <p>Controle total dos serviços</p>
                </div>
              </a>
              <a href="/#recursos" class="dropdown-item">
                <div class="dd-icon">💰</div>
                <div class="dd-text">
                  <strong>Financeiro</strong>
                  <p>Fluxo de caixa e conciliação</p>
                </div>
              </a>
              <a href="/#recursos" class="dropdown-item">
                <div class="dd-icon">📋</div>
                <div class="dd-text">
                  <strong>CRM e Clientes</strong>
                  <p>Fidelização e histórico</p>
                </div>
              </a>
              <a href="/#recursos" class="dropdown-item">
                <div class="dd-icon">📦</div>
                <div class="dd-text">
                  <strong>Estoque</strong>
                  <p>Controle de peças e insumos</p>
                </div>
              </a>
            </div>
          </div>
        </div>

        <a href="/#planos" class="nav-link">Preços</a>
        <a href="/#clientes" class="nav-link">Clientes</a>

        <div class="nav-item-dropdown">
          <button class="nav-link dropdown-trigger">
            Empresa <span class="chevron">▾</span>
          </button>
          <div class="dropdown-menu dropdown-menu-sm">
            <a href="https://wa.me/5511987654321" target="_blank" rel="noopener" class="dropdown-link">
              <span>💬</span> WhatsApp Suporte
            </a>
            <router-link to="/contato" class="dropdown-link">
              <span>📧</span> Fale por E-mail
            </router-link>
          </div>
        </div>
      </nav>

      <!-- Actions -->
      <div class="nav-actions">
        <a href="/login" class="link-login">Entrar</a>
        <router-link to="/teste-gratis" class="btn btn-primary btn-sm" id="nav-cta-btn">
          Teste Grátis
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
          <a href="/#showcase" class="mobile-link" @click="menuMobileAberto = false">Produto</a>
          <a href="/#planos" class="mobile-link" @click="menuMobileAberto = false">Preços</a>
          <a href="/#clientes" class="mobile-link" @click="menuMobileAberto = false">Clientes</a>
          <a href="/#contato" class="mobile-link" @click="menuMobileAberto = false">Suporte</a>
        </div>
        <div class="mobile-actions">
          <a href="/login" class="btn btn-outline-dark btn-lg mobile-btn">Entrar</a>
          <router-link to="/teste-gratis" class="btn btn-primary btn-lg mobile-btn" @click="menuMobileAberto = false">
            Começar Grátis →
          </router-link>
        </div>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

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
  padding: 16px 0;
  z-index: 1000;
  transition: background 0.3s ease, padding 0.3s ease, box-shadow 0.3s ease, backdrop-filter 0.3s ease;
  background: transparent;
}

.header.scrolled {
  background: rgba(10, 37, 64, 0.88);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  padding: 10px 0;
  box-shadow: 0 1px 0 rgba(255,255,255,0.08), 0 4px 24px rgba(0,0,0,0.25);
}

/* ── Layout ── */
.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

/* ── Logo ── */
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  flex-shrink: 0;
}

.logo-mark {
  width: 32px;
  height: 32px;
  background: var(--primary-indigo);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: var(--font-heading);
  font-weight: 800;
  font-size: 1rem;
  color: white;
}

.logo-wordmark {
  font-family: var(--font-heading);
  font-size: 1.25rem;
  font-weight: 800;
  color: white;
  letter-spacing: -0.04em;
}

.logo-accent {
  color: var(--primary-cyan);
}

/* ── Desktop Nav ── */
.nav-desktop {
  display: flex;
  align-items: center;
  gap: 2px;
}

.nav-link {
  font-size: 0.875rem;
  font-weight: 500;
  color: rgba(255,255,255,0.82) !important;
  padding: 6px 12px;
  border-radius: 6px;
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.2s ease;
  white-space: nowrap;
  text-decoration: none;
  font-family: var(--font-body);
}

.nav-link:hover {
  background: rgba(255,255,255,0.1);
  color: white !important;
}

/* ── Dropdown ── */
.nav-item-dropdown {
  position: relative;
}

.chevron {
  font-size: 0.7rem;
  opacity: 0.6;
  transition: transform 0.2s ease;
}

.nav-item-dropdown:hover .chevron {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  left: 50%;
  transform: translateX(-50%) translateY(-8px) scale(0.97);
  opacity: 0;
  visibility: hidden;
  min-width: 300px;
  background: white;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--border);
  padding: 8px;
  transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
  z-index: 100;
}

.dropdown-menu-sm { min-width: 200px; }

.nav-item-dropdown:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(0) scale(1);
}

.dropdown-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
}

.dropdown-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px 12px;
  border-radius: var(--radius-md);
  text-decoration: none;
  transition: background 0.15s;
}

.dropdown-item:hover {
  background: var(--light-bg);
}

.dd-icon {
  font-size: 1.125rem;
  margin-top: 1px;
  flex-shrink: 0;
}

.dd-text strong {
  display: block;
  font-size: 0.8125rem;
  color: var(--midnight-navy);
  font-weight: 600;
  margin-bottom: 2px;
}

.dd-text p {
  font-size: 0.75rem;
  color: var(--text-muted);
  line-height: 1.4;
}

.dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-main) !important;
  text-decoration: none;
  transition: background 0.15s;
}

.dropdown-link:hover {
  background: var(--light-bg);
  color: var(--primary-indigo) !important;
}

/* ── Nav Actions ── */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.link-login {
  font-size: 0.875rem;
  font-weight: 500;
  color: rgba(255,255,255,0.82) !important;
  transition: color 0.2s;
  text-decoration: none;
}

.link-login:hover {
  color: white !important;
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

.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

/* ── Mobile Menu ── */
.mobile-menu {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  background: rgba(10, 37, 64, 0.96);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-bottom: 1px solid rgba(255,255,255,0.08);
  padding: 1rem 1.5rem 2rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 1.5rem;
}

.mobile-link {
  display: block;
  padding: 12px 8px;
  font-size: 1.125rem;
  font-weight: 500;
  color: rgba(255,255,255,0.85) !important;
  border-bottom: 1px solid rgba(255,255,255,0.06);
  text-decoration: none;
  transition: color 0.2s;
}

.mobile-link:hover { color: white !important; }

.mobile-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mobile-btn {
  width: 100%;
  justify-content: center;
}

/* ── Transitions ── */
.mobile-slide-enter-active,
.mobile-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.mobile-slide-enter-from,
.mobile-slide-leave-to {
  opacity: 0;
  transform: translateY(-12px);
}

/* ── Responsive ── */
@media (max-width: 900px) {
  .nav-desktop { display: none !important; }
  .nav-actions { display: none !important; }
  .menu-toggle { display: block !important; }
}
</style>
