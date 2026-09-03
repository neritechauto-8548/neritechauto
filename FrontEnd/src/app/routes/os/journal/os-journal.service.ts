import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ComentarioOrdemServico, ComentarioOrdemServicoCriacao } from './os-journal.models';

@Injectable({ providedIn: 'root' })
export class DiarioOrdemServicoService {
  private readonly http = inject(HttpClient);
  private readonly urlBase = `${environment.baseUrl}/v1/ordens-servico`;

  listar(ordemServicoId: number): Observable<ComentarioOrdemServico[]> {
    return this.http.get<ComentarioOrdemServico[]>(`${this.urlBase}/${ordemServicoId}/comentarios`);
  }

  criar(ordemServicoId: number, requisicao: ComentarioOrdemServicoCriacao): Observable<ComentarioOrdemServico> {
    return this.http.post<ComentarioOrdemServico>(`${this.urlBase}/${ordemServicoId}/comentarios`, requisicao);
  }
}
