import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { A11yModule } from '@angular/cdk/a11y';
import { HotToastService } from '@ngxpert/hot-toast';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { FuncionarioRequest, FuncionarioResponse, FuncionarioService } from '../funcionario.service';
import { CargoResponse, CargoService } from '../../cargos/cargo.service';
import { Epi, EpiEntrega, EpiService } from '../../epis/epi.service';
import { ContatoFuncionario, ContatoFuncionarioService, ContatoPayload, MeioContato, TipoMeio } from '../contato-funcionario.service';
import { PermissaoService, FuncaoResponse } from '../../permissoes/permissao.service';
import { UsuarioService } from '../../usuario.service';

type AbaFuncionario = 'geral' | 'profissional' | 'epis' | 'contatos';

@Component({
  selector: 'cadastro-colaborador',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, A11yModule],
  templateUrl: './cadastro-colaborador.html',
  styleUrls: ['./cadastro-colaborador.scss']
})
export class CadastroColaborador implements OnInit {
  @Input() drawerMode = false;
  @Input() drawerFuncionarioId?: number;
  @Output() drawerClosed = new EventEmitter<boolean>();
  private readonly funcionarios = inject(FuncionarioService);
  private readonly cargosService = inject(CargoService);
  private readonly episService = inject(EpiService);
  private readonly contatosService = inject(ContatoFuncionarioService);
  private readonly permissoes = inject(PermissaoService);
  private readonly usuarios = inject(UsuarioService);
  private readonly storage = inject(LocalStorageService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly toast = inject(HotToastService);

  funcionarioId?: number;
  usuarioId?: number;
  aba: AbaFuncionario = 'geral';
  formulario: FuncionarioRequest = { empresaId: 0, nomeCompleto: '', cpf: '', status: 'ATIVO' };
  salvando = false;
  erro = '';
  carregando = false;
  cargos: CargoResponse[] = [];
  fotoSelecionada: File | null = null;
  urlFoto: string | null = null;
  get empresaId(): number { return Number(empresaIdAtual(this.storage) ?? 0); }
  get edicao(): boolean { return !!this.funcionarioId; }
  get formularioValido(): boolean { return this.formulario.nomeCompleto.trim().length >= 2 && this.cpfValido(this.formulario.cpf); }

  epis: Epi[] = [];
  entregas: EpiEntrega[] = [];
  epiAberto = false;
  epiItens = [{ epiId: null as number | null, dataEntrega: '', dataValidade: '' }];
  erroEpi = '';
  salvandoEpi = false;
  carregandoEpi = false;

  contatos: ContatoFuncionario[] = [];
  contatoAberto = false;
  contatoId: number | null = null;
  contato: ContatoPayload = { nome: '', sexo: '', principal: false, meios: [] };
  erroContato = '';
  salvandoContato = false;
  carregandoContato = false;

  usuarioAberto = false;
  usuarioSalvando = false;
  usuarioErro = '';
  usuarioFormulario = { login: '', email: '', senha: '', confirmar: '', grupoId: null as number | null };
  funcoes: FuncaoResponse[] = [];
  mostrarSenha = false;

  ngOnInit(): void {
    this.formulario.empresaId = this.empresaId;
    this.funcionarioId = this.drawerMode ? this.drawerFuncionarioId : (Number(this.route.snapshot.paramMap.get('id')) || undefined);
    this.cargosService.list({ page: 0, size: 100 }).subscribe({ next: pagina => this.cargos = pagina.content || [], error: () => this.cargos = [] });
    if (this.funcionarioId) this.carregar();
  }

  private cpfValido(valor: string): boolean {
    const cpf = (valor || '').replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;
    const digito = (tam: number) => ((cpf.slice(0, tam).split('').reduce((acc, n, i) => acc + Number(n) * (tam + 1 - i), 0) * 10) % 11) % 10;
    return digito(9) === Number(cpf[9]) && digito(10) === Number(cpf[10]);
  }
  formatarCpf(): void {
    const v = (this.formulario.cpf || '').replace(/\D/g, '').slice(0, 11);
    this.formulario.cpf = v.replace(/^(\d{3})(\d)/, '$1.$2').replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3').replace(/\.(\d{3})(\d)/, '.$1-$2');
  }
  formatarCep(): void {
    const cep = (this.formulario.cep || '').replace(/\D/g, '').slice(0, 8);
    this.formulario.cep = cep.replace(/^(\d{5})(\d)/, '$1-$2');
  }
  carregar(): void {
    if (!this.funcionarioId) return;
    this.carregando = true; this.erro = '';
    this.funcionarios.getById(this.funcionarioId).subscribe({
      next: dados => {
        this.formulario = { ...dados, empresaId: this.empresaId };
        this.usuarioId = dados.usuarioId;
        this.carregando = false;
        this.urlFoto = dados.fotoFuncionarioUrl ? this.fotoUrl(dados.id) : null;
        if (this.aba === 'epis') this.carregarEpis();
        if (this.aba === 'contatos') this.carregarContatos();
      },
      error: () => { this.carregando = false; this.erro = 'Não foi possível carregar o cadastro de funcionário.'; }
    });
  }
  fotoUrl(id: number): string { return '/api/v1/rh/funcionarios/' + id + '/foto'; }
  selecionarFoto(evento: Event): void {
    const arquivo = (evento.target as HTMLInputElement).files?.[0];
    if (!arquivo) return;
    if (!['image/png', 'image/jpeg'].includes(arquivo.type) || arquivo.size > 3 * 1024 * 1024) {
      this.erro = 'A foto deve ser PNG ou JPG e ter no máximo 3 MB.'; return;
    }
    this.fotoSelecionada = arquivo;
    if (this.urlFoto?.startsWith('blob:')) URL.revokeObjectURL(this.urlFoto);
    this.urlFoto = URL.createObjectURL(arquivo);
  }
  salvar(): void {
    if (this.salvando) return;
    if (!this.formularioValido) { this.erro = 'Informe nome completo e CPF válido.'; return; }
    if (!this.empresaId) { this.erro = 'Empresa não identificada na sessão.'; return; }
    if (this.formulario.dataNascimento && this.formulario.dataNascimento > new Date().toISOString().slice(0, 10)) {
      this.erro = 'Data de nascimento não pode ser futura.'; return;
    }
    this.erro = ''; this.salvando = true;
    const payload = { ...this.formulario, empresaId: this.empresaId };
    const requisicao = this.funcionarioId
      ? this.funcionarios.update(this.funcionarioId, payload)
      : this.funcionarios.create(payload);
    requisicao.subscribe({
      next: salvo => {
        this.funcionarioId = salvo.id; this.formulario = { ...salvo };
        const finalizar = () => { this.salvando = false; this.toast.success('Funcionário salvo com sucesso.'); this.encerrar(true); };
        if (!this.fotoSelecionada) { finalizar(); return; }
        this.funcionarios.uploadFoto(salvo.id, this.fotoSelecionada).subscribe({
          next: () => finalizar(),
          error: () => { this.salvando = false; this.erro = 'Cadastro salvo, mas a foto não foi enviada. Tente novamente.'; }
        });
      },
      error: falha => {
        this.salvando = false;
        this.erro = falha.status === 409 ? 'Cadastro alterado em outra sessão ou CPF duplicado.'
          : falha.status === 403 ? 'Sem permissão para salvar este funcionário.'
          : 'Não foi possível salvar. Confira os campos e tente novamente.';
      }
    });
  }
  encerrar(salvo: boolean): void {
    if (this.drawerMode) { this.drawerClosed.emit(salvo); return; }
    if (salvo) this.router.navigate(['/configuracoes/colaboradores/cadastro', this.funcionarioId]);
    else this.location.back();
  }
  abaSelecionada(aba: AbaFuncionario): void {
    this.aba = aba;
    if (aba === 'epis') this.carregarEpis();
    if (aba === 'contatos') this.carregarContatos();
  }
  alternarSituacao(): void {
    if (!this.funcionarioId) return;
    if (this.formulario.status !== 'ATIVO') {
      this.formulario.status = 'ATIVO'; this.salvar(); return;
    }
    if (!confirm('Inativar este funcionário? O histórico permanecerá preservado.')) return;
    this.funcionarios.delete(this.funcionarioId).subscribe({
      next: () => { this.toast.success('Funcionário inativado'); this.carregar(); },
      error: () => this.erro = 'Não foi possível inativar este funcionário.'
    });
  }

  carregarEpis(): void {
    if (!this.funcionarioId) return;
    this.carregandoEpi = true;
    this.episService.entregas(this.funcionarioId).subscribe({
      next: itens => { this.entregas = itens; this.carregandoEpi = false; },
      error: () => { this.erroEpi = 'Não foi possível consultar as entregas.'; this.carregandoEpi = false; }
    });
    this.episService.listar('', 0, 100).subscribe({
      next: pagina => this.epis = pagina.content || [],
      error: () => this.erroEpi = 'Não foi possível consultar o catálogo de EPIs.'
    });
  }
  abrirEpi(): void { this.epiItens = [{ epiId: null, dataEntrega: '', dataValidade: '' }]; this.erroEpi = ''; this.epiAberto = true; }
  adicionarOutroEpi(): void { this.epiItens.push({ epiId: null, dataEntrega: '', dataValidade: '' }); }
  removerEpi(indice: number): void { if (this.epiItens.length > 1) this.epiItens.splice(indice, 1); }
  registrarEpis(): void {
    if (!this.funcionarioId || this.salvandoEpi) return;
    if (this.epiItens.some(x => !x.epiId || !x.dataEntrega || (x.dataValidade && x.dataValidade < x.dataEntrega))) {
      this.erroEpi = 'Informe EPI, entrega e validade coerentes.'; return;
    }
    this.salvandoEpi = true; this.erroEpi = '';
    // A API persiste cada entrega. Falhas parciais são explicitadas; o botão não simula lote atômico.
    const itens = [...this.epiItens];
    const proximo = (indice: number) => {
      if (indice === itens.length) {
        this.salvandoEpi = false; this.epiAberto = false; this.toast.success('Entregas registradas.'); this.carregarEpis(); return;
      }
      const item = itens[indice];
      this.episService.entregar(this.funcionarioId!, { epiId: item.epiId!, dataEntrega: item.dataEntrega, dataValidade: item.dataValidade || undefined }).subscribe({
        next: () => proximo(indice + 1),
        error: () => { this.salvandoEpi = false; this.erroEpi = 'Falha no item ' + (indice + 1) + '. Entregas anteriores foram gravadas. Confira o histórico.'; this.carregarEpis(); }
      });
    };
    proximo(0);
  }
  carregarContatos(): void {
    if (!this.funcionarioId) return;
    this.carregandoContato = true;
    this.contatosService.listar(this.funcionarioId).subscribe({
      next: dados => { this.contatos = dados; this.carregandoContato = false; },
      error: () => { this.carregandoContato = false; this.erroContato = 'Falha ao consultar contatos.'; }
    });
  }
  abrirContato(contato?: ContatoFuncionario): void {
    this.contatoAberto = true; this.erroContato = ''; this.contatoId = contato?.id || null;
    this.contato = contato ? { nome: contato.nome, sexo: contato.sexo, dataNascimento: contato.dataNascimento,
      principal: contato.principal, meios: contato.meios.map(m => ({ tipo: m.tipo, valor: m.valor })) }
      : { nome: '', sexo: '', principal: false, meios: [] };
  }
  novoMeio(): void { this.contato.meios.push({ tipo: 'CELULAR', valor: '' }); }
  excluirMeio(indice: number): void { this.contato.meios.splice(indice, 1); }
  valorMeio(contato: ContatoFuncionario, tipo: TipoMeio): string {
    return contato.meios.filter(m => m.tipo === tipo).map(m => m.valor).join(', ') || '—';
  }
  salvarContato(): void {
    if (!this.funcionarioId || this.salvandoContato) return;
    if (this.contato.nome.trim().length < 2) { this.erroContato = 'Nome é obrigatório.'; return; }
    this.salvandoContato = true; this.erroContato = '';
    const req = this.contatoId
      ? this.contatosService.atualizar(this.funcionarioId, this.contatoId, this.contato)
      : this.contatosService.criar(this.funcionarioId, this.contato);
    req.subscribe({
      next: () => { this.salvandoContato = false; this.contatoAberto = false; this.toast.success('Contato salvo.'); this.carregarContatos(); },
      error: erro => { this.salvandoContato = false; this.erroContato = erro.status === 409 ? 'Contato já alterado.' : 'Não foi possível salvar o contato.'; }
    });
  }
  excluirContato(contato: ContatoFuncionario): void {
    if (!this.funcionarioId || !confirm('Excluir este contato?')) return;
    this.contatosService.excluir(this.funcionarioId, contato.id).subscribe({
      next: () => this.carregarContatos(), error: () => this.erroContato = 'Não foi possível excluir o contato.'
    });
  }
  criarUsuario(): void {
    if (!this.funcionarioId || this.usuarioId) return;
    this.usuarioErro = '';
    this.usuarioFormulario = { login: '', email: '', senha: '', confirmar: '', grupoId: null };
    this.permissoes.listFuncoes().subscribe({
      next: grupos => this.funcoes = grupos.filter(g => g.ativo),
      error: () => this.usuarioErro = 'Não foi possível consultar os grupos.'
    });
    this.usuarioAberto = true;
  }
  salvarUsuario(): void {
    if (!this.funcionarioId || this.usuarioSalvando || this.usuarioId) return;
    const u = this.usuarioFormulario;
    if (!u.login.trim() || !u.email.includes('@') || u.senha.length < 8 || u.senha !== u.confirmar || !u.grupoId) {
      this.usuarioErro = 'Informe login, e-mail, senha forte (mínimo 8 caracteres), confirmação e grupo.'; return;
    }
    // O login da autenticação atual é o e-mail. Não armazenar segundo identificador sem contrato de autenticação.
    if (u.login.trim().toLowerCase() !== u.email.trim().toLowerCase()) {
      this.usuarioErro = 'No sistema atual, o login deve ser o próprio e-mail de acesso.'; return;
    }
    this.usuarioSalvando = true; this.usuarioErro = '';
    this.usuarios.create({ nomeCompleto: this.formulario.nomeCompleto, email: u.email.trim(),
      senha: u.senha, ativo: true, bloqueado: false, funcoesIds: [u.grupoId] }).subscribe({
      next: usuario => {
        this.usuarioId = usuario.id;
        this.funcionarios.update(this.funcionarioId!, { ...this.formulario, usuarioId: usuario.id }).subscribe({
          next: () => { this.usuarioSalvando = false; this.usuarioAberto = false; this.toast.success('Usuário criado e vinculado.'); this.carregar(); },
          error: () => { this.usuarioSalvando = false; this.usuarioErro = 'Usuário criado, mas o vínculo falhou. O administrador deve revisar o cadastro; não tente criar outro.'; }
        });
      },
      error: () => { this.usuarioSalvando = false; this.usuarioErro = 'Não foi possível criar o usuário.'; }
    });
  }
}
