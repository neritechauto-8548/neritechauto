import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-administrador',
  templateUrl: './administrador.html',
  standalone: true,
  imports: [CommonModule, MatIconModule, ToastModule],
  providers: [MessageService]
})
export class Administrador {
  private readonly adminService = inject(AdminService);
  private readonly messageService = inject(MessageService);

  carregandoFipe = false;
  resultadoFipe: string | null = null;

  carregarFipe() {
    this.carregandoFipe = true;
    this.resultadoFipe = null;
    this.adminService.carregarFipe().subscribe({
      next: (res) => {
        this.carregandoFipe = false;
        this.resultadoFipe = res.mensagem;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: res.mensagem });
      },
      error: (err) => {
        this.carregandoFipe = false;
        const msg = err.error?.mensagem || 'Erro ao carregar dados FIPE.';
        this.resultadoFipe = msg;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      }
    });
  }
}
