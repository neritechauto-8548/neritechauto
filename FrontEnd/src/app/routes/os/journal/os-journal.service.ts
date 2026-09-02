import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OsComment, OsCommentCreateRequest } from './os-journal.models';

@Injectable({ providedIn: 'root' })
export class OsJournalService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.baseUrl}/v1/ordens-servico`;

  list(osId: number): Observable<OsComment[]> {
    return this.http.get<OsComment[]>(`${this.api}/${osId}/comments`);
  }

  create(osId: number, request: OsCommentCreateRequest): Observable<OsComment> {
    return this.http.post<OsComment>(`${this.api}/${osId}/comments`, request);
  }
}
