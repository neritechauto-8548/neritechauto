import { Menu } from './menu.service';
import {
  applyCanonicalMenuContract,
  CANONICAL_MOVIMENTACAO_ORDER,
  CANONICAL_ROOT_MENU_ORDER,
} from './menu-contract';

describe('UI-MASTER-001 menu contract', () => {
  const item = (name: string, children?: Menu['children']): Menu => ({
    route: name,
    name,
    type: children ? 'sub' : 'link',
    icon: name,
    children,
  });

  it('keeps the official root order regardless of payload order', () => {
    const shuffled = [
      item('relatorios'),
      item('clientes'),
      item('home'),
      item('gestao-patio'),
      item('financeiro'),
      item('operacional'),
      item('cadastros'),
      item('fiscal'),
      item('historico'),
      item('graficos'),
      item('agendamentos'),
      item('movimentacao', CANONICAL_MOVIMENTACAO_ORDER.map(name => ({
        route: name,
        name,
        type: 'link',
      }))),
    ];

    const result = applyCanonicalMenuContract(shuffled);

    expect(result.map(entry => entry.name)).toEqual([...CANONICAL_ROOT_MENU_ORDER]);
  });

  it('does not surface unreviewed root modules', () => {
    const result = applyCanonicalMenuContract([
      item('home'),
      item('marketing'),
      item('portal-cliente'),
      item('clientes'),
    ]);

    expect(result.map(entry => entry.name)).toEqual(['home', 'clientes']);
  });

  it('locks Movimentacao to the six documented destinations and their order', () => {
    const movimentacao = item('movimentacao', [
      { route: 'faturamento', name: 'faturamento', type: 'link' },
      { route: 'pdv', name: 'pdv', type: 'link' },
      { route: 'checklists', name: 'checklists', type: 'link' },
      { route: 'orcamentos', name: 'orcamentos', type: 'link' },
      { route: 'pecas', name: 'pecas', type: 'link' },
      { route: 'aprovacoes', name: 'aprovacoes', type: 'link' },
      { route: 'ordens-servico', name: 'ordens-servico', type: 'link' },
    ]);

    const [result] = applyCanonicalMenuContract([movimentacao]);

    expect(result.children?.map(child => child.name)).toEqual([
      ...CANONICAL_MOVIMENTACAO_ORDER,
    ]);
  });

  it('does not invent menu entries that were removed by permission filtering', () => {
    const result = applyCanonicalMenuContract([item('home'), item('clientes')]);

    expect(result).toHaveSize(2);
    expect(result.map(entry => entry.name)).toEqual(['home', 'clientes']);
  });
});
