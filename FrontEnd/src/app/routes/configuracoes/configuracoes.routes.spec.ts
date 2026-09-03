import { permissionGuard } from '@core';
import { routes } from './configuracoes.routes';

describe('Configurações routes authorization contract', () => {
  const byPath = (path: string) => routes.find(route => route.path === path);

  it('keeps subscription reachable without system configuration permission', () => {
    const assinatura = byPath('assinatura');

    expect(assinatura).toBeDefined();
    expect(assinatura?.canActivate ?? []).not.toContain(permissionGuard);
    expect(assinatura?.data?.['permissions']).toBeUndefined();
  });

  it('protects administrative surfaces with GERAL_CONFIG_SISTEMA', () => {
    const administrativePaths = [
      'usuarios',
      'usuarios/novo',
      'usuarios/editar/:id',
      'empresa',
      'colaboradores',
      'colaboradores/cadastro',
      'colaboradores/cadastro/:id',
      'formas-pagamento',
      'categoria',
      'departamentos',
      'setores',
      'situacao',
      'contas',
      'localizacao',
      'inventario',
      'inventario/:id/itens',
      'questionario-envio',
      'opcoes-envio',
      'opcoes-envio/cadastro',
      'opcoes-envio/editar/:id',
      'permissoes',
      'permissoes/cadastro',
      'permissoes/editar/:id',
      'veiculo-modelo',
      'administrador',
    ];

    administrativePaths.forEach(path => {
      const route = byPath(path);
      expect(route).withContext(`Rota ausente: ${path}`).toBeDefined();
      expect(route?.canActivate)
        .withContext(`Rota ${path} deve exigir permissionGuard`)
        .toContain(permissionGuard);
      expect(route?.data?.['permissions'])
        .withContext(`Rota ${path} deve exigir GERAL_CONFIG_SISTEMA`)
        .toEqual(['GERAL_CONFIG_SISTEMA']);
    });
  });

  it('allows checklist configuration to system administrators or checklist managers', () => {
    const checklist = byPath('checklist');

    expect(checklist?.canActivate).toContain(permissionGuard);
    expect(checklist?.data?.['permissions']).toEqual([
      'GERAL_CONFIG_CHECKLIST',
      'GERAL_CONFIG_SISTEMA',
    ]);
  });
});
