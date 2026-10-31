import { TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { RecursosApi } from './recursos-api.service';
import { RecursosComponent } from './recursos.component';

describe('Gestão de recursos — navegação de Cadastros', () => {
  function montar(path: 'recursos' | 'servicos') {
    const api = jasmine.createSpyObj<RecursosApi>('RecursosApi',
      ['areas', 'servicos', 'acessorios']);
    api.areas.and.returnValue(of([]));
    api.servicos.and.returnValue(of([]));
    api.acessorios.and.returnValue(of([]));
    TestBed.configureTestingModule({
      imports: [RecursosComponent],
      providers: [
        { provide: RecursosApi, useValue: api },
        { provide: ActivatedRoute, useValue: { snapshot: { routeConfig: { path } } } },
      ]
    });
    const fixture = TestBed.createComponent(RecursosComponent);
    fixture.detectChanges();
    return { fixture, api };
  }

  it('abre Áreas e mostra as três abas quando o usuário entra em Gestão de recursos > Cadastros', () => {
    const { fixture, api } = montar('recursos');
    const abas = Array.from(fixture.nativeElement.querySelectorAll('.recursos-tabs button')) as HTMLButtonElement[];
    expect(abas.length).toBe(3);
    expect(abas[0].textContent).toContain('Áreas');
    expect(abas[0].classList.contains('active')).toBeTrue();
    expect(abas[1].textContent).toContain('Serviços');
    expect(abas[2].textContent).toContain('Acessórios');
    expect(api.areas).toHaveBeenCalled();
    expect(api.servicos).not.toHaveBeenCalled();
  });

  it('preserva Serviços como entrada padrão na URL antiga de serviços', () => {
    const { fixture, api } = montar('servicos');
    const abas = Array.from(fixture.nativeElement.querySelectorAll('.recursos-tabs button')) as HTMLButtonElement[];
    expect(abas[1].classList.contains('active')).toBeTrue();
    expect(api.servicos).toHaveBeenCalled();
  });
});
