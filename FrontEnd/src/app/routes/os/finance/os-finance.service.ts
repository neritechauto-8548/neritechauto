import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OsInvoiceSummary, OsPaymentsPage } from './os-finance.models';

@Injectable({ providedIn: 'root' })
export class OsFinanceService {
  private readonly http = inject(HttpClient);
  private readonly invoicesUrl = `${environment.baseUrl}/v1/financeiro/faturas`;
  private readonly paymentsUrl = `${environment.baseUrl}/v1/financeiro/pagamentos`;

  getInvoiceByOrder(osId: number): Observable<OsInvoiceSummary | null> {
    return this.http.get<OsInvoiceSummary | null>(`${this.invoicesUrl}/ordem-servico/${osId}`);
  }

  listPaymentsByOrder(osId: number): Observable<OsPaymentsPage> {
    const params = new HttpParams().set('page', '0').set('size', '20').set('sort', 'dataPagamento,desc');
    return this.http.get<OsPaymentsPage>(`${this.paymentsUrl}/ordem-servico/${osId}`, { params });
  }
}
