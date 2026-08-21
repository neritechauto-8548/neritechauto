import { Routes } from '@angular/router';
import { CadastroCliente } from './cadastro-cliente/cadastro-cliente';
import { Cliente } from './cliente/cliente';

export const routes: Routes = [
  { path: '', component: Cliente, data: { title: 'Clientes' } },
  { path: 'listar', redirectTo: '', pathMatch: 'full' },
  { path: 'cadastro', component: CadastroCliente, data: { title: 'Novo Cliente' } },
  { path: 'editar/:uuid', component: CadastroCliente, data: { title: 'Editar Cliente' } },
];
