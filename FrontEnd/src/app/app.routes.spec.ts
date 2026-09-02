import { permissionGuard, planGuard } from '@core';
import { Route } from '@angular/router';
import { routes } from './app.routes';

describe('App routes authorization contract', () => {
  const protectedShell = routes.find(route => route.path === '');
  const childRoutes = protectedShell?.children ?? [];

  const byPath = (path: string): Route | undefined =>
    childRoutes.find(route => route.path === path);

  const expectProtectedAliasPair = (canonicalPath: string, aliasPath: string) => {
    const canonicalRoute = byPath(canonicalPath);
    const aliasRoute = byPath(aliasPath);

    expect(canonicalRoute).withContext(`Rota canônica ausente: ${canonicalPath}`).toBeDefined();
    expect(aliasRoute).withContext(`Alias ausente: ${aliasPath}`).toBeDefined();

    expect(canonicalRoute?.canActivate)
      .withContext(`Rota ${canonicalPath} deve exigir permissionGuard`)
      .toContain(permissionGuard);
    expect(aliasRoute?.canActivate)
      .withContext(`Alias ${aliasPath} deve exigir o mesmo permissionGuard`)
      .toContain(permissionGuard);
    expect(aliasRoute?.data?.['permissions'])
      .withContext(`Alias ${aliasPath} deve usar as mesmas permissões de ${canonicalPath}`)
      .toEqual(canonicalRoute?.data?.['permissions']);
  };

  it('keeps cliente aliases under the same authorization contract', () => {
    expectProtectedAliasPair('clientes', 'cliente');
  });

  it('keeps veiculo aliases under the same authorization contract', () => {
    expectProtectedAliasPair('veiculos', 'veiculo');
  });

  it('exposes documented ordens-servico route while preserving /os compatibility', () => {
    const canonical = byPath('ordens-servico');
    const legacy = byPath('os');

    expect(canonical).withContext('Rota canônica /ordens-servico ausente').toBeDefined();
    expect(legacy).withContext('Alias legado /os deve permanecer durante o rebuild').toBeDefined();
    expect(canonical?.loadChildren).toBeDefined();
    expect(legacy?.loadChildren).toBeDefined();
  });

  it('protects sensitive placeholder routes from direct URL access', () => {
    const expectedPermissions: Record<string, string> = {
      'gestao-patio': 'GERAL_USUARIO',
      'checklists-operacionais': 'OS_VIS_CHECKLIST',
      aprovacoes: 'ORCAMENTO_DESCONTO_APROVAR',
      'pecas-movimentacao': 'PS_LISTAR_PROD',
      'faturamento-operacional': 'GERAL_FATURAS',
      historico: 'GERAL_USUARIO',
      graficos: 'REL_GRAFICOS',
    };

    Object.entries(expectedPermissions).forEach(([path, permission]) => {
      const route = byPath(path);
      expect(route).withContext(`Rota protegida ausente: ${path}`).toBeDefined();
      expect(route?.canActivate)
        .withContext(`Rota ${path} deve exigir permissionGuard`)
        .toContain(permissionGuard);
      expect(route?.data?.['permissions'])
        .withContext(`Rota ${path} deve exigir ${permission}`)
        .toBe(permission);
    });
  });

  it('protects Fiscal by the same minimum plan used by navigation', () => {
    const fiscal = byPath('fiscal');

    expect(fiscal).toBeDefined();
    expect(fiscal?.canActivate).toContain(planGuard);
    expect(fiscal?.data?.['minPlan']).toBe(3);
  });
});
