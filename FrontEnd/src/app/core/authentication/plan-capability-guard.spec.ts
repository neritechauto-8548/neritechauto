import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, UrlTree, provideRouter } from '@angular/router';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from './interface';
import { planCapabilityGuard } from './plan-capability-guard';

@Component({
  template: '',
})
class Dummy {}

describe('planCapabilityGuard', () => {
  let router: Router;
  let user$: BehaviorSubject<User>;
  let authenticated: boolean;

  const route = (capability = 'acessoFiscal', fallbackMinPlan = 2) => ({
    data: { capability, fallbackMinPlan },
  } as unknown as ActivatedRouteSnapshot);

  beforeEach(() => {
    user$ = new BehaviorSubject<User>({});
    authenticated = true;

    TestBed.configureTestingModule({
      providers: [
        provideRouter([
          { path: 'fiscal', component: Dummy },
          { path: 'configuracoes/assinatura', component: Dummy },
          { path: 'auth/login', component: Dummy },
          { path: '403', component: Dummy },
        ]),
        {
          provide: AuthService,
          useValue: {
            check: () => authenticated,
            user: () => user$.asObservable(),
          },
        },
      ],
    });

    router = TestBed.inject(Router);
  });

  async function runGuard(snapshot = route()) {
    const result = TestBed.runInInjectionContext(() => planCapabilityGuard(snapshot));
    if (result instanceof Observable) {
      return firstValueFrom(result);
    }
    return result;
  }

  it('deve liberar capacidade fiscal explícita mesmo em nível inferior', async () => {
    user$.next({ planoNivel: 1, acessoFiscal: true });

    expect(await runGuard()).toBeTrue();
  });

  it('deve bloquear capacidade fiscal explicitamente negada mesmo em nível superior', async () => {
    user$.next({ planoNivel: 3, acessoFiscal: false });

    expect(await runGuard()).toEqual(router.parseUrl('/configuracoes/assinatura'));
  });

  it('deve usar nível 2 como fallback para API anterior', async () => {
    user$.next({ planoNivel: 2 });

    expect(await runGuard()).toBeTrue();
  });

  it('deve bloquear fallback abaixo do nível mínimo', async () => {
    user$.next({ planoNivel: 1 });

    expect(await runGuard()).toEqual(router.parseUrl('/configuracoes/assinatura'));
  });

  it('deve redirecionar usuário sem autenticação para login', async () => {
    authenticated = false;

    const result = await runGuard();
    expect(result as UrlTree).toEqual(router.parseUrl('/auth/login'));
  });
});
