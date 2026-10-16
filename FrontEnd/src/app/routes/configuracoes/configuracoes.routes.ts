import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
    path: 'usuarios',
    canActivate: [permissionGuard],
    loadComponent: () => import('./usuarios/usuarios').then(m => m.UsuariosComponent),
    data: { title: 'Usuários', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'usuarios/novo',
    canActivate: [permissionGuard],
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { title: 'Novo Usuário', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'usuarios/editar/:id',
    canActivate: [permissionGuard],
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { title: 'Editar Usuário', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'preferencias',
    canActivate: [permissionGuard],
    loadComponent: () => import('./preferencias/preferencias').then(m => m.PreferenciasGeraisComponent),
    data: { title: 'Configurações gerais', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'empresa',
    canActivate: [permissionGuard],
    loadComponent: () => import('./empresa/empresa').then(m => m.EmpresaConfig),
    data: {
      title: 'Empresa',
      requiredPermissions: ['GERAL_CONFIG_SISTEMA', 'ADM_EMPRESA'],
      permissionMode: 'any'
    }
  },
  {
    path: 'transportadoras',
    canActivate: [permissionGuard],
    loadComponent: () => import('./transportadoras/transportadoras').then(m => m.TransportadorasComponent),
    data: {
      title: 'Transportadoras',
      requiredPermissions: ['GERAL_CONFIG_SISTEMA', 'FORN_LISTAR_PEDIDOS'],
      permissionMode: 'any'
    }
  },
  {
    path: 'seguradoras',
    canActivate: [permissionGuard],
    loadComponent: () => import('./seguradoras/seguradoras').then(m => m.SeguradorasComponent),
    data: {
      title: 'Seguradoras',
      requiredPermissions: ['GERAL_CONFIG_SISTEMA', 'OS_EDITAR'],
      permissionMode: 'any'
    }
  },
  {
    path: 'assinatura',
    loadComponent: () => import('./assinatura/assinatura').then(m => m.AssinaturaComponent),
    data: { title: 'Assinatura' }
  },
  {
    path: 'checklist',
    loadComponent: () => import('./checklist/checklist').then(m => m.Checklist),
    data: { title: 'Checklist' }
  },
  {
    path: 'colaboradores',
    canActivate: [permissionGuard],
    loadComponent: () => import('./colaboradores/colaboradores').then(m => m.Colaboradores),
    data: { title: 'Colaboradores', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'colaboradores/cadastro',
    canActivate: [permissionGuard],
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { title: 'Novo Colaborador', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'colaboradores/cadastro/:id',
    canActivate: [permissionGuard],
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { title: 'Editar Colaborador', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'formas-pagamento',
    loadComponent: () => import('./formas-pagamento/formas-pagamento').then(m => m.FormasPagamento),
    data: { title: 'Formas de Pagamento' }
  },
  {
    path: 'categoria',
    loadComponent: () => import('./categoria/categoria').then(m => m.Categoria),
    data: { title: 'Categoria' }
  },
  {
    path: 'cargos',
    canActivate: [permissionGuard],
    loadComponent: () => import('./cargos/cargos').then(m => m.CargosComponent),
    data: { title: 'Cargos', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'departamentos',
    canActivate: [permissionGuard],
    loadComponent: () => import('./departamentos/departamentos').then(m => m.Departamentos),
    data: { title: 'Áreas', requiredPermissions: 'GERAL_CONFIG_SISTEMA' }
  },
  {
    path: 'setores',
    loadComponent: () => import('./setores/setores').then(m => m.Setores),
    data: { title: 'Setores' }
  },
  {
    path: 'situacao',
    loadComponent: () => import('./situacao/situacao').then(m => m.Situacao),
    data: { title: 'Situações' }
  },
  {
    path: 'contas',
    loadComponent: () => import('./contas/contas').then(m => m.Contas),
    data: { title: 'Contas Bancárias' }
  },
  {
    path: 'localizacao',
    loadComponent: () => import('./localizacao/localizacao').then(m => m.Localizacao),
    data: { title: 'Localização' }
  },
  {
    path: 'inventario',
    loadComponent: () => import('./inventario/inventario').then(m => m.Inventario),
    data: { title: 'Inventário' }
  },
  {
    path: 'inventario/:id/itens',
    loadComponent: () => import('./inventario/itens/itens-inventario-page').then(m => m.ItensInventarioPage),
    data: { title: 'Peças Bipadas' }
  },
  {
    path: 'questionario-envio',
    loadComponent: () => import('./questionamento/questionamento').then(m => m.Questionamento),
    data: { title: 'Questionário de Envio' },
  },
  {
    path: 'opcoes-envio',
    loadComponent: () => import('./modelos-mensagens/modelos-mensagens').then(m => m.ModelosMensagens),
    data: { title: 'Opções de Envio' },
  },
  {
    path: 'opcoes-envio/cadastro',
    loadComponent: () => import('./modelos-mensagens/cadastro-mensagem/cadastro-mensagem').then(m => m.CadastroMensagem),
    data: { title: 'Novo Modelo de Envio' },
  },
  {
    path: 'opcoes-envio/editar/:id',
    loadComponent: () => import('./modelos-mensagens/cadastro-mensagem/cadastro-mensagem').then(m => m.CadastroMensagem),
    data: { title: 'Editar Modelo de Envio' },
  },
  {
    path: 'permissoes',
    canActivate: [permissionGuard],
    loadComponent: () => import('./permissoes/permissoes').then(m => m.Permissoes),
    data: { title: 'Permissões', requiredPermissions: 'ADMIN' }
  },
  {
    path: 'permissoes/cadastro',
    canActivate: [permissionGuard],
    loadComponent: () => import('./permissoes/permissoes-cadastro').then(m => m.PermissoesCadastro),
    data: { title: 'Cadastro de Perfil', requiredPermissions: 'ADMIN' }
  },
  {
    path: 'permissoes/editar/:id',
    canActivate: [permissionGuard],
    loadComponent: () => import('./permissoes/permissoes-cadastro').then(m => m.PermissoesCadastro),
    data: { title: 'Editar Perfil', requiredPermissions: 'ADMIN' }
  },
  {
    path: 'veiculo-modelo',
    loadComponent: () => import('./veiculo-modelo/veiculo-modelo').then(m => m.VeiculoModelo),
    data: { title: 'Modelos de Veículo' }
  },
  {
    path: 'administrador',
    loadComponent: () => import('./administrador/administrador').then(m => m.Administrador),
    data: { title: 'Administrador' }
  },
  {
    path: '',
    redirectTo: 'usuarios',
    pathMatch: 'full'
  }
];
