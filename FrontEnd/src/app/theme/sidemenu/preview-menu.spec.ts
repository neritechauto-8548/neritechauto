import { TestBed } from '@angular/core/testing';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { AuthService } from '@core/authentication';
import { Menu, MenuChildrenItem, MenuService } from '@core';
import { StartupService } from '../../core/bootstrap/startup.service';

describe('Navegação lateral aprovada', () => {
  let startup: StartupService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        StartupService, MenuService,
        { provide: AuthService, useValue: {} },
        { provide: NgxPermissionsService, useValue: {} },
        { provide: NgxRolesService, useValue: {} },
      ],
    });
    startup = TestBed.inject(StartupService);
    menuService = TestBed.inject(MenuService);
  });

  function localizar(itens: Array<Menu | MenuChildrenItem>, nome: string, pais: string[] = []):
    { item: Menu | MenuChildrenItem; caminho: string } | undefined {
    for (const item of itens) {
      const segmentos = [...pais, item.route];
      if (item.name === nome) return { item, caminho: menuService.buildRoute(segmentos) };
      const encontrado = item.children && localizar(item.children, nome, segmentos);
      if (encontrado) return encontrado;
    }
    return undefined;
  }

  it('mantém a ordem e a hierarquia lateral aprovadas', () => {
    const itens = (startup as any).obterMenuPadrao() as Menu[];
    expect(itens.map(item => item.name)).toEqual([
      'Visão geral', 'Gestão de pátio', 'Agendamentos', 'Área do cliente',
      'Orçamentos & Serviços', 'Clientes', 'Gestão de pessoas', 'Gestão de recursos',
      'Peças & Estoque', 'Financeiro', 'Fiscal', 'Importações', 'Relatórios', 'Venda direta',
    ]);
    expect(itens.find(item => item.name === 'Área do cliente')?.children?.map(item => item.name))
      .toEqual(['Solicitações']);
    expect(itens.find(item => item.name === 'Orçamentos & Serviços')?.children?.map(item => item.name))
      .toEqual(['Checklist', 'Diagnósticos', 'Orçamentos', 'Ordens de serviço']);
    expect(itens.find(item => item.name === 'Clientes')?.children?.map(item => item.name))
      .toEqual(['Gerenciar clientes', 'Histórico de veículos']);
    expect(itens.find(item => item.name === 'Gestão de recursos')?.children?.map(item => item.name))
      .toEqual(['Cadastros', 'Seguradoras', 'Fornecedores', 'Transportadoras']);
    expect(itens.find(item => item.name === 'Peças & Estoque')?.children?.map(item => item.name))
      .toEqual(['Produtos', 'Consulta de peças', 'Kits', 'Cotações', 'Compras',
        'Entregas & Devoluções', 'Inventários', 'Correções de estoque', 'Transferências de estoque']);
    expect(itens.find(item => item.name === 'Financeiro')?.children?.map(item => item.name))
      .toEqual(['Contas', 'Pagar & Receber', 'Movimentação financeira', 'Boletos bancários', 'Conciliação bancária']);
    expect(itens.find(item => item.name === 'Fiscal')?.children?.map(item => item.name))
      .toEqual(['Nota fiscal', 'Manifestação do destinatário', 'Sintegra', 'NCM', 'Inutilizar faixa de numeração']);
    expect(JSON.stringify(itens)).not.toMatch(/CRM|Portal do Cliente|Indicadores|Configurar menu/);
  });

  it('não habilita destinos ausentes e preserva módulos confirmados', () => {
    const itens = (startup as any).obterMenuPadrao() as Menu[];
    for (const [nome, caminho] of [
      ['Visão geral', '/home'], ['Gestão de pátio', '/patio'],
      ['Checklist', '/configuracoes/checklist'], ['Orçamentos', '/orcamento/orcamento'],
      ['Ordens de serviço', '/os'], ['Gerenciar clientes', '/cliente/listar'],
      ['Histórico de veículos', '/cliente/historico-veiculos'],
      ['Produtos', '/produtos-servicos/estoque'], ['Compras', '/fornecedor/compras'],
      ['Contas', '/financeiro/contas'], ['Pagar & Receber', '/financeiro/receber'],
      ['Nota fiscal', '/fiscal/nfe/nfe-lista'],
      ['Inutilizar faixa de numeração', '/fiscal/nfe/inutilizar-numeracao'],
      ['Relatórios', '/relatorios/relatorio-vendas'], ['Venda direta', '/pdv'],
    ]) {
      const encontrado = localizar(itens, nome);
      expect(encontrado?.caminho).withContext(nome).toBe(caminho);
      expect(encontrado?.item.disabled).withContext(nome).toBeFalse();
    }
    for (const nome of [
      'Agendamentos', 'Solicitações', 'Diagnósticos', 'Consulta de peças', 'Kits', 'Cotações',
      'Entregas & Devoluções', 'Correções de estoque', 'Transferências de estoque',
      'Boletos bancários', 'Conciliação bancária', 'Manifestação do destinatário',
      'Sintegra', 'NCM', 'Importações',
    ]) {
      expect(localizar(itens, nome)?.item.disabled).withContext(nome).toBeTrue();
    }
  });
});
