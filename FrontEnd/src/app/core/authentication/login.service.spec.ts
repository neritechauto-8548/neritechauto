import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoginService } from './login.service';

describe('LoginService tenant security', () => {
  let service: LoginService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(LoginService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('nao deve enviar X-Tenant-Id no login', () => {
    service.login('usuario@oficina.com.br', 'senha').subscribe();

    const request = httpMock.expectOne('/auth/login');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    expect(request.request.body).toEqual({ email: 'usuario@oficina.com.br', senha: 'senha' });
    request.flush({ accessToken: 'token', refreshToken: 'refresh' });
  });

  it('envia recuperação anti-enumeração sem autoridade de tenant', () => {
    service.recoverPassword('usuario@oficina.com.br').subscribe();

    const request = httpMock.expectOne('/auth/recover-password');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    expect(request.request.body).toEqual({ email: 'usuario@oficina.com.br' });
    expect(request.request.body['tenantId']).toBeUndefined();
    expect(request.request.body['empresaId']).toBeUndefined();
    request.flush({});
  });

  it('redefine a senha apenas com o token opaco e a nova senha', () => {
    service.resetPassword({ token: 'opaque-token', novaSenha: 'nova-senha' }).subscribe();

    const request = httpMock.expectOne('/auth/reset-password');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    expect(request.request.body).toEqual({ token: 'opaque-token', novaSenha: 'nova-senha' });
    expect(request.request.body['tenantId']).toBeUndefined();
    expect(request.request.body['empresaId']).toBeUndefined();
    request.flush({});
  });

  it('nao deve enviar X-Tenant-Id no refresh', () => {
    service.refresh({ refresh_token: 'refresh-token' }).subscribe();

    const request = httpMock.expectOne('/auth/refresh');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    expect(request.request.body).toEqual({ refreshToken: 'refresh-token' });
    request.flush({ accessToken: 'novo-token', refreshToken: 'novo-refresh' });
  });
});
