import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { forkJoin } from 'rxjs';
import { PermissaoService, FuncaoResponse, PermissaoResponse } from './permissao.service';

interface ModuloPermissoes { modulo: string; permissoes: PermissaoResponse[]; }

@Component({
  selector: 'permissoes',
  standalone: true,
  templateUrl: './permissoes.html',
  styleUrls: ['./permissoes.scss'],
  imports: [CommonModule, FormsModule],
})
export class Permissoes implements OnInit {
  private readonly toast   = inject(HotToastService);
  private readonly service = inject(PermissaoService);

  funcoes:  FuncaoResponse[] = [];
  todasPermissoes: PermissaoResponse[] = [];
  modulosPermissoes: ModuloPermissoes[] = [];

  dialogOpen = false;
  salvando   = false;

  novaFuncao: { nome: string; descricao: string; permissoesIds: number[] } = {
    nome: '', descricao: '', permissoesIds: [],
  };

  ngOnInit() {
    forkJoin({
      funcoes:    this.service.listFuncoes(),
      permissoes: this.service.listPermissoes(),
    }).subscribe({
      next: ({ funcoes, permissoes }) => {
        this.funcoes = funcoes;
        this.todasPermissoes = permissoes;
        this.agruparPermissoes(permissoes);
      },
      error: () => this.toast.error('Erro ao carregar dados'),
    });
  }

  private agruparPermissoes(permissoes: PermissaoResponse[]) {
    const map = new Map<string, PermissaoResponse[]>();
    permissoes.forEach(p => {
      const key = p.modulo || 'Geral';
      if (!map.has(key)) map.set(key, []);
      map.get(key)!.push(p);
    });
    this.modulosPermissoes = Array.from(map.entries())
      .map(([modulo, perms]) => ({ modulo, permissoes: perms }))
      .sort((a, b) => a.modulo.localeCompare(b.modulo));
  }

  abrirDialog() {
    this.novaFuncao = { nome: '', descricao: '', permissoesIds: [] };
    this.dialogOpen = true;
  }
  fecharDialog() { this.dialogOpen = false; }

  togglePermissao(id: number, checked: boolean) {
    if (checked) {
      if (!this.novaFuncao.permissoesIds.includes(id)) this.novaFuncao.permissoesIds.push(id);
    } else {
      this.novaFuncao.permissoesIds = this.novaFuncao.permissoesIds.filter(i => i !== id);
    }
  }

  salvarFuncao() {
    if (!this.novaFuncao.nome.trim()) {
      this.toast.error('Nome da função é obrigatório');
      return;
    }
    this.salvando = true;
    this.service.createFuncao({
      nome: this.novaFuncao.nome,
      descricao: this.novaFuncao.descricao || undefined,
      ativo: true,
      permissoes: this.novaFuncao.permissoesIds.map(id => ({ id })),
    }).subscribe({
      next: (f) => {
        this.funcoes.unshift(f);
        this.toast.success(`Função "${f.nome}" criada com sucesso!`);
        this.salvando = false;
        this.fecharDialog();
      },
      error: (err) => {
        this.salvando = false;
        const msg = err?.error?.message || 'Erro ao criar função';
        this.toast.error(msg);
      },
    });
  }
}
