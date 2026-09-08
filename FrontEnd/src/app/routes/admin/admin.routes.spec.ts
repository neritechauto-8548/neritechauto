import { permissionGuard } from '@core';
import { routes } from './admin.routes';

describe('Admin routes authorization contract', () => {
  it('protects the admin dashboard with system configuration permission', () => {
    const dashboard = routes.find(route => route.path === 'dashboard');

    expect(dashboard).toBeDefined();
    expect(dashboard?.canActivate).toContain(permissionGuard);
    expect(dashboard?.data?.['permissions']).toEqual(['GERAL_CONFIG_SISTEMA']);
  });
});
