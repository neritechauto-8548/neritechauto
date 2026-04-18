import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProdutoService } from '../produto.service';
import { ProdutoResponse } from '../models/produto.models';

interface EstoqueItem {
  codigo: number;
  descricao: string;
  codOriginal?: string;
  codFab?: string;
  preco: number;
  end?: string;
  estoqueAtual: number;
  estoqueMin: number;
  estoqueMax: number;
}

@Component({
  selector: 'app-estoque',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    InputTextModule,
    TagModule,
    TooltipModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    ToastModule,
    DialogModule,
    ButtonModule
  ],
  providers: [MessageService],
  templateUrl: './estoque.html',
  styleUrls: ['./estoque.scss'],
})
export class Estoque implements OnInit {
  constructor(private router: Router) {}
  private readonly produtoService = inject(ProdutoService);
  // Barra superior
  imprimirItems: MenuItem[] = [
    { label: 'Imprimir', icon: 'pi pi-print', command: () => this.onImprimir() },
    { label: 'Imprimir etiquetas', icon: 'pi pi-ticket', command: () => this.onImprimirEtiquetas() },
  ];

  arquivosItems: MenuItem[] = [
    { label: 'Exportar CSV', icon: 'pi pi-file-export', command: () => this.onExport('CSV') },
    { label: 'Exportar XML', icon: 'pi pi-file-export', command: () => this.onExport('XML') },
    { separator: true },
    { label: 'Importar CSV', icon: 'pi pi-file-import', command: () => this.onImport('CSV') },
    { label: 'Importar XML', icon: 'pi pi-file-import', command: () => this.openImportXmlDialog() },
  ];

  // Filtros
  ordenarPor = 'NOME';
  ordenarOptions = [
    { label: 'NOME', value: 'NOME' },
    { label: 'CODIGO', value: 'CODIGO' },
    { label: 'PREÇO', value: 'PRECO' },
  ];

  buscaTipo = 'BUSCA GERAL';
  buscaOptions = [
    { label: 'BUSCA GERAL', value: 'BUSCA GERAL' },
    { label: 'CODIGO', value: 'CODIGO' },
    { label: 'DESCRIÇÃO', value: 'DESCRICAO' },
    { label: 'COD ORIGINAL', value: 'COD_ORIGINAL' },
    { label: 'COD FAB', value: 'COD_FAB' },
  ];

  termo = '';

  // Tabela e paginação (mock)
  items: EstoqueItem[] = [];

  rows = 10;
  first = 0;
  totalRecords = 0;
  loading = false;

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  // Current item for the actions menu
  currentItem: EstoqueItem | null = null;

  setItem(item: EstoqueItem) {
    this.currentItem = item;
  }

  // Import Popup State
  displayImportXmlDialog = false;
  selectedFileName = 'Nenhum arquivo escolhido';
  selectedFile: File | null = null;

  openImportXmlDialog() {
    this.selectedFile = null;
    this.selectedFileName = 'Nenhum arquivo escolhido';
    this.displayImportXmlDialog = true;
  }

  onFileSelectedXml(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files[0];
    if (file) {
      this.selectedFile = file;
      this.selectedFileName = file.name;
    } else {
      this.selectedFile = null;
      this.selectedFileName = 'Nenhum arquivo escolhido';
    }
  }

  abrirSeletorArquivoXml() {
    document.getElementById('importXmlFileInput')?.click();
  }

  submitXmlImport() {
    if (!this.selectedFile) return;
    this.importFromXML(this.selectedFile);
    this.displayImportXmlDialog = false;
  }

  ngOnInit() {
    this.buscar();
  }

  buscar() {
    this.loading = true;
    const page = Math.floor(this.first / this.rows);
    const size = this.rows;
    const sort = this.getSortParam();
    this.produtoService.list({ page, size, sort, nome: this.termo, codigo: this.termo }).subscribe({
      next: (res) => {
        this.items = (res?.content || []).map(p => this.mapProdutoToItem(p));
        this.totalRecords = res?.totalElements || this.items.length;
        this.loading = false;
      },
      error: () => {
        this.items = [];
        this.totalRecords = 0;
        this.loading = false;
      },
    });
  }

  private getSortParam(): string {
    switch (this.ordenarPor) {
      case 'CODIGO':
        return 'codigoInterno,asc';
      case 'PREÇO':
      case 'PRECO':
        return 'precoVenda,asc';
      case 'NOME':
      default:
        return 'nome,asc';
    }
  }

  private mapProdutoToItem(p: ProdutoResponse): EstoqueItem {
    return {
      codigo: p.id,
      descricao: p.descricao ? `${p.nome} - ${p.descricao}` : p.nome,
      codOriginal: p.codigoBarras || '',
      codFab: p.codigoFabricante || '',
      preco: p.precoVenda || 0,
      end: '',
      estoqueAtual: p.quantidadeEstoque || 0,
      estoqueMin: p.estoqueMinimo || 0,
      estoqueMax: 0,
    };
  }

  cadastrarNovoProduto() {
    this.router.navigate(['/produtos-servicos/cadastro-produto']);
  }
  onImprimir() {
    this.produtoService.printEstoque().subscribe({
      next: (blob) => {
        const fileUrl = window.URL.createObjectURL(blob);
        window.open(fileUrl, '_blank', 'noopener');
      },
      error: (e) => console.error('Erro ao gerar relatório', e)
    });
  }

  onImprimirEtiquetas() {
    this.produtoService.printEtiquetas().subscribe({
      next: (blob) => {
        const fileUrl = window.URL.createObjectURL(blob);
        window.open(fileUrl, '_blank', 'noopener');
      },
      error: (e) => console.error('Erro ao gerar etiquetas', e)
    });
  }

  editarItem(codigo: number) {
    this.router.navigate(['/produtos-servicos/cadastro-produto', codigo]);
  }

  onExport(tipo: 'CSV' | 'XML') {
    if (tipo === 'CSV') {
      this.exportToCSV();
    } else {
      this.exportToXML();
    }
  }

  onImport(tipo: 'CSV' | 'XML') {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = tipo === 'CSV' ? '.csv' : '.xml';
    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        if (tipo === 'CSV') {
          this.importFromCSV(file);
        } else {
          this.importFromXML(file);
        }
      }
    };
    input.click();
  }

  private exportToCSV() {
    const headers = ['Codigo', 'Descricao', 'Cod Original', 'Cod Fab', 'Preco', 'End', 'Estoque Atual', 'Estoque Min', 'Estoque Max'];
    const csvContent = [
      headers.join(','),
      ...this.items.map(item => [
        item.codigo,
        `"${item.descricao.replace(/"/g, '""')}"`,
        `"${item.codOriginal || ''}"`,
        `"${item.codFab || ''}"`,
        item.preco,
        `"${item.end || ''}"`,
        item.estoqueAtual,
        item.estoqueMin,
        item.estoqueMax
      ].join(','))
    ].join('\n');

    this.downloadFile(csvContent, 'estoque.csv', 'text/csv');
  }

  private exportToXML() {
    const xmlContent = `<?xml version="1.0" encoding="UTF-8"?>
<estoque>
${this.items.map(item => `  <item>
    <codigo>${item.codigo}</codigo>
    <descricao><![CDATA[${item.descricao}]]></descricao>
    <codOriginal><![CDATA[${item.codOriginal || ''}]]></codOriginal>
    <codFab><![CDATA[${item.codFab || ''}]]></codFab>
    <preco>${item.preco}</preco>
    <end><![CDATA[${item.end || ''}]]></end>
    <estoqueAtual>${item.estoqueAtual}</estoqueAtual>
    <estoqueMin>${item.estoqueMin}</estoqueMin>
    <estoqueMax>${item.estoqueMax}</estoqueMax>
  </item>`).join('\n')}
</estoque>`;

    this.downloadFile(xmlContent, 'estoque.xml', 'application/xml');
  }

  private importFromCSV(file: File) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const csv = e.target?.result as string;
      const lines = csv.split('\n');
      const headers = lines[0].split(',');

      const newItems: EstoqueItem[] = [];
      for (let i = 1; i < lines.length; i++) {
        const line = lines[i].trim();
        if (line) {
          const values = this.parseCSVLine(line);
          if (values.length >= 9) {
            newItems.push({
              codigo: parseInt(values[0]) || 0,
              descricao: values[1].replace(/^"|"$/g, '').replace(/""/g, '"'),
              codOriginal: values[2].replace(/^"|"$/g, ''),
              codFab: values[3].replace(/^"|"$/g, ''),
              preco: parseFloat(values[4]) || 0,
              end: values[5].replace(/^"|"$/g, ''),
              estoqueAtual: parseInt(values[6]) || 0,
              estoqueMin: parseInt(values[7]) || 0,
              estoqueMax: parseInt(values[8]) || 0
            });
          }
        }
      }

      if (newItems.length > 0) {
        this.items = [...this.items, ...newItems];
        alert(`${newItems.length} itens importados com sucesso!`);
      }
    };
    reader.readAsText(file);
  }

  private importFromXML(file: File) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const xml = e.target?.result as string;
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xml, 'application/xml');

      const itemNodes = xmlDoc.getElementsByTagName('item');
      const newItems: EstoqueItem[] = [];

      for (let i = 0; i < itemNodes.length; i++) {
        const item = itemNodes[i];
        newItems.push({
          codigo: parseInt(item.getElementsByTagName('codigo')[0]?.textContent || '0'),
          descricao: item.getElementsByTagName('descricao')[0]?.textContent || '',
          codOriginal: item.getElementsByTagName('codOriginal')[0]?.textContent || '',
          codFab: item.getElementsByTagName('codFab')[0]?.textContent || '',
          preco: parseFloat(item.getElementsByTagName('preco')[0]?.textContent || '0'),
          end: item.getElementsByTagName('end')[0]?.textContent || '',
          estoqueAtual: parseInt(item.getElementsByTagName('estoqueAtual')[0]?.textContent || '0'),
          estoqueMin: parseInt(item.getElementsByTagName('estoqueMin')[0]?.textContent || '0'),
          estoqueMax: parseInt(item.getElementsByTagName('estoqueMax')[0]?.textContent || '0')
        });
      }

      if (newItems.length > 0) {
        this.items = [...this.items, ...newItems];
        alert(`${newItems.length} itens importados com sucesso!`);
      }
    };
    reader.readAsText(file);
  }

  private parseCSVLine(line: string): string[] {
    const result = [];
    let current = '';
    let inQuotes = false;

    for (let i = 0; i < line.length; i++) {
      const char = line[i];

      if (char === '"') {
        if (inQuotes && line[i + 1] === '"') {
          current += '"';
          i++;
        } else {
          inQuotes = !inQuotes;
        }
      } else if (char === ',' && !inQuotes) {
        result.push(current);
        current = '';
      } else {
        current += char;
      }
    }

    result.push(current);
    return result;
  }

  private downloadFile(content: string, filename: string, mimeType: string) {
    const blob = new Blob([content], { type: mimeType });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  onPage(e: any) {
    this.first = e.first;
    this.rows = e.rows;
    this.buscar();
  }

  onSearch() {
    this.first = 0;
    this.buscar();
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
    this.buscar();
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first = this.first + this.rows;
      this.buscar();
    }
  }

  getPage(state: any): number {
    const first = state?.first ?? this.first;
    const rows = state?.rows ?? this.rows;
    return Math.floor((first || 0) / (rows || 10)) + 1;
  }

  getTotalPages(state: any): number {
    const total = state?.totalRecords ?? this.items.length;
    const rows = state?.rows ?? this.rows;
    return Math.max(1, Math.ceil((total || 0) / (rows || 10)));
  }
}
