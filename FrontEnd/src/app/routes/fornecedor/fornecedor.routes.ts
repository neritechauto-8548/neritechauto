import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
      path: 'lista',
      canActivate: [permissionGuard],
      data: { title: 'Fornecedores', requiredPermissions: ['PS_LISTAR_FORN','FORNECEDOR_VISUALIZAR','GERAL_CONFIG_SISTEMA'], permissionMode: 'any' },
      loadComponent: () => import('./fornecedor-referencia/fornecedor-referencia').then(m => m.FornecedorReferenciaComponent)
  },
  {
      path: 'novo',
      canActivate: [permissionGuard],
      data: { title: 'Fornecedores', requiredPermissions: ['PS_INC_FORN','FORNECEDOR_CRIAR','GERAL_CONFIG_SISTEMA'], permissionMode: 'any' },
      loadComponent: () => import('./fornecedor-referencia/fornecedor-referencia').then(m => m.FornecedorReferenciaComponent)
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
