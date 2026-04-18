import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LocalStorageService } from '@shared/services/storage.service';

export interface ComunicacaoEnviadaRequest {
  empresaId: number;
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
  // Outros campos de resposta sÃ£o ignorados se nÃ£o precisarmos
}

@Injectable({
  providedIn: 'root',
})
export class ComunicacaoService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private api = `${environment.baseUrl}/v1/comunicacao/envios`;

  get tenantId(): number {
    return this.storage.get('tenantId') || 1;
  }

  enviarComunicacao(data: ComunicacaoEnviadaRequest): Observable<ComunicacaoEnviadaResponse> {
    data.empresaId = this.tenantId;
    return this.http.post<ComunicacaoEnviadaResponse>(this.api, data);
  }
}
