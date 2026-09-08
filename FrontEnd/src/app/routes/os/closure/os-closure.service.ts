import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OsClosureCommandResult, OsClosureReviewModel } from './os-closure.models';

@Injectable({ providedIn: 'root' })
export class OsClosureService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/ordens-servico`;

  review(osId: number): Observable<OsClosureReviewModel> {
    return this.http
      .get<OsClosureReviewModel>(`${this.base}/${osId}/closure-review`)
      .pipe(map((response: any) => response?.data ?? response));
  }

  validate(osId: number): Observable<OsClosureReviewModel> {
    return this.http
      .post<OsClosureReviewModel>(`${this.base}/${osId}/closure-review/validate`, null)
      .pipe(map((response: any) => response?.data ?? response));
  }

  completeOperationally(
    osId: number,
    aggregateVersion: number,
    idempotencyKey: string
  ): Observable<OsClosureCommandResult> {
    const headers = new HttpHeaders({
      'If-Match': `"${aggregateVersion}"`,
      'Idempotency-Key': idempotencyKey,
    });
    return this.http
      .post<OsClosureCommandResult>(`${this.base}/${osId}/complete-operationally`, null, { headers })
      .pipe(map((response: any) => response?.data ?? response));
  }
}
