import { permissionGuard } from '@core';
import { routes } from './os.routes';

describe('OS route contract', () => {
  it('protects the canonical cockpit route by GERAL_USUARIO', async () => {
    const cockpit = routes.find(route => route.path === ':id');

    expect(cockpit).withContext('Rota canônica /ordens-servico/{id} ausente').toBeDefined();
    expect(cockpit?.canActivate).toContain(permissionGuard);
    expect(cockpit?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
    expect(cockpit?.loadComponent).toBeDefined();

    const component = await cockpit!.loadComponent!();
    expect((component as any).name).toBe('OsCockpitShell');
  });

  it('keeps legacy cockpit aliases while the application is rebuilt', async () => {
    for (const path of ['visualizar-os/:numero', 'visualizar-editar-os/:numero']) {
      const route = routes.find(candidate => candidate.path === path);
      expect(route).withContext(`Alias legado ausente: ${path}`).toBeDefined();
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual(['GERAL_USUARIO']);

      const component = await route!.loadComponent!();
      expect((component as any).name).toBe('VisualizarOS');
    }
  });

  it('keeps static routes before the dynamic cockpit route', () => {
    const cadastroIndex = routes.findIndex(route => route.path === 'cadastro');
    const dynamicIndex = routes.findIndex(route => route.path === ':id');

    expect(cadastroIndex).toBeGreaterThanOrEqual(0);
    expect(dynamicIndex).toBeGreaterThan(cadastroIndex);
  });
});
