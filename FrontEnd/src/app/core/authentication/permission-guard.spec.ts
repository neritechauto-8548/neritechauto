import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, provideRouter } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';
import { permissionGuard } from './permission-guard';

describe('permissionGuard', () => {
  let permissions: jasmine.SpyObj<NgxPermissionsService>;
  let router: Router;
  const allowed = new Set<string>();

  beforeEach(() => {
    allowed.clear();
    permissions = jasmine.createSpyObj<NgxPermissionsService>('NgxPermissionsService', ['getPermission']);
    permissions.getPermission.and.callFake(name => allowed.has(name) ? ({ name } as any) : undefined);

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: NgxPermissionsService, useValue: permissions },
      ],
    });
    router = TestBed.inject(Router);
  });

  function check(requiredPermissions?: string | string[], permissionMode?: 'all' | 'any') {
    const route = {
      data: { requiredPermissions, permissionMode },
    } as unknown as ActivatedRouteSnapshot;
    return TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot)
    );
  }

  it('allows routes with no explicit permission requirement', () => {
    expect(check()).toBeTrue();
    expect(permissions.getPermission).not.toHaveBeenCalled();
  });

  it('denies a single missing permission with the 403 route', () => {
    expect(check('clientes.visualizar')).toEqual(router.parseUrl('/403'));
  });

  it('allows a single granted permission', () => {
    allowed.add('clientes.visualizar');
    expect(check('clientes.visualizar')).toBeTrue();
  });

  it('requires every named permission in all mode (default)', () => {
    allowed.add('clientes.visualizar');
    expect(check(['clientes.visualizar', 'clientes.editar'])).toEqual(router.parseUrl('/403'));
    allowed.add('clientes.editar');
    expect(check(['clientes.visualizar', 'clientes.editar'])).toBeTrue();
  });

  it('accepts any one permission only when the route requests any mode', () => {
    allowed.add('clientes.visualizar');
    expect(check(['clientes.visualizar', 'clientes.editar'], 'any')).toBeTrue();
    allowed.clear();
    expect(check(['clientes.visualizar', 'clientes.editar'], 'any')).toEqual(router.parseUrl('/403'));
  });
});
