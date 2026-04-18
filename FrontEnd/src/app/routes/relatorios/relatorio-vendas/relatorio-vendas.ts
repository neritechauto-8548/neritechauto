import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PanelModule } from 'primeng/panel';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-vendas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    PanelModule,
    SelectModule,
    InputTextModule,
    ButtonModule,
    CheckboxModule,
  ],
  templateUrl: './relatorio-vendas.html',
  styleUrls: ['./relatorio-vendas.scss'],
})
export class RelatorioVendas {
  private relatoriosService = inject(RelatoriosService);

  // Filtros conforme imagem de referência
  setor = 'TODOS';
  setores = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'MECÂNICA', value: 'MECANICA' },
    { label: 'ELÉTRICA', value: 'ELETRICA' },
    { label: 'ALINHAMENTO', value: 'ALINHAMENTO' },
  ];

  dataInicial = '';
  dataFinal = '';

  tipo = 'TODOS';
  tipos = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'VENDA', value: 'VENDA' },
    { label: 'SERVIÇO', value: 'SERVICO' },
  ];

  pdvos = 'TODOS';
  pdvosOptions = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'PDV', value: 'PDV' },
    { label: 'OS', value: 'OS' },
  ];

  filtroAvancado = false;

  filtrarPor = 'TODOS - NAO USAR FILTRO';
  filtrarPorOptions = [
    { label: 'TODOS - NAO USAR FILTRO', value: 'TODOS' },
    { label: 'CODIGO', value: 'CODIGO' },
    { label: 'DESCRIÇÃO', value: 'DESCRICAO' },
    { label: 'CLIENTE', value: 'CLIENTE' },
  ];

  filtro = '';

  gerarRelatorio() {
    const payload = {
      setor: this.setor,
      dataInicial: this.dataInicial,
      dataFinal: this.dataFinal,
      tipo: this.tipo,
      pdvos: this.pdvos,
      filtroAvancado: this.filtroAvancado,
      filtrarPor: this.filtrarPor,
      filtro: this.filtro,
    };

    console.log('Gerando relatório de vendas...', payload);

    this.relatoriosService.gerarRelatorio('vendas', payload).subscribe({
      next: blob => {
        this.relatoriosService.downloadBlob(blob, 'relatorio-vendas.pdf');
      },
      error: err => {
        console.error('Erro ao gerar relatório', err);
        alert('Erro ao gerar relatório. Verifique o console.');
      },
    });
  }
}
