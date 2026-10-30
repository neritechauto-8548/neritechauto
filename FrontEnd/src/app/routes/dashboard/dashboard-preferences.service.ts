import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '@env/environment';
import { DEFAULT_LAYOUT, WidgetLayout } from './dashboard-widgets';

export interface DashboardLayoutResponse { componentes: WidgetLayout[]; }
@Injectable({providedIn:'root'})
export class DashboardPreferencesService {
  private readonly http=inject(HttpClient);
  private readonly url=(environment.baseUrl || '/api')+'/dashboard/configuracao';

  consultar(): Observable<DashboardLayoutResponse> {
    if(environment.uxPreview) return of({componentes:DEFAULT_LAYOUT.map(x=>({...x}))});
    return this.http.get<DashboardLayoutResponse>(this.url);
  }
  salvar(componentes:WidgetLayout[]):Observable<DashboardLayoutResponse> {
    const payload={componentes:componentes.map(x=>({id:x.id,tipo:x.tipo,colunas:x.colunas}))};
    if(environment.uxPreview)return of(payload);
    return this.http.put<DashboardLayoutResponse>(this.url,payload);
  }
}
