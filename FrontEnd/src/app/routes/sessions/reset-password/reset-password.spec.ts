import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { LoginService } from '@core/authentication/login.service';
import { of, Subject, throwError } from 'rxjs';
import { ResetPassword } from './reset-password';

describe('AUTH-003 — Redefinição de senha', () => {
  let loginService: jasmine.SpyObj<LoginService>;
  let harness: RouterTestingHarness;
  let component: ResetPassword;
  const token = 't'.repeat(43);

  async function visit(url = '/auth/reset-password?token=' + token) {
    component = await harness.navigateByUrl(url, ResetPassword);
    harness.detectChanges();
    return harness.routeNativeElement as HTMLElement;
  }

  beforeEach(async () => {
    loginService = jasmine.createSpyObj<LoginService>('LoginService', ['resetPassword']);
    TestBed.configureTestingModule({
      providers: [
        provideRouter([
          { path: 'auth/reset-password', component: ResetPassword },
          { path: 'auth/login', component: ResetPassword },
          { path: 'auth/recover', component: ResetPassword },
        ]),
        { provide: LoginService, useValue: loginService },
      ],
    });
    harness = await RouterTestingHarness.create();
  });

  it('rejects missing tokens with accessible link to request a new one', async () => {
    const html = await visit('/auth/reset-password');
    expect(component.linkError).toContain('inválido');
    expect(html.querySelector('[role="alert"]')?.textContent).toContain('Solicite um novo');
    expect(html.querySelector('a[href="/auth/recover"]')).not.toBeNull();
    expect(html.querySelector('form')).toBeNull();
    expect(loginService.resetPassword).not.toHaveBeenCalled();
  });

  it('accepts URL fragments without leaving the reset secret in browser history', async () => {
    const html = await visit('/auth/reset-password#token=' + token);
    expect(component.linkError).toBeNull();
    expect(html.querySelector('form')).not.toBeNull();
    expect(location.hash).toBe('');
    expect(location.search).toBe('');
  });

  it('requires 12 characters and matching passwords before calling backend', async () => {
    const html = await visit();
    component.resetForm.setValue({ password: '123456', confirmPassword: '123456' });
    component.resetPassword();
    harness.detectChanges();
    expect(component.password.hasError('minlength')).toBeTrue();
    expect(html.textContent).toContain('12 caracteres');
    expect(loginService.resetPassword).not.toHaveBeenCalled();

    component.resetForm.setValue({ password: 'Minha frase-senha forte 2026', confirmPassword: 'diferente' });
    component.resetPassword();
    harness.detectChanges();
    expect(component.resetForm.hasError('mismatch')).toBeTrue();
    expect(html.textContent).toContain('As senhas não coincidem');
    expect(loginService.resetPassword).not.toHaveBeenCalled();
  });

  it('never reports success until backend confirms and resists double submit', async () => {
    const html = await visit();
    const pending = new Subject<void>();
    loginService.resetPassword.and.returnValue(pending.asObservable());
    component.resetForm.setValue({
      password: 'Minha frase-senha com unicode ✓ 2026',
      confirmPassword: 'Minha frase-senha com unicode ✓ 2026',
    });
    component.resetPassword();
    component.resetPassword();
    harness.detectChanges();
    expect(loginService.resetPassword).toHaveBeenCalledTimes(1);
    expect(loginService.resetPassword).toHaveBeenCalledWith(
      jasmine.objectContaining({ token, novaSenha: 'Minha frase-senha com unicode ✓ 2026' })
    );
    expect(component.completed).toBeFalse();
    expect(component.isSubmitting).toBeTrue();
    expect(html.querySelector('.reset-success')).toBeNull();

    pending.next(); pending.complete();
    harness.detectChanges();
    expect(component.completed).toBeTrue();
    expect(component.password.value).toBe('');
    expect(html.querySelector('[role="status"]')?.textContent).toContain('Senha atualizada');
    expect(html.querySelector('a[href="/auth/login"]')).not.toBeNull();
    expect(html.querySelector('form')).toBeNull();
  });

  it('guides expired, used and conflicting tokens back to recovery', async () => {
    const html = await visit();
    component.resetForm.setValue({ password: 'Minha senha segura extensa ✓', confirmPassword: 'Minha senha segura extensa ✓' });
    for (const [status, message, expected] of [
      [400, 'O link de recuperação expirou', 'expirou'],
      [400, 'Este link já foi utilizado', 'utilizado'],
      [409, 'conflito de concorrência', 'utilizado'],
    ] as const) {
      component.linkError = null;
      loginService.resetPassword.and.returnValue(throwError(() =>
        new HttpErrorResponse({ status, error: { message } })));
      component.resetPassword();
      harness.detectChanges();
      expect(component.linkError).toContain(expected);
      expect(html.querySelector('a[href="/auth/recover"]')).not.toBeNull();
    }
  });

  it('handles invalid 401 credentials and forbidden 403 without reporting success', async () => {
    const html = await visit();
    const phrase = 'Frase longa suficiente ✓';
    component.resetForm.setValue({ password: phrase, confirmPassword: phrase });
    loginService.resetPassword.and.returnValue(throwError(() => new HttpErrorResponse({ status: 401 })));
    component.resetPassword();
    harness.detectChanges();
    expect(component.linkError).toContain('inválido');
    expect(html.querySelector('a[href="/auth/recover"]')).not.toBeNull();
    expect(component.completed).toBeFalse();

    component.linkError = null;
    loginService.resetPassword.and.returnValue(throwError(() => new HttpErrorResponse({ status: 403 })));
    component.resetPassword();
    harness.detectChanges();
    expect(component.errorMessage).toContain('não permitida');
    expect(component.completed).toBeFalse();
    expect(html.querySelector('form')).not.toBeNull();
  });

  it('preserves form for 422 and network errors without false success', async () => {
    const html = await visit();
    const phrase = 'Frase longa suficiente ✓';
    component.resetForm.setValue({ password: phrase, confirmPassword: phrase });
    for (const status of [422, 0, 503]) {
      component.password.updateValueAndValidity();
      loginService.resetPassword.and.returnValue(throwError(() => new HttpErrorResponse({ status })));
      component.resetPassword();
      harness.detectChanges();
      expect(component.completed).toBeFalse();
      expect(component.errorMessage).toBeTruthy();
      expect(html.querySelector('form')).not.toBeNull();
    }
    expect(component.password.value).toBe(phrase);
  });

  it('reveals password only after explicit keyboard-operable button activation', async () => {
    const html = await visit();
    const button = html.querySelector('.password-toggle') as HTMLButtonElement;
    expect(button.getAttribute('type')).toBe('button');
    expect(button.getAttribute('aria-pressed')).toBe('false');
    button.click();
    harness.detectChanges();
    expect(button.getAttribute('aria-pressed')).toBe('true');
    expect((html.querySelector('#new-password') as HTMLInputElement).type).toBe('text');
  });
});
