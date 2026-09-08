import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { permissionGuard } from './permission-guard';

@Component({
  template: '',
})
class Dummy {}

describe('permissionGuard', () => {
  let router: Router;
  let permissionsService: NgxPermissionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NgxPermissionsModule.forRoot()],
      providers: [provideRouter([{ path: '403', component: Dummy }])],
    });

    router = TestBed.inject(Router);
    permissionsService = TestBed.inject(NgxPermissionsService);
    permissionsService.flushPermissions();
  });

  it('allows a route when no permission is declared', () => {
    const route: any = { data: {} };

    const result = TestBed.runInInjectionContext(() => permissionGuard(route, {} as any));

    expect(result).toBeTrue();
  });

  it('allows a route when one required permission was granted by backend bootstrap', () => {
    permissionsService.loadPermissions(['GERAL_USUARIO']);
    const route: any = { data: { permissions: ['GERAL_USUARIO'] } };

    const result = TestBed.runInInjectionContext(() => permissionGuard(route, {} as any));

    expect(result).toBeTrue();
  });

  it('redirects to 403 when required permission is missing', () => {
    permissionsService.loadPermissions(['CLIENTE_CRIAR']);
    const route: any = { data: { permissions: ['GERAL_USUARIO'] } };

    const result = TestBed.runInInjectionContext(() => permissionGuard(route, {} as any));

    expect(result).toEqual(router.parseUrl('/403'));
  });
});
