import { ElementRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY, of } from 'rxjs';
import { AuthService, MenuService, SettingsService } from '@core';
import { Header } from './header';
import { EmpresaService } from '../../routes/configuracoes/empresa/services/empresa.service';
import { UsuarioService } from '../../routes/configuracoes/usuario.service';
import { LocalStorageService } from '../../shared/services/storage.service';
import { StartupService } from '../../core/bootstrap/startup.service';

describe('Menu superior', () => {
  let cabecalho: Header;
  let roteador: { events: typeof EMPTY; navigateByUrl: jasmine.Spy };
  let menu: MenuService;
  const empresa = { id: 3, nomeFantasia: 'Empresa autorizada', razaoSocial: 'Empresa autorizada Ltda', cnpj: '12345678000199', ativo: true };

  beforeEach(() => {
    roteador = { events: EMPTY, navigateByUrl: jasmine.createSpy('navigateByUrl') };
    TestBed.configureTestingModule({ providers: [
      MenuService,
      { provide: ElementRef, useValue: { nativeElement: document.createElement('header') } },
      { provide: StartupService, useValue: { obterMenuPadrao: () => [] } },
      { provide: Router, useValue: roteador }, { provide: ActivatedRoute, useValue: {} },
      { provide: SettingsService, useValue: { options: {}, getThemeColor: () => 'light', setTheme: jasmine.createSpy() } },
      { provide: TranslateService, useValue: { instant: (chave: string) => chave } },
      { provide: AuthService, useValue: { user: () => of({ permissions: ['ADMIN'], funcoes: ['ADMIN'] }) } },
      { provide: EmpresaService, useValue: { listarEmpresas: () => of({ content: [empresa], totalElements: 1 }), getEmpresa: (id: number) => of({ ...empresa, id }) } },
      { provide: UsuarioService, useValue: { search: () => of({ content: [], totalElements: 0 }), getRoles: () => of([]) } },
      { provide: LocalStorageService, useValue: { has: () => true, get: () => '3', set: jasmine.createSpy(), remove: jasmine.createSpy() } },
    ] });
    menu = TestBed.inject(MenuService);
    cabecalho = TestBed.runInInjectionContext(() => new Header());
    menu.set([{ route: 'home', name: 'Visão geral', type: 'link', icon: 'home' }, { route: 'clientes', name: 'Clientes', type: 'sub', icon: 'users', children: [{ route: 'listar', name: 'Gerenciar clientes', type: 'link' }] }]);
  });

  it('expõe os seis acessos superiores na busca e navega para destinos disponíveis', () => {
    expect(cabecalho.acessosSuperiores.length).toBe(6);
    cabecalho.buscarNoMenu('Empresas');
    expect(cabecalho.resultadosBusca.map(resultado => resultado.rota)).toContain('/configuracoes/empresa');
    cabecalho.navegarBusca(cabecalho.resultadosBusca[0]);
    expect(roteador.navigateByUrl).toHaveBeenCalled();
  });

  it('mantém apenas um menu superior aberto por vez', () => {
    cabecalho.alternarMenuAplicativos();
    expect(cabecalho.menuAplicativosAberto).toBeTrue();
    cabecalho.alternarMenuPlano();
    expect(cabecalho.menuAplicativosAberto).toBeFalse();
    expect(cabecalho.menuPlanoAberto).toBeTrue();
    cabecalho.fecharMenus();
    expect(cabecalho.menuPlanoAberto).toBeFalse();
  });

  it('carrega empresas e usuários pelo serviço vinculado ao servidor', () => {
    cabecalho.alternarMenuEmpresas();
    expect(cabecalho.empresas).toEqual([empresa]);
    expect(cabecalho.usuarios.length).toBe(0);
  });

  it('abre os dados gerais ao clicar no nome da empresa ativa e fecha o popover', () => {
    cabecalho.menuEmpresasAberto = true;
    cabecalho.abrirEmpresa(empresa);
    expect(roteador.navigateByUrl).toHaveBeenCalledWith('/configuracoes/empresa');
    expect(cabecalho.menuEmpresasAberto).toBeFalse();
  });

  it('não abre uma empresa fora do contexto autenticado', () => {
    cabecalho.abrirEmpresa({ ...empresa, id: 9 });
    expect(roteador.navigateByUrl).not.toHaveBeenCalled();
    expect(cabecalho.verificacaoEmpresa).toContain('confirmar o acesso');
  });
});
