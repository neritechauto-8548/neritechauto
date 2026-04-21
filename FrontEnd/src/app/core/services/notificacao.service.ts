import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface NotificacaoSistemaResponse {
  id: number;
  empresaId: number;
  usuarioDestinatarioId: number;
  tipoNotificacao: 'INFO' | 'SUCESSO' | 'AVISO' | 'ERRO';
  categoria: string;
  titulo: string;
  mensagem: string;
  dadosContexto?: string;
  linkAcao?: string;
  textoBotaoAcao?: string;
  prioridade: 'BAIXA' | 'MEDIA' | 'ALTA' | 'CRITICA';
  exigeConfirmacao: boolean;
  lida: boolean;
  dataLeitura?: string;
  confirmada: boolean;
  dataConfirmacao?: string;
  dataExpiracao?: string;
  icone?: string;
  cor?: string;
  somNotificacao?: string;
  enviarEmail: boolean;
  enviarSms: boolean;
  enviarPush: boolean;
  origemSistema?: string;
  dataCadastro: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class NotificacaoService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/comunicacao/notificacoes`;

  // Listar notificações por usuário
  findByUsuario(usuarioId: number | string, page = 0, size = 10): Observable<Page<NotificacaoSistemaResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'dataCadastro,desc');
    
    return this.http.get<any>(`${this.base}/usuario/${usuarioId}`, { params })
      .pipe(map(resp => resp?.data ?? resp));
  }

  // Marcar como lida (Atualizar o objeto completo como o controller sugere no PUT /{id})
  // Ou se houver um patch específico, mas o controller tem update(id, request)
  marcarComoLida(id: number, notificacao: any): Observable<NotificacaoSistemaResponse> {
    const payload = { ...notificacao, lida: true, dataLeitura: new Date().toISOString() };
    return this.http.put<NotificacaoSistemaResponse>(`${this.base}/${id}`, payload);
  }

  // Excluir notificação
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
