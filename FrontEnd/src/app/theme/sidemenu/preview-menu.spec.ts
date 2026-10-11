import { TestBed } from '@angular/core/testing';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { AuthService } from '@core/authentication';
import { Menu, MenuChildrenItem, MenuService } from '@core';
import { StartupService } from '../../core/bootstrap/startup.service';

describe('Application Shell preview navigation', () => {
  let startup: StartupService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        StartupService, MenuService,
        { provide: AuthService, useValue: {} },
        { provide: NgxPermissionsService, useValue: {} },
        { provide: NgxRolesService, useValue: {} },
      ],
    });
    startup = TestBed.inject(StartupService);
    menuService = TestBed.inject(MenuService);
  });

  function lookup(items: Array<Menu | MenuChildrenItem>, name: string, parents: string[] = []):
    { item: Menu | MenuChildrenItem; path: string } | undefined {
    for (const item of items) {
      const segments = [...parents, item.route];
      if (item.name === name) return { item, path: menuService.buildRoute(segments) };
      const match = item.children && lookup(item.children, name, segments);
      if (match) return match;
    }
    return undefined;
  }

  it('routes existing modules to real Angular routes, preserving official navigation', () => {
    const items = (startup as any).getPreviewMenu() as Menu[];
    expect(lookup(items, 'Oficina')?.item.type).toBe('sub');
    const routes: Array<[string, string]> = [
      ['Início', '/home'],
      ['Agenda', '/agendamento/calendario'],
      ['Orçamentos', '/orcamento'],
      ['Ordens de Serviço', '/os'],
      ['Serviços', '/produtos-servicos/servicos'],
      ['Veículos', '/veiculo'],
      ['Vendas', '/pdv'],
      ['Estoque', '/produtos-servicos/estoque'],
      ['Inventário', '/configuracoes/inventario'],
      ['NF-e', '/fiscal/nfe'],
      ['Inutilização', '/fiscal/nfe/inutilizar-numeracao'],
    ];
    for (const [name, expectedPath] of routes) {
      const result = lookup(items, name);
      expect(result?.path).withContext(name).toBe(expectedPath);
      expect(result?.item.disabled).withContext(name).toBeFalse();
    }
  });

  it('keeps unimplemented official menu items visible and non-navigable', () => {
    const items = (startup as any).getPreviewMenu() as Menu[];
    for (const name of ['Gestão de Pátio', 'Comunicação', 'Questionamento de pesquisa',
      'Diagnósticos', 'Kits', 'Movimento Bancário', 'Gráficos Gerenciais']) {
      expect(lookup(items, name)?.item.disabled).withContext(name).toBeTrue();
    }
  });
});
