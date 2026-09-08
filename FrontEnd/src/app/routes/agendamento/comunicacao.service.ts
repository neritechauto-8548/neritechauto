import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ComunicacaoEnviadaRequest {
  templateId?: number;
  campanhaId?: number;
  tipoComunicacao: string; // EMAIL, SMS, WHATSAPP, PUSH_NOTIFICATION, LIGACAO
  destinatarioTipo: string; // CLIENTE, FUNCIONARIO, FORNECEDOR, OUTROS
  destinatarioId: number;
  destinatarioNome: string;
  destinatarioContato: string;
  assunto?: string;
  conteudo: string;
  anexos?: string;
  agendadaPara?: string; // LocalDateTime string ISO
  status?: string; // AGENDADA, ENVIADA, LIDA, CLICADA, FALHOU, ENTREGUE, CANCELADA
  automatica?: boolean;
  ordemServicoId?: number;
  agendamentoId?: number;
  faturaId?: number;
  usuarioEnvio?: number;
}

export interface ComunicacaoEnviadaResponse {
  id: number;
  // Outros campos de resposta são ignorados quando não forem necessários
}

@Injectable({
  providedIn: 'root',
})
export class ComunicacaoService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.baseUrl}/v1/comunicacao/envios`;

  enviarComunicacao(data: ComunicacaoEnviadaRequest): Observable<ComunicacaoEnviadaResponse> {
    // A empresa é resolvida pela sessão autenticada no backend. O payload não concede autoridade de tenant.
    return this.http.post<ComunicacaoEnviadaResponse>(this.api, data);
  }
}
