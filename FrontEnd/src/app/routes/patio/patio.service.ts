import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '@env/environment';
import { map } from 'rxjs';
import { ItemOSServicoResponse } from '../os/models/os.models';

export interface PatioColumn {
  id: number;
  nome: string;
  codigo: string;
  cor: string | null;
}

export interface PatioCard {
  osId: number;
  numeroOS: string;
  versao: number;
  statusId: number | null;
  statusNome: string | null;
  statusCodigo: string | null;
  statusCor: string | null;
  placa: string | null;
  modelo: string | null;
  cliente: string | null;
  responsavel: string | null;
  responsavelId: number | null;
  boxId: number | null;
  boxNome: string | null;
  prioridade: string | null;
  previsaoEntrega: string | null;
  atrasado: boolean;
  servicosTotal: number;
  servicosConcluidos: number;
  minutosPrevistos: number;
  minutosTrabalhados: number;
}

export interface PatioBoard {
  cards: PatioCard[];
  columns: PatioColumn[];
  count: number;
  page: number;
  size: number;
  updatedAt: string;
}

export interface PatioTvBoard {
  stages: { etapa: string; total: number; atrasadas: number }[];
  count: number;
  updatedAt: string;
}

export interface PatioResource {
  id: number;
  nome: string;
  tipo: string;
  capacidade: number;
  localizacao: string | null;
  habilitado: boolean;
}

export interface PatioReservation {
  id: number;
  recursoId: number;
  osId: number;
  numeroOS: string;
  inicio: string;
  fim: string;
  estado: 'RESERVADO' | 'OCUPADO' | 'LIBERADO' | 'CANCELADO';
  versao: number;
}

export interface PatioReservationBoard {
  reservas: PatioReservation[];
  legadoIndeterminado: boolean;
  data: string;
}

export interface PatioAdjustment {
  id: number;
  itemId: number;
  tempoId: number;
  deltaMinutos: number;
  motivo: string;
  usuarioId: number;
  criadoEm: string;
}

export interface PatioAlert {
  id: number;
  osId: number;
  numeroOS: string;
  responsavel: string | null;
  tipo: string;
  severidade: 'BAIXA' | 'MEDIA' | 'ALTA' | 'CRITICA';
  estado: 'OPEN' | 'ACKNOWLEDGED' | 'SNOOZED' | 'RESOLVED';
  motivo: string;
  detectadoEm: string;
  atualizadoEm: string;
  adiadoAte: string | null;
  versao: number;
}

export interface PatioAlertBoard {
  alertas: PatioAlert[];
  count: number;
  page: number;
  size: number;
  updatedAt: string;
}

export interface PatioAlertEvent {
  id: number;
  estadoAntes: string | null;
  estadoDepois: string;
  usuarioId: number | null;
  motivo: string;
  criadoEm: string;
}

export type PatioItem = ItemOSServicoResponse & { versao: number };
export interface PatioTeamMember { id: number; nome: string; }
export interface PatioTimer {
  id: number;
  itemId: number;
  funcionarioId: number;
  estado: 'EM_EXECUCAO' | 'PAUSADO' | 'FINALIZADO';
  segundosAtivos: number;
  inicio: string;
  fim: string | null;
  atualizadoEm: string;
}

export interface PatioFilters {
  search: string;
  statusId: number | null;
  responsavelId: number | null;
  prioridade: string;
  atrasado: boolean | null;
}

@Injectable({ providedIn: 'root' })
export class PatioService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/patio`;

  board(filters: PatioFilters, page: number, size: number) {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filters.search.trim()) params = params.set('search', filters.search.trim());
    if (filters.statusId != null) params = params.set('statusId', filters.statusId);
    if (filters.responsavelId != null) params = params.set('responsavelId', filters.responsavelId);
    if (filters.prioridade) params = params.set('prioridade', filters.prioridade);
    if (filters.atrasado != null) params = params.set('atrasado', filters.atrasado);
    return this.http.get<{ data: PatioBoard }>(this.base, { params }).pipe(map(response => response.data));
  }

  move(card: PatioCard, statusId: number) {
    return this.http.put<{ data: { osId: number; statusId: number; statusCodigo: string; versao: number } }>(
      `${this.base}/${card.osId}/etapa`, { statusId, versao: card.versao }
    ).pipe(map(response => response.data));
  }

  services(osId: number) {
    return this.http.get<{ data: PatioItem[] }>(`${environment.baseUrl}/v1/itens-os-servicos/ordem-servico/${osId}`)
      .pipe(map(response => response.data));
  }

  team() {
    return this.http.get<{ data: PatioTeamMember[] }>(`${this.base}/equipe`).pipe(map(response => response.data));
  }

  assign(item: PatioItem, funcionarioId: number) {
    return this.http.put<{ data: { itemId: number; funcionarioId: number; versao: number } }>(
      `${this.base}/itens/${item.id}/responsavel`, { funcionarioId, versao: item.versao }
    ).pipe(map(response => response.data));
  }

  myEmployee() {
    return this.http.get<{ data: { id: number } }>(`${this.base}/me/funcionario`,
      { headers: new HttpHeaders({ 'X-Skip-Toast': 'true' }) })
      .pipe(map(response => response.data.id));
  }

  timers(itemId: number) {
    return this.http.get<{ data: PatioTimer[] }>(`${this.base}/itens/${itemId}/apontamentos`)
      .pipe(map(response => response.data));
  }

  timerCommand(itemId: number, action: 'start' | 'pause' | 'resume' | 'finish', idempotencyKey: string) {
    return this.http.post<{ data: PatioTimer }>(`${this.base}/itens/${itemId}/timer/${action}`, { idempotencyKey })
      .pipe(map(response => response.data));
  }

  tv() {
    return this.http.get<{ data: PatioTvBoard }>(`${this.base}/tv`).pipe(map(response => response.data));
  }

  resources() {
    return this.http.get<{ data: PatioResource[] }>(`${this.base}/recursos`).pipe(map(response => response.data));
  }

  reservations(day: string) {
    return this.http.get<{ data: PatioReservationBoard }>(`${this.base}/recursos/reservas`,
      { params: new HttpParams().set('data', day) }).pipe(map(response => response.data));
  }

  reserve(resourceId: number, osId: number, inicio: string, fim: string, idempotencyKey: string) {
    return this.http.post<{ data: PatioReservation }>(`${this.base}/recursos/${resourceId}/reservas`,
      { osId, inicio, fim, idempotencyKey }).pipe(map(response => response.data));
  }

  reservationAction(reservation: PatioReservation, action: 'occupy' | 'release' | 'cancel') {
    return this.http.put<{ data: PatioReservation }>(`${this.base}/recursos/reservas/${reservation.id}/${action}`,
      { versao: reservation.versao }).pipe(map(response => response.data));
  }

  adjustments(itemId: number) {
    return this.http.get<{ data: PatioAdjustment[] }>(`${this.base}/itens/${itemId}/ajustes`)
      .pipe(map(response => response.data));
  }

  adjust(item: PatioItem, timeId: number, deltaMinutos: number, motivo: string, idempotencyKey: string) {
    return this.http.post<{ data: PatioAdjustment }>(
      `${this.base}/itens/${item.id}/apontamentos/${timeId}/ajustes`,
      { deltaMinutos, motivo, versao: item.versao, idempotencyKey }
    ).pipe(map(response => response.data));
  }

  alerts(page: number, size: number) {
    return this.http.get<{ data: PatioAlertBoard }>(`${this.base}/alertas`,
      { params: new HttpParams().set('page', page).set('size', size) }).pipe(map(response => response.data));
  }

  alertHistory(alertId: number) {
    return this.http.get<{ data: PatioAlertEvent[] }>(`${this.base}/alertas/${alertId}/historico`)
      .pipe(map(response => response.data));
  }

  alertAction(alert: PatioAlert, action: 'acknowledge' | 'snooze' | 'resolve',
              motivo?: string, adiadoAte?: string) {
    return this.http.put<{ data: PatioAlert }>(`${this.base}/alertas/${alert.id}/${action}`,
      { versao: alert.versao, motivo, adiadoAte }).pipe(map(response => response.data));
  }
}
