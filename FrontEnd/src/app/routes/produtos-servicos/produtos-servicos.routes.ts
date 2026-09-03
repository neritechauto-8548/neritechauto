import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { Estoque } from './estoque/estoque';
import { CadastroProduto } from './cadastro-produto/cadastro-produto';
import { IncluirXml } from './incluir-xml/incluir-xml';
import { Servicos } from './servicos/servicos';

export const routes: Routes = [
  { path: '', redirectTo: 'estoque', pathMatch: 'full' },
  {
    path: 'estoque',
    component: Estoque,
    canActivate: [permissionGuard],
    data: { title: 'Estoque', permissions: ['PS_LISTAR_PROD'] },
  },
  {
    path: 'cadastro-produto',
    component: CadastroProduto,
    canActivate: [permissionGuard],
    data: { title: 'Novo Produto', permissions: ['PS_INC_PROD'] },
  },
  {
    path: 'cadastro-produto/:id',
    component: CadastroProduto,
    canActivate: [permissionGuard],
    data: { title: 'Editar Produto', permissions: ['PS_EDIT_PROD'] },
  },
  {
    path: 'incluir-xml',
    component: IncluirXml,
    canActivate: [permissionGuard],
    data: { title: 'Importar XML', permissions: ['FIN_INC_NFE_COMPRA'] },
  },
  {
    path: 'servicos',
    component: Servicos,
    canActivate: [permissionGuard],
    data: { title: 'Serviços', permissions: ['PS_LISTAR_SERV'] },
  },
];
