import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

const systemConfig = {
  canActivate: [permissionGuard],
  data: { permissions: ['GERAL_CONFIG_SISTEMA'] },
};

const checklistConfig = {
  canActivate: [permissionGuard],
  data: { permissions: ['GERAL_CONFIG_CHECKLIST', 'GERAL_CONFIG_SISTEMA'] },
};

export const routes: Routes = [
  {
    path: 'usuarios',
    ...systemConfig,
    loadComponent: () => import('./usuarios/usuarios').then(m => m.UsuariosComponent),
    data: { ...systemConfig.data, title: 'Usuários' },
  },
  {
    path: 'usuarios/novo',
    ...systemConfig,
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { ...systemConfig.data, title: 'Novo Usuário' },
  },
  {
    path: 'usuarios/editar/:id',
    ...systemConfig,
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { ...systemConfig.data, title: 'Editar Usuário' },
  },
  {
    path: 'empresa',
    ...systemConfig,
    loadComponent: () => import('./empresa/empresa').then(m => m.EmpresaConfig),
    data: { ...systemConfig.data, title: 'Empresa' },
  },
  {
    path: 'assinatura',
    loadComponent: () => import('./assinatura/assinatura').then(m => m.AssinaturaComponent),
    data: { title: 'Assinatura' },
  },
  {
    path: 'checklist',
    ...checklistConfig,
    loadComponent: () => import('./checklist/checklist').then(m => m.Checklist),
    data: { ...checklistConfig.data, title: 'Checklist' },
  },
  {
    path: 'colaboradores',
    ...systemConfig,
    loadComponent: () => import('./colaboradores/colaboradores').then(m => m.Colaboradores),
    data: { ...systemConfig.data, title: 'Colaboradores' },
  },
  {
    path: 'colaboradores/cadastro',
    ...systemConfig,
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { ...systemConfig.data, title: 'Novo Colaborador' },
  },
  {
    path: 'colaboradores/cadastro/:id',
    ...systemConfig,
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { ...systemConfig.data, title: 'Editar Colaborador' },
  },
  {
    path: 'formas-pagamento',
    ...systemConfig,
    loadComponent: () => import('./formas-pagamento/formas-pagamento').then(m => m.FormasPagamento),
    data: { ...systemConfig.data, title: 'Formas de Pagamento' },
  },
  {
    path: 'categoria',
    ...systemConfig,
    loadComponent: () => import('./categoria/categoria').then(m => m.Categoria),
    data: { ...systemConfig.data, title: 'Categoria' },
  },
  {
    path: 'departamentos',
    ...systemConfig,
    loadComponent: () => import('./departamentos/departamentos').then(m => m.Departamentos),
    data: { ...systemConfig.data, title: 'Departamentos' },
  },
  {
    path: 'setores',
    ...systemConfig,
    loadComponent: () => import('./setores/setores').then(m => m.Setores),
    data: { ...systemConfig.data, title: 'Setores' },
  },
  {
    path: 'situacao',
    ...systemConfig,
    loadComponent: () => import('./situacao/situacao').then(m => m.Situacao),
    data: { ...systemConfig.data, title: 'Situações' },
  },
  {
    path: 'contas',
    ...systemConfig,
    loadComponent: () => import('./contas/contas').then(m => m.Contas),
    data: { ...systemConfig.data, title: 'Contas Bancárias' },
  },
  {
    path: 'localizacao',
    ...systemConfig,
    loadComponent: () => import('./localizacao/localizacao').then(m => m.Localizacao),
    data: { ...systemConfig.data, title: 'Localização' },
  },
  {
    path: 'inventario',
    ...systemConfig,
    loadComponent: () => import('./inventario/inventario').then(m => m.Inventario),
    data: { ...systemConfig.data, title: 'Inventário' },
  },
  {
    path: 'inventario/:id/itens',
    ...systemConfig,
    loadComponent: () => import('./inventario/itens/itens-inventario-page').then(m => m.ItensInventarioPage),
    data: { ...systemConfig.data, title: 'Peças Bipadas' },
  },
  {
    path: 'questionario-envio',
    ...systemConfig,
    loadComponent: () => import('./questionamento/questionamento').then(m => m.Questionamento),
    data: { ...systemConfig.data, title: 'Questionário de Envio' },
  },
  {
    path: 'opcoes-envio',
    ...systemConfig,
    loadComponent: () => import('./modelos-mensagens/modelos-mensagens').then(m => m.ModelosMensagens),
    data: { ...systemConfig.data, title: 'Opções de Envio' },
  },
  {
    path: 'opcoes-envio/cadastro',
    ...systemConfig,
    loadComponent: () => import('./modelos-mensagens/cadastro-mensagem/cadastro-mensagem').then(m => m.CadastroMensagem),
    data: { ...systemConfig.data, title: 'Novo Modelo de Envio' },
  },
  {
    path: 'opcoes-envio/editar/:id',
    ...systemConfig,
    loadComponent: () => import('./modelos-mensagens/cadastro-mensagem/cadastro-mensagem').then(m => m.CadastroMensagem),
    data: { ...systemConfig.data, title: 'Editar Modelo de Envio' },
  },
  {
    path: 'permissoes',
    ...systemConfig,
    loadComponent: () => import('./permissoes/permissoes').then(m => m.Permissoes),
    data: { ...systemConfig.data, title: 'Permissões' },
  },
  {
    path: 'permissoes/cadastro',
    ...systemConfig,
    loadComponent: () => import('./permissoes/permissoes-cadastro').then(m => m.PermissoesCadastro),
    data: { ...systemConfig.data, title: 'Cadastro de Perfil' },
  },
  {
    path: 'permissoes/editar/:id',
    ...systemConfig,
    loadComponent: () => import('./permissoes/permissoes-cadastro').then(m => m.PermissoesCadastro),
    data: { ...systemConfig.data, title: 'Editar Perfil' },
  },
  {
    path: 'veiculo-modelo',
    ...systemConfig,
    loadComponent: () => import('./veiculo-modelo/veiculo-modelo').then(m => m.VeiculoModelo),
    data: { ...systemConfig.data, title: 'Modelos de Veículo' },
  },
  {
    path: 'administrador',
    ...systemConfig,
    loadComponent: () => import('./administrador/administrador').then(m => m.Administrador),
    data: { ...systemConfig.data, title: 'Administrador' },
  },
  {
    path: '',
    redirectTo: 'usuarios',
    pathMatch: 'full',
  },
];
