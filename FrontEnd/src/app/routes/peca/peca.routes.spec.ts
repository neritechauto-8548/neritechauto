import { permissionGuard } from '@core';
import { routes } from './peca.routes';

describe('Rotas operacionais de peças', () => {
  it('protege central e preparação com a permissão persistida de consulta', () => {
    for (const path of ['', 'preparar-reserva']) {
      const route = routes.find(candidate => candidate.path === path);
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(['PS_LISTAR_PROD']);
      expect(route?.loadComponent).toBeDefined();
    }
  });
});
