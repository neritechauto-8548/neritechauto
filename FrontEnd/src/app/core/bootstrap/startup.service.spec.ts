import { TestBed } from '@angular/core/testing';
import { AuthService, User } from '@core/authentication';
import { Menu, MenuService } from '@core/bootstrap/menu.service';
import { StartupService } from '@core/bootstrap/startup.service';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { of, Subject } from 'rxjs';

describe('StartupService', () => {
  let startup: StartupService;
  let user$: Subject<User>;
  let authService: jasmine.SpyObj<AuthService>;
  let menuService: jasmine.SpyObj<MenuService>;
  let permissionsService: jasmine.SpyObj<NgxPermissionsService>;
  let rolesService: jasmine.SpyObj<NgxRolesService>;

  beforeEach(() => {
    user$ = new Subject<User>();

    authService = jasmine.createSpyObj<AuthService>('AuthService', ['change', 'menu']);
    menuService = jasmine.createSpyObj<MenuService>('MenuService', ['addNamespace', 'set']);
    permissionsService = jasmine.createSpyObj<NgxPermissionsService>('NgxPermissionsService', [
      'flushPermissions',
      'loadPermissions',
    ]);
    rolesService = jasmine.createSpyObj<NgxRolesService>('NgxRolesService', [
      'flushRoles',
      'addRole',
    ]);

    authService.change.and.returnValue(user$.asObservable());
    authService.menu.and.returnValue(of([]));

    TestBed.configureTestingModule({
      providers: [
        StartupService,
        { provide: AuthService, useValue: authService },
        { provide: MenuService, useValue: menuService },
        { provide: NgxPermissionsService, useValue: permissionsService },
        { provide: NgxRolesService, useValue: rolesService },
      ],
    });

    startup = TestBed.inject(StartupService);
  });

  afterEach(() => user$.complete());

  it('should load exactly the permissions and roles returned by the backend user', async () => {
    const permissions = ['CLIENTE_VISUALIZAR', 'VEICULO_VISUALIZAR'];
    const loadPromise = startup.load();

    user$.next({
      permissions,
      funcoes: ['ATENDENTE'],
      planoNivel: 1,
    });

    await loadPromise;

    expect(permissionsService.flushPermissions).toHaveBeenCalledTimes(1);
    expect(permissionsService.loadPermissions).toHaveBeenCalledOnceWith(permissions);
    expect(rolesService.flushRoles).toHaveBeenCalledTimes(1);
    expect(rolesService.addRole).toHaveBeenCalledOnceWith('ATENDENTE', permissions);
    expect(authService.menu).toHaveBeenCalledTimes(1);
    expect(menuService.addNamespace).toHaveBeenCalledOnceWith([], 'menu');
    expect(menuService.set).toHaveBeenCalledOnceWith([]);
  });

  it('should filter menu entries by permission and plan before applying the canonical menu', async () => {
    const menu: Menu[] = [
      {
        route: 'clientes',
        name: 'clientes',
        type: 'link',
        icon: 'tabler-users',
        permissions: { only: 'CLIENTE_VISUALIZAR' },
      },
      {
        route: 'financeiro',
        name: 'financeiro',
        type: 'link',
        icon: 'tabler-cash',
        permissions: { only: 'FINANCEIRO_VISUALIZAR' },
      },
      {
        route: 'relatorios',
        name: 'relatorios',
        type: 'link',
        icon: 'tabler-report',
        minPlan: 3,
      },
    ];

    authService.menu.and.returnValue(of(menu));
    const loadPromise = startup.load();

    user$.next({
      permissions: ['CLIENTE_VISUALIZAR'],
      funcoes: ['ATENDENTE'],
      planoNivel: 1,
    });

    await loadPromise;

    const visibleMenu = menuService.set.calls.mostRecent().args[0] as Menu[];

    expect(visibleMenu.map(item => item.route)).toEqual(['clientes']);
    expect(menuService.addNamespace).toHaveBeenCalledOnceWith(visibleMenu, 'menu');
    expect(menuService.set).toHaveBeenCalledOnceWith(visibleMenu);
  });
});
