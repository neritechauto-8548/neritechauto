import { Routes } from '@angular/router';
import { Cliente } from './cliente/cliente';
import { CadastroCliente } from './cadastro-cliente/cadastro-cliente';

export const routes: Routes = [
  { path: '', redirectTo: 'listar', pathMatch: 'full' },
  { path: 'listar', component: Cliente, data: { title: 'Clientes' } },
  { path: 'cadastro', component: CadastroCliente, data: { title: 'Novo Cliente' } },
  { path: 'editar/:uuid', component: CadastroCliente, data: { title: 'Editar Cliente' } },
];
