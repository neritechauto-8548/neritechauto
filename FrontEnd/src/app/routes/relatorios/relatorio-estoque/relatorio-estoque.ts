import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-estoque',
  standalone: true,
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule],
  templateUrl: './relatorio-estoque.html',
  styleUrls: ['./relatorio-estoque.scss'],
})
export class RelatorioEstoque {
  private relatoriosService = inject(RelatoriosService);
  ordenarOptions = [
    { value: 'nome', label: 'Nome' },
    { value: 'codigo', label: 'Código' },
    { value: 'quantidade', label: 'Quantidade' },
    { value: 'dataCadastro', label: 'Data de cadastro' },
  ];
  ordenarPor = this.ordenarOptions[0].value;
  situacaoOptions = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'DISPONIVEL', label: 'Disponível' },
    { value: 'EM_FALTA', label: 'Em falta' },
    { value: 'RESERVADO', label: 'Reservado' },
  ];
  situacao = this.situacaoOptions[0].value;
  filtro = '';

  gerarRelatorio() {
    console.log('Gerando relatório de estoque...');
    this.relatoriosService.gerarRelatorio('estoque', {}).subscribe({
      next: blob => this.relatoriosService.downloadBlob(blob, 'relatorio-estoque.pdf'),
      error: err => console.error(err),
    });
  }
}
