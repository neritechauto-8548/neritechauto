import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, catchError, iif, map, merge, of, share, switchMap, tap, take, timeout } from 'rxjs';
import { filterObject, isEmptyObject } from './helpers';
import { User } from './interface';
import { LoginService } from './login.service';
import { TokenService } from './token.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { SettingsService } from '../bootstrap/settings.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly loginService = inject(LoginService);
  private readonly tokenService = inject(TokenService);
  private readonly storage = inject(LocalStorageService);
  private readonly settings = inject(SettingsService);

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
      tap(() => this.clearSession()),
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
      this.clearLegacyTenantStorage();
      return of({}).pipe(tap(user => this.user$.next(user)));
    }

    if (!isEmptyObject(this.user$.getValue())) {
      return of(this.user$.getValue());
    }

    return this.loginService.user().pipe(
      take(1),
      timeout(1000),
      catchError(() => of({} as User)),
      tap((user: User) => {
        this.user$.next(user);
        this.clearLegacyTenantStorage();

        if (isEmptyObject(user as any)) {
          return;
        }

        const u = user as any;
        if (u.preferencias && !isEmptyObject(u.preferencias)) {
          this.settings.setOptions(u.preferencias);
          if (u.preferencias.theme) {
            this.settings.setTheme(u.preferencias.theme);
          }
          if (u.preferencias.dir) {
            this.settings.setDirection(u.preferencias.dir);
          }
        }
      })
    );
  }

  private clearSession() {
    this.tokenService.clear();
    this.user$.next({});
    this.clearLegacyTenantStorage();
  }

  private clearLegacyTenantStorage() {
    this.storage.remove('tenantId');
    this.storage.remove('empresaId');
    localStorage.removeItem('tenantId');
    localStorage.removeItem('empresaId');
  }
}
