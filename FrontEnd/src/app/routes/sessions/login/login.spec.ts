import { HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { AuthService } from '@core/authentication';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { MessageService } from 'primeng/api';
import { of, Subject, throwError } from 'rxjs';
import { Login } from './login';

describe('AUTH-001 Login — estados e navegação', () => {
  let auth: jasmine.SpyObj<AuthService>;
  let router: Router;
  let fixture: ComponentFixture<Login>;

  function credentials(rememberMe = false) {
    fixture.componentInstance.loginForm.patchValue({
      username: '  user@oficina.com  ',
      password: 'senha válida',
      rememberMe,
    });
    fixture.detectChanges();
  }

  beforeEach(() => {
    auth = jasmine.createSpyObj<AuthService>('AuthService', ['login', 'user', 'logout']);
    auth.user.and.returnValue(of({ id: 7, empresaId: 15 } as any));
    auth.logout.and.returnValue(of(true));
    TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: auth },
        { provide: LocalStorageService, useClass: MemoryStorageService },
        MessageService,
      ],
    });
    router = TestBed.inject(Router);
    spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    fixture = TestBed.createComponent(Login);
    fixture.detectChanges();
  });

  it('exige e-mail válido e senha e não memoriza email por padrão', () => {
    expect(fixture.componentInstance.rememberMe.value).toBeFalse();
    fixture.componentInstance.login();
    fixture.detectChanges();
    expect(auth.login).not.toHaveBeenCalled();
    expect(fixture.nativeElement.querySelector('#email-error')).not.toBeNull();
  });

  it('só navega depois de token válido e perfil/empresa autenticados', () => {
    auth.login.and.returnValue(of(true));
    credentials(true);
    fixture.componentInstance.login();
    expect(auth.login).toHaveBeenCalledWith('user@oficina.com', 'senha válida', true);
    expect(router.navigateByUrl).toHaveBeenCalledWith('/home');
    expect(fixture.componentInstance.password.value).toBe('');
    expect(TestBed.inject(LocalStorageService).get('loginDefaults')).toEqual({ username: 'user@oficina.com' });
  });

  it('não navega com login falsamente bem-sucedido', () => {
    auth.login.and.returnValue(of(false));
    credentials();
    fixture.componentInstance.login();
    expect(router.navigateByUrl).not.toHaveBeenCalled();
    expect(fixture.componentInstance.loginError).toContain('Não foi possível validar');
  });

  it('mostra 401 genérico e suporta teclado no botão de senha', () => {
    auth.login.and.returnValue(throwError(() => new HttpErrorResponse({ status: 401 })));
    credentials();
    fixture.componentInstance.login();
    expect(router.navigateByUrl).not.toHaveBeenCalled();
    expect(fixture.componentInstance.loginError).toContain('E-mail ou senha inválidos');
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[role="alert"]')).not.toBeNull();
    const show = fixture.nativeElement.querySelector('.password-toggle') as HTMLButtonElement;
    expect(show.getAttribute('aria-pressed')).toBe('false');
    show.click();
    fixture.detectChanges();
    expect(show.getAttribute('aria-pressed')).toBe('true');
    expect((fixture.nativeElement.querySelector('#password') as HTMLInputElement).type).toBe('text');
  });

  it('apresenta 429 com orientação sem afirmar bloqueio permanente', () => {
    auth.login.and.returnValue(throwError(() => new HttpErrorResponse({ status: 429 })));
    credentials();
    fixture.componentInstance.login();
    expect(fixture.componentInstance.loginError).toContain('15 minutos');
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });

  it('apresenta rede indisponível sem êxito falso', () => {
    auth.login.and.returnValue(throwError(() => new HttpErrorResponse({ status: 0 })));
    credentials();
    fixture.componentInstance.login();
    expect(fixture.componentInstance.loginError).toContain('Sem conexão');
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });

  it('bloqueia duplo clique enquanto o servidor responde', () => {
    const pending = new Subject<boolean>();
    auth.login.and.returnValue(pending.asObservable());
    credentials();
    fixture.componentInstance.login();
    fixture.componentInstance.login();
    expect(auth.login).toHaveBeenCalledTimes(1);
    expect(fixture.componentInstance.isSubmitting).toBeTrue();
    pending.error(new HttpErrorResponse({ status: 401 }));
    expect(fixture.componentInstance.isSubmitting).toBeFalse();
  });

  it('não usa JWT sem perfil/empresa e cancela sessão parcialmente aberta', () => {
    auth.login.and.returnValue(of(true));
    auth.user.and.returnValue(throwError(() => new HttpErrorResponse({ status: 503 })));
    credentials();
    fixture.componentInstance.login();
    expect(auth.logout).toHaveBeenCalled();
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });
});
