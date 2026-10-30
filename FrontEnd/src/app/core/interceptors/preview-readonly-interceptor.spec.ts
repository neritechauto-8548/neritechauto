import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '@env/environment';
import { isPreviewStaticResource, previewReadonlyInterceptor } from './preview-readonly-interceptor';

describe('Master UX demo: backend isolation', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  const previous = environment.uxPreview;

  beforeEach(() => {
    environment.uxPreview = true;
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([previewReadonlyInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    controller.verify();
    environment.uxPreview = previous;
  });

  it('permite apenas arquivos estáticos do próprio frontend via GET', () => {
    expect(isPreviewStaticResource('/data/menu.json', 'GET')).toBeTrue();
    expect(isPreviewStaticResource('i18n/pt-BR.json', 'GET')).toBeTrue();
    expect(isPreviewStaticResource('/api/dashboard', 'GET')).toBeFalse();
    expect(isPreviewStaticResource('/data/menu.json', 'POST')).toBeFalse();
    expect(isPreviewStaticResource('https://api.neritechauto.com.br/api/dashboard', 'GET')).toBeFalse();
    expect(isPreviewStaticResource('https://example.com/data/menu.json', 'GET')).toBeFalse();
  });

  it('não envia requisições GET à API quando a prévia estiver ativa', () => {
    let status = 0;
    http.get('/api/dashboard').subscribe({ error: error => status = error.status });
    expect(status).toBe(503);
    controller.expectNone('/api/dashboard');
  });

  it('não envia gravações nem chamadas autenticadas à API', () => {
    for (const method of ['post', 'patch', 'delete'] as const) {
      let status = 0;
      if (method === 'post') http.post('/api/clientes', { nome:'Teste' }).subscribe({ error: err => status = err.status });
      if (method === 'patch') http.patch('/api/notificacoes/123/ler', {}).subscribe({ error: err => status = err.status });
      if (method === 'delete') http.delete('/api/clientes/123').subscribe({ error: err => status = err.status });
      expect(status).toBe(503);
    }
    controller.expectNone('/api/clientes');
    controller.expectNone('/api/notificacoes/123/ler');
    controller.expectNone('/api/clientes/123');
  });

  it('carrega menu estático local e não cria sessão ou token', () => {
    let menu: unknown;
    http.get('/data/menu.json').subscribe(value => menu = value);
    controller.expectOne('/data/menu.json').flush({ menu: [{ name:'Início', route:'home' }] });
    expect(menu).toEqual({ menu: [{ name:'Início', route:'home' }] });
  });
});
