import { of, throwError } from 'rxjs';
import { CadastroVeiculo } from './cadastro-veiculo';

describe('Criação de veículo — fluxo do drawer', () => {
  let subject: CadastroVeiculo;
  let lookup: jasmine.Spy;

  beforeEach(() => {
    subject = Object.create(CadastroVeiculo.prototype) as CadastroVeiculo;
    subject.modalMode = true;
    subject.form = { clienteId: 4, placa:'ABC1D23', marcaId:1, modeloId:2, anoModeloId:3, combustivelId:4 };
    subject.selectedCliente = {id:4,nome:'Oficina de Exemplo'};
    subject.existingVehicleId = null;
    subject.saving = false;
    subject.searchMode = 'placa';
    subject.lookupState = 'idle';
    subject.lookupMessage = '';
    (subject as any).isUxPreview = false;
    lookup = jasmine.createSpy('getByPlacaInterna').and.returnValue(throwError(() => ({status:404})));
    (subject as any).veiculoService = { getByPlacaInterna: lookup };
  });

  it('exige cliente e todos os campos antes de permitir salvar', () => {
    expect(subject.canSaveModal).toBeTrue();
    subject.selectedCliente = null;
    expect(subject.canSaveModal).toBeFalse();
  });

  it('consulta apenas a base interna e permite cadastro quando não localizado', () => {
    subject.consultarCadastroInterno();
    expect(lookup).toHaveBeenCalledWith('ABC1D23');
    expect(subject.lookupState).toBe('none');
    expect(subject.canSaveModal).toBeTrue();
  });

  it('bloqueia duplicidade após encontrar veículo cadastrado', () => {
    lookup.and.returnValue(of({id:33, placa:'ABC1D23',clienteId:4}));
    subject.consultarCadastroInterno();
    expect(subject.lookupState).toBe('found');
    expect(subject.canSaveModal).toBeFalse();
  });

  it('não faz consulta externa por chassi', () => {
    subject.searchMode = 'chassi';
    subject.consultarCadastroInterno();
    expect(subject.lookupState).toBe('unavailable');
    expect(lookup).not.toHaveBeenCalled();
  });
});
