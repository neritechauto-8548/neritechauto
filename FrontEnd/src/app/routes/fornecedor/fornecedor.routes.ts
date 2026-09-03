import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
    path: 'lista',
    canActivate: [permissionGuard],
    data: { title: 'Fornecedores', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./fornecedor/fornecedor').then(m => m.Fornecedor),
  },
  {
    path: 'novo',
    canActivate: [permissionGuard],
    data: { title: 'Novo Fornecedor', permissions: ['GERAL_CONFIG_SISTEMA'] },
    loadComponent: () => import('./cadastro-fornecedor/cadastro-fornecedor').then(m => m.CadastroFornecedor),
  },
  {
    path: 'editar/:id',
    canActivate: [permissionGuard],
    data: { title: 'Editar Fornecedor', permissions: ['GERAL_CONFIG_SISTEMA'] },
    loadComponent: () => import('./cadastro-fornecedor/cadastro-fornecedor').then(m => m.CadastroFornecedor),
  },
  {
    path: 'compras',
    canActivate: [permissionGuard],
    data: { title: 'Compras', permissions: ['FORN_INC_PEDIDOS'] },
    loadComponent: () => import('./pedido-compra/pedido-compra').then(m => m.PedidoCompraComponent),
  },
  {
    path: 'pedidos',
    canActivate: [permissionGuard],
    data: { title: 'Pedidos de Fornecedor', permissions: ['FORN_LISTAR_PEDIDOS'] },
    loadComponent: () => import('./pedido-fornecedor/pedido-fornecedor').then(m => m.PedidoFornecedor),
  },
  {
    path: 'pedidos/cadastro',
    canActivate: [permissionGuard],
    data: { title: 'Novo Pedido', permissions: ['FORN_INC_PEDIDOS'] },
    loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor),
  },
  {
    path: 'pedidos/relatorio',
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Pedidos', permissions: ['FORN_IMPRIMIR'] },
    loadComponent: () => import('./relatorio-pedidos/relatorio-pedidos').then(m => m.RelatorioPedidosFornecedor),
  },
  {
    path: 'pedidos/editar/:id',
    canActivate: [permissionGuard],
    data: { title: 'Editar Pedido', permissions: ['FORN_EDIT_PEDIDOS'] },
    loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor),
  },
  {
    path: 'pedidos/visualizar/:id',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Pedido', permissions: ['FORN_VER_ITENS', 'FORN_LISTAR_PEDIDOS'] },
    loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor),
  },
  { path: '', redirectTo: 'lista', pathMatch: 'full' },
];
