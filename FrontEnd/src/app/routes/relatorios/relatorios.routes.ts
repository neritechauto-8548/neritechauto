import { Routes } from '@angular/router';
import { permissionGuard, planGuard } from '@core';
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
  { path: '', redirectTo: 'relatorio-contas', pathMatch: 'full' },
  {
    path: 'relatorio-vendas',
    component: RelatorioVendas,
    canActivate: [permissionGuard, planGuard],
    data: { title: 'Relatório de Vendas', permissions: ['REL_VENDAS'], minPlan: 3 },
  },
  {
    path: 'relatorio-contas',
    component: RelatorioContas,
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Contas', permissions: ['REL_CONTAS'] },
  },
  {
    path: 'relatorio-estoque',
    component: RelatorioEstoque,
    canActivate: [permissionGuard, planGuard],
    data: { title: 'Relatório de Estoque', permissions: ['REL_ESTOQUE'], minPlan: 3 },
  },
  {
    path: 'relatorio-receitas',
    component: RelatorioReceitas,
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Receitas', permissions: ['REL_CONTAS'] },
  },
  {
    path: 'relatorio-receitas-despesas',
    component: RelatorioReceitasDespesas,
    canActivate: [permissionGuard],
    data: { title: 'Receitas e Despesas', permissions: ['REL_CONTAS'] },
  },
  {
    path: 'relatorio-fluxo-caixa',
    component: RelatorioFluxoCaixa,
    canActivate: [permissionGuard],
    data: { title: 'Fluxo de Caixa', permissions: ['REL_FLUXO_CAIXA'] },
  },
  {
    path: 'relatorio-uso-sistema',
    component: RelatorioUsoSistema,
    canActivate: [permissionGuard],
    data: { title: 'Uso do Sistema', permissions: ['REL_USO_SISTEMA'] },
  },
  {
    path: 'relatorio-despesas',
    component: RelatorioDespesas,
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Despesas', permissions: ['REL_CONTAS'] },
  },
  {
    path: 'relatorio-questionarios',
    component: RelatorioQuestionarios,
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Questionários', permissions: ['REL_QUESTIONARIOS'] },
  },
  {
    path: 'relatorio-aniversariantes',
    component: RelatorioAniversariantes,
    canActivate: [permissionGuard],
    data: { title: 'Aniversariantes', permissions: ['GERAL_USUARIO'] },
  },
  {
    path: 'relatorio-produtos',
    component: RelatorioProdutos,
    canActivate: [permissionGuard],
    data: {
      title: 'Relatório de Produtos',
      permissions: ['REL_ESTOQUE', 'REL_ESTOQUE_SEM_VALOR'],
    },
  },
  {
    path: 'relatorio-clientes',
    component: RelatorioClientes,
    canActivate: [permissionGuard],
    data: { title: 'Relatório de Clientes', permissions: ['GERAL_USUARIO'] },
  },
];
