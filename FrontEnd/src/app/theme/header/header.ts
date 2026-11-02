import { CommonModule } from '@angular/common';
import { Component, DestroyRef, ElementRef, EventEmitter, HostListener, Input, Output, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateService } from '@ngx-translate/core';
import { catchError, filter, map, mergeMap, of, take } from 'rxjs';
import screenfull from 'screenfull';

import { environment } from '@env/environment';
import { AuthService, Menu, MenuChildrenItem, MenuService, SettingsService } from '@core';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { Empresa } from '../../routes/configuracoes/empresa/models/empresa.models';
import { EmpresaService } from '../../routes/configuracoes/empresa/services/empresa.service';
import { UsuarioResponse } from '../../routes/configuracoes/permissoes/usuario.models';
import { UsuarioService } from '../../routes/configuracoes/usuario.service';
import { StartupService } from '../../core/bootstrap/startup.service';
import { Branding } from '../widgets/branding';
import { NotificationButton } from '../widgets/notification-button';
import { UserButton } from '../widgets/user-button';

interface ResultadoBusca {
  nome: string;
  rota: string;
  grupo: string;
}

interface ItemAplicativo {
  nome: string;
  rota: string;
  grupo: string;
  icone: string;
  desabilitado: boolean;
  aviso?: string;
}

interface GrupoAplicativo {
  nome: string;
  itens: ItemAplicativo[];
}

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.scss',
  host: { class: 'matero-header' },
  encapsulation: ViewEncapsulation.None,
  imports: [CommonModule, FormsModule, MatTooltipModule, Branding, RouterLink, RouterLinkActive, NotificationButton, UserButton],
})
export class Header {
  @Input() showToggle = true;
  @Input() showBranding = false;
  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() toggleSidenavNotice = new EventEmitter<void>();

  readonly settings = inject(SettingsService);
  readonly suporteUrl = environment.supportUrl;
  readonly preVisualizacao = environment.uxPreview;
  private readonly roteador = inject(Router);
  private readonly elemento = inject(ElementRef<HTMLElement>);
  private readonly menu = inject(MenuService);
  private readonly traducao = inject(TranslateService);
  private readonly auth = inject(AuthService);
  private readonly armazenamento = inject(LocalStorageService);
  private readonly empresaApi = inject(EmpresaService);
  private readonly usuarioApi = inject(UsuarioService);
  private readonly inicio = inject(StartupService);
  private readonly aoDestruir = inject(DestroyRef);

  @ViewChild('entradaBuscaGlobal') entradaBuscaGlobal?: ElementRef<HTMLInputElement>;
  @ViewChild('botaoBusca') botaoBusca?: ElementRef<HTMLButtonElement>;

  buscaAberta = false;
  modoAcoes = false;
  indiceResultadoAtivo = -1;
  termoBusca = '';
  resultadosBusca: ResultadoBusca[] = [];
  acessosRecentes: ResultadoBusca[] = [];
  itensMenu: Menu[] = [];

  menuAplicativosAberto = false;
  menuEmpresasAberto = false;
  menuUsuariosAberto = false;
  menuPlanoAberto = false;
  categoriaAplicativo: 'sistema' | 'crm' = 'sistema';

  empresas: Empresa[] = [];
  buscaEmpresa = '';
  totalEmpresasAutorizadas = 0;
  empresaAtivaId: number | null = null;
  empresasCarregando = false;
  empresasErro = '';
  verificacaoEmpresa = '';

  usuarios: UsuarioResponse[] = [];
  gruposUsuario: Array<{ nome?: string; name?: string; descricao?: string }> = [];
  abaUsuarios: 'usuarios' | 'grupos' = 'usuarios';
  buscaUsuario = '';
  usuariosCarregando = false;
  usuariosErro = '';
  gruposCarregando = false;
  gruposErro = '';

  readonly acessosSuperiores: ResultadoBusca[] = [
    { nome: 'Aplicativos / Sistema de Gestão / Visão geral', rota: '/home', grupo: 'Aplicativos' },
    { nome: 'Empresas / Cadastro e gestão de empresas', rota: '/configuracoes/empresa', grupo: 'Empresas' },
    { nome: 'Usuários / Gerenciamento de usuários', rota: '/configuracoes/usuarios', grupo: 'Usuários' },
    { nome: 'Integrações / Configurações de integrações', rota: '/configuracoes/preferencias', grupo: 'Integrações' },
    { nome: 'Informativos / Central de informações e ajuda', rota: environment.supportUrl, grupo: 'Informativos' },
    { nome: 'Master / Assinatura e plano', rota: '/configuracoes/assinatura', grupo: 'Master' },
  ];

  readonly itensCrm: ItemAplicativo[] = [
    { nome: 'Visão geral', rota: '', grupo: 'CRM', icone: 'dashboard', desabilitado: true, aviso: 'Em breve' },
    { nome: 'Oportunidades', rota: '/cliente/crm', grupo: 'CRM', icone: 'oportunidades', desabilitado: false },
    { nome: 'Campanhas', rota: '', grupo: 'CRM', icone: 'campanhas', desabilitado: true },
    { nome: 'Pesquisas', rota: '/cliente/crm/pesquisa', grupo: 'CRM', icone: 'pesquisas', desabilitado: false },
    { nome: 'E-mails', rota: '/cliente/crm/comunicacao', grupo: 'CRM', icone: 'email', desabilitado: false },
    { nome: 'Templates de e-mail', rota: '', grupo: 'CRM', icone: 'modelos', desabilitado: true },
  ];

  constructor() {
    this.menu.getAll().pipe(takeUntilDestroyed(this.aoDestruir)).subscribe(itens => {
      this.itensMenu = itens;
      this.carregarAcessosRecentes();
    });
    this.auth.user().pipe(takeUntilDestroyed(this.aoDestruir)).subscribe(usuario => {
      this.empresaAtivaId = empresaIdAtual(this.armazenamento);
    });
    this.roteador.events.pipe(
      filter(evento => evento instanceof NavigationEnd),
      takeUntilDestroyed(this.aoDestruir)
    ).subscribe(() => {
      this.fecharMenus();
      this.registrarAcessoAtual();
    });
  }

  posicaoPainel(tipo: 'aplicativos' | 'empresas' | 'usuarios' | 'plano'): number {
    const seletor = tipo === 'aplicativos' ? '.nt-apps-wrap'
      : tipo === 'empresas' ? '.nt-company-wrap'
      : tipo === 'usuarios' ? '.nt-user-wrap' : '.nt-plan-wrap';
    const ancora = this.elemento.nativeElement.querySelector(seletor) as HTMLElement | null;
    const barra = this.elemento.nativeElement.querySelector('.nt-topbar') as HTMLElement | null;
    if (!ancora || !barra) return 0;
    return Math.max(0, ancora.getBoundingClientRect().left - barra.getBoundingClientRect().left);
  }

  get acessoEmpresas(): boolean { return this.temPermissao('GERAL_CONFIG_SISTEMA', 'ADM_EMPRESA', 'ADMIN') || this.preVisualizacao; }
  get acessoUsuarios(): boolean { return this.temPermissao('GERAL_CONFIG_SISTEMA', 'ADMIN') || this.preVisualizacao; }
  get empresasFiltradas(): Empresa[] {
    const termo = this.buscaEmpresa.trim().toLocaleLowerCase('pt-BR');
    if (!termo) return this.empresas;
    return this.empresas.filter(empresa => `${empresa.nomeFantasia} ${empresa.razaoSocial} ${empresa.cnpj}`
      .toLocaleLowerCase('pt-BR').includes(termo));
  }
  get usuariosFiltrados(): UsuarioResponse[] {
    const termo = this.buscaUsuario.trim().toLocaleLowerCase('pt-BR');
    return this.usuarios.filter(usuario => !termo || `${usuario.nomeCompleto} ${usuario.cargo ?? ''}`
      .toLocaleLowerCase('pt-BR').includes(termo)).slice(0, 5);
  }
  get gruposAplicativo(): GrupoAplicativo[] {
    const grupos: GrupoAplicativo[] = [];
    const itens = this.itensMenu.length ? this.itensMenu : this.inicio.obterMenuPadrao();
    for (const item of itens) {
      if (item.type === 'link') {
        grupos.push({ nome: this.nomeVisivel(item.name), itens: [this.converterItemAplicativo(item, [])] });
      } else if (item.type === 'sub' && item.children?.length) {
        const filhos = item.children.filter(filho => filho.type === 'link');
        if (filhos.length) grupos.push({
          nome: this.nomeVisivel(item.name),
          itens: filhos.map(filho => this.converterItemAplicativo(filho, [item.route])),
        });
      }
    }
    return grupos;
  }

  nomeVisivel(chave: string): string {
    const traduzido = this.traducao.instant(chave);
    if (typeof traduzido === 'string' && traduzido !== chave) return traduzido;
    return chave.replace(/^menu\./, '').split('.').pop()?.replace(/[_-]/g, ' ') || chave;
  }

  alternarMenuAplicativos(): void {
    const abrir = !this.menuAplicativosAberto;
    this.fecharMenus();
    this.menuAplicativosAberto = abrir;
  }
  alternarMenuEmpresas(): void {
    const abrir = !this.menuEmpresasAberto;
    this.fecharMenus();
    this.menuEmpresasAberto = abrir;
    if (abrir) this.carregarEmpresas();
  }
  alternarMenuUsuarios(): void {
    const abrir = !this.menuUsuariosAberto;
    this.fecharMenus();
    this.menuUsuariosAberto = abrir;
    if (abrir) this.carregarUsuarios();
  }
  alternarMenuPlano(): void {
    const abrir = !this.menuPlanoAberto;
    this.fecharMenus();
    this.menuPlanoAberto = abrir;
  }
  fecharMenus(): void {
    this.menuAplicativosAberto = false;
    this.menuEmpresasAberto = false;
    this.menuUsuariosAberto = false;
    this.menuPlanoAberto = false;
  }

  selecionarCategoriaAplicativo(categoria: 'sistema' | 'crm'): void { this.categoriaAplicativo = categoria; }
  navegarAplicativo(item: ItemAplicativo): void {
    if (item.desabilitado || !item.rota) return;
    this.fecharMenus();
    this.roteador.navigateByUrl(item.rota);
  }

  temPermissao(...permissoes: string[]): boolean {
    if (this.preVisualizacao) return true;
    const usuario = this.usuarioAtual;
    const permissoesUsuario = Array.isArray(usuario?.permissions) ? usuario.permissions : [];
    const funcoes = [...(usuario?.funcoes ?? []), ...(usuario?.roles ?? [])]
      .map((funcao: any) => typeof funcao === 'string' ? funcao : (funcao?.nome ?? funcao?.name ?? ''));
    return funcoes.some((funcao: string) => ['ADMIN', 'ADMINISTRADOR', 'SUPER_ADMIN', 'ROLE_ADMIN'].includes(funcao.toUpperCase())) ||
      permissoes.some(permissao => permissoesUsuario.includes(permissao));
  }

  private get usuarioAtual(): any {
    let atual: any = {};
    // O último perfil vem do mesmo fluxo autenticado que alimenta o menu e a sessão.
    this.auth.user().pipe(take(1)).subscribe(usuario => atual = usuario ?? {});
    return atual;
  }

  private carregarEmpresas(): void {
    this.empresasErro = '';
    this.verificacaoEmpresa = '';
    if (this.preVisualizacao) {
      this.empresas = [];
      this.totalEmpresasAutorizadas = 0;
      this.empresasErro = 'Entre com sua conta para consultar as empresas autorizadas.';
      return;
    }
    if (!this.acessoEmpresas) {
      this.empresas = [];
      this.empresasErro = 'Seu perfil não tem acesso à gestão de empresas.';
      return;
    }
    this.empresasCarregando = true;
    this.empresaApi.listarEmpresas().pipe(take(1), catchError(erro => {
      console.error('Falha ao consultar empresas autorizadas.', { status: erro?.status });
      this.empresasErro = `Não foi possível consultar as empresas (HTTP ${erro?.status || 'sem resposta'}).`;
      return of({ content: [], totalElements: 0 });
    })).subscribe(resposta => {
      this.empresas = resposta.content ?? [];
      this.totalEmpresasAutorizadas = resposta.totalElements ?? this.empresas.length;
      this.empresasCarregando = false;
      this.empresaAtivaId = empresaIdAtual(this.armazenamento);
    });
  }

  selecionarEmpresa(empresa: Empresa): void {
    if (!empresa.id) return;
    const empresaDoToken = empresaIdAtual(this.armazenamento);
    if (empresaDoToken !== empresa.id) {
      this.verificacaoEmpresa = 'A troca entre empresas ainda não está habilitada para este usuário.';
      return;
    }
    this.verificacaoEmpresa = 'Verificando o acesso à empresa…';
    this.empresaApi.getEmpresa(empresa.id).pipe(take(1)).subscribe({
      next: empresaVerificada => {
        if (empresaVerificada.id !== empresa.id || empresaIdAtual(this.armazenamento) !== empresa.id) {
          this.verificacaoEmpresa = 'O servidor não confirmou esta empresa para sua sessão.';
          return;
        }
        this.armazenamento.set('empresaId', String(empresa.id));
        this.armazenamento.set('tenantId', String(empresa.id));
        this.empresaAtivaId = empresa.id;
        this.verificacaoEmpresa = 'Esta empresa já está ativa nesta sessão.';
      },
      error: erro => {
        console.error('Servidor recusou a confirmação da empresa selecionada.', { status: erro?.status });
        this.verificacaoEmpresa = `Acesso não confirmado pelo servidor (HTTP ${erro?.status || 'sem resposta'}).`;
      },
    });
  }

  private carregarUsuarios(): void {
    this.usuariosErro = '';
    if (this.preVisualizacao) {
      this.usuarios = [];
      this.usuariosErro = 'Entre com sua conta para consultar os usuários da empresa.';
      return;
    }
    if (!this.acessoUsuarios) {
      this.usuarios = [];
      this.usuariosErro = 'Seu perfil não tem acesso ao gerenciamento de usuários.';
      return;
    }
    this.usuariosCarregando = true;
    this.usuarioApi.list().pipe(take(1), catchError(erro => {
      console.error('Falha ao consultar usuários da empresa.', { status: erro?.status });
      this.usuariosErro = 'Não foi possível carregar os usuários. Tente novamente ou abra Gerenciamento de usuários.';
      return of([]);
    })).subscribe(resposta => {
      this.usuarios = Array.isArray(resposta) ? resposta.slice(0, 8) : [];
      this.usuariosCarregando = false;
    });
  }

  selecionarAbaUsuarios(aba: 'usuarios' | 'grupos'): void {
    this.abaUsuarios = aba;
    if (aba !== 'grupos' || this.gruposUsuario.length || this.gruposCarregando || this.gruposErro) return;
    if (this.preVisualizacao) {
      this.gruposErro = 'Entre com sua conta para consultar os grupos de acesso.';
      return;
    }
    this.gruposCarregando = true;
    this.usuarioApi.getRoles().pipe(take(1), catchError(erro => {
      console.error('Falha ao consultar grupos de acesso.', { status: erro?.status });
      this.gruposErro = `Não foi possível consultar grupos (HTTP ${erro?.status || 'sem resposta'}).`;
      return of([]);
    })).subscribe(grupos => {
      this.gruposUsuario = grupos ?? [];
      this.gruposCarregando = false;
    });
  }

  irParaCadastroEmpresa(): void { this.fecharMenus(); this.roteador.navigateByUrl('/configuracoes/empresa'); }
  irParaUsuarios(): void { this.fecharMenus(); this.roteador.navigateByUrl('/configuracoes/usuarios'); }
  irParaNovoUsuario(): void { this.fecharMenus(); this.roteador.navigateByUrl('/configuracoes/usuarios/novo'); }
  abrirAjuda(): void { window.open(this.suporteUrl, '_blank', 'noopener,noreferrer'); }

  alternarBusca(): void {
    this.buscaAberta = !this.buscaAberta;
    if (this.buscaAberta) queueMicrotask(() => this.entradaBuscaGlobal?.nativeElement.focus());
    else this.limparBusca();
  }
  abrirBusca(modoAcoes = false): void {
    this.fecharMenus();
    this.buscaAberta = true;
    this.modoAcoes = modoAcoes;
    this.termoBusca = modoAcoes ? '/' : '';
    this.buscarNoMenu(this.termoBusca);
    queueMicrotask(() => this.entradaBuscaGlobal?.nativeElement.focus());
  }
  fecharBusca(): void {
    this.buscaAberta = false;
    this.limparBusca();
    queueMicrotask(() => this.botaoBusca?.nativeElement.focus());
  }
  limparAcessosRecentes(): void {
    this.acessosRecentes = [];
    if (typeof sessionStorage !== 'undefined') sessionStorage.removeItem('nt-acessos-recentes');
  }

  buscarNoMenu(termo: string): void {
    this.termoBusca = termo;
    this.indiceResultadoAtivo = -1;
    const normalizado = termo.trim().replace(/^\/+/, '').toLocaleLowerCase('pt-BR');
    const consultaAcao = termo.trim().startsWith('/');
    if (!normalizado && !consultaAcao) { this.resultadosBusca = []; return; }
    const encontrados: ResultadoBusca[] = [];
    const percorrer = (itens: Array<Menu | MenuChildrenItem>, pais: string[] = [], rotas: string[] = []) => {
      for (const item of itens) {
        if (item.disabled) continue;
        const nome = this.nomeVisivel(item.name);
        const caminho = item.route ? [...rotas, item.route] : rotas;
        if (item.route && item.type === 'link') {
          const grupo = [...pais, nome].slice(0, -1).join(' › ') || 'Sistema de Gestão';
          const rota = this.menu.buildRoute(caminho);
          if ((!normalizado || `${nome} ${grupo}`.toLocaleLowerCase('pt-BR').includes(normalizado)) &&
              !encontrados.some(resultado => resultado.rota === rota)) {
            encontrados.push({ nome, rota, grupo });
          }
        }
        if (item.children?.length) percorrer(item.children, [...pais, nome], caminho);
      }
    };
    percorrer(this.itensMenu);
    for (const item of this.itensCrm.filter(item => !item.desabilitado)) {
      if ((!normalizado || `${item.nome} CRM`.toLocaleLowerCase('pt-BR').includes(normalizado))) {
        encontrados.push({ nome: item.nome, rota: item.rota, grupo: 'CRM' });
      }
    }
    for (const acesso of this.acessosSuperiores) {
      if ((!normalizado || acesso.nome.toLocaleLowerCase('pt-BR').includes(normalizado)) &&
          !encontrados.some(resultado => resultado.rota === acesso.rota)) encontrados.push(acesso);
    }
    this.resultadosBusca = encontrados.slice(0, 6);
  }

  aoNavegarBusca(evento: KeyboardEvent): void {
    if (evento.key === 'Escape') { evento.preventDefault(); this.fecharBusca(); return; }
    if (!this.resultadosBusca.length) return;
    if (evento.key === 'ArrowDown') {
      evento.preventDefault();
      this.indiceResultadoAtivo = (this.indiceResultadoAtivo + 1) % this.resultadosBusca.length;
    } else if (evento.key === 'ArrowUp') {
      evento.preventDefault();
      this.indiceResultadoAtivo = this.indiceResultadoAtivo <= 0
        ? this.resultadosBusca.length - 1 : this.indiceResultadoAtivo - 1;
    } else if (evento.key === 'Enter') {
      evento.preventDefault();
      this.navegarBusca(this.resultadosBusca[this.indiceResultadoAtivo >= 0 ? this.indiceResultadoAtivo : 0]);
    }
  }
  navegarBusca(resultado: ResultadoBusca): void {
    if (/^https?:\/\//i.test(resultado.rota)) window.open(resultado.rota, '_blank', 'noopener,noreferrer');
    else this.roteador.navigateByUrl(resultado.rota);
    this.fecharBusca();
  }

  @HostListener('document:click', ['$event'])
  aoClicarFora(evento: MouseEvent): void {
    const alvo = evento.target as HTMLElement | null;
    if (!alvo?.closest('.nt-apps-wrap, .nt-apps-popover, .nt-company-wrap, .nt-company-popover, .nt-user-wrap, .nt-user-popover, .nt-plan-wrap, .nt-plan-popover')) this.fecharMenus();
  }

  @HostListener('document:keydown', ['$event'])
  aoAcionarAtalho(evento: KeyboardEvent): void {
    const alvo = evento.target as HTMLElement | null;
    const editando = alvo?.isContentEditable || ['INPUT', 'TEXTAREA', 'SELECT'].includes(alvo?.tagName ?? '');
    if (evento.key === 'Escape') {
      if (this.buscaAberta) this.fecharBusca();
      else this.fecharMenus();
      return;
    }
    if ((evento.ctrlKey || evento.metaKey) && evento.key.toLowerCase() === 'k') {
      evento.preventDefault(); this.abrirBusca(false); return;
    }
    if (!editando && evento.key === '/' && !evento.ctrlKey && !evento.metaKey && !evento.altKey) {
      evento.preventDefault(); this.abrirBusca(true);
    }
  }

  private converterItemAplicativo(item: Menu | MenuChildrenItem, pais: string[]): ItemAplicativo {
    const caminho = [...pais, item.route].filter(Boolean);
    return {
      nome: this.nomeVisivel(item.name),
      rota: this.menu.buildRoute(caminho),
      grupo: pais.map(nome => this.nomeVisivel(nome)).join(' › '),
      icone: item.icon ?? item.name,
      desabilitado: Boolean(item.disabled),
      aviso: item.disabled ? (('label' in item && item.label?.value) || 'Indisponível') : undefined,
    };
  }
  private limparBusca(): void {
    this.termoBusca = '';
    this.resultadosBusca = [];
    this.indiceResultadoAtivo = -1;
    this.modoAcoes = false;
  }
  private registrarAcessoAtual(): void {
    const rota = this.roteador.url.split(/[?#]/)[0].replace(/\/+$/, '') || '/home';
    if (rota.startsWith('http')) return;
    const resultado = this.encontrarRota(rota);
    if (!resultado || typeof sessionStorage === 'undefined') return;
    this.acessosRecentes = [resultado, ...this.acessosRecentes.filter(acesso => acesso.rota !== rota)].slice(0, 5);
    sessionStorage.setItem('nt-acessos-recentes', JSON.stringify(this.acessosRecentes));
  }
  private carregarAcessosRecentes(): void {
    if (typeof sessionStorage === 'undefined') return;
    try {
      const gravados = JSON.parse(sessionStorage.getItem('nt-acessos-recentes') || '[]') as ResultadoBusca[];
      this.acessosRecentes = gravados.filter(acesso => this.encontrarRota(acesso.rota) !== undefined).slice(0, 5);
    } catch {
      this.acessosRecentes = [];
    }
  }
  private encontrarRota(rota: string): ResultadoBusca | undefined {
    let encontrado: ResultadoBusca | undefined;
    const percorrer = (itens: Array<Menu | MenuChildrenItem>, pais: string[] = [], rotas: string[] = []) => {
      for (const item of itens) {
        if (item.disabled) continue;
        const caminho = item.route ? [...rotas, item.route] : rotas;
        if (item.type === 'link' && this.menu.buildRoute(caminho) === rota) {
          const nome = this.nomeVisivel(item.name);
          encontrado = { nome, rota, grupo: pais.map(this.nomeVisivel.bind(this)).join(' › ') || 'Sistema de Gestão' };
          return;
        }
        if (item.children?.length) percorrer(item.children, [...pais, item.name], caminho);
      }
    };
    percorrer(this.itensMenu);
    if (!encontrado) encontrado = this.itensCrm.find(item => !item.desabilitado && item.rota === rota)
      ? { nome: this.itensCrm.find(item => item.rota === rota)!.nome, rota, grupo: 'CRM' } : undefined;
    return encontrado;
  }

  nomeGrupo(grupo: any): string { return String(grupo?.nome ?? grupo?.name ?? grupo?.descricao ?? 'Grupo de acesso'); }
  formatarCnpj(cnpj: string): string {
    const numeros = (cnpj ?? '').replace(/\D/g, '');
    return numeros.length === 14 ? numeros.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5') : cnpj;
  }

  alternarTema(): void { this.settings.setTheme(this.settings.getThemeColor() === 'dark' ? 'light' : 'dark'); }
  alternarTelaCheia(): void { if (screenfull.isEnabled) screenfull.toggle(); }
}
