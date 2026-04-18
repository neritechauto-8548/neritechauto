import { Routes } from '@angular/router';
import { RelatorioVendas } from './relatorio-vendas/relatorio-vendas';
import { RelatorioContas } from './relatorio-contas/relatorio-contas';
import { RelatorioEstoque } from './relatorio-estoque/relatorio-estoque';
import { RelatorioReceitas } from './relatorio-receitas/relatorio-receitas';
import { RelatorioReceitasDespesas } from './relatorio-receitas-despesas/relatorio-receitas-despesas';
import { RelatorioFluxoCaixa } from './relatorio-fluxo-caixa/relatorio-fluxo-caixa';
import { RelatorioUsoSistema } from './relatorio-uso-sistema/relatorio-uso-sistema';
import { RelatorioDespesas } from './relatorio-despesas/relatorio-despesas';
import { RelatorioQuestionarios } from './relatorio-questionarios/relatorio-questionarios';
import { RelatorioAniversariantes } from './relatorio-aniversariantes/relatorio-aniversariantes';
import { RelatorioProdutos } from './relatorio-produtos/relatorio-produtos';
import { RelatorioClientes } from './relatorio-clientes/relatorio-clientes';

export const routes: Routes = [
  { path: '', redirectTo: 'relatorio-vendas', pathMatch: 'full' },
  { path: 'relatorio-vendas', component: RelatorioVendas },
  { path: 'relatorio-contas', component: RelatorioContas },
  { path: 'relatorio-estoque', component: RelatorioEstoque },
  { path: 'relatorio-receitas', component: RelatorioReceitas },
  { path: 'relatorio-receitas-despesas', component: RelatorioReceitasDespesas },
  { path: 'relatorio-fluxo-caixa', component: RelatorioFluxoCaixa },
  { path: 'relatorio-uso-sistema', component: RelatorioUsoSistema },
  { path: 'relatorio-despesas', component: RelatorioDespesas },
  { path: 'relatorio-questionarios', component: RelatorioQuestionarios },
  { path: 'relatorio-aniversariantes', component: RelatorioAniversariantes },
  { path: 'relatorio-produtos', component: RelatorioProdutos },
  { path: 'relatorio-clientes', component: RelatorioClientes },
];