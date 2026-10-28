import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ToastModule } from 'primeng/toast';
import { PermissaoService, PermissaoResponse } from './permissao.service';
import { HttpErrorResponse } from '@angular/common/http';

export interface GrupoPermissao {
  chave: string;
  permissoes: PermissaoResponse[];
}

@Component({
  selector: 'permissoes-cadastro',
  standalone: true,
  templateUrl: './permissoes-cadastro.html',
  styleUrls: ['./permissoes-cadastro.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,

    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    ToastModule,
  ],
})
export class PermissoesCadastro implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly permissaoService = inject(PermissaoService);
  private readonly messageService = inject(MessageService);

  form: FormGroup = this.fb.group({
    nomePerfil: ['', [Validators.required, Validators.maxLength(50)]],
    descricao: ['', Validators.maxLength(255)],
    ativo: [true],
    permissoesSelecionadas: this.fb.group({})
  });

  isAdmin = false;
  grupos: GrupoPermissao[] = [];
  todasPermissoes: PermissaoResponse[] = [];
  loading = true;
  funcaoId?: number;
  versao?: number;
  loadError = '';
  historico: Array<{ operacao: string; data: string }> = [];

  ngOnInit() {
    const nav = this.router.getCurrentNavigation();
    const state = (nav && nav.extras && (nav.extras.state as any)) || {};
    
    this.funcaoId = state.id || this.route.snapshot.params['id'];

    if (this.funcaoId) {
      if (state?.nome) {
        this.popularForm(state.nome, state.descricao, state.ativo, state.permissoes, state.sistema, state.versao);
      } else {
        this.carregarFuncao(this.funcaoId);
      }
    } else {
      this.carregarPermissoes();
    }
  }

  carregarFuncao(id: number) {
    this.permissaoService.getFuncao(id).subscribe({
      next: (f) => {
        this.popularForm(f.nome, f.descricao, f.ativo, f.permissoes, f.sistema, f.versao);
        this.permissaoService.historico(id).subscribe({ next: page => this.historico = page.content ?? [] });
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.loadError = error.status === 403 ? 'Você não tem permissão para acessar este perfil.' : 'Não foi possível carregar o perfil.';
      }
    });
  }

  popularForm(nome: string, descricao?: string, ativo?: boolean, permissoes?: PermissaoResponse[], sistema?: boolean, versao?: number) {
    this.form.patchValue({ 
      nomePerfil: nome,
      descricao: descricao || '',
      ativo: ativo ?? true
    });
    this.isAdmin = !!sistema;
    this.versao = versao;
    this.carregarPermissoes(permissoes);
  }

  carregarPermissoes(permissoesSalvas?: PermissaoResponse[]) {
    this.permissaoService.listPermissoes().subscribe({
      next: (permissoes) => {
        this.todasPermissoes = permissoes;
        const agrupadas = permissoes.reduce((acc, p) => {
          if (!acc[p.chave]) {
            acc[p.chave] = [];
          }
          acc[p.chave].push(p);
          return acc;
        }, {} as Record<string, PermissaoResponse[]>);

        this.grupos = Object.keys(agrupadas).map(chave => ({
          chave,
          permissoes: agrupadas[chave]
        }));

        // Cria os formControls dinamicamente e seta os valores se estiver editando
        const permissoesGroup = this.form.get('permissoesSelecionadas') as FormGroup;
        permissoes.forEach(p => {
          const isSelecionado = permissoesSalvas?.some(salva => salva.id === p.id) || false;
          permissoesGroup.addControl(p.valor, this.fb.control({value: isSelecionado, disabled: this.isAdmin}));
        });

        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.loadError = 'Não foi possível carregar a matriz de capabilities.';
      }
    });
  }

  get permissoesForm() {
    return this.form.get('permissoesSelecionadas') as FormGroup;
  }

  salvar() {
    if (this.isAdmin) return;
    
    if (this.form.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'Por favor, preencha todos os campos obrigatórios' });
      return;
    }
    
    this.loading = true;
    const value = this.form.value;
    
    // Filtra as keys selecionadas
    const valoresSelecionados = Object.keys(value.permissoesSelecionadas)
      .filter(key => value.permissoesSelecionadas[key]);
      
    // Mapeia para os IDs originais das permissões
    const permissoesIds = this.todasPermissoes
      .filter(p => valoresSelecionados.includes(p.valor))
      .map(p => ({ id: p.id }));

    const request = {
      nome: value.nomePerfil,
      descricao: value.descricao || value.nomePerfil,
      ativo: value.ativo,
      permissaoIds: permissoesIds.map(p => p.id),
      versao: this.versao
    };

    const action$ = this.funcaoId 
      ? this.permissaoService.updateFuncao(this.funcaoId, request)
      : this.permissaoService.createFuncao(request);

    action$.subscribe({
      next: () => {
        this.loading = false;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Perfil salvo com sucesso' });
        this.router.navigateByUrl('/configuracoes/permissoes');
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        const detail = error.status === 403 ? 'Operação não autorizada.'
          : error.status === 409 ? (error.error?.message || 'Conflito: recarregue o perfil antes de salvar.')
          : error.status === 0 ? 'Sem conexão. Verifique sua rede e tente novamente.'
          : 'Erro ao salvar perfil';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail });
      }
    });
  }

  voltar() {
    this.router.navigateByUrl('/configuracoes/permissoes');
  }
}
