import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { LocalStorageService } from '@shared/services/storage.service';
import { PanelModule } from 'primeng/panel';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService } from '@shared/services/confirmation.service';
// PrimeNG v20 tabs (standalone components)
import { Tabs, TabList, Tab, TabPanels, TabPanel } from 'primeng/tabs';
import { CategoriaItemDialog } from './categoria-item-dialog';
import { UnidadeItemDialog } from './unidade-item-dialog';
import { CategoriaProdutoService, CategoriaProdutoResponse } from './categoria-produto.service';
import { UnidadeMedidaService, UnidadeMedidaResponse } from './unidade-medida.service';
import { MatIconModule } from '@angular/material/icon';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmationDialogComponent } from '@shared/components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'categoria',
  standalone: true,
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    PanelModule,
    DynamicDialogModule,
    Tabs,
    TabList,
    Tab,
    TabPanels,
    TabPanel,
    MatIconModule,
    InputTextModule,
    ToastModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService],
})
export class Categoria implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly categoriaService = inject(CategoriaProdutoService);
  private readonly unidadeService = inject(UnidadeMedidaService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  dialogRef: DynamicDialogRef<any> | null = null;
  activeIndex = 0;
  loading = false;
  searchTerm = '';
  searchUnidadeTerm = '';

  // Categorias
  categorias: CategoriaProdutoResponse[] = [];
  catFirst = 0;
  catRows = 10;

  // Unidades
  unidades: UnidadeMedidaResponse[] = [];
  unFirst = 0;
  unRows = 10;

  ngOnInit() {
    this.loadCategorias();
    this.loadUnidades();
  }

  loadCategorias() {
    this.loading = true;
    this.categoriaService.list({ size: 1000 }).subscribe({
      next: (page) => {
        this.categorias = page.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao listar categorias', err);
        this.loading = false;
      }
    });
  }

  // Getters para Categoria
  get filteredCategorias() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.categorias;
    return this.categorias.filter(c => c.nome.toLowerCase().includes(term));
  }

  get pagedCategorias() {
    return this.filteredCategorias.slice(this.catFirst, this.catFirst + this.catRows);
  }

  get catTotalRecords() {
    return this.filteredCategorias.length;
  }

  get catRangeStart() {
    return this.catTotalRecords === 0 ? 0 : this.catFirst + 1;
  }

  get catRangeEnd() {
    return Math.min(this.catFirst + this.catRows, this.catTotalRecords);
  }

  onSearch() {
    this.catFirst = 0;
  }

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  goCatNext() {
    if (this.catFirst + this.catRows < this.catTotalRecords) {
      this.catFirst = this.catFirst + this.catRows;
    }
  }

  inicialLetter(nome: string): string {
    return nome ? nome.charAt(0).toUpperCase() : '?';
  }

  openAddDialog() {
    const ref = this.dialogService.open(CategoriaItemDialog, {
      header: 'Incluindo Categoria de produtos',
      width: '800px',
      closable: true,
      dismissableMask: true,
    });
    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result) {
            let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
            if (!tenantId || typeof tenantId === 'object') tenantId = '1';

            result.empresaId = Number(tenantId);

            this.categoriaService.create(result).subscribe({
              next: () => {
                this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Categoria cadastrada com sucesso!' });
                this.loadCategorias();
              },
              error: (err) => {
                console.error('Erro ao criar categoria', err);
                this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar categoria.' });
              }
            });
        }
      });
    }
  }

  openEditDialog(categoria: CategoriaProdutoResponse) {
    const ref = this.dialogService.open(CategoriaItemDialog, {
      header: 'Editando Categoria de produtos',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: categoria
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result) {
            let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
            if (!tenantId || typeof tenantId === 'object') tenantId = '1';

            const payload: any = {
              empresaId: Number(tenantId),
              nome: result.nome,
              ativo: result.ativo
            };

           this.categoriaService.update(categoria.id, payload).subscribe({
             next: () => {
               this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Categoria atualizada com sucesso!' });
               this.loadCategorias();
             },
             error: (err) => {
               console.error('Erro ao atualizar categoria', err);
               this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar categoria.' });
             }
           });
        }
      });
    }
  }

  deleteCategoria(id: number) {
    this.confirmationService.confirm({
      title: 'Excluir Categoria',
      message: 'Tem certeza que deseja excluir esta categoria? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Categoria',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.categoriaService.delete(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Categoria apagada com sucesso!' });
            this.loadCategorias();
          },
          error: (err) => {
            console.error('Erro ao excluir categoria', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir categoria.' });
          }
        });
      }
    });
  }

  loadUnidades() {
    this.unidadeService.list({ size: 1000 }).subscribe({
      next: (page) => {
        this.unidades = page.content || [];
      },
      error: (err) => console.error('Erro ao listar unidades', err)
    });
  }

  // Getters para Unidade
  get filteredUnidades() {
    const term = this.searchUnidadeTerm.trim().toLowerCase();
    if (!term) return this.unidades;
    return this.unidades.filter(u =>
      u.nome.toLowerCase().includes(term) ||
      (u.sigla && u.sigla.toLowerCase().includes(term))
    );
  }

  get pagedUnidades() {
    return this.filteredUnidades.slice(this.unFirst, this.unFirst + this.unRows);
  }

  get unTotalRecords() {
    return this.filteredUnidades.length;
  }

  get unRangeStart() {
    return this.unTotalRecords === 0 ? 0 : this.unFirst + 1;
  }

  get unRangeEnd() {
    return Math.min(this.unFirst + this.unRows, this.unTotalRecords);
  }

  onSearchUnidade() {
    this.unFirst = 0;
  }

  goUnPrev() {
    this.unFirst = Math.max(0, this.unFirst - this.unRows);
  }

  goUnNext() {
    if (this.unFirst + this.unRows < this.unTotalRecords) {
      this.unFirst = this.unFirst + this.unRows;
    }
  }

  openAddUnidadeDialog() {
    const ref = this.dialogService.open(UnidadeItemDialog, {
      header: 'Incluindo Unidade de Medida',
      width: '800px',
      closable: true,
      dismissableMask: true,
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          this.unidadeService.create(result).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Unidade cadastrada com sucesso!' });
              this.loadUnidades();
            },
            error: (err) => {
              console.error('Erro ao criar unidade', err);
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar unidade.' });
            }
          });
        }
      });
    }
  }

  openEditUnidadeDialog(unidade: UnidadeMedidaResponse) {
    const ref = this.dialogService.open(UnidadeItemDialog, {
      header: 'Editando Unidade de Medida',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: unidade
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          const payload = {
            nome: result.nome,
            sigla: result.sigla,
            ativo: result.ativo,
          };

          this.unidadeService.update(unidade.id, payload).subscribe({
             next: () => {
               this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Unidade atualizada com sucesso!' });
               this.loadUnidades();
             },
             error: (err) => {
               console.error('Erro ao atualizar unidade', err);
               this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar unidade.' });
             }
          });
        }
      });
    }
  }

  deleteUnidade(id: number) {
    this.confirmationService.confirm({
      title: 'Excluir Unidade',
      message: 'Tem certeza que deseja excluir esta unidade de medida? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Unidade',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.unidadeService.delete(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Unidade apagada com sucesso!' });
            this.loadUnidades();
          },
          error: (err) => {
            console.error('Erro ao excluir unidade', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir unidade.' });
          }
        });
      }
    });
  }
}
