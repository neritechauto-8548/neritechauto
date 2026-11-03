import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface ConfiguracaoSmtp {
  email: string; servidorSmtp: string; portaSmtp: number; usarSsl: boolean; usarTls: boolean;
  assinatura: string; temSenha: boolean;
}
export interface CertificadoProtegido {
  id: number; nomeArquivo: string; email: string; dataValidade: string; dataCadastro: string;
}

@Injectable({ providedIn: 'root' })
export class ConfiguracoesSegurasService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/configuracoes-seguras`;
  obterSmtp(escopo: 'MATRIZ' | 'CRM'): Observable<ConfiguracaoSmtp> { return this.http.get<ConfiguracaoSmtp>(`${this.base}/smtp/${escopo}`); }
  salvarSmtp(escopo: 'MATRIZ' | 'CRM', dados: Omit<ConfiguracaoSmtp, 'temSenha'> & { senha: string }): Observable<ConfiguracaoSmtp> { return this.http.put<ConfiguracaoSmtp>(`${this.base}/smtp/${escopo}`, dados); }
  verificarSmtp(escopo: 'MATRIZ' | 'CRM', dados: Omit<ConfiguracaoSmtp, 'temSenha'> & { senha: string }): Observable<void> { return this.http.post<void>(`${this.base}/smtp/${escopo}/verificar`, dados); }
  listarCertificados(): Observable<CertificadoProtegido[]> { return this.http.get<CertificadoProtegido[]>(`${this.base}/certificados`); }
  criarCertificado(dados: FormData): Observable<void> { return this.http.post<void>(`${this.base}/certificados`, dados); }
}
