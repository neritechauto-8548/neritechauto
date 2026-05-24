import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from '@shared/services/storage.service';

@Injectable({ providedIn: 'root' })
export class RelatoriosService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);

  /**
   * Gera relatório PDF.
   * @param tipo 'vendas', 'estoque', 'financeiro', 'clientes', 'produtos', 'aniversariantes', 'os/{id}'
   * @param params Filtros adicionais
   */
  gerarRelatorio(tipo: string, params: any = {}) {
    let empresaId = this.storage.get('tenantId');
    
    // Se empresaId for um objeto vazio ou inválido, tenta um fallback
    if (!empresaId || (typeof empresaId === 'object' && Object.keys(empresaId).length === 0)) {
        empresaId = this.storage.get('empresaId');
    }
    
    // Se ainda for um objeto, tenta extrair o ID
    if (empresaId && typeof empresaId === 'object' && empresaId.id) {
        empresaId = empresaId.id;
    }

    // Tratamento especial para OS que tem ID na URL
    const url = tipo.startsWith('os/') ? `/v1/relatorios/${tipo}` : `/v1/relatorios/${tipo}`;

    // Constrói os parâmetros limpando valores inválidos
    const queryParams: any = { ...params };
    if (empresaId && `${empresaId}` !== '[object Object]') {
        queryParams.empresaId = empresaId;
    }

    return this.http.get(url, {
      params: queryParams,
      responseType: 'blob',
    });
  }

  getClientesDados(params: any = {}) {
    return this.http.get<any[]>('/v1/relatorios/clientes-dados', {
      params
    });
  }

  getLogsAlteracoes() {
    return this.http.get<any[]>('/v1/logs-alteracoes');
  }

  downloadBlob(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  abrirBlobEmNovaAba(blob: Blob) {
    const url = window.URL.createObjectURL(blob);
    const aba = window.open(url, '_blank');
    // Libera memória após a aba carregar
    if (aba) {
      aba.addEventListener('load', () => window.URL.revokeObjectURL(url));
    }
  }
}
