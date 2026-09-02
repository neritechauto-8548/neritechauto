import { permissionGuard } from '@core';
import { routes } from './os.routes';

describe('OS route contract', () => {
  it('protects the canonical cockpit route by GERAL_USUARIO', () => {
    const cockpit = routes.find(route => route.path === ':id');

    expect(cockpit).withContext('Rota canônica /ordens-servico/{id} ausente').toBeDefined();
    expect(cockpit?.canActivate).toContain(permissionGuard);
    expect(cockpit?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
    expect(cockpit?.loadComponent).toBeDefined();
  });

  it('keeps legacy cockpit aliases while the application is rebuilt', () => {
    ['visualizar-os/:numero', 'visualizar-editar-os/:numero'].forEach(path => {
      const route = routes.find(candidate => candidate.path === path);
      expect(route).withContext(`Alias legado ausente: ${path}`).toBeDefined();
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
    });
  });

  it('keeps static routes before the dynamic cockpit route', () => {
    const cadastroIndex = routes.findIndex(route => route.path === 'cadastro');
    const dynamicIndex = routes.findIndex(route => route.path === ':id');

    expect(cadastroIndex).toBeGreaterThanOrEqual(0);
    expect(dynamicIndex).toBeGreaterThan(cadastroIndex);
  });
});
