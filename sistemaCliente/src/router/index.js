import { createRouter, createWebHistory } from 'vue-router';
import { auth } from '@/api/osService';

// Importar views
import ConsultaRapida from '@/views/ConsultaRapida.vue';
import Login from '@/views/Login.vue';
import Historico from '@/views/Historico.vue';
import DetalheOS from '@/views/DetalheOS.vue';

const routes = [
  {
    path: '/',
    redirect: '/consulta',
  },
  {
    path: '/consulta',
    name: 'ConsultaRapida',
    component: ConsultaRapida,
    meta: {
      title: 'Consulta Rápida - NeriTech',
      requiresAuth: false,
    },
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: 'Portal do Cliente - NeriTech',
      requiresAuth: false,
    },
  },
  {
    path: '/historico',
    name: 'Historico',
    component: Historico,
    meta: {
      title: 'Histórico de Ordens - NeriTech',
      requiresAuth: true,
    },
  },
  {
    path: '/os/:id',
    name: 'DetalheOS',
    component: DetalheOS,
    meta: {
      title: 'Detalhes da Ordem - NeriTech',
      requiresAuth: false, // Permitir visualização via link direto ou consulta pública
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Guard de navegação para rotas protegidas
router.beforeEach((to, from, next) => {
  // Atualizar título da página
  document.title = to.meta.title || 'NeriTech';

  // Verificar autenticação
  const requiresAuth = to.meta.requiresAuth;
  const isAuthenticated = auth.isAuthenticated();

  if (requiresAuth && !isAuthenticated) {
    // Redirecionar para login se rota requer autenticação
    next({
      name: 'Login',
      query: { redirect: to.fullPath },
    });
  } else if (to.name === 'Login' && isAuthenticated) {
    // Se já está autenticado e tenta acessar login, redirecionar para histórico
    next({ name: 'Historico' });
  } else {
    next();
  }
});

export default router;
