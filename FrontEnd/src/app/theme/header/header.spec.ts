import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY } from 'rxjs';
import { MenuService, SettingsService } from '@core';
import { Header } from './header';

describe('Header global search navigation', () => {
  let header: Header;
  let router: { events: typeof EMPTY; navigateByUrl: jasmine.Spy };
  let menu: MenuService;

  beforeEach(() => {
    router = { events: EMPTY, navigateByUrl: jasmine.createSpy('navigateByUrl') };
    TestBed.configureTestingModule({
      providers: [
        MenuService,
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: {} },
        { provide: SettingsService, useValue: { options: {}, getThemeColor: () => 'light' } },
        { provide: TranslateService, useValue: { instant: (key: string) => key } },
      ],
    });
    menu = TestBed.inject(MenuService);
    header = TestBed.runInInjectionContext(() => new Header());
    menu.set([
      { route: 'home', name: 'Início', type: 'link', icon: 'home' },
      { route: 'cliente', name: 'Clientes', type: 'sub', icon: 'users', children: [
        { route: 'listar', name: 'Listar', type: 'link' },
        { route: 'cadastro', name: 'Cadastrar', type: 'link' },
        { route: 'crm', name: 'CRM', type: 'link', disabled: true },
      ] },
      { route: 'patio', name: 'Gestão de Pátio', type: 'extLink', icon: 'warehouse' },
    ]);
  });

  function press(key: string): KeyboardEvent {
    return new KeyboardEvent('keydown', { key, cancelable: true });
  }

  it('uses arrows and Enter to navigate to the selected result', () => {
    header.onSearch('clientes');
    expect(header.searchResults.map(result => result.route)).toEqual(['/cliente/listar', '/cliente/cadastro']);
    header.onSearchKeydown(press('ArrowDown'));
    expect(header.activeSearchIndex).toBe(0);
    header.onSearchKeydown(press('ArrowDown'));
    expect(header.activeSearchIndex).toBe(1);
    header.onSearchKeydown(press('Enter'));
    expect(router.navigateByUrl).toHaveBeenCalledWith('/cliente/cadastro');
    expect(header.searchTerm).toBe('');
    expect(header.activeSearchIndex).toBe(-1);
  });

  it('supports wrapped selection, Escape and unmatched queries', () => {
    header.onSearch('clientes');
    header.onSearchKeydown(press('ArrowUp'));
    expect(header.activeSearchIndex).toBe(1);
    header.onSearchKeydown(press('Escape'));
    expect(header.searchResults.length).toBe(0);
    expect(header.activeSearchIndex).toBe(-1);
    header.onSearch('impossivel');
    expect(header.searchResults).toEqual([]);
  });

  it('does not suggest disabled preview destinations', () => {
    header.onSearch('crm');
    expect(header.searchResults).toEqual([]);
  });

  it('does not offer placeholder external destinations', () => {
    header.onSearch('pátio');
    expect(header.searchResults).toEqual([]);
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });
  it('lista somente destinos permitidos no menu Aplicativos sem criar rotas', () => {
    header.appTab = 'system';
    expect(header.appEntries.map(entry => entry.route)).toContain('/cliente/listar');
    expect(header.appEntries.map(entry => entry.route)).not.toContain('/cliente/crm');
    header.appTab = 'crm';
    expect(header.appEntries).toEqual([]);
  });

  it('abre e fecha Aplicativos e Plano sem sobrepor os dois menus', () => {
    header.toggleApps();
    expect(header.appMenuOpen).toBeTrue();
    header.togglePlan();
    expect(header.appMenuOpen).toBeFalse();
    expect(header.planMenuOpen).toBeTrue();
    header.closeMenus();
    expect(header.planMenuOpen).toBeFalse();
  });

});
