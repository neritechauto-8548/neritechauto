import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';

export interface ComunicacaoEnviada {
  id: number;
  empresaId: number;
  tipoComunicacao: string;
  destinatarioTipo: string;
  destinatarioId: number;
  destinatarioNome: string;
  destinatarioContato: string;
  assunto?: string;
  dataEnvio?: string;
  dataCadastro?: string;
  status: string;
  erroEnvio?: string;
}

export interface PaginaComunicacoes {
  content: ComunicacaoEnviada[];
  totalElements: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class ComunicacaoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly url = environment.baseUrl + '/v1/comunicacao/envios';

  listar(page: number, size: number): Observable<PaginaComunicacoes> {
    const empresaId = empresaIdAtual(this.storage);
    if (!empresaId) {
      return throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente.'));
    }
    const params = new HttpParams()
      .set('empresaId', String(empresaId))
      .set('page', String(page))
      .set('size', String(size))
      .set('sort', 'dataCadastro,desc');
    return this.http.get<PaginaComunicacoes>(this.url, { params });
  }
}
