import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

export interface AdminDashboardMetricsResponse {
  totalStripeCustomers: number;
  activeSubscriptions: number;
  trailingSubscriptions: number;
  canceledSubscriptions: number;
  pastDueSubscriptions: number;
  mrr: number;
  totalOficinas: number;
  totalVeiculos: number;
  totalOrdensServico: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdminDashboardService {
  private apiUrl = `${environment.baseUrl}/admin/dashboard`;

  constructor(private http: HttpClient) {}

  getMetrics(): Observable<AdminDashboardMetricsResponse> {
    return this.http.get<AdminDashboardMetricsResponse>(`${this.apiUrl}/metrics`);
  }
}
