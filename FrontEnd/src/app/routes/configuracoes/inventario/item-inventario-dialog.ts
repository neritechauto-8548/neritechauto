import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { MatIconModule } from '@angular/material/icon';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { StatusItemInventario, ItemInventarioResponse } from './item-inventario.service';
import { ProdutoService } from '../../produtos-servicos/produto.service';
import { ProdutoResponse } from '../../produtos-servicos/models/produto.models';
import { LocalizacaoEstoqueService, LocalizacaoEstoqueResponse } from './localizacao-estoque.service';
import { LocalStorageService } from '@shared/services/storage.service';

@Component({
  selector: 'item-inventario-dialog',
  standalone: true,
  templateUrl: './item-inventario-dialog.html',
  imports: [CommonModule, FormsModule, InputTextModule, SelectModule, TextareaModule, ButtonModule, MatIconModule],
})
export class ItemInventarioDialog implements OnInit {
  private readonly produtoService = inject(ProdutoService);
  private readonly localizacaoEstoqueService = inject(LocalizacaoEstoqueService);
  private readonly storage = inject(LocalStorageService);

  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  inventarioId: number = this.config.data?.inventarioId ?? 0;
  itemParaEditar: ItemInventarioResponse | null = this.config.data?.item || null;
  produtos: ProdutoResponse[] = [];
  localizacoes: LocalizacaoEstoqueResponse[] = [];

  form = {
    produtoId: this.itemParaEditar?.produtoId || (null as number | null),
    localizacaoId: this.itemParaEditar?.localizacaoId || (null as number | null),
    loteNumero: this.itemParaEditar?.loteNumero || '',
    quantidadeSistema: this.itemParaEditar?.quantidadeSistema || 0,
    quantidadeContada: this.itemParaEditar?.quantidadeContada !== undefined ? this.itemParaEditar.quantidadeContada : null,
    valorUnitario: this.itemParaEditar?.valorUnitario !== undefined ? this.itemParaEditar.valorUnitario : null,
    status: this.itemParaEditar?.status || ('PENDENTE' as StatusItemInventario),
    motivoDiferenca: this.itemParaEditar?.motivoDiferenca || '',
    observacoes: this.itemParaEditar?.observacoes || '',
    usuarioContagem: this.itemParaEditar?.usuarioContagem || null,
    usuarioConferencia: this.itemParaEditar?.usuarioConferencia || null,
    fotoComprovanteUrl: this.itemParaEditar?.fotoComprovanteUrl || '',
  };

  statusOptions = [
    { label: 'PENDENTE', value: 'PENDENTE' },
    { label: 'CONTADO', value: 'CONTADO' },
    { label: 'CONFERIDO', value: 'CONFERIDO' },
    { label: 'AJUSTADO', value: 'AJUSTADO' },
  ];

  ngOnInit() {
    const tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    const empresaId = Number(tenantId);

    this.produtoService.list({ size: 1000, sort: 'nome,asc' }).subscribe(page => {
      this.produtos = page.content || [];
    });
    this.localizacaoEstoqueService.listAtivasPorEmpresa(empresaId, { size: 1000, sort: 'descricao,asc' }).subscribe(page => {
      this.localizacoes = page.content || [];
    });
  }

  cancelar() {
    this.ref.close(null);
  }

  salvar(fechar: boolean) {
    this.ref.close({ ...this.form, inventarioId: this.inventarioId, continuar: !fechar });
  }
}
