import { permissionGuard } from '@core';
import { routes } from './recepcao.routes';

describe('Rotas de recepção', () => {
  it('expõe fila e check-in com permissões explícitas', () => {
    const fila = routes.find(route => route.path === 'fila');
    const checkIn = routes.find(route => route.path === 'check-in');
    const checkInAgendado = routes.find(route => route.path === 'check-in/:agendamentoId');

    expect(fila?.canActivate).toContain(permissionGuard);
    expect(fila?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
    expect(checkIn?.canActivate).toContain(permissionGuard);
    expect(checkIn?.data?.['permissions']).toEqual(['OS_INCLUIR']);
    expect(checkInAgendado?.data?.['permissions']).toEqual(['OS_INCLUIR']);
  });
});
