import { createRouter, createWebHistory } from 'vue-router';
import PaginaHome from '../views/PaginaHome.vue';
import PaginaTeste from '../views/PaginaTeste.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: PaginaHome
  },
  {
    path: '/teste-gratis',
    name: 'trial',
    component: PaginaTeste
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
