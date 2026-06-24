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
import { OrdemServicoService } from '../os/ordem-servico.service';
import { ItemOSService } from '../os/item-os.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

interface ItemOrcamento { quant: number; descricao: string; valor: number; valorTotal: number; }

@Component({
  standalone: true,
  selector: 'app-visualizar-editar-orcamento',
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule, SelectModule, InputTextModule, DialogModule, SplitButtonModule, ToastModule],
  templateUrl: './visualizar-editar-orcamento.html',
  styleUrls: ['./visualizar-editar-orcamento.scss'],
  providers: [MessageService]
})
export class VisualizarEditarOrcamentoComponent implements OnInit {
  id: number = 0;
  osData: any = null;
  loading = false;

  constructor(
    private route: ActivatedRoute, 
    private router: Router,
    private osService: OrdemServicoService,
    private itemService: ItemOSService,
    private messageService: MessageService
  ) {
    const idStr = this.route.snapshot.paramMap.get('numero');
    this.id = idStr ? Number(idStr) : 0;
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

  itens: any[] = [];
  total = 0;
  
  ngOnInit() { 
    if (this.id) {
      this.carregarOS();
    }
  }

  carregarOS() {
    this.loading = true;
    this.osService.getById(this.id).subscribe({
      next: (os) => {
        this.osData = os;
        this.form.veiculo = os.nomeVeiculo || 'N/A';
        this.form.cliente = os.nomeCliente || 'N/A';
        this.form.descricao = os.observacoesCliente || '';
        this.carregarItens();
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar o orçamento.' });
      }
    });
  }

  carregarItens() {
    this.itemService.listProdutos(this.id).subscribe(produtos => {
      this.itens = produtos.map((p: any) => ({
        id: p.id,
        quant: p.quantidade,
        descricao: p.descricao || p.nomeProduto || 'Produto',
        valor: p.valorUnitario,
        valorTotal: p.valorFinal
      }));
      this.recalcularTotal();
      this.loading = false;
    });
  }

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
  
  salvar() { 
    this.osService.update(this.id, {
      ...this.osData,
      observacoesCliente: this.form.descricao
    }).subscribe({
      next: () => this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Orçamento atualizado!' }),
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar.' })
    });
  }

  aprovar() {
    // Regra de negócio: Converter Orçamento para Manutenção
    this.osService.update(this.id, {
      ...this.osData,
      tipoOS: 'MANUTENCAO' as any
    }).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Aprovado', detail: 'Orçamento convertido em Ordem de Serviço!' });
        setTimeout(() => this.router.navigate(['/os/visualizar-os', this.id]), 1500);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao aprovar.' })
    });
  }
}