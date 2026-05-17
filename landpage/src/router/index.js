import { createRouter, createWebHistory } from 'vue-router';
import PaginaHome from '../views/PaginaHome.vue';
import PaginaTeste from '../views/PaginaTeste.vue';
import PaginaPrecos from '../views/PaginaPrecos.vue';
import PaginaBlog from '../views/PaginaBlog.vue';
import PoliticaPrivacidade from '../views/legal/PoliticaPrivacidade.vue';
import TermosUso from '../views/legal/TermosUso.vue';
import PoliticaCookies from '../views/legal/PoliticaCookies.vue';
import DPA from '../views/legal/DPA.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: PaginaHome
  },
  {
    path: '/precos',
    name: 'precos',
    component: PaginaPrecos
  },
  {
    path: '/blog',
    name: 'blog',
    component: PaginaBlog
  },
  {
    path: '/blog/:id',
    name: 'blog-post',
    component: PaginaBlog
  },
  {
    path: '/teste-gratis',
    name: 'trial',
    component: PaginaTeste
  },
  {
    path: '/politica-de-privacidade',
    name: 'privacidade',
    component: PoliticaPrivacidade
  },
  {
    path: '/termos-de-uso',
    name: 'termos',
    component: TermosUso
  },
  {
    path: '/politica-de-cookies',
    name: 'cookies',
    component: PoliticaCookies
  },
  {
    path: '/dpa',
    name: 'dpa',
    component: DPA
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth',
      }
    }
    return { top: 0 }
  }
});

export default router;
