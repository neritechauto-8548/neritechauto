import { routes } from './agendamento.routes';

describe('Rotas de agendamento', () => {
  it('protege aniversários e mantém a comunicação segura no componente dedicado', () => {
    const route = routes.find(item => item.path === 'aniversario');

    expect(route).toBeDefined();
    expect(route?.component).toBeDefined();
    expect(route?.loadComponent).toBeUndefined();
    expect(route?.canActivate).toBeDefined();
    expect(route?.data?.['permissions']).toEqual(['GERAL_USUARIO']);
  });
});
