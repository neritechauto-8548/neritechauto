import { Routes } from '@angular/router';
import { Veiculo } from './veiculo/veiculo';
import { CadastroVeiculo } from './cadastro-veiculo/cadastro-veiculo';

export const routes: Routes = [
  { path: '', component: Veiculo },
  { path: 'cadastro', component: CadastroVeiculo },
  { path: 'editar/:id', component: CadastroVeiculo },
];
