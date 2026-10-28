import { permissionGuard } from '@core';
import { routes } from './checklist.routes';

describe('Rotas de inspeções', () => {
  it('protege central e criação com a permissão persistida de checklist', () => {
    for (const path of ['', 'nova']) {
      const route = routes.find(candidate => candidate.path === path);
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(['OS_VIS_CHECKLIST']);
      expect(route?.loadComponent).toBeDefined();
    }
  });
});
