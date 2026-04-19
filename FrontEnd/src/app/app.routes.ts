import { Routes } from '@angular/router';
import { authGuard } from '@core';
import { AdminLayout } from '@theme/admin-layout/admin-layout';
import { AuthLayout } from '@theme/auth-layout/auth-layout';
import { Dashboard } from './routes/dashboard/dashboard';
import { Error403 } from './routes/sessions/error-403';
import { Error404 } from './routes/sessions/error-404';
import { Error500 } from './routes/sessions/error-500';
import { Login } from './routes/sessions/login/login';
import { Recover } from './routes/sessions/recover/recover';
import { ResetPassword } from './routes/sessions/reset-password/reset-password';

export const routes: Routes = [
  {
    path: '',
    component: AdminLayout,
    canActivate: [authGuard],
    canActivateChild: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard, data: { title: 'Dashboard' } },
      { path: '403', component: Error403 },
      { path: '404', component: Error404 },
      { path: '500', component: Error500 },
      {
        path: 'design',
        loadChildren: () => import('./routes/design/design.routes').then(m => m.routes),
      },
      {
        path: 'material',
        loadChildren: () => import('./routes/material/material.routes').then(m => m.routes),
      },
      {
        path: 'media',
        loadChildren: () => import('./routes/media/media.routes').then(m => m.routes),
      },
      {
        path: 'forms',
        loadChildren: () => import('./routes/forms/forms.routes').then(m => m.routes),
      },
      {
        path: 'tables',
        loadChildren: () => import('./routes/tables/tables.routes').then(m => m.routes),
      },
      {
        path: 'profile',
        loadChildren: () => import('./routes/profile/profile.routes').then(m => m.routes),
      },
      {
        path: 'permissions',
        loadChildren: () => import('./routes/permissions/permissions.routes').then(m => m.routes),
      },
      {
        path: 'cliente',
        loadChildren: () => import('./routes/cliente/cliente.routes').then(m => m.routes),
      },
      {
        path: 'configuracoes',
        loadChildren: () => import('./routes/configuracoes/configuracoes.routes').then(m => m.routes),
      },
      {
        path: 'veiculo',
        loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes),
      },
      {
        path: 'fornecedor',
        loadChildren: () => import('./routes/fornecedor/fornecedor.routes').then(m => m.routes),
      },
      {
        path: 'os',
        loadChildren: () => import('./routes/os/os.routes').then(m => m.routes),
      },
      {
        path: 'pdv',
        loadChildren: () => import('./routes/pdv/pdv.routes').then(m => m.routes),
      },
      {
        path: 'utilities',
        loadChildren: () => import('./routes/utilities/utilities.routes').then(m => m.routes),
      },
      {
        path: 'relatorios',
        loadChildren: () => import('./routes/relatorios/relatorios.routes').then(m => m.routes),
      },
      {
        path: 'agendamento',
        loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },
      {
        path: 'fiscal',
        loadChildren: () => import('./routes/fiscal/fiscal.routes').then(m => m.routes),
      },
      {
        path: 'financeiro',
        loadChildren: () => import('./routes/financeiro/financeiro.routes').then(m => m.routes),
      },
      {
        path: 'produtos-servicos',
        loadChildren: () => import('./routes/produtos-servicos/produtos-servicos.routes').then(m => m.routes),
      },
      {
        path: 'orcamento',
        loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes),
      },
    ],
  },
  {
    path: 'auth',
    component: AuthLayout,
    children: [
      { path: 'login', component: Login },
      { path: 'recover', component: Recover },
      { path: 'reset-password', component: ResetPassword },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];
