import { permissionGuard } from '@core';
import { routes } from './pdv.routes';

describe('Rotas de PDV', () => {
  it('protege cada superfície de venda com as permissões persistidas', () => {
    const expectativas: Record<string, string[]> = {
      '': ['PDV_LISTAR_VENDAS', 'PDV_REALIZAR_VENDAS'],
      'listar-vendas': ['PDV_LISTAR_VENDAS'],
      'venda-balcao': ['PDV_REALIZAR_VENDAS'],
      'venda-balcao/:id': ['PDV_LISTAR_VENDAS'],
    };

    for (const [path, permissoes] of Object.entries(expectativas)) {
      const route = routes.find(candidate => candidate.path === path);
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(permissoes);
      expect(route?.loadComponent).toBeDefined();
    }
  });
});
