import { TestBed } from '@angular/core/testing';
import { AuthService } from '@core/authentication';
import { MenuService } from '@core/bootstrap/menu.service';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { StartupService } from './startup.service';

describe('Application Shell roles and navigation', () => {
  let startup: StartupService;
  let permissions: jasmine.SpyObj<NgxPermissionsService>;
  let roles: jasmine.SpyObj<NgxRolesService>;

  beforeEach(() => {
    permissions = jasmine.createSpyObj<NgxPermissionsService>('NgxPermissionsService', ['loadPermissions']);
    roles = jasmine.createSpyObj<NgxRolesService>('NgxRolesService', ['flushRoles', 'addRole']);
    TestBed.configureTestingModule({
      providers: [
        StartupService,
        { provide: AuthService, useValue: jasmine.createSpyObj('AuthService', ['change', 'menu']) },
        { provide: MenuService, useValue: jasmine.createSpyObj('MenuService', ['set', 'addNamespace', 'buildRoute']) },
        { provide: NgxPermissionsService, useValue: permissions },
        { provide: NgxRolesService, useValue: roles },
      ],
    });
    startup = TestBed.inject(StartupService);
  });

  it('does not elevate permissions for a non-administrator', () => {
    (startup as any).setPermissions({
      funcoes: ['MECANICO'], permissions: ['OS_VISUALIZAR'],
    });
    expect(permissions.loadPermissions).toHaveBeenCalledWith(['OS_VISUALIZAR']);
    expect(roles.addRole).toHaveBeenCalledWith('MECANICO', ['OS_VISUALIZAR']);
  });

  it('applies administrator permissions only to an administrator', () => {
    (startup as any).setPermissions({ funcoes: ['ADMIN'], permissions: ['OS_VISUALIZAR'] });
    const loaded = permissions.loadPermissions.calls.mostRecent().args[0] as string[];
    expect(loaded).toContain('GERAL_CONFIG_SISTEMA');
    expect(loaded).toContain('OS_VISUALIZAR');
    expect(roles.addRole).toHaveBeenCalledWith('ADMIN', loaded);
  });

  it('hides plan-restricted destinations and empty navigation groups', () => {
    const items: any[] = [
      { route: 'home', name: 'Início', type: 'link' },
      { route: 'admin', name: 'Administração', type: 'sub', children: [
        { route: 'empresa', name: 'Empresa', type: 'link', minPlan: 3 },
      ] },
    ];
    const filtered = (startup as any).filterMenuByPlan(items, 1);
    expect(filtered.map((item: any) => item.name)).toEqual(['Início']);
  });
});
