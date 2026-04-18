import { Routes } from '@angular/router';
import { Estoque } from './estoque/estoque';
import { CadastroProduto } from './cadastro-produto/cadastro-produto';
import { IncluirXml } from './incluir-xml/incluir-xml';
import { Servicos } from './servicos/servicos';

export const routes: Routes = [
  { path: '', redirectTo: 'estoque', pathMatch: 'full' },
  { path: 'estoque', component: Estoque, data: { title: 'Estoque' } },
  { path: 'cadastro-produto', component: CadastroProduto, data: { title: 'Novo Produto' } },
  { path: 'cadastro-produto/:id', component: CadastroProduto, data: { title: 'Editar Produto' } },
  { path: 'incluir-xml', component: IncluirXml, data: { title: 'Importar XML' } },
  { path: 'servicos', component: Servicos, data: { title: 'Serviços' } },
];
