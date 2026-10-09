import { permissionGuard } from '@core';
import { routes } from './faturamento.routes';
describe('Rotas de faturamento operacional', () => { it('protege fila e preparação com a permissão persistida', () => { for (const path of ['', 'preparar-cobranca']) { const route = routes.find(candidate => candidate.path === path); expect(route?.canActivate).toContain(permissionGuard); expect(route?.data?.['permissions']).toEqual(['GERAL_FATURAS']); expect(route?.loadComponent).toBeDefined(); } }); });
