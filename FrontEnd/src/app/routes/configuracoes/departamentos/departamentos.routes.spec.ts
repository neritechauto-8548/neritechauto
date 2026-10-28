import { permissionGuard } from '@core';
import { routes } from '../configuracoes.routes';
describe('Rota ADM-007',()=>{it('protege acesso direto com capability real',()=>{const route=routes.find(r=>r.path==='departamentos');expect(route?.canActivate).toContain(permissionGuard);expect(route?.data?.['requiredPermissions']).toBe('GERAL_CONFIG_SISTEMA');expect(route?.data?.['permissions']).toBeUndefined();});});
