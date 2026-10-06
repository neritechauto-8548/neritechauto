import { permissionGuard } from '@core';
import { routes } from './grafico.routes';
describe('Rota de análises', () => { it('protege gráficos com a permissão persistida', () => { expect(routes[0].canActivate).toContain(permissionGuard); expect(routes[0].data?.['permissions']).toEqual(['REL_GRAFICOS']); expect(routes[0].loadComponent).toBeDefined(); }); });
