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

  ngOnInit() {
    this.service.listFuncoes().subscribe({
      next: (funcoes) => {
        this.funcoes = funcoes;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar funções' }),
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
    this.confirmationService.confirm({
      title: 'Confirmar Exclusão',
      message: 'Tem certeza que deseja excluir esta função? Esta ação não poderá ser desfeita.',
      confirmText: 'Sim, Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete_forever'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.deleteFuncao(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Função excluída com sucesso' });
            this.funcoes = this.funcoes.filter(f => f.id !== id);
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir função' })
        });
      }
    });
  }
}
