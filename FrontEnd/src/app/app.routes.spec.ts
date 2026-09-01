import { permissionGuard } from '@core';
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
});
