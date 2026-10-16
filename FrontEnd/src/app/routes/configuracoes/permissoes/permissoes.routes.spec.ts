import { permissionGuard } from '@core';
import { routes } from '../configuracoes.routes';

describe('Rotas ADM-006', () => {
  it('requires ADMIN for direct access to every permissions route', () => {
    const protectedRoutes = routes.filter(route => route.path?.startsWith('permissoes'));
    expect(protectedRoutes.length).toBe(3);
    protectedRoutes.forEach(route => {
      expect(route.canActivate).toContain(permissionGuard);
      expect(route.data?.['requiredPermissions']).toBe('ADMIN');
      expect(route.data?.['permissions']).toBeUndefined();
    });
  });
});
