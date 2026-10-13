import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { TokenService } from '@core/authentication';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { MessageService } from 'primeng/api';
import { errorInterceptor } from './error-interceptor';
import { BASE_URL } from './base-url-interceptor';

describe('ErrorInterceptor', () => {
  let http: HttpClient;
  let requests: HttpTestingController;
  let router: Router;
  let messages: MessageService;
  let tokens: TokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(withInterceptors([errorInterceptor])),
      provideHttpClientTesting(),
      provideRouter([]),
      { provide: BASE_URL, useValue: 'https://api.neritechauto.com.br/api' },
      { provide: LocalStorageService, useClass: MemoryStorageService },
      MessageService,
    ] });
    http = TestBed.inject(HttpClient);
    requests = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    messages = TestBed.inject(MessageService);
    tokens = TestBed.inject(TokenService);
  });

  afterEach(() => requests.verify());

  function fail(url: string, status: number, body: object = {}) {
    http.get(url).subscribe({ error: () => {} });
    requests.expectOne(url).flush(body, { status, statusText: 'Error' });
  }

  it('keeps invalid credentials on the login screen', () => {
    const navigate = spyOn(router, 'navigateByUrl');
    const add = spyOn(messages, 'add');
    fail('/api/auth/login', 401, { message: 'Internal detail' });
    expect(navigate).not.toHaveBeenCalled();
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({ detail: 'Credenciais inválidas.' }));
  });

  it('clears the session after a protected request returns 401', () => {
    tokens.set({ access_token: 'token', token_type: 'Bearer' });
    const navigate = spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    fail('/api/usuarios/me', 401);
    expect(tokens.valid()).toBeFalse();
    expect(navigate).toHaveBeenCalledWith(router.createUrlTree(['/auth/login'], { queryParams: { returnUrl: router.url } }));
  });

  it('preserva a sessão diante de erro 401 vindo de serviço externo', () => {
    tokens.set({ access_token: 'token', token_type: 'Bearer' });
    const navigate = spyOn(router, 'navigateByUrl');
    fail('https://terceiros.example.com/dados', 401);
    expect(tokens.valid()).toBeTrue();
    expect(navigate).not.toHaveBeenCalled();
  });

  it('encerra a sessão quando uma API própria absoluta responde 401', () => {
    tokens.set({ access_token: 'token', token_type: 'Bearer' });
    const navigate = spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    fail('https://api.neritechauto.com.br/api/usuarios/me', 401);
    expect(tokens.valid()).toBeFalse();
    expect(navigate).toHaveBeenCalled();
  });

  it('shows resource errors without navigating away from the current page', () => {
    const navigate = spyOn(router, 'navigateByUrl');
    const add = spyOn(messages, 'add');
    fail('/api/clientes/unknown', 404);
    expect(navigate).not.toHaveBeenCalled();
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({ detail: 'O recurso solicitado não foi encontrado.' }));
  });

  it('does not expose a server error body', () => {
    const add = spyOn(messages, 'add');
    fail('/api/clientes', 500, { message: 'stack trace' });
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      detail: 'O servidor não conseguiu concluir a operação. Tente novamente mais tarde.', sticky: true,
    }));
  });

  it('keeps the local toast option out of API headers', () => {
    const add = spyOn(messages, 'add');
    http.get('/api/empresas/1', { headers: { 'X-Skip-Toast': 'true' } }).subscribe({ error: () => {} });
    const request = requests.expectOne('/api/empresas/1');
    expect(request.request.headers.has('X-Skip-Toast')).toBeFalse();
    request.flush({}, { status: 404, statusText: 'Not Found' });
    expect(add).not.toHaveBeenCalled();
  });
});
