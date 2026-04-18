import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/admin`;

  carregarFipe(): Observable<{ status: string; mensagem: string }> {
    return this.http.post<{ status: string; mensagem: string }>(
      `${this.base}/fipe/carregar`,
      {}
    );
  }
}
