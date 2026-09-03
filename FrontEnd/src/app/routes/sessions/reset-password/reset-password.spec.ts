import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { of, throwError } from 'rxjs';

import { ResetPassword } from './reset-password';

describe('ResetPassword', () => {
  let component: ResetPassword;
  let currentToken: string;
  let loginService: jasmine.SpyObj<LoginService>;
  let toast: jasmine.SpyObj<HotToastService>;

  beforeEach(() => {
    currentToken = 'opaque-token';
    loginService = jasmine.createSpyObj<LoginService>('LoginService', ['resetPassword']);
    toast = jasmine.createSpyObj<HotToastService>('HotToastService', ['success', 'error']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      providers: [
        { provide: LoginService, useValue: loginService },
        { provide: HotToastService, useValue: toast },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParamMap: { get: () => currentToken } } },
        },
      ],
    });

    component = TestBed.runInInjectionContext(() => new ResetPassword());
  });

  it('shows a persistent invalid-link state when the token is missing', () => {
    currentToken = '';
    component.ngOnInit();

    expect(component.invalidToken).toBeTrue();
    expect(component.passwordUpdated).toBeFalse();
    expect(loginService.resetPassword).not.toHaveBeenCalled();
  });

  it('shows a persistent success state after updating the password', () => {
    loginService.resetPassword.and.returnValue(of({}));
    component.ngOnInit();
    component.resetForm.setValue({ password: 'nova-senha', confirmPassword: 'nova-senha' });

    component.resetPassword();

    expect(loginService.resetPassword).toHaveBeenCalledOnceWith({
      token: 'opaque-token',
      novaSenha: 'nova-senha',
    });
    expect(component.passwordUpdated).toBeTrue();
    expect(component.resetForm.disabled).toBeTrue();
    expect(toast.success).toHaveBeenCalled();
  });

  it('turns an expired or reused token into a persistent invalid-link state', () => {
    loginService.resetPassword.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 410 }))
    );
    component.ngOnInit();
    component.resetForm.setValue({ password: 'nova-senha', confirmPassword: 'nova-senha' });

    component.resetPassword();

    expect(component.invalidToken).toBeTrue();
    expect(component.resetForm.disabled).toBeTrue();
    expect(toast.error).toHaveBeenCalled();
  });

  it('does not submit mismatched passwords', () => {
    component.ngOnInit();
    component.resetForm.setValue({ password: 'nova-senha', confirmPassword: 'outra-senha' });

    component.resetPassword();

    expect(loginService.resetPassword).not.toHaveBeenCalled();
    expect(component.resetForm.hasError('mismatch')).toBeTrue();
  });
});
