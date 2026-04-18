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
    const empresaId = this.storage.get('tenantId');
    // Tratamento especial para OS que tem ID na URL
    const url = tipo.startsWith('os/') ? `/v1/relatorios/${tipo}` : `/v1/relatorios/${tipo}`;

    // Params sempre inclui empresaId
    const queryParams = { ...params, empresaId };

    return this.http.get(url, {
      params: queryParams,
      responseType: 'blob',
    });
  }

  downloadBlob(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
