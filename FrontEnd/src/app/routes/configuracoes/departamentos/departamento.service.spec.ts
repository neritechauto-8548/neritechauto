import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { DepartamentoService } from './departamento.service';

describe('DepartamentoService ADM-007', () => {
  let service: DepartamentoService; let http: HttpTestingController;
  beforeEach(() => { const storage=jasmine.createSpyObj<LocalStorageService>('LocalStorageService',['get','has']);
    storage.has.and.callFake((k:string)=>k==='empresaId'); storage.get.and.returnValue('12');
    TestBed.configureTestingModule({providers:[provideHttpClient(),provideHttpClientTesting(),{provide:LocalStorageService,useValue:storage}]});
    service=TestBed.inject(DepartamentoService); http=TestBed.inject(HttpTestingController); });
  afterEach(()=>http.verify());
  it('sends tenant filters and optimistic version',()=>{ service.list({page:0,size:20,q:'mec',ativo:true}).subscribe();
    const list=http.expectOne(r=>r.url===`${environment.baseUrl}/v1/departamentos-contabio-empresa`); expect(list.request.params.get('q')).toBe('mec'); expect(list.request.headers.get('X-Tenant-Id')).toBe('12'); list.flush({content:[]});
    service.update(4,{empresaId:12,codigo:'MEC',descricao:'Mecânica',versao:2}).subscribe(); const update=http.expectOne(`${environment.baseUrl}/v1/departamentos-contabio-empresa/4`); expect(update.request.body.versao).toBe(2); update.flush({id:4}); });
  it('uses logical inactivation endpoint',()=>{service.delete(4).subscribe();const req=http.expectOne(`${environment.baseUrl}/v1/departamentos-contabio-empresa/4`);expect(req.request.method).toBe('DELETE');req.flush(null);});
});
