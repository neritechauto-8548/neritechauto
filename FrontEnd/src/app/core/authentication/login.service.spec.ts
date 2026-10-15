import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoginService } from './login.service';

describe('AUTH-001 LoginService — contrato HTTP real', () => {
  let login: LoginService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    login = TestBed.inject(LoginService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('envia somente e-mail sanitizado e senha, sem metadados falsificáveis', () => {
    let token: any;
    login.login('  Oficina@Exemplo.com  ', 'senha unicode ✓').subscribe(value => token = value);
    const req = http.expectOne('/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'Oficina@Exemplo.com', senha: 'senha unicode ✓' });
    expect(req.request.headers.get('X-Skip-Toast')).toBe('true');
    req.flush({ accessToken: 'signed.jwt', refreshToken: 'refresh', expiresIn: 900000 });
    expect(token.access_token).toBe('signed.jwt');
    expect(token.refresh_token).toBe('refresh');
    expect(token.expires_in).toBe(900);
  });

  it('recusa resposta 200 sem token em vez de aceitar login falso', () => {
    let failed: HttpErrorResponse | undefined;
    let success = false;
    login.login('user@exemplo.com', 'senha').subscribe({
      next: () => success = true,
      error: error => failed = error,
    });
    http.expectOne('/auth/login').flush({ empresaId: 4, usuarioId: 7 });
    expect(success).toBeFalse();
    expect(failed?.status).toBe(502);
  });

  it('repassa 401 de senha incorreta e 429 de rate limit', () => {
    for (const status of [401, 429]) {
      let error: HttpErrorResponse | undefined;
      login.login('user@exemplo.com', 'senha').subscribe({ error: e => error = e });
      http.expectOne('/auth/login').flush({}, { status, statusText: 'Blocked' });
      expect(error?.status).toBe(status);
    }
  });

  it('rejeita refresh sem token e usa expiresIn em milissegundos', () => {
    let renewed: any;
    login.refresh({ refresh_token: 'refresh-abc' }).subscribe(value => renewed = value);
    const req = http.expectOne('/auth/refresh');
    expect(req.request.body).toEqual({ refreshToken: 'refresh-abc' });
    req.flush({ accessToken: 'new.jwt', refreshToken: 'refresh-abc', expiresIn: 600000 });
    expect(renewed.expires_in).toBe(600);
  });
});
