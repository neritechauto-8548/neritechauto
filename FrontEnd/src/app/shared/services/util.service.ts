import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({ providedIn: 'root' })
export class UtilService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  /**
   * Valida matematicamente um CPF ou CNPJ no backend.
   * @param valor O valor do documento (com ou sem máscara)
   * @param tipo 'Física' | 'Jurídica' (ou 'CPF' | 'CNPJ')
   */
  validarDocumento(valor: string, tipo: string): Observable<boolean> {
    const url = `${this.base}/v1/util/documentos/validar`;
    const params = new HttpParams().set('valor', valor).set('tipo', tipo);
    return this.http.get<boolean>(url, { params });
  }
  
  /**
   * Busca endereço por CEP (ViaCEP).
   * @param cep O CEP a ser pesquisado
   */
  buscarCep(cep: string): Observable<any> {
    const cleanCep = (cep || '').replace(/\D/g, '');
    return this.http.get(`https://viacep.com.br/ws/${cleanCep}/json/`);
  }
}
