import { HttpClient, HttpErrorResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { apiInterceptor } from './api-interceptor';
import { errorInterceptor } from './error-interceptor';
import { BASE_URL } from './base-url-interceptor';

describe('ApiResponse contract in the shared interceptors', () => {
  let http: HttpClient;
  let requests: HttpTestingController;
  let messages: MessageService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([errorInterceptor, apiInterceptor])),
        provideHttpClientTesting(),
        provideRouter([]),
        { provide: BASE_URL, useValue: '/api' },
        { provide: LocalStorageService, useClass: MemoryStorageService },
        MessageService,
      ],
    });
    http = TestBed.inject(HttpClient);
    requests = TestBed.inject(HttpTestingController);
    messages = TestBed.inject(MessageService);
    router = TestBed.inject(Router);
  });

  afterEach(() => requests.verify());

  it('unwraps a successful backend ApiResponse without inventing a toast', () => {
    const add = spyOn(messages, 'add');
    let data: any;
    http.get('/api/clientes').subscribe(value => data = value);
    requests.expectOne('/api/clientes').flush({
      data: { id: 1 }, code: null, errors: null, timestamp: '2026-10-15T09:00:00',
    });
    expect(data).toEqual({ id: 1 });
    expect(add).not.toHaveBeenCalled();
  });

  it('maps application failure envelopes to typed HTTP errors and safe feedback', () => {
    const add = spyOn(messages, 'add');
    const navigate = spyOn(router, 'navigateByUrl');
    let succeeded = false;
    let received: HttpErrorResponse | undefined;
    http.post('/api/clientes', { email: 'invalido' }).subscribe({
      next: () => succeeded = true,
      error: error => received = error,
    });
    requests.expectOne('/api/clientes').flush({
      code: 'BUSINESS_RULE', data: null, errors: ['Database constraint SQL detail'],
      timestamp: '2026-10-15T09:00:00',
    });
    expect(succeeded).toBeFalse();
    expect(received instanceof HttpErrorResponse).toBeTrue();
    expect(received?.status).toBe(400);
    expect((received?.error as any).errors).toEqual(['Database constraint SQL detail']);
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'Solicitação inválida',
      detail: 'Confira os dados da solicitação e tente novamente.',
    }));
    expect(navigate).not.toHaveBeenCalled();
  });

  it('rejects failed envelopes when the optional code is null', () => {
    let error: HttpErrorResponse | undefined;
    http.post('/api/ordens', {}).subscribe({ error: value => error = value });
    requests.expectOne('/api/ordens').flush({
      code: null, data: null, errors: ['Regra de negócio'],
      timestamp: '2026-10-15T09:00:00',
    });
    expect(error?.status).toBe(400);
  });

  it('preserves regular domain objects whose data field is not an API envelope', () => {
    let body: any;
    http.get('/api/relatorios').subscribe(value => body = value);
    const domainObject = { data: [1, 2], total: 2 };
    requests.expectOne('/api/relatorios').flush(domainObject);
    expect(body).toEqual(domainObject);
  });

  it('does not emit a global toast when the caller owns validation feedback', () => {
    const add = spyOn(messages, 'add');
    http.get('/api/config', { headers: { 'X-Skip-Toast': 'true' } })
      .subscribe({ error: () => {} });
    const req = requests.expectOne('/api/config');
    expect(req.request.headers.has('X-Skip-Toast')).toBeFalse();
    req.flush({ code: 'VALIDATION', errors: ['Missing field'], data: null });
    expect(add).not.toHaveBeenCalled();
  });
});
