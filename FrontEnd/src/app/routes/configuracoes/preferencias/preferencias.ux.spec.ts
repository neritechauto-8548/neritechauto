import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { PreferenciasGeraisComponent } from './preferencias';
import { Politica, PreferenciasService } from './preferencias.service';

describe('ADM-011 — layout responsivo e teclado', () => {
  let fixture: ComponentFixture<PreferenciasGeraisComponent>;
  let api: jasmine.SpyObj<PreferenciasService>;
  const politica: Politica = {
    chave: 'locale', secao: 'Empresa', titulo: 'Idioma e formato',
    valorAplicado: 'pt-BR', valorSalvo: null, origem: 'PADRAO',
    ativo: true, versao: null, alteradoEm: null, responsavel: null
  };

  beforeEach(async () => {
    api = jasmine.createSpyObj<PreferenciasService>('PreferenciasService',
      ['listar', 'listarIntegracoes', 'historico', 'criar', 'editar', 'status']);
    api.listar.and.returnValue(of({
      content: [politica], totalElements: 1, totalPages: 1, number: 0, size: 20
    }));
    api.listarIntegracoes.and.returnValue(of([
      { canal: 'E-mail', estado: 'NAO_CONFIGURADA' },
      { canal: 'SMS', estado: 'NAO_CONFIGURADA' },
      { canal: 'WhatsApp', estado: 'NAO_CONFIGURADA' }
    ]));
    api.historico.and.returnValue(of({
      content: [], totalElements: 0, totalPages: 0, number: 0, size: 20
    }));
    await TestBed.configureTestingModule({
      imports: [PreferenciasGeraisComponent],
      providers: [provideRouter([]), { provide: PreferenciasService, useValue: api }]
    }).compileComponents();
    fixture = TestBed.createComponent(PreferenciasGeraisComponent);
    fixture.detectChanges();
  });

  afterEach(() => fixture.destroy());

  it('mantém o conteúdo dentro do viewport e a tabela rolável em 320/768/1440 px', () => {
    const host = fixture.nativeElement as HTMLElement;
    const main = host.querySelector('main') as HTMLElement;
    const scroller = host.querySelector('[aria-label="Tabela de parâmetros com rolagem"]') as HTMLElement;
    host.style.display = 'block';
    for (const width of [320, 768, 1440]) {
      host.style.width = width + 'px';
      fixture.detectChanges();
      expect(main.getBoundingClientRect().width).withContext('main width at ' + width).toBeLessThanOrEqual(width + 1);
      expect(main.scrollWidth).withContext('main overflow at ' + width).toBeLessThanOrEqual(main.clientWidth + 2);
      if (width <= 768) {
        expect(scroller.scrollWidth).withContext('table scroll width at ' + width)
          .toBeGreaterThan(scroller.clientWidth);
      }
    }
  });

  it('controles possuem rótulos acessíveis e a tabela possui caption', () => {
    const host = fixture.nativeElement as HTMLElement;
    expect(host.querySelector('table caption')?.textContent).toContain('Valores aplicados');
    const controls = Array.from(host.querySelectorAll('input, select')) as (HTMLInputElement | HTMLSelectElement)[];
    expect(controls.length).toBeGreaterThan(0);
    for (const control of controls) {
      expect(!!control.labels?.length || !!control.getAttribute('aria-label'))
        .withContext('control ' + control.outerHTML).toBeTrue();
    }
  });

  it('abre o editor com foco no título e mantém ações acessíveis ao teclado', () => {
    const page = fixture.componentInstance;
    page.editar(politica);
    fixture.detectChanges();
    const host = fixture.nativeElement as HTMLElement;
    const heading = host.querySelector('#editar-politica') as HTMLElement;
    expect(heading).toBeTruthy();
    expect(document.activeElement).toBe(heading);
    expect(heading.getAttribute('tabindex')).toBe('-1');
    const actions = Array.from(host.querySelectorAll('button, a')) as HTMLElement[];
    for (const action of actions) {
      expect(action.getBoundingClientRect().height).withContext(action.textContent?.trim() || 'action')
        .toBeGreaterThanOrEqual(44);
    }
  });
});
