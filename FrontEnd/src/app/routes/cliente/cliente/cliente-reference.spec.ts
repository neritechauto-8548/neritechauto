import { Cliente } from './cliente';

describe('Clientes — referência de listagem', () => {
  let component: Cliente;
  beforeEach(() => {
    component = Object.create(Cliente.prototype) as Cliente;
    component.selectedColumns = ['cliente','cpf','categoria','tipo','criacao'];
    component.draftColumns = [...component.selectedColumns];
    component.advancedDocument = '';
    component.advancedName = '';
    component.advancedFrom = '';
    component.advancedTo = '';
    component.advancedOpen = true;
    component.advancedError = '';
    component.first = 20;
  });

  it('mantém Cliente fixa e permite reorganizar outras colunas', () => {
    component.shiftColumn('tipo',-1);
    expect(component.draftColumns).toEqual(['cliente','cpf','tipo','categoria','criacao']);
    component.toggleColumn('cliente');
    expect(component.draftColumns[0]).toBe('cliente');
    component.toggleColumn('cpf');
    expect(component.draftColumns).not.toContain('cpf');
  });

  it('bloqueia intervalo inválido sem consultar o servidor', () => {
    component.advancedFrom = '2026-12-01';
    component.advancedTo = '2026-10-01';
    const fetch = spyOn<any>(component,'fetchPage');
    component.applyAdvancedFilters();
    expect(fetch).not.toHaveBeenCalled();
    expect(component.advancedError).toContain('data inicial');
    expect(component.advancedOpen).toBeTrue();
  });

  it('aplica filtros válidos e reinicia a página', () => {
    component.advancedFrom = '2026-01-01';
    component.advancedTo = '2026-10-30';
    const fetch = spyOn<any>(component,'fetchPage');
    component.applyAdvancedFilters();
    expect(component.first).toBe(0);
    expect(component.advancedOpen).toBeFalse();
    expect(fetch).toHaveBeenCalledTimes(1);
  });
});
