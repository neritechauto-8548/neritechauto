import { Routes } from '@angular/router';
import { AgendamentosAlertas } from './agendamentos-alertas/agendamentos-alertas';
import { CadastrarAgendamento } from './cadastrar-agendamento/cadastrar-agendamento';
import { CalendarioAgendamento } from './calendario-agendamento/calendario-agendamento';
import { AniversarioAgendamento } from './aniversario/aniversario';

export const routes: Routes = [
  { path: 'agendamentos-alertas', component: AgendamentosAlertas },
  { path: 'cadastro', component: CadastrarAgendamento },
  { path: 'calendario', component: CalendarioAgendamento },
  { path: 'aniversario', component: AniversarioAgendamento },
];