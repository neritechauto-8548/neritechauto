import { HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { of, Subject, throwError } from 'rxjs';
import { Recover } from './recover';

describe('AUTH-002 Recuperar acesso — seguro e acessível', () => {
  let fixture: ComponentFixture<Recover>;
  let service: jasmine.SpyObj<LoginService>;
  let router: Router;
  beforeEach(() => {
    service = jasmine.createSpyObj<LoginService>('LoginService', ['recoverPassword']);
    TestBed.configureTestingModule({
      imports: [Recover],
      providers: [provideRouter([]), { provide: LoginService, useValue: service }],
    });
    fixture = TestBed.createComponent(Recover);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });
  const submit = (email = ' usuario@exemplo.com ') => {
    fixture.componentInstance.email.setValue(email);
    fixture.detectChanges();
    fixture.componentInstance.recover();
    fixture.detectChanges();
  };

  it('requires a valid e-mail and does not call API on invalid input', () => {
    submit('invalido');
    expect(service.recoverPassword).not.toHaveBeenCalled();
    const form: HTMLElement = fixture.nativeElement;
    expect(form.querySelector('#recover-email-error')?.textContent).toContain('válido');
    expect(form.querySelector('#recover-email')?.getAttribute('aria-invalid')).toBe('true');
  });

  it('sends a trimmed e-mail and shows identical generic confirmation', () => {
    service.recoverPassword.and.returnValue(of(undefined));
    submit();
    expect(service.recoverPassword).toHaveBeenCalledOnceWith('usuario@exemplo.com');
    const screen: HTMLElement = fixture.nativeElement;
    expect(screen.querySelector('[role="status"]')?.textContent).toContain('Se o e-mail estiver cadastrado');
    expect(screen.querySelector('[role="status"]')?.textContent).not.toContain('usuario@exemplo.com');
    expect(screen.querySelector('a.auth-primary-link')?.getAttribute('href')).toBe('/auth/login');
    expect(screen.querySelector('.auth-form')).toBeNull();
  });

  it('prevents duplicate submission while in-flight', () => {
    const pending = new Subject<void>();
    service.recoverPassword.and.returnValue(pending.asObservable());
    submit();
    expect(fixture.componentInstance.isSubmitting).toBeTrue();
    fixture.componentInstance.recover();
    expect(service.recoverPassword).toHaveBeenCalledTimes(1);
    pending.next(); pending.complete();
    expect(fixture.componentInstance.isSubmitting).toBeFalse();
  });

  it('reports unavailable SMTP via 503 without fake success', () => {
    service.recoverPassword.and.returnValue(throwError(() =>
      new HttpErrorResponse({ status: 503 })));
    submit();
    expect(fixture.componentInstance.emailSent).toBeFalse();
    expect(fixture.nativeElement.querySelector('[role="alert"]')?.textContent).toContain('indisponível');
  });

  it('handles rate limits and offline mode without identifying the account', () => {
    for (const status of [429, 0]) {
      service.recoverPassword.and.returnValue(throwError(() =>
        new HttpErrorResponse({ status })));
      submit('outra@exemplo.com');
      expect(fixture.componentInstance.emailSent).toBeFalse();
      expect(fixture.componentInstance.errorMessage).toBeTruthy();
      expect(fixture.componentInstance.errorMessage).not.toContain('outra@exemplo.com');
    }
  });

  it('always exposes an accessible back-to-login link', () => {
    const back = fixture.nativeElement.querySelector('a.back-link') as HTMLAnchorElement;
    expect(back?.getAttribute('href')).toBe('/auth/login');
    expect(back?.textContent).toContain('Voltar para o login');
  });
  it('esclarece que o login é o e-mail sem inventar um segundo identificador na API', () => {
    const toggle = fixture.nativeElement.querySelector('.auth-help-toggle') as HTMLButtonElement;
    expect(toggle.getAttribute('aria-expanded')).toBe('false');
    toggle.click();
    fixture.detectChanges();
    expect(toggle.getAttribute('aria-expanded')).toBe('true');
    expect(fixture.nativeElement.querySelector('#recover-login-help')?.textContent)
      .toContain('o login é o e-mail cadastrado');
  });

});
