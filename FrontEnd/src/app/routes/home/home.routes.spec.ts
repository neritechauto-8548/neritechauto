import { PainelHome } from './painel-home';
import { routes } from './home.routes';

describe('Rotas Home', () => {
  it('usa painéis próprios sem indicador local estimado', () => {
    const paineis = ['financeiro', 'orcamentos', 'operacional'];

    paineis.forEach(caminho => {
      const rota = routes.find(item => item.path === caminho);

      expect(rota?.component).toBe(PainelHome);
      expect(rota?.data?.['metricas']).toBeDefined();
      expect(rota?.data?.['aviso']).toEqual(jasmine.any(String));
    });
  });
});
