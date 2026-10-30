import { Veiculo } from './veiculo';
import { StatusVeiculo } from '../models/veiculo.models';

describe('Veículos — filtros da listagem', () => {
  let component: Veiculo;
  beforeEach(() => {
    component = Object.create(Veiculo.prototype) as Veiculo;
    component.vehicles = [
      {id:1,clienteId:1,placa:'ABC1D23',marcaNome:'FIAT',modeloNome:'500',clienteNome:'Ana',status:StatusVeiculo.ATIVO},
      {id:2,clienteId:2,placa:'DEF4G56',marcaNome:'VOLKSWAGEN',modeloNome:'GOL',clienteNome:'João',status:StatusVeiculo.ATIVO},
    ];
    component.searchTerm = '';
    component.filterCliente = component.filterMarca = component.filterModelo = component.filterPlaca = '';
    component.draftCliente = component.draftMarca = component.draftModelo = component.draftPlaca = '';
    component.first = 10;
    component.filterOpen = true;
  });

  it('filtra cliente, marca, modelo e placa no conjunto retornado', () => {
    component.draftMarca = 'fiat';
    component.draftPlaca = 'abc';
    component.applyFilter();
    expect(component.filtered.map(v => v.id)).toEqual([1]);
    expect(component.first).toBe(0);
    expect(component.filterOpen).toBeFalse();
  });

  it('busca por chassi, placa, modelo ou cliente', () => {
    component.searchTerm = 'GOL';
    expect(component.filtered.map(v => v.id)).toEqual([2]);
    component.searchTerm = '';
    expect(component.filtered.length).toBe(2);
  });

  it('reseta filtros sem modificar os veículos carregados', () => {
    component.draftModelo = '500'; component.applyFilter();
    component.resetFilters();
    expect(component.filtered.length).toBe(2);
    expect(component.vehicles.length).toBe(2);
  });
});
