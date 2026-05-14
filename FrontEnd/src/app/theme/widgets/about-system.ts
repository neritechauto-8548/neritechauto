import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { TabsModule } from 'primeng/tabs';
import { AccordionModule } from 'primeng/accordion';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-about-system',
  standalone: true,
  imports: [CommonModule, DialogModule, TabsModule, AccordionModule, MatIconModule],
  template: `
    <p-dialog
      [(visible)]="visible"
      (onHide)="onClose()"
      [modal]="true"
      [draggable]="false"
      [resizable]="false"
      appendTo="body"
      header="Sobre o Sistema"
      [style]="{ width: '550px' }"
      class="about-dialog"
    >
      <div class="flex flex-col gap-4">
        <!-- System Info Header -->
        <div class="flex items-center gap-4 p-4 bg-slate-50 rounded-xl border border-slate-100">
          <div class="w-12 h-12 rounded-xl bg-blue-600 flex items-center justify-center shadow-lg shadow-blue-600/20">
            <span class="text-white font-black text-xl">N</span>
          </div>
          <div>
            <h3 class="text-lg font-bold text-slate-900 leading-tight">NeriTech<span class="text-blue-600">auto</span></h3>
          </div>
          <div class="ml-auto text-right">
            <span class="px-2.5 py-1 bg-blue-100 text-blue-700 rounded-full text-[11px] font-bold border border-blue-200">v1.0.0</span>
          </div>
        </div>

        <p-tabs value="0">
          <p-tablist class="!border-b !border-slate-100">
            <p-tab [value]="0" class="!text-sm !font-bold">Histórico de Versões</p-tab>
          </p-tablist>
          <p-tabpanels class="!px-0 !py-4">
            <p-tabpanel [value]="0">
              <div class="py-4 px-2">
                <div class="flex items-start gap-3 p-4 bg-slate-50 rounded-lg border border-slate-200">
                  <mat-icon class="text-emerald-600 !text-[20px]">check_circle</mat-icon>
                  <div>
                    <p class="text-sm font-bold text-slate-900">Sistema Atualizado</p>
                    <p class="text-xs text-slate-600">Sua versão está em conformidade com as últimas melhorias de segurança e performance.</p>
                  </div>
                </div>
              </div>
            </p-tabpanel>
          </p-tabpanels>
        </p-tabs>
      </div>
    </p-dialog>
  `,
  styles: `
    :host ::ng-deep {
      .about-dialog {
        .p-dialog-header {
          padding: 1.5rem;
          background: white;
          border-bottom: 1px solid #f1f5f9;
        }
        .p-dialog-content {
          padding: 1.5rem;
          background: white;
        }
        .p-tablist-tab-list {
          gap: 1.5rem;
        }
        .p-accordionheader {
          transition: transform 0.2s ease;
          &:hover {
            transform: translateX(4px);
          }
        }
      }
    }
  `
})
export class AboutSystemDialog {
  @Input() visible = false;
  @Output() visibleChange = new EventEmitter<boolean>();

  onClose() {
    this.visible = false;
    this.visibleChange.emit(this.visible);
  }
}
