import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { AuthService } from './auth.service';
import { authGuard } from './auth-guard';
import { environment } from '@env/environment';

describe('authGuard', () => {
  const state: any = { url: '/cliente/listar?busca=ana' };
  let router: Router;
  let auth: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    auth = jasmine.createSpyObj<AuthService>('AuthService', ['check']);
    TestBed.configureTestingModule({ providers: [
      { provide: AuthService, useValue: auth },
      provideRouter([]),
    ] });
    router = TestBed.inject(Router);
  });

  it('allows an authenticated user', () => {
    auth.check.and.returnValue(true);
    expect(TestBed.runInInjectionContext(() => authGuard(undefined, state))).toBeTrue();
  });

  it('allows only an explicitly requested visual preview without claiming a valid login', () => {
    const original = environment.uxPreview;
    try {
      environment.uxPreview = true;
      auth.check.and.returnValue(false);
      expect(TestBed.runInInjectionContext(() => authGuard(undefined, state))).toBeTrue();
      expect(auth.check()).toBeFalse();
    } finally {
      environment.uxPreview = original;
    }
  });

  it('preserves the requested URL when login is required', () => {
    auth.check.and.returnValue(false);
    expect(TestBed.runInInjectionContext(() => authGuard(undefined, state))).toEqual(
      router.createUrlTree(['/auth/login'], { queryParams: { returnUrl: state.url } })
    );
  });
});
