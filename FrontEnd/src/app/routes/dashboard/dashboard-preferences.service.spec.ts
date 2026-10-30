import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DashboardPreferencesService } from './dashboard-preferences.service';
import { DEFAULT_LAYOUT } from './dashboard-widgets';
import { environment } from '@env/environment';

describe('HOME — configuração pessoal persistida no backend',()=>{
  let service:DashboardPreferencesService;
  let http:HttpTestingController;
  const url=(environment.baseUrl||'/api')+'/dashboard/configuracao';
  beforeEach(()=>{
    TestBed.configureTestingModule({providers:[provideHttpClient(),provideHttpClientTesting()]});
    service=TestBed.inject(DashboardPreferencesService);
    http=TestBed.inject(HttpTestingController);
  });
  afterEach(()=>http.verify());

  it('consulta sem aceitar empresaId ou usuarioId do navegador',()=>{
    if(environment.uxPreview){pending('somente modo de preview');return;}
    let values:any;
    service.consultar().subscribe(res=>values=res);
    const req=http.expectOne(url);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.has('empresaId')).toBeFalse();
    expect(req.request.params.has('usuarioId')).toBeFalse();
    req.flush({componentes:DEFAULT_LAYOUT});
    expect(values.componentes.length).toBe(2);
  });

  it('salva ordem, tipo e quantidade de colunas em uma única operação',()=>{
    if(environment.uxPreview){pending('somente modo de preview');return;}
    const input=[{id:'ordens',tipo:'grafico' as const,colunas:2}];
    let saved:any;
    service.salvar(input).subscribe(res=>saved=res);
    const req=http.expectOne(url);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({componentes:input});
    expect(req.request.body).not.toEqual(jasmine.objectContaining({empresaId:1}));
    req.flush({componentes:input});
    expect(saved.componentes).toEqual(input);
  });

  it('propaga erro de conflito do backend, sem confirmar gravação fictícia',()=>{
    if(environment.uxPreview){pending('somente modo de preview');return;}
    let status=0;
    service.salvar(DEFAULT_LAYOUT).subscribe({error:e=>status=e.status});
    http.expectOne(url).flush({message:'Conflito'},
      {status:409,statusText:'Conflict'});
    expect(status).toBe(409);
  });
});
