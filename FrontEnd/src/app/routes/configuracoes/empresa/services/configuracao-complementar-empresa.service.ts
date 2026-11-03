import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface ConfiguracaoComplementarEmpresa {
  id: number;
  empresaId: number;
  secao: string;
  dados: Record<string, any>;
  versao: number;
  atualizadoEm: string;
}

@Injectable({ providedIn: 'root' })
export class ConfiguracaoComplementarEmpresaService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/v1/configuracoes-complementares-empresa';

  listar(): Observable<ConfiguracaoComplementarEmpresa[]> {
    return this.http.get<ConfiguracaoComplementarEmpresa[]>(this.base);
  }

  salvar(secao: string, dados: Record<string, any>, versao: number | null): Observable<ConfiguracaoComplementarEmpresa> {
    return this.http.put<ConfiguracaoComplementarEmpresa>(`${this.base}/${secao}`, { dados, versao });
  }
}
