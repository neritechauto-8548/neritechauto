import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { AuthService } from '@core';
import { environment } from '@env/environment';
import { UsuarioResponse } from '../../configuracoes/permissoes/usuario.models';
import { MinhaContaService } from '../minha-conta.service';

@Component({ selector: 'app-profile-overview', templateUrl: './overview.html', styleUrl: './overview.scss', imports: [CommonModule, ReactiveFormsModule] })
export class ProfileOverview implements OnInit {
  private readonly construtor = inject(FormBuilder);
  private readonly servico = inject(MinhaContaService);
  private readonly roteador = inject(Router);
  private readonly autenticacao = inject(AuthService);
  readonly preVisualizacao = environment.uxPreview;
  usuario?: UsuarioResponse;
  secao: 'geral' | 'email' | 'vinculos' = 'geral';
  aba: 'perfil' | 'matriz' = 'perfil';
  telaCheia = false;
  carregando = false;
  erro = '';
  sucesso = '';
  readonly formularioEmail = this.construtor.nonNullable.group({ email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]], senhaAtual: ['', [Validators.required, Validators.minLength(12)]] });
  readonly formularioSenha = this.construtor.nonNullable.group({ senhaAtual: ['', [Validators.required, Validators.minLength(12)]], novaSenha: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(128)]], confirmarSenha: ['', Validators.required] });

  ngOnInit(): void { this.carregar(); }
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
    const dados = this.formularioEmail.getRawValue(); this.carregando = true;
    this.servico.atualizarEmail(dados.email, dados.senhaAtual).pipe(take(1)).subscribe({
      next: usuario => { this.usuario = usuario; this.carregando = false; this.sucesso = 'E-mail atualizado. Entre novamente usando o novo endereço.'; this.autenticacao.logout().pipe(take(1)).subscribe({ next: () => void this.roteador.navigateByUrl('/auth/login'), error: () => void this.roteador.navigateByUrl('/auth/login') }); },
      error: falha => { console.error('Falha ao salvar o e-mail da própria conta.', { status: falha?.status }); this.carregando = false; this.erro = falha?.status === 409 ? 'Este e-mail já está cadastrado nesta empresa.' : 'Não foi possível atualizar o e-mail. Confira sua senha atual.'; },
    });
  }
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
  fechar(): void { void this.roteador.navigateByUrl('/home'); }
  alternarTelaCheia(): void { this.telaCheia = !this.telaCheia; }
}
