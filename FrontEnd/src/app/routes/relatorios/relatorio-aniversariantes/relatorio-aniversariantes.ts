import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-aniversariantes',
  standalone: true,
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule, SelectModule],
  templateUrl: './relatorio-aniversariantes.html',
  styleUrls: ['./relatorio-aniversariantes.scss'],
})
export class RelatorioAniversariantes {
  private relatoriosService = inject(RelatoriosService);

  meses = [
    { label: 'Janeiro', value: 1 },
    { label: 'Fevereiro', value: 2 },
    { label: 'Março', value: 3 },
    { label: 'Abril', value: 4 },
    { label: 'Maio', value: 5 },
    { label: 'Junho', value: 6 },
    { label: 'Julho', value: 7 },
    { label: 'Agosto', value: 8 },
    { label: 'Setembro', value: 9 },
    { label: 'Outubro', value: 10 },
    { label: 'Novembro', value: 11 },
    { label: 'Dezembro', value: 12 },
  ];

  mesSelecionado = new Date().getMonth() + 1;
  page = 1;
  totalPages = 1;
  total = 0;
  pageItems: { nome: string; email: string; celular: string; nascimento: string }[] = [];

  gerarRelatorio() {
    console.log('Gerando relatório de aniversariantes para mes:', this.mesSelecionado);
    this.relatoriosService
      .gerarRelatorio('aniversariantes', { mes: this.mesSelecionado })
      .subscribe({
        next: blob => this.relatoriosService.downloadBlob(blob, 'relatorio-aniversariantes.pdf'),
        error: err => console.error(err),
      });
  }

  imprimir() {
    this.gerarRelatorio();
  }

  enviarAlerta() {
    console.log('Enviar alerta de aniversariantes');
  }

  buscar() {
    const dados = [
      { nome: 'João da Silva', email: 'joao@example.com', celular: '(11) 99999-0001', nascimento: '1990-01-10' },
      { nome: 'Maria Souza', email: 'maria@example.com', celular: '(11) 99999-0002', nascimento: '1985-01-22' },
      { nome: 'Carlos Lima', email: 'carlos@example.com', celular: '(11) 99999-0003', nascimento: '1992-01-30' }
    ];
    this.pageItems = dados;
    this.total = dados.length;
    this.page = 1;
    this.totalPages = 1;
  }

  anterior() {
    if (this.page > 1) this.page--;
  }

  proxima() {
    if (this.page < this.totalPages) this.page++;
  }
}
