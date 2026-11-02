// BASE 01 (Trello vigente): contrato de navegação do sistema na branch master.
// Executar em FrontEnd: node --test scripts/navigation-contract.test.cjs
const { test } = require('node:test');
const assert = require('node:assert/strict');
const { readFileSync } = require('node:fs');
const { resolve } = require('node:path');

const read = file => readFileSync(resolve(__dirname, '..', file), 'utf8');
const menuText = read('public/data/menu.json');
const menu = JSON.parse(menuText).menu;
const links = [];
const walk = (nodes, parents = []) => {
  for (const item of nodes) {
    const segments = [...parents, item.route].filter(Boolean);
    if (item.type === 'link') links.push({ ...item, fullPath: '/' + segments.join('/') });
    if (item.children) walk(item.children, segments);
  }
};
walk(menu);

// Rotas confirmadas em app.routes.ts + arquivos de rotas lazy na data da BASE-03.
// Não tratar uma rota conceitual da documentação como uma tela Angular existente.
const existing = new Set([
  '/home', '/patio', '/cliente/historico-veiculos', '/cliente/indicadores', '/produtos-servicos/recursos', '/cliente/listar', '/cliente/crm', '/cliente/crm/comunicacao', '/cliente/crm/pesquisa',
  '/agendamento/calendario', '/orcamento/orcamento', '/os',
  '/produtos-servicos/servicos', '/veiculo', '/pdv', '/produtos-servicos/estoque',
  '/fornecedor/compras', '/configuracoes/inventario',
  '/financeiro/contas', '/financeiro/receber', '/financeiro/pagar', '/financeiro/caixa',
  '/financeiro/comissoes', '/fiscal/nfe', '/fiscal/nfce', '/fiscal/nfse',
  '/fiscal/nfe/inutilizar-numeracao', '/fornecedor/lista',
  '/configuracoes/empresa', '/configuracoes/preferencias', '/configuracoes/seguradoras', '/configuracoes/transportadoras', '/configuracoes/usuarios', '/configuracoes/colaboradores',
  '/configuracoes/cargos', '/configuracoes/permissoes', '/configuracoes/departamentos',
  '/configuracoes/setores', '/configuracoes/assinatura', '/configuracoes/formas-pagamento',
  '/configuracoes/checklist', '/configuracoes/contas', '/configuracoes/localizacao',
  '/configuracoes/categoria', '/configuracoes/opcoes-envio',
  '/relatorios/relatorio-vendas', '/relatorios/relatorio-clientes',
  '/relatorios/relatorio-estoque', '/relatorios/relatorio-contas',
  '/relatorios/relatorio-receitas-despesas'
]);

test('menu de produção mantém os grupos atuais definidos para o sistema no Trello', () => {
  assert.deepEqual(menu.map(item => item.name), [
    'Visão geral', 'Gestão de pátio', 'Agendamentos', 'Área do cliente',
    'Orçamentos & Serviços', 'Clientes', 'Gestão de pessoas', 'Gestão de recursos',
    'Peças & Estoque', 'Financeiro', 'Fiscal', 'Importações', 'Relatórios',
    'Venda direta', 'Configurar menu', 'Mais funcionalidades'
  ]);
  assert.doesNotMatch(menuText, /Filiais|CLI-004|"operacional"|"pi pi-/i);
});

test('não oferece cliques para rotas ausentes e mantém Pátio dentro do produto', () => {
  for (const item of links) {
    assert.equal(item.disabled === true, !existing.has(item.fullPath), item.fullPath);
    assert.ok(item.fullPath.startsWith('/') && !item.fullPath.startsWith('//'), item.fullPath);
  }
  assert.equal(links.find(item => item.fullPath === '/patio')?.disabled, false);
  assert.equal(links.filter(item => !item.disabled).length, 48);
  assert.equal(new Set(links.filter(item => !item.disabled).map(item => item.fullPath)).size, 48);
});

test('guards cobrem orçamento, calendário, caixa e rotas administrativas', () => {
  const app = read('src/app/app.routes.ts');
  const finance = read('src/app/routes/financeiro/financeiro.routes.ts');
  const agenda = read('src/app/routes/agendamento/agendamento.routes.ts');
  const admin = read('src/app/routes/configuracoes/configuracoes.routes.ts');
  assert.match(app, /canActivateChild: \[authGuard, subscriptionGuard, permissionGuard\]/);
  assert.match(app, /path: 'orcamento',\s*canActivate: \[permissionGuard\],\s*data: \{ requiredPermissions: 'GERAL_ORCAMENTO' \}/);
  assert.match(app, /\{ path: '\*\*', redirectTo: '404' \}/);
  assert.match(finance, /requiredPermissions: 'FIN_VIS_CAIXA'/);
  assert.match(agenda, /requiredPermissions: 'GERAL_AGENDAMENTO_VISUALIZAR'/);
  assert.ok((admin.match(/requiredPermissions: 'GERAL_CONFIG_SISTEMA'/g) || []).length >= 5);
  assert.ok((admin.match(/canActivate: \[permissionGuard\]/g) || []).length >= 5);
  assert.match(app, /path: 'patio'/);
  assert.match(read('src/app/routes/cliente/cliente.routes.ts'), /path: 'historico-veiculos'/);
  assert.match(read('src/app/routes/produtos-servicos/produtos-servicos.routes.ts'), /path: 'recursos'/);
});

test('larguras globais do shell usam 264/72 px', () => {
  const style = read('src/styles.scss');
  const layout = read('src/app/theme/admin-layout/admin-layout.scss');
  const sidebar = read('src/app/theme/sidebar/sidebar.scss');
  assert.match(style, /--nt-sidebar-width:\s*264px/);
  assert.match(layout, /--sidenav-width:\s*var\(--nt-sidebar-width,\s*264px\)/);
  assert.match(layout, /--sidenav-collapsed-width:\s*72px/);
  assert.match(sidebar, /--nt-sidebar-collapsed-width:\s*72px/);
});

test('prévia UX reutiliza a fonte JSON e não exige autenticação', () => {
  assert.match(read('src/app/core/bootstrap/startup.service.ts'), /return this\.authService\.menu\(\)\.pipe\(/);
  assert.match(read('src/app/core/authentication/auth.service.ts'), /this\.check\(\) \|\| environment\.uxPreview/);
});
