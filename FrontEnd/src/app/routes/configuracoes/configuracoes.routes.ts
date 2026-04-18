import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'usuarios',
    loadComponent: () => import('./usuarios/usuarios').then(m => m.UsuariosComponent),
    data: { title: 'Usuários' }
  },
  {
    path: 'usuarios/novo',
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { title: 'Novo Usuário' }
  },
  {
    path: 'usuarios/editar/:id',
    loadComponent: () => import('./usuarios/cadastro-usuario/cadastro-usuario').then(m => m.CadastroUsuarioComponent),
    data: { title: 'Editar Usuário' }
  },
  {
    path: 'empresa',
    loadComponent: () => import('./empresa/empresa').then(m => m.EmpresaConfig),
    data: { title: 'Empresa' }
  },
  {
    path: 'checklist',
    loadComponent: () => import('./checklist/checklist').then(m => m.Checklist),
    data: { title: 'Checklist' }
  },
  {
    path: 'colaboradores',
    loadComponent: () => import('./colaboradores/colaboradores').then(m => m.Colaboradores),
    data: { title: 'Colaboradores' }
  },
  {
    path: 'colaboradores/cadastro',
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { title: 'Novo Colaborador' }
  },
  {
    path: 'colaboradores/cadastro/:id',
    loadComponent: () => import('./colaboradores/cadastro-colaborador/cadastro-colaborador').then(m => m.CadastroColaborador),
    data: { title: 'Editar Colaborador' }
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
    path: 'departamentos',
    loadComponent: () => import('./departamentos/departamentos').then(m => m.Departamentos),
    data: { title: 'Departamentos' }
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
    loadComponent: () => import('./permissoes/permissoes').then(m => m.Permissoes),
    data: { title: 'Permissões' }
  },
  {
    path: 'permissoes/cadastro/:id',
    loadComponent: () => import('./permissoes/permissoes-cadastro').then(m => m.PermissoesCadastro),
    data: { title: 'Cadastro de Permissões' }
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
