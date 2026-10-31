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

  it('mostra a exceção real do backend quando o diagnóstico local está habilitado', () => {
    const add = spyOn(messages, 'add');
    const consoleSpy = spyOn(console, 'error');
    fail('/api/clientes', 500, {
      type: 'INTERNAL_ERROR',
      message: 'Falha ao salvar cliente',
      exception: 'org.springframework.dao.DataIntegrityViolationException',
      cause: 'duplicate key value violates unique constraint',
    });
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'HTTP 500 • org.springframework.dao.DataIntegrityViolationException',
      detail: 'Falha ao salvar cliente | Causa: duplicate key value violates unique constraint',
      sticky: true,
    }));
    expect(consoleSpy).toHaveBeenCalled();
  });

  it('não inventa exceções quando o servidor de produção não expõe detalhes', () => {
    const add = spyOn(messages, 'add');
    fail('/api/clientes', 500, { type: 'INTERNAL_ERROR', message: 'Erro interno do servidor.' });
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'HTTP 500 • INTERNAL_ERROR',
      detail: 'Erro interno do servidor.',
    }));
  });

  it('preserves 422 field payload and shows actionable validation feedback', () => {
    const add = spyOn(messages, 'add');
    let original: any;
    http.post('/api/clientes', { nome: '' }).subscribe({ error: value => original = value });
    requests.expectOne('/api/clientes').flush(
      { message: 'internal sql details', errors: [{ field: 'nome', message: 'Obrigatório' }] },
      { status: 422, statusText: 'Unprocessable Entity' }
    );
    expect(original.status).toBe(422);
    expect(original.error.errors[0].field).toBe('nome');
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'HTTP 422 • HttpErrorResponse',
      detail: 'internal sql details | nome: Obrigatório',
    }));
  });

  it('retains the real Spring validation error map for inline form feedback', () => {
    const add = spyOn(messages, 'add');
    let response: any;
    let acceptedAsSuccess = false;
    http.post('/api/clientes', { nome: '' }).subscribe({
      next: () => acceptedAsSuccess = true,
      error: value => response = value,
    });
    requests.expectOne('/api/clientes').flush({
      type: 'VALIDATION',
      message: 'Erro de validação nos campos',
      errors: { nome: 'Campo obrigatório', cpfCnpj: 'Documento inválido' },
      timestamp: '2026-10-15T14:00:00',
    }, { status: 422, statusText: 'Unprocessable Entity' });
    expect(acceptedAsSuccess).toBeFalse();
    expect(response.status).toBe(422);
    expect(response.error.errors.nome).toBe('Campo obrigatório');
    expect(response.error.errors.cpfCnpj).toBe('Documento inválido');
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'HTTP 422 • VALIDATION',
    }));
  });

  it('não substitui a explicação real de conflito (409) por mensagem genérica', () => {
    const add = spyOn(messages, 'add');
    fail('/api/clientes/123', 409, { type: 'BUSINESS_RULE', message: 'CPF já cadastrado nesta empresa' });
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      summary: 'HTTP 409 • BUSINESS_RULE',
      detail: 'CPF já cadastrado nesta empresa',
    }));
  });

  it('preserva login com mensagem genérica para não revelar informação de autenticação', () => {
    const add = spyOn(messages, 'add');
    fail('/api/auth/login', 401, { exception: 'BadCredentialsException', cause: 'Senha inválida' });
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({ detail: 'Credenciais inválidas.' }));
  });

  it('announces offline state and does not navigate to an unrelated error route', () => {
    const navigate = spyOn(router, 'navigateByUrl');
    const add = spyOn(messages, 'add');
    http.get('/api/clientes').subscribe({ error: () => {} });
    requests.expectOne('/api/clientes').error(new ProgressEvent('error'));
    expect(navigate).not.toHaveBeenCalled();
    expect(add).toHaveBeenCalledWith(jasmine.objectContaining({
      detail: 'Sem conexão com o servidor. Verifique sua rede e tente novamente.', sticky: true,
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
