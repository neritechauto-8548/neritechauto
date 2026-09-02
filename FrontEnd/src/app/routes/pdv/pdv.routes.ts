import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { PdvComponent } from './pdv';
import { ListarVendasPDV } from './listar-vendas/listar-vendas';
import { VendaBalcaoPDV } from './venda-balcao/venda-balcao';

export const routes: Routes = [
  {
    path: '',
    component: PdvComponent,
    canActivate: [permissionGuard],
    data: {
      title: 'PDV',
      permissions: ['PDV_LISTAR_VENDAS', 'PDV_REALIZAR_VENDAS'],
    },
  },
  {
    path: 'listar-vendas',
    component: ListarVendasPDV,
    canActivate: [permissionGuard],
    data: { title: 'Vendas', permissions: ['PDV_LISTAR_VENDAS'] },
  },
  {
    path: 'venda-balcao',
    component: VendaBalcaoPDV,
    canActivate: [permissionGuard],
    data: { title: 'Venda Balcão', permissions: ['PDV_REALIZAR_VENDAS'] },
  },
];
