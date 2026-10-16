import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { AgendamentosAlertas } from './agendamentos-alertas/agendamentos-alertas';
import { CadastrarAgendamento } from './cadastrar-agendamento/cadastrar-agendamento';
import { CalendarioAgendamento } from './calendario-agendamento/calendario-agendamento';
import { AniversarioAgendamento } from './aniversario/aniversario';

export const routes: Routes = [
  { path: 'agendamentos-alertas', component: AgendamentosAlertas },
  { path: 'cadastro', component: CadastrarAgendamento },
  { path: 'calendario', component: CalendarioAgendamento, canActivate: [permissionGuard], data: { requiredPermissions: 'GERAL_AGENDAMENTO_VISUALIZAR' } },
  { path: 'aniversario', component: AniversarioAgendamento },
];