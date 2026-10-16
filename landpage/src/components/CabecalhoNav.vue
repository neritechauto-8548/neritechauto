<template>
  <header class="header" :class="{ scrolled: estaRolar, 'menu-open': menuMobileAberto }">
    <div class="container nav-container">
      <LogoMarca to="/" size="md" @click="menuMobileAberto = false" />

      <nav class="nav-desktop" role="navigation" aria-label="Navegação principal">
        <router-link to="/" class="nav-link">Início</router-link>
        <router-link to="/funcionalidades" class="nav-link">Produtos</router-link>
        <router-link to="/precos" class="nav-link">Preços</router-link>
        <router-link to="/blog" class="nav-link">Recursos</router-link>
        <a href="/#contato" class="nav-link">Contato</a>
      </nav>

      <div class="nav-actions">
        <button
          class="btn-theme-toggle"
          @click="alternarTema"
          :aria-label="ehModoEscuro ? 'Ativar modo claro' : 'Ativar modo escuro'"
        >
          <i :class="ehModoEscuro ? 'pi pi-sun' : 'pi pi-moon'"></i>
        </button>

        <a :href="urlSistemaCliente" class="link-login">Entrar</a>
        <router-link to="/teste-gratis" class="btn-try-free" id="nav-cta-btn">
          Começar agora
        </router-link>
      </div>

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

    <Transition name="mobile-slide">
      <div v-if="menuMobileAberto" class="mobile-menu">
        <div class="mobile-nav">
          <router-link to="/" class="mobile-link" @click="menuMobileAberto = false">Início</router-link>
          <router-link to="/funcionalidades" class="mobile-link" @click="menuMobileAberto = false">Produtos</router-link>
          <a href="/#planos" class="mobile-link" @click="menuMobileAberto = false">Preços</a>
          <router-link to="/blog" class="mobile-link" @click="menuMobileAberto = false">Recursos</router-link>
          <a href="/#contato" class="mobile-link" @click="menuMobileAberto = false">Contato</a>
        </div>
        <div class="mobile-actions">
          <button class="btn-mobile-theme-toggle" @click="alternarTema">
            <i :class="ehModoEscuro ? 'pi pi-sun' : 'pi pi-moon'"></i>
            <span>{{ ehModoEscuro ? 'Modo Claro' : 'Modo Escuro' }}</span>
          </button>
          <a :href="urlSistemaCliente" class="btn-mobile-login">Entrar</a>
          <router-link to="/teste-gratis" class="btn-mobile-cta" @click="menuMobileAberto = false">
            Começar agora →
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
  estaRolar.value = window.scrollY > 20;
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
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid transparent;
  transition: background 0.25s ease, border-color 0.25s ease, box-shadow 0.25s ease;
}

.header.scrolled {
  background: rgba(255, 255, 255, 0.92);
  border-bottom-color: rgba(50, 50, 93, 0.08);
  box-shadow: 0 1px 0 rgba(50, 50, 93, 0.04);
}

:global(.p-dark) .header {
  background: rgba(10, 37, 64, 0.85);
}
:global(.p-dark) .header.scrolled {
  background: rgba(10, 37, 64, 0.95);
  border-bottom-color: rgba(255, 255, 255, 0.08);
}

.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  height: 64px;
  width: 100%;
}

.nav-desktop {
  display: flex;
  align-items: center;
  gap: 1.75rem;
}

.nav-link {
  font-size: 0.875rem;
  font-weight: 500;
  color: #425466 !important;
  text-decoration: none;
  transition: color 0.15s ease;
  letter-spacing: -0.01em;
}
.nav-link:hover {
  color: #0A2540 !important;
}
.nav-link.router-link-active {
  color: #0A2540 !important;
}

:global(.p-dark) .nav-link {
  color: #C7D0DB !important;
}
:global(.p-dark) .nav-link:hover,
:global(.p-dark) .nav-link.router-link-active {
  color: #F6F9FC !important;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-shrink: 0;
}

.link-login {
  font-size: 0.875rem;
  font-weight: 500;
  color: #425466 !important;
  text-decoration: none;
  transition: color 0.15s;
}
.link-login:hover { color: #0A2540 !important; }
:global(.p-dark) .link-login { color: #C7D0DB !important; }
:global(.p-dark) .link-login:hover { color: #F6F9FC !important; }

.btn-try-free {
  background: #635BFF;
  color: white !important;
  padding: 0.5rem 1.1rem;
  border-radius: 9999px;
  font-weight: 500;
  font-size: 0.875rem;
  border: none;
  transition: all 0.2s ease;
  text-decoration: none;
  box-shadow: 0 4px 14px rgba(99, 91, 255, 0.28);
}
.btn-try-free:hover {
  background: #7A73FF;
  transform: translateY(-1px);
}

.btn-theme-toggle {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  color: #425466;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  transition: background 0.2s, color 0.2s;
}
.btn-theme-toggle:hover {
  background: #F6F9FC;
  color: #635BFF;
}
:global(.p-dark) .btn-theme-toggle {
  color: #C7D0DB;
}
:global(.p-dark) .btn-theme-toggle:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #A5A0FF;
}

.menu-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.hamburger {
  width: 20px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}
.hamburger span {
  display: block;
  width: 100%;
  height: 2px;
  background: #0A2540;
  border-radius: 2px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
:global(.p-dark) .hamburger span {
  background: #F6F9FC;
}
.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

.mobile-menu {
  position: absolute;
  top: 100%;
  left: 1rem;
  right: 1rem;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border: 1px solid #E6EBF1;
  border-radius: 12px;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  z-index: 999;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.12);
  margin-top: 8px;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
}

.mobile-link {
  display: block;
  padding: 10px 0;
  font-size: 1rem;
  font-weight: 500;
  color: #0A2540 !important;
  border-bottom: 1px solid #EEF2F7;
  text-decoration: none;
}
.mobile-link:hover { color: #635BFF !important; }

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
  border: 1px solid #E6EBF1;
  border-radius: 9999px;
  color: #0A2540 !important;
  font-weight: 500;
  text-decoration: none;
}

.btn-mobile-cta {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0.75rem;
  background: #635BFF;
  border-radius: 9999px;
  color: white !important;
  font-weight: 500;
  box-shadow: 0 4px 14px rgba(99, 91, 255, 0.28);
  text-decoration: none;
}

.btn-mobile-theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #E6EBF1;
  border-radius: 9999px;
  background: transparent;
  color: #0A2540;
  font-weight: 500;
  cursor: pointer;
}

.mobile-slide-enter-active,
.mobile-slide-leave-active {
  transition: all 0.22s ease;
}
.mobile-slide-enter-from,
.mobile-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

@media (max-width: 992px) {
  .nav-desktop { display: none !important; }
  .nav-actions { display: none !important; }
  .menu-toggle { display: block !important; }
}
</style>
