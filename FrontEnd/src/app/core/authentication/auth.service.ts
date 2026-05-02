import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, catchError, iif, map, merge, of, share, switchMap, tap } from 'rxjs';
import { filterObject, isEmptyObject } from './helpers';
import { User } from './interface';
import { LoginService } from './login.service';
import { TokenService } from './token.service';
import { LocalStorageService } from '@shared/services/storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly loginService = inject(LoginService);
  private readonly tokenService = inject(TokenService);
  private readonly storage = inject(LocalStorageService);

  private user$ = new BehaviorSubject<User>({});
  private change$ = merge(
    this.tokenService.change(),
    this.tokenService.refresh().pipe(switchMap(() => this.refresh()))
  ).pipe(
    switchMap(() => this.assignUser()),
    share()
  );

  init() {
    return new Promise<void>(resolve => this.change$.subscribe(() => resolve()));
  }

  change() {
    return this.change$;
  }

  check() {
    return this.tokenService.valid();
  }

  login(username: string, password: string, rememberMe = false) {
    return this.loginService.login(username, password, rememberMe).pipe(
      tap(token => this.tokenService.set(token)),
      map(() => this.check())
    );
  }

  refresh() {
    return this.loginService
      .refresh(filterObject({ refresh_token: this.tokenService.getRefreshToken() }))
      .pipe(
        catchError(() => of(undefined)),
        tap(token => this.tokenService.set(token)),
        map(() => this.check())
      );
  }

  logout() {
    return this.loginService.logout().pipe(
      tap(() => {
        this.tokenService.clear();
        this.storage.remove('tenantId');
      }),
      map(() => !this.check())
    );
  }

  user() {
    return this.user$.pipe(share());
  }

  menu() {
    return iif(() => this.check(), this.loginService.menu(), of([]));
  }

  private assignUser() {
    if (!this.check()) {
      return of({}).pipe(tap(user => this.user$.next(user)));
    }

    if (!isEmptyObject(this.user$.getValue())) {
      return of(this.user$.getValue());
    }

    return this.loginService.user().pipe(
      catchError(() => {
        this.tokenService.clear();
        return of({});
      }),
      tap(user => {
        this.user$.next(user);
        if (isEmptyObject(user)) {
          return;
        }
        // Salva o tenantId (empresaId) no LocalStorage
        const u = user as any;
        let tenantId = u.empresaId || u.tenantId || u.idEmpresa || u.id_empresa || u.companyId;
        
        if (tenantId && typeof tenantId === 'object' && tenantId.id) {
          tenantId = tenantId.id;
        }

        if (tenantId) {
          console.log('🏢 Setting Tenant ID from user profile:', tenantId);
          this.storage.set('tenantId', String(tenantId));
          this.storage.set('empresaId', String(tenantId)); // Dual storage for compatibility
        } else {
          console.warn('⚠️ No tenant ID found in user profile. Profile data:', u);
        }
      })
    );
  }
}
