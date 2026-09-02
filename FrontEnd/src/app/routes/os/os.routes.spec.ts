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

  it('maps documented operational deep routes to the canonical cockpit', async () => {
    const expected = [
      { path: ':id/pecas', permission: 'GERAL_USUARIO', tab: 'scope' },
      { path: ':id/diagnosticos', permission: 'OS_VIS_SOLICITACOES', tab: 'diagnostics' },
      { path: ':id/checklists', permission: 'OS_VIS_CHECKLIST', tab: 'checklist' },
      { path: ':id/fotos', permission: 'GERAL_USUARIO', tab: 'evidence' },
    ];

    for (const contract of expected) {
      const route = routes.find(candidate => candidate.path === contract.path);
      expect(route).withContext(`Rota operacional ausente: ${contract.path}`).toBeDefined();
      expect(route?.canActivate).toContain(permissionGuard);
      expect(route?.data?.['permissions']).toEqual([contract.permission]);
      expect(route?.data?.['operationsTab']).toBe(contract.tab);

      const component = await route!.loadComponent!();
      expect((component as any).name).toBe('OsCockpitShell');
    }
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

  it('keeps static and deep routes before the dynamic cockpit route', () => {
    const cadastroIndex = routes.findIndex(route => route.path === 'cadastro');
    const checklistIndex = routes.findIndex(route => route.path === ':id/checklists');
    const dynamicIndex = routes.findIndex(route => route.path === ':id');

    expect(cadastroIndex).toBeGreaterThanOrEqual(0);
    expect(checklistIndex).toBeGreaterThan(cadastroIndex);
    expect(dynamicIndex).toBeGreaterThan(checklistIndex);
  });
});
