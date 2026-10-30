import { ClientesIndicadores } from './indicadores';
import { ClienteResponse, TipoCliente } from '../models/cliente.models';

describe('Indicadores de clientes — dados sem estimativas', () => {
  let component: ClientesIndicadores;
  beforeEach(() => {
    component = Object.create(ClientesIndicadores.prototype) as ClientesIndicadores;
    component.grouping = 'mes';
    component.male = component.female = component.unknown = 0;
    component.bars = [];
  });

  it('contabiliza apenas registros com nascimento válido', () => {
    const records: ClienteResponse[] = [
      { id:1, empresaId:1, tipoCliente:TipoCliente.PESSOA_FISICA, nomeCompleto:'Ana',
        dataNascimento:'1990-04-01',sexo:'F' as any, dataCadastro:'2026-10-01T10:00:00' },
      { id:2, empresaId:1, tipoCliente:TipoCliente.PESSOA_FISICA, nomeCompleto:'João',
        dataNascimento:'1988-04-01',sexo:'M' as any, dataCadastro:'2026-10-05T10:00:00' },
      { id:3, empresaId:1, tipoCliente:TipoCliente.PESSOA_FISICA, nomeCompleto:'Sem aniversário',
        dataCadastro:'2026-10-02T10:00:00' },
    ];
    (component as any).calculate(records);
    expect(component.male).toBe(1);
    expect(component.female).toBe(1);
    expect(component.unknown).toBe(0);
    expect(component.bars.map(v => v.count)).toEqual([2]);
  });
});
