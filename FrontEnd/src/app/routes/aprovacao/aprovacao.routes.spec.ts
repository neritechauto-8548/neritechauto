import { permissionGuard } from '@core';
import { routes } from './aprovacao.routes';

describe('Rotas da central de aprovações', () => {
  it('protege a fila e o registro de decisão com a permissão persistida', () => {
    for (const path of ['', 'registrar-decisao']) {
      const route = routes.find(candidate => candidate.path === path);
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(['ORCAMENTO_DESCONTO_APROVAR']);
      expect(route?.loadComponent).toBeDefined();
    }
  });
});
