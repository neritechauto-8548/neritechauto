import { TestBed } from '@angular/core/testing';
import { of, Subject, throwError } from 'rxjs';
import { AuthService } from '@core/authentication';
import { MenuService } from '@core/bootstrap/menu.service';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { StartupService } from './startup.service';

describe('Application Shell roles and navigation', () => {
  let startup: StartupService;
  let permissions: jasmine.SpyObj<NgxPermissionsService>;
  let roles: jasmine.SpyObj<NgxRolesService>;

  beforeEach(() => {
    permissions = jasmine.createSpyObj<NgxPermissionsService>('NgxPermissionsService', ['loadPermissions', 'getPermission']);
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

  it('limpa o menu após falha e volta a carregar em outra sessão', async () => {
    const auth = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    const menu = TestBed.inject(MenuService) as jasmine.SpyObj<MenuService>;
    const sessions = new Subject<any>();
    auth.change.and.returnValue(sessions.asObservable());
    auth.menu.and.returnValues(
      throwError(() => new Error('menu unavailable')),
      of([{ route: 'home', name: 'Início', type: 'link', icon: 'home' }])
    );

    const started = startup.load();
    sessions.next({ planoNivel: 1, permissions: [], funcoes: [] });
    await started;
    expect(menu.set).toHaveBeenCalledWith([]);
    sessions.next({ planoNivel: 1, permissions: [], funcoes: [] });
    expect(menu.set).toHaveBeenCalledWith(
      jasmine.arrayContaining([jasmine.objectContaining({ route: 'home' })])
    );
    sessions.complete();
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

  it('hides restricted menu entries for a profile without grants', () => {
    permissions.getPermission.and.returnValue(undefined);
    const menu: any[] = [
      { route: 'home', name: 'Início', type: 'link' },
      { route: 'cliente', name: 'Clientes', type: 'sub', permissions: { only: 'GERAL_USUARIO' },
        children: [{ route: 'listar', name: 'Clientes', type: 'link' }] },
    ];
    expect((startup as any).filterMenuByPermissions(menu).map((item: any) => item.name))
      .toEqual(['Início']);
  });

  it('keeps allowed entries, excludes forbidden ones and removes empty groups', () => {
    permissions.getPermission.and.callFake(name =>
      name === 'CLIENTE_VISUALIZAR' ? ({ name } as any) : undefined
    );
    const menu: any[] = [
      { route: 'cliente', name: 'Clientes', type: 'sub', children: [
        { route: 'listar', name: 'Listar', type: 'link',
          permissions: { only: ['CLIENTE_VISUALIZAR', 'CLIENTE_EDITAR'] } },
        { route: 'excluir', name: 'Excluir', type: 'link', permissions: { only: 'CLIENTE_EXCLUIR' } },
        { route: 'restrito', name: 'Restrito', type: 'link', permissions: { except: 'CLIENTE_VISUALIZAR' } },
      ] },
      { route: 'admin', name: 'Administração', type: 'sub', children: [
        { route: 'config', name: 'Configurações', type: 'link',
          permissions: { only: 'GERAL_CONFIG_SISTEMA' } },
      ] },
    ];
    const filtered = (startup as any).filterMenuByPermissions(menu);
    expect(filtered.map((item: any) => item.name)).toEqual(['Clientes']);
    expect(filtered[0].children.map((item: any) => item.name)).toEqual(['Listar']);
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
  it('does not elevate roles whose name merely contains ADMIN', () => {
    (startup as any).setPermissions({
      funcoes: ['ADMINISTRATIVO'], permissions: ['OS_VISUALIZAR'],
    });
    expect(permissions.loadPermissions).toHaveBeenCalledWith(['OS_VISUALIZAR']);
    expect(roles.addRole).toHaveBeenCalledWith('ADMINISTRATIVO', ['OS_VISUALIZAR']);
  });


});
