import { Routes } from '@angular/router';
import { PdvComponent } from './pdv';
import { ListarVendasPDV } from './listar-vendas/listar-vendas';
import { VendaBalcaoPDV } from './venda-balcao/venda-balcao';

export const routes: Routes = [
  { path: '', component: PdvComponent },
  { path: 'listar-vendas', component: ListarVendasPDV },
  { path: 'venda-balcao', component: VendaBalcaoPDV },
];
