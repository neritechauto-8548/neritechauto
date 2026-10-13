import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { PermissaoService, FuncaoResponse, PermissaoResponse } from './permissao.service';
import { ConfirmationService } from '@shared/services/confirmation.service';



@Component({
  selector: 'permissoes',
  standalone: true,
  templateUrl: './permissoes.html',
  styleUrls: ['./permissoes.scss'],
  imports: [CommonModule, FormsModule, ToastModule],
})
export class Permissoes implements OnInit {
  private readonly messageService = inject(MessageService);
  private readonly service = inject(PermissaoService);

  private readonly router = inject(Router);
  private readonly confirmationService = inject(ConfirmationService);

  funcoes:  FuncaoResponse[] = [];
  loading = false;
  loadError = '';
  deletingId: number | null = null;

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.loadError = '';
    this.service.listFuncoes().subscribe({
      next: (funcoes) => {
        this.funcoes = funcoes;
        this.loading = false;
      },
      error: () => { this.loading = false; this.funcoes = []; this.loadError = 'Não foi possível carregar as funções. Tente novamente.'; },
    });
  }



  abrirCadastro(funcao?: FuncaoResponse) {
    const url = funcao 
      ? `/configuracoes/permissoes/editar/${funcao.id}` 
      : '/configuracoes/permissoes/cadastro';

    this.router.navigate([url], {
      state: funcao ? { 
        id: funcao.id, 
        nome: funcao.nome,
        permissoes: funcao.permissoes
      } : {}
    });
  }

  excluirFuncao(id: number) {
    if (this.deletingId !== null) return;
    this.confirmationService.confirm({
      title: 'Confirmar Exclusão',
      message: 'Tem certeza que deseja excluir esta função? Esta ação não poderá ser desfeita.',
      confirmText: 'Sim, Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete_forever'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.deletingId = id;
        this.service.deleteFuncao(id).subscribe({
          next: () => {
            this.deletingId = null;
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Função excluída com sucesso' });
            this.funcoes = this.funcoes.filter(f => f.id !== id);
          },
          error: () => { this.deletingId = null; this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir função' }); }
        });
      }
    });
  }
}
