import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { forkJoin, take } from 'rxjs';
import { AuthService } from '@core';
import { environment } from '@env/environment';
import { UsuarioResponse } from '../../configuracoes/permissoes/usuario.models';
import { MinhaContaService } from '../minha-conta.service';
import { Chave, Politica, PreferenciasService } from '../../configuracoes/preferencias/preferencias.service';

@Component({ selector: 'app-profile-overview', templateUrl: './overview.html', styleUrl: './overview.scss', imports: [CommonModule, ReactiveFormsModule, RouterLink] })
export class ProfileOverview implements OnInit {
  @Input() modal = false;
  @Input() abaInicial: 'perfil' | 'matriz' = 'perfil';
  @Output() fecharConta = new EventEmitter<void>();
  private readonly construtor = inject(FormBuilder);
  private readonly servico = inject(MinhaContaService);
  private readonly roteador = inject(Router);
  private readonly autenticacao = inject(AuthService);
  private readonly preferencias = inject(PreferenciasService);
  readonly preVisualizacao = environment.uxPreview;
  usuario?: UsuarioResponse;
  secao: 'geral' | 'email' | 'vinculos' = 'geral';
  aba: 'perfil' | 'matriz' = 'perfil';
  secaoMatriz = 'configGeral';
  grupoMatriz = 'geral';
  politicasMatriz: Partial<Record<Chave, Politica>> = {};
  valoresMatriz: Partial<Record<Chave, string>> = {};
  carregandoMatriz = false;
  salvandoMatriz = false;
  erroMatriz = '';
  sucessoMatriz = '';
  telaCheia = false;
  carregando = false;
  erro = '';
  sucesso = '';
  readonly formularioEmail = this.construtor.nonNullable.group({ email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]] });
  readonly formularioSenha = this.construtor.nonNullable.group({ senhaAtual: ['', [Validators.required, Validators.minLength(12)]], novaSenha: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(128)]], confirmarSenha: ['', Validators.required] });
  visibilidadeSenhas = { atual: false, nova: false, confirmacao: false };
  readonly opcoesCategoriaMatriz: { chave: Chave; rotulo: string }[] = [
    { chave: 'categoriaEmContasReceber', rotulo: 'Exibir categoria do cliente em contas a receber?' },
    { chave: 'categoriaEmVendaDireta', rotulo: 'Exibir categoria do cliente na venda direta?' },
    { chave: 'categoriaEmNotaFiscal', rotulo: 'Exibir categoria do cliente na nota fiscal?' },
    { chave: 'categoriaEmOrdemServico', rotulo: 'Exibir categoria do cliente na ordem de serviço?' },
    { chave: 'categoriaEmListagem', rotulo: 'Exibir categoria do cliente na listagem de cliente?' },
  ];

  ngOnInit(): void { this.aba = this.abaInicial; this.carregar(); if (this.aba === 'matriz') this.carregarPoliticasMatriz(); }
  selecionarAba(aba: 'perfil' | 'matriz'): void { this.aba = aba; if (aba === 'matriz' && !Object.keys(this.politicasMatriz).length) this.carregarPoliticasMatriz(); }
  carregar(): void {
    this.carregando = true; this.erro = '';
    this.servico.obterMinhaConta().pipe(take(1)).subscribe({
      next: usuario => { this.usuario = usuario; this.formularioEmail.patchValue({ email: usuario.email }); this.carregando = false; },
      error: falha => { console.error('Falha ao carregar a própria conta.', { status: falha?.status }); this.erro = 'Não foi possível carregar sua conta. Atualize a tela e tente novamente.'; this.carregando = false; },
    });
  }
  salvarEmail(): void {
    this.sucesso = ''; this.erro = '';
    if (this.formularioEmail.invalid) { this.formularioEmail.markAllAsTouched(); return; }
    const dados = this.formularioEmail.getRawValue();
    const senhaAtual = this.formularioSenha.controls.senhaAtual.value;
    if (!senhaAtual) { this.erro = 'Informe a senha atual em Credenciais de acesso para confirmar a alteração do e-mail.'; this.formularioSenha.controls.senhaAtual.markAsTouched(); return; }
    this.carregando = true;
    this.servico.atualizarEmail(dados.email, senhaAtual).pipe(take(1)).subscribe({
      next: usuario => { this.usuario = usuario; this.carregando = false; this.sucesso = 'E-mail atualizado. Entre novamente usando o novo endereço.'; this.autenticacao.logout().pipe(take(1)).subscribe({ next: () => void this.roteador.navigateByUrl('/auth/login'), error: () => void this.roteador.navigateByUrl('/auth/login') }); },
      error: falha => { console.error('Falha ao salvar o e-mail da própria conta.', { status: falha?.status }); this.carregando = false; this.erro = falha?.status === 409 ? 'Este e-mail já está cadastrado nesta empresa.' : 'Não foi possível atualizar o e-mail. Confira sua senha atual.'; },
    });
  }
  alternarVisibilidadeSenha(campo: 'atual' | 'nova' | 'confirmacao'): void { this.visibilidadeSenhas[campo] = !this.visibilidadeSenhas[campo]; }
  selecionarSecaoMatriz(secao: string, grupo: string): void { this.secaoMatriz = secao; this.grupoMatriz = grupo; if (['configGeral','configSenha','clienteGeral'].includes(secao) && !Object.keys(this.politicasMatriz).length) this.carregarPoliticasMatriz(); }
  carregarPoliticasMatriz(): void {
    this.carregandoMatriz = true; this.erroMatriz = '';
    this.preferencias.listar('', '', undefined, 0, 100).pipe(take(1)).subscribe({
      next: resposta => { this.politicasMatriz = Object.fromEntries(resposta.content.map(p => [p.chave, p])) as Partial<Record<Chave, Politica>>; this.valoresMatriz = Object.fromEntries(resposta.content.map(p => [p.chave, p.valorAplicado])) as Partial<Record<Chave, string>>; this.carregandoMatriz = false; },
      error: falha => { console.error('Falha ao carregar configurações da matriz.', { status: falha?.status }); this.erroMatriz = 'Não foi possível carregar as configurações autorizadas desta empresa.'; this.carregandoMatriz = false; },
    });
  }
  salvarPoliticasMatriz(chaves: Chave[]): void {
    const alteradas = chaves.filter(chave => this.valoresMatriz[chave] !== undefined && this.valoresMatriz[chave] !== (this.politicasMatriz[chave]?.valorAplicado ?? ''));
    if (!alteradas.length || this.salvandoMatriz) return;
    this.salvandoMatriz = true; this.erroMatriz = ''; this.sucessoMatriz = '';
    const requisicoes = alteradas.map(chave => {
      const valor = this.valoresMatriz[chave]!;
      const politica = this.politicasMatriz[chave];
      return politica?.versao == null ? this.preferencias.criar(chave, valor) : this.preferencias.editar(chave, valor, politica.versao);
    });
    forkJoin(requisicoes).pipe(take(1)).subscribe({
      next: atualizadas => { for (const politica of atualizadas) { this.politicasMatriz[politica.chave] = politica; this.valoresMatriz[politica.chave] = politica.valorAplicado; } this.sucessoMatriz = 'Configurações salvas.'; this.salvandoMatriz = false; },
      error: falha => { console.error('Falha ao salvar configurações da matriz.', { status: falha?.status }); this.erroMatriz = falha?.status === 403 ? 'Você não tem permissão para alterar as configurações da empresa.' : falha?.status === 409 ? 'As configurações mudaram em outra sessão. Recarregue e tente novamente.' : 'Não foi possível salvar as configurações.'; this.salvandoMatriz = false; },
    });
  }
  atualizarPolitica(chave: Chave, valor: string): void { this.valoresMatriz[chave] = valor; this.sucessoMatriz = ''; }
  valorCategoriaMatriz(chave: string): string { return this.valoresMatriz[chave as Chave] ?? ''; }
  atualizarCategoriaMatriz(chave: string, valor: string): void { this.atualizarPolitica(chave as Chave, valor); }
  algumaPoliticaAlterada(chaves: Chave[]): boolean { return chaves.some(chave => this.valoresMatriz[chave] !== undefined && this.valoresMatriz[chave] !== (this.politicasMatriz[chave]?.valorAplicado ?? '')); }
  cancelarPoliticasMatriz(chaves: Chave[]): void { for (const chave of chaves) this.valoresMatriz[chave] = this.politicasMatriz[chave]?.valorAplicado ?? this.valorPadraoPolitica(chave); this.erroMatriz = ''; this.sucessoMatriz = ''; }
  private valorPadraoPolitica(chave: Chave): string { return ({ codigoPadraoPecas: 'INTERNO', kmObrigatorio: 'false', atualizarKmAutomaticamente: 'true', ordemSelecaoAvaria: 'LOCAL_AVARIA', periodoExpiracaoSenhas: 'NAO_EXPIRA', categoriaEmContasReceber: 'false', categoriaEmVendaDireta: 'false', categoriaEmNotaFiscal: 'false', categoriaEmOrdemServico: 'false', categoriaEmListagem: 'false' } as Partial<Record<Chave, string>>)[chave] ?? ''; }
  salvarSenha(): void {
    this.sucesso = ''; this.erro = '';
    if (this.formularioSenha.invalid) { this.formularioSenha.markAllAsTouched(); return; }
    const dados = this.formularioSenha.getRawValue();
    if (dados.novaSenha !== dados.confirmarSenha) { this.erro = 'A confirmação da senha não corresponde.'; return; }
    this.carregando = true;
    this.servico.alterarSenha(dados.senhaAtual, dados.novaSenha).pipe(take(1)).subscribe({
      next: () => { this.formularioSenha.reset(); this.carregando = false; this.sucesso = 'Senha atualizada com sucesso.'; },
      error: falha => { console.error('Falha ao atualizar a senha da própria conta.', { status: falha?.status }); this.carregando = false; this.erro = falha?.status === 422 ? 'Use uma frase-senha de 12 a 128 caracteres, diferente da senha atual.' : 'Não foi possível atualizar a senha. Confira a senha atual.'; },
    });
  }
  fechar(): void { if (this.modal) this.fecharConta.emit(); else void this.roteador.navigateByUrl('/home'); }
  alternarTelaCheia(): void { this.telaCheia = !this.telaCheia; }
}
