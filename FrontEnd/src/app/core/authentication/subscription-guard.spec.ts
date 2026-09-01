import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { BehaviorSubject, Observable, firstValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from './interface';
import { subscriptionGuard } from './subscription-guard';

describe('subscriptionGuard', () => {
  let router: Router;
  let user$: BehaviorSubject<User>;
  let authenticated: boolean;

  const route: any = {};
  const state = (url = '/dashboard') => ({ url } as any);

  beforeEach(() => {
    user$ = new BehaviorSubject<User>({});
    authenticated = true;

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
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

  async function runGuard(url = '/dashboard') {
    const result = TestBed.runInInjectionContext(() => subscriptionGuard(route, state(url)));
    if (result instanceof Observable) {
      return firstValueFrom(result);
    }
    return result;
  }

  it('deve priorizar assinaturaAtiva=true', async () => {
    user$.next({ assinaturaAtiva: true, subscriptionStatus: 'SUSPENSO' });

    expect(await runGuard()).toBeTrue();
  });

  it('deve bloquear assinaturaAtiva=false mesmo se status persistido ainda for TESTE', async () => {
    user$.next({ assinaturaAtiva: false, subscriptionStatus: 'TESTE' });

    expect(await runGuard()).toEqual(router.parseUrl('/configuracoes/assinatura'));
  });

  it('deve manter compatibilidade com status antigo quando flag explícita não existir', async () => {
    user$.next({ subscriptionStatus: 'ATIVO' });

    expect(await runGuard()).toBeTrue();
  });

  it('deve sempre permitir acesso à página de assinatura', async () => {
    user$.next({ assinaturaAtiva: false, subscriptionStatus: 'SUSPENSO' });

    expect(await runGuard('/configuracoes/assinatura')).toBeTrue();
  });

  it('deve redirecionar para login sem token válido', async () => {
    authenticated = false;

    expect(await runGuard()).toEqual(router.parseUrl('/auth/login'));
  });
});
