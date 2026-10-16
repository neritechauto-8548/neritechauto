import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoginService } from '@core/authentication/login.service';

describe('AUTH-002 Contrato de API de recuperação', () => {
  let service: LoginService;
  let http: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(LoginService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('posts to existing backend route, never sends session/tenant or reveals whether email exists', () => {
    let done = false;
    service.recoverPassword('  ALGUEM@example.com ').subscribe(() => done = true);
    const req = http.expectOne('/auth/recover-password');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'ALGUEM@example.com' });
    expect(req.request.headers.get('X-Skip-Toast')).toBe('true');
    req.flush(null);
    expect(done).toBeTrue();
  });
});
