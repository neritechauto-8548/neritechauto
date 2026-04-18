import { Routes } from '@angular/router';

export const routes: Routes = [
  {
      path: 'lista',
      loadComponent: () => import('./fornecedor/fornecedor').then(m => m.Fornecedor)
  },
  {
      path: 'novo',
      loadComponent: () => import('./cadastro-fornecedor/cadastro-fornecedor').then(m => m.CadastroFornecedor)
  },
  {
      path: 'editar/:id',
      loadComponent: () => import('./cadastro-fornecedor/cadastro-fornecedor').then(m => m.CadastroFornecedor)
  },
  {
      path: 'compras',
      loadComponent: () => import('./pedido-compra/pedido-compra').then(m => m.PedidoCompraComponent)
  },
  {
      path: 'pedidos',
      loadComponent: () => import('./pedido-fornecedor/pedido-fornecedor').then(m => m.PedidoFornecedor)
  },
  {
      path: 'pedidos/cadastro',
      loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor)
  },
  {
      path: 'pedidos/relatorio',
      loadComponent: () => import('./relatorio-pedidos/relatorio-pedidos').then(m => m.RelatorioPedidosFornecedor)
  },
  {
      path: 'pedidos/editar/:id',
      loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor)
  },
  {
      path: 'pedidos/visualizar/:id',
      loadComponent: () => import('./cadastro-pedido-fornecedor/cadastro-pedido-fornecedor').then(m => m.CadastroPedidoFornecedor)
  },
  { path: '', redirectTo: 'lista', pathMatch: 'full' }
];
