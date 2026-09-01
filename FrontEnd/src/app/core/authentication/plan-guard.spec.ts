import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { firstValueFrom, of, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { planGuard } from './plan-guard';

@Component({
  template: '',
})
class Dummy {}

describe('planGuard', () => {
  let router: Router;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['check', 'user']);

    TestBed.configureTestingModule({
      providers: [
        provideRouter([
          { path: '403', component: Dummy },
          { path: 'auth/login', component: Dummy },
        ]),
        { provide: AuthService, useValue: authService },
      ],
    });

    router = TestBed.inject(Router);
  });

  it('allows routes without a premium plan requirement', () => {
    const route: any = { data: { minPlan: 1 } };

    const result = TestBed.runInInjectionContext(() => planGuard(route, {} as any));

    expect(result).toBeTrue();
    expect(authService.check).not.toHaveBeenCalled();
  });

  it('redirects unauthenticated users to login', () => {
    authService.check.and.returnValue(false);
    const route: any = { data: { minPlan: 3 } };

    const result = TestBed.runInInjectionContext(() => planGuard(route, {} as any));

    expect(result).toEqual(router.parseUrl('/auth/login'));
  });

  it('allows users whose plan level meets the route requirement', async () => {
    authService.check.and.returnValue(true);
    authService.user.and.returnValue(of({ planoNivel: 3 }));
    const route: any = { data: { minPlan: 3 } };

    const result = TestBed.runInInjectionContext(() => planGuard(route, {} as any));
    const decision = await firstValueFrom(result as any);

    expect(decision).toBeTrue();
  });

  it('redirects users whose plan is below the route requirement', async () => {
    authService.check.and.returnValue(true);
    authService.user.and.returnValue(of({ planoNivel: 1 }));
    const route: any = { data: { minPlan: 3 } };

    const result = TestBed.runInInjectionContext(() => planGuard(route, {} as any));
    const decision = await firstValueFrom(result as any);

    expect(decision).toEqual(router.parseUrl('/403'));
  });

  it('fails closed when the user profile cannot be resolved', async () => {
    authService.check.and.returnValue(true);
    authService.user.and.returnValue(throwError(() => new Error('profile unavailable')));
    const route: any = { data: { minPlan: 3 } };

    const result = TestBed.runInInjectionContext(() => planGuard(route, {} as any));
    const decision = await firstValueFrom(result as any);

    expect(decision).toEqual(router.parseUrl('/403'));
  });
});
