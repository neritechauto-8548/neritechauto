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

  it('nao deve enviar X-Tenant-Id no refresh', () => {
    service.refresh({ refresh_token: 'refresh-token' }).subscribe();

    const request = httpMock.expectOne('/auth/refresh');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    expect(request.request.body).toEqual({ refreshToken: 'refresh-token' });
    request.flush({ accessToken: 'novo-token', refreshToken: 'novo-refresh' });
  });
});
