import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PanelModule } from 'primeng/panel';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';

@Component({
  selector: 'app-relatorio-contas',
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
  templateUrl: './relatorio-contas.html',
  styleUrls: ['./relatorio-contas.scss'],
})
export class RelatorioContas {
  // Campos conforme imagem de referência
  dataDe = 'VENCIMENTO';
  dataDeOptions = [
    { label: 'Vencimento', value: 'VENCIMENTO' },
    { label: 'Emissão', value: 'EMISSAO' },
    { label: 'Pagamento', value: 'PAGAMENTO' },
  ];

  dataInicial = '';
  dataFinal = '';

  situacaoTipo = 'TODOS';
  situacaoTipoOptions = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'A RECEBER', value: 'A_RECEBER' },
    { label: 'A PAGAR', value: 'A_PAGAR' },
    { label: 'PENDENTE', value: 'PENDENTE' },
    { label: 'PAGO', value: 'PAGO' },
    { label: 'CANCELADO', value: 'CANCELADO' },
  ];

  departamento = 'TODOS';
  departamentoOptions = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'FINANCEIRO', value: 'FINANCEIRO' },
    { label: 'ADMINISTRATIVO', value: 'ADMINISTRATIVO' },
    { label: 'COMERCIAL', value: 'COMERCIAL' },
  ];

  ordenarPor = 'DATA DE VENCIMENTO';
  ordenarOptions = [
    { label: 'DATA DE VENCIMENTO', value: 'VENCIMENTO' },
    { label: 'DATA DE EMISSÃO', value: 'EMISSAO' },
    { label: 'DATA DE PAGAMENTO', value: 'PAGAMENTO' },
    { label: 'CLIENTE', value: 'CLIENTE' },
    { label: 'VALOR', value: 'VALOR' },
  ];

  filtroAvancado = false;

  filtrarPor = 'TODOS - NAO USAR FILTRO';
  filtrarPorOptions = [
    { label: 'TODOS - NAO USAR FILTRO', value: 'TODOS' },
    { label: 'CODIGO', value: 'CODIGO' },
    { label: 'DESCRIÇÃO', value: 'DESCRICAO' },
    { label: 'CLIENTE', value: 'CLIENTE' },
    { label: 'DOCUMENTO', value: 'DOCUMENTO' },
    { label: 'DEPARTAMENTO', value: 'DEPARTAMENTO' },
  ];

  filtro = '';

  gerarRelatorio() {
    const payload = {
      dataDe: this.dataDe,
      dataInicial: this.dataInicial,
      dataFinal: this.dataFinal,
      situacaoTipo: this.situacaoTipo,
      departamento: this.departamento,
      ordenarPor: this.ordenarPor,
      filtroAvancado: this.filtroAvancado,
      filtrarPor: this.filtrarPor,
      filtro: this.filtro,
    };
    console.log('Gerar relatório de contas', payload);
    alert('Relatório de contas gerado com sucesso (demo).');
  }
}