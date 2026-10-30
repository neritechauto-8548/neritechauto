import { Routes } from '@angular/router';
import { Cliente } from './cliente/cliente';
import { CadastroCliente } from './cadastro-cliente/cadastro-cliente';

export const routes: Routes = [
  { path: '', redirectTo: 'listar', pathMatch: 'full' },
  { path: 'listar', component: Cliente, data: { title: 'Clientes' } },
  { path: 'cadastro', component: CadastroCliente, data: { title: 'Novo Cliente' } },
  { path: 'editar/:uuid', component: CadastroCliente, data: { title: 'Editar Cliente' } },
  { path: 'indicadores', loadComponent: () => import('./indicadores/indicadores').then(m => m.ClientesIndicadores), data: { title: 'Indicadores de Clientes' } },
  {
    path: 'crm',
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () => import('./crm/crm').then(m => m.Crm),
        data: { title: 'CRM' },
      },
      {
        path: 'comunicacao',
        loadComponent: () => import('./crm/comunicacao/comunicacao').then(m => m.Comunicacao),
        data: { title: 'Comunicação' },
      },
      {
        path: 'pesquisa',
        loadComponent: () => import('../configuracoes/questionamento/questionamento').then(m => m.Questionamento),
        data: { title: 'Questionamento de pesquisa' },
      },
    ],
  },
];
