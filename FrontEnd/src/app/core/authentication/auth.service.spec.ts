import { HttpRequest, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AuthService, LoginService, TokenService, User } from '@core/authentication';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { Observable, skip } from 'rxjs';
import { SettingsService } from '../bootstrap/settings.service';

describe('AuthService', () => {
  let authService: AuthService;
  let loginService: LoginService;
  let tokenService: TokenService;
  let httpMock: HttpTestingController;
  let user$: Observable<User>;
  const email = 'foo@bar.com';
  const token = { access_token: 'token', token_type: 'bearer' };
  const user = { id: 1, email };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: LocalStorageService, useClass: MemoryStorageService },
        { provide: SettingsService, useValue: jasmine.createSpyObj('SettingsService', ['setOptions', 'setTheme', 'setDirection']) },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
      ],
    });
    loginService = TestBed.inject(LoginService);
    authService = TestBed.inject(AuthService);
    tokenService = TestBed.inject(TokenService);
    httpMock = TestBed.inject(HttpTestingController);

    user$ = authService.user();
    authService.change().subscribe(user => {
      expect(user).toBeInstanceOf(Object);
    });
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.removeItem('tenantId');
    localStorage.removeItem('empresaId');
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  it('should log in failed', () => {
    authService.login(email, 'password', false).subscribe(isLogin => expect(isLogin).toBeFalse());
    httpMock.expectOne('/auth/login').flush({});

    expect(authService.check()).toBeFalse();
  });

  it('should log in successful and get user info', () => {
    user$.pipe(skip(1)).subscribe(currentUser => expect(currentUser.id).toEqual(user.id));
    authService.login(email, 'password', false).subscribe(isLogin => expect(isLogin).toBeTrue());
    httpMock.expectOne('/auth/login').flush(token);

    expect(authService.check()).toBeTrue();
    httpMock.expectOne('/usuarios/me').flush(user);
  });

  it('should log out failed when user is not login', () => {
    spyOn(loginService, 'logout').and.callThrough();
    expect(authService.check()).toBeFalse();

    authService.logout().subscribe();
    httpMock.expectOne('/auth/logout').flush({});

    expect(authService.check()).toBeFalse();
    expect(loginService.logout).toHaveBeenCalled();
  });

  it('should log out successful when user is login', () => {
    tokenService.set(token);
    expect(authService.check()).toBeTrue();
    httpMock.expectOne('/usuarios/me').flush(user);

    user$.pipe(skip(1)).subscribe(currentUser => expect(currentUser.id).toBeUndefined());
    authService.logout().subscribe();
    httpMock.expectOne('/auth/logout').flush({});

    expect(authService.check()).toBeFalse();
  });

  it('limpa sessão local se a revogação no servidor falhar', () => {
    const storage = TestBed.inject(LocalStorageService);
    tokenService.set(token);
    httpMock.expectOne('/usuarios/me').flush({ id: 1, empresaId: 1 });
    storage.set('tenantId', '1');
    storage.set('empresaId', '1');
    localStorage.setItem('tenantId', '1');
    localStorage.setItem('empresaId', '1');

    let logoutFailed = false;
    authService.logout().subscribe({ error: () => logoutFailed = true });
    httpMock.expectOne('/auth/logout').flush({}, { status: 503, statusText: 'Service Unavailable' });

    expect(logoutFailed).toBeTrue();
    expect(authService.check()).toBeFalse();
    expect(storage.has('tenantId')).toBeFalse();
    expect(storage.has('empresaId')).toBeFalse();
    expect(localStorage.getItem('tenantId')).toBeNull();
    expect(localStorage.getItem('empresaId')).toBeNull();
    let currentUser: User = { id: 99 };
    authService.user().subscribe(value => currentUser = value);
    expect(currentUser.id).toBeUndefined();
  });

  it('aguarda o perfil além de um segundo sem descartar a sessão válida', fakeAsync(() => {
    tokenService.set(token);
    const profile = httpMock.expectOne('/usuarios/me');
    tick(1500);
    expect(authService.check()).toBeTrue();
    let received: User = {};
    authService.user().subscribe(value => received = value);
    profile.flush({ id: 33, empresaId: 7 });
    expect(received.id).toBe(33);
    expect(TestBed.inject(LocalStorageService).get('tenantId')).toBe('7');
    tokenService.ngOnDestroy();
  }));

  it('should refresh token when access_token is valid', fakeAsync(() => {
    tokenService.set(Object.assign({ expires_in: 5 }, token));
    expect(authService.check()).toBeTrue();
    httpMock.expectOne('/usuarios/me').flush(user);
    const match = (req: HttpRequest<any>) => req.url === '/auth/refresh' && !req.body.refreshToken;

    tick(4000);
    expect(authService.check()).toBeTrue();
    httpMock.match(match)[0].flush(token);

    expect(authService.check()).toBeTrue();
    httpMock.expectNone('/usuarios/me');
    tokenService.ngOnDestroy();
  }));

  it('should refresh token when access_token is invalid and refresh_token is valid', fakeAsync(() => {
    tokenService.set(Object.assign({ expires_in: 5, refresh_token: 'foo' }, token));
    const match = (req: HttpRequest<any>) =>
      req.url === '/auth/refresh' && req.body.refreshToken === 'foo';

    expect(authService.check()).toBeTrue();
    httpMock.expectOne('/usuarios/me').flush(user);
    tick(10000);
    expect(authService.check()).toBeFalse();
    httpMock.match(match)[0].flush(token);

    expect(authService.check()).toBeTrue();
    httpMock.expectNone('/usuarios/me');
    tokenService.ngOnDestroy();
  }));

  it('it should clear token when access_token is invalid and refresh token response is 401', fakeAsync(() => {
    spyOn(tokenService, 'set').and.callThrough();
    tokenService.set(Object.assign({ expires_in: 5, refresh_token: 'foo' }, token));
    const match = (req: HttpRequest<any>) =>
      req.url === '/auth/refresh' && req.body.refreshToken === 'foo';

    httpMock.expectOne('/usuarios/me').flush({});
    tick(4000);
    expect(authService.check()).toBeTrue();
    httpMock.match(match)[0].flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(authService.check()).toBeFalse();
    expect(tokenService.set).toHaveBeenCalledWith(undefined);
    tokenService.ngOnDestroy();
  }));

  it('recarrega o usuário ao trocar de login e não reutiliza a empresa anterior', () => {
    const storage = TestBed.inject(LocalStorageService);
    const jwt = (empresaId: number) =>
      'header.' + btoa(JSON.stringify({ empresaId })) + '.signature';

    authService.login(email, 'password').subscribe();
    httpMock.expectOne('/auth/login').flush({ accessToken: jwt(1) });
    httpMock.expectOne('/usuarios/me').flush({ id: 1, empresaId: 1 });
    expect(storage.get('tenantId')).toBe('1');

    authService.login('segundo@exemplo.com', 'password').subscribe();
    httpMock.expectOne('/auth/login').flush({ accessToken: jwt(2) });
    httpMock.expectOne('/usuarios/me').flush({ id: 2, empresaId: 2 });
    let current: User = {};
    authService.user().subscribe(value => current = value);
    expect(current.id).toBe(2);
    expect(storage.get('tenantId')).toBe('2');
    expect(localStorage.getItem('tenantId')).toBe('2');
    tokenService.ngOnDestroy();
  });

  it('limpa as referências da empresa quando a sessão perde o token', () => {
    const storage = TestBed.inject(LocalStorageService);
    tokenService.set(token);
    httpMock.expectOne('/usuarios/me').flush(user);
    storage.set('tenantId', '7');
    storage.set('empresaId', '7');
    localStorage.setItem('tenantId', '7');
    localStorage.setItem('empresaId', '7');
    tokenService.clear();
    expect(storage.has('tenantId')).toBeFalse();
    expect(storage.has('empresaId')).toBeFalse();
    expect(localStorage.getItem('tenantId')).toBeNull();
    expect(localStorage.getItem('empresaId')).toBeNull();
  });

  it('it only call http request once when on change subscribe twice', () => {
    authService.change().subscribe();
    tokenService.set(token);
    httpMock.expectOne('/usuarios/me').flush({});
  });
});
