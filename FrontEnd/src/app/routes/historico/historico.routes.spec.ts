import { permissionGuard } from '@core'; import { routes } from './historico.routes';
describe('Rota de histórico operacional',()=>{it('protege a timeline com a permissão persistida',()=>{expect(routes[0].canActivate).toContain(permissionGuard);expect(routes[0].data?.['permissions']).toEqual(['GERAL_USUARIO']);expect(routes[0].loadComponent).toBeDefined();});});
