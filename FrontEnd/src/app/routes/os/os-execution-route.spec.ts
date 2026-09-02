import { routes } from './os.routes';

describe('OS execution route contract', () => {
  it('expõe a rota D4 de execução protegida por permissão real', () => {
    const executionRoute = routes.find(route => route.path === ':id/execucao');

    expect(executionRoute).toBeDefined();
    expect(executionRoute?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
    expect(executionRoute?.canActivate?.length).toBeGreaterThan(0);
  });

  it('mantém a rota específica de execução antes do cockpit parametrizado', () => {
    const executionIndex = routes.findIndex(route => route.path === ':id/execucao');
    const cockpitIndex = routes.findIndex(route => route.path === ':id');

    expect(executionIndex).toBeGreaterThan(-1);
    expect(cockpitIndex).toBeGreaterThan(executionIndex);
  });
});
