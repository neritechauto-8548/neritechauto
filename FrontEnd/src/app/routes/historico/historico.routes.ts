import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
export const routes: Routes = [{ path:'', canActivate:[permissionGuard], data:{title:'Histórico Operacional',permissions:['GERAL_USUARIO']}, loadComponent:()=>import('./historico-central').then(m=>m.HistoricoCentral) }];
