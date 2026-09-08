import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { ModuleWorkspace } from '../system/module-workspace';

const linksOperacionais = [
  {
    title: 'Estoque',
    description: 'Consulte disponibilidade e cadastros antes de iniciar uma venda.',
    route: '/operacional/estoque',
    icon: 'package',
  },
  {
    title: 'Ordens de serviço',
    description: 'Continue vendas vinculadas ao atendimento de um veículo.',
    route: '/ordens-servico',
    icon: 'tool',
  },
  {
    title: 'Contas a receber',
    description: 'Acompanhe títulos já gerados pelo fluxo financeiro.',
    route: '/financeiro/contas',
    icon: 'cash',
  },
];

export const routes: Routes = [
  {
    path: '',
    component: ModuleWorkspace,
    canActivate: [permissionGuard],
    data: {
      title: 'PDV',
      eyebrow: 'Venda de balcão',
      description: 'Venda produtos com baixa de estoque e recebimento confirmados em uma única operação.',
      permissions: ['PDV_LISTAR_VENDAS', 'PDV_REALIZAR_VENDAS'],
      links: linksOperacionais,
      statusTitle: 'Venda segura aguardando comando transacional',
      statusDescription:
        'A finalização permanece bloqueada até o backend oferecer um comando atômico que valide empresa, operador, cliente, preço, estoque, pagamento e numeração. Nenhum saldo ou título é alterado apenas pelo navegador.',
    },
  },
  {
    path: 'listar-vendas',
    component: ModuleWorkspace,
    canActivate: [permissionGuard],
    data: {
      title: 'Vendas de balcão',
      eyebrow: 'PDV',
      description: 'Consulte vendas concluídas sem misturar registros de outras operações ou empresas.',
      permissions: ['PDV_LISTAR_VENDAS'],
      links: linksOperacionais,
      statusTitle: 'Histórico de vendas aguardando read model',
      statusDescription:
        'A listagem será liberada quando a API fornecer paginação, filtros e identificação de cliente e operador em uma consulta tenant-safe. O frontend não filtra todas as OS nem busca clientes registro a registro.',
    },
  },
  {
    path: 'venda-balcao',
    component: ModuleWorkspace,
    canActivate: [permissionGuard],
    data: {
      title: 'Nova venda de balcão',
      eyebrow: 'PDV',
      description: 'Prepare uma venda rápida com rastreabilidade de estoque, pagamento e operador.',
      permissions: ['PDV_REALIZAR_VENDAS'],
      links: linksOperacionais,
      statusTitle: 'Finalização temporariamente indisponível',
      statusDescription:
        'O sistema não cria número aleatório, consumidor padrão, vendedor fictício nem baixa de estoque no navegador. A venda será habilitada após a entrega do contrato transacional autoritativo.',
    },
  },
  {
    path: 'venda-balcao/:id',
    component: ModuleWorkspace,
    canActivate: [permissionGuard],
    data: {
      title: 'Detalhes da venda',
      eyebrow: 'PDV',
      description: 'Revise uma venda preservando cliente, operador, itens, pagamento e eventos de estoque.',
      permissions: ['PDV_LISTAR_VENDAS'],
      links: linksOperacionais,
      statusTitle: 'Detalhe aguardando consulta dedicada',
      statusDescription:
        'A visualização não reutiliza uma ordem de serviço incompleta como venda. O detalhe será liberado com uma consulta própria, auditável e isolada pela empresa autenticada.',
    },
  },
];
