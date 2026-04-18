import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { MenuItem } from 'primeng/api';
import { SplitButtonModule } from 'primeng/splitbutton';
import { ActivatedRoute, Router } from '@angular/router';

interface ItemOrcamento { quant: number; descricao: string; valor: number; valorTotal: number; }

@Component({
  standalone: true,
  selector: 'app-visualizar-editar-orcamento',
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule, SelectModule, InputTextModule, DialogModule, SplitButtonModule],
  templateUrl: './visualizar-editar-orcamento.html',
  styleUrls: ['./visualizar-editar-orcamento.scss'],
})
export class VisualizarEditarOrcamentoComponent implements OnInit {
  numero = 1;

  constructor(private route: ActivatedRoute, private router: Router) {
    const n = this.route.snapshot.paramMap.get('numero');
    this.numero = n ? Number(n) : 1;
  }

  // Toolbar actions
  excluir() { alert('Excluir orçamento'); }
  imprimir() { alert('Imprimir orçamento'); }
  formatoCupom() { alert('Formato Cupom'); }

  menuEnviarItems: MenuItem[] = [
    { label: 'Enviar por E-mail', icon: 'pi pi-send', command: () => alert('Enviar por E-mail') },
    { label: 'Enviar por Automação WhatsApp', icon: 'pi pi-whatsapp', command: () => alert('Enviar via Automação WhatsApp') },
    { label: 'Enviar por WhatsApp Web', icon: 'pi pi-whatsapp', command: () => alert('Enviar via WhatsApp Web') },
  ];
  formatBRL(v: number) { return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v); }

  incluirItemVisible = false;
  abrirIncluirItem() { this.incluirItemVisible = true; }
  novoItem = { descricao: '', quant: 1, valor: 0 };
  confirmarIncluirItem() {
    const total = this.novoItem.quant * this.novoItem.valor;
    this.itens.push({ quant: this.novoItem.quant, descricao: this.novoItem.descricao, valor: this.novoItem.valor, valorTotal: total });
    this.novoItem = { descricao: '', quant: 1, valor: 0 };
    this.incluirItemVisible = false;
    this.recalcularTotal();
  }
  removerItem(idx: number) { this.itens.splice(idx, 1); this.recalcularTotal(); }
  confirmarItem(idx: number) { /* ação simulada de confirmar */ }
  duplicarItem(idx: number) {
    const i = this.itens[idx];
    this.itens.splice(idx + 1, 0, { ...i });
    this.recalcularTotal();
  }

  itens: ItemOrcamento[] = [
    { quant: 1, descricao: 'CORREIA DENTADA', valor: 45, valorTotal: 45 },
    { quant: 2, descricao: 'AMORTECEDOR DIANTEIRO', valor: 250, valorTotal: 500 },
    { quant: 2, descricao: 'AMORTECEDOR TRASEIRO', valor: 250, valorTotal: 500 },
    { quant: 1, descricao: 'COIFA DA HOMOCINETICA', valor: 27, valorTotal: 54 },
    { quant: 1, descricao: 'TRIZETA DO CAMBIO AUTOMATICO', valor: 780, valorTotal: 780 },
    { quant: 10, descricao: 'DSADSA', valor: 0.2, valorTotal: 2 },
  ];
  total = 0;
  ngOnInit() { this.recalcularTotal(); }
  recalcularTotal() { this.total = this.itens.reduce((s, i) => s + i.valorTotal, 0); }

  funcionarios = [ { label: 'ALEXANDRE ROMULO A', value: 'alexandre' } ];
  form = {
    funcionario: this.funcionarios[0].value,
    veiculo: 'PEUGEOT',
    email: 'MODESTO@TESTEEXEMPLO',
    celular: '3138995700',
    emissao: '2016-08-02',
    validade: '2016-10-26',
    cliente: 'JULIO MODESTO DA SILVA COSTA',
    descricao: 'Aqui pode-se adicionar uma descrição deste orcamento ou alguma informação relevante para o cliente.',
  };

  cancelar() { this.router.navigate(['/orcamento/orcamento']); }
  salvar() { alert('Orçamento atualizado com sucesso'); }
}