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
            <p class="text-xs font-semibold text-slate-500 uppercase tracking-widest">Versão Profissional</p>
          </div>
          <div class="ml-auto text-right">
            <span class="px-2.5 py-1 bg-blue-100 text-blue-700 rounded-full text-[11px] font-bold border border-blue-200">v1.5.2</span>
          </div>
        </div>

        <p-tabs value="0">
          <p-tablist class="!border-b !border-slate-100">
            <p-tab [value]="0" class="!text-sm !font-bold">Atualizações realizadas</p-tab>
            <p-tab [value]="1" class="!text-sm !font-bold">Atualizações futuras</p-tab>
          </p-tablist>
          <p-tabpanels class="!px-0 !py-4">
            <p-tabpanel [value]="0">
              <p-accordion [multiple]="false" [value]="'0'">
                <p-accordion-panel value="0">
                  <p-accordion-header class="!bg-blue-600 !text-white !rounded-lg !mb-2">
                    <span class="font-bold text-sm">04/08/2025</span>
                  </p-accordion-header>
                  <p-accordion-content>
                    <div class="py-2">
                      <p class="text-[13px] font-bold text-slate-400 mb-2 uppercase tracking-wide">- Melhorias e Novidades:</p>
                      <p class="text-sm text-slate-600 leading-relaxed">
                        <strong class="text-slate-800">- Automações com WhatsApp:</strong> Novo módulo de automações com WhatsApp integrado com a NeriTech. Acesse o menu Configurações > Módulos e Extensões.
                      </p>
                    </div>
                  </p-accordion-content>
                </p-accordion-panel>

                <p-accordion-panel value="1">
                  <p-accordion-header class="!bg-blue-600 !text-white !rounded-lg !mb-2">
                    <span class="font-bold text-sm">24/07/2025</span>
                  </p-accordion-header>
                  <p-accordion-content>
                    <div class="py-2 text-sm text-slate-600">
                      <p>- Ajustes na performance do Dashboard.</p>
                      <p>- Nova visualização de fluxo de caixa mensal.</p>
                      <p>- Melhorias na segurança de tokens JWT.</p>
                    </div>
                  </p-accordion-content>
                </p-accordion-panel>

                <p-accordion-panel value="2">
                  <p-accordion-header class="!bg-blue-600 !text-white !rounded-lg !mb-2">
                    <span class="font-bold text-sm">29/05/2025</span>
                  </p-accordion-header>
                  <p-accordion-content>
                    <div class="py-2 text-sm text-slate-600">
                      <p>- Lançamento do módulo de Checklist Inteligente.</p>
                      <p>- Integração com fornecedores de peças via API.</p>
                    </div>
                  </p-accordion-content>
                </p-accordion-panel>
              </p-accordion>
            </p-tabpanel>

            <p-tabpanel [value]="1">
              <div class="space-y-3 px-2">
                <div class="flex items-start gap-3 p-3 bg-indigo-50 rounded-lg border border-indigo-100">
                  <mat-icon class="text-indigo-600 !text-[20px]">rocket_launch</mat-icon>
                  <div>
                    <p class="text-sm font-bold text-indigo-900">Aplicativo Mobile</p>
                    <p class="text-xs text-indigo-700">Versão nativa para iOS e Android em desenvolvimento.</p>
                  </div>
                </div>
                <div class="flex items-start gap-3 p-3 bg-emerald-50 rounded-lg border border-emerald-100">
                  <mat-icon class="text-emerald-600 !text-[20px]">payments</mat-icon>
                  <div>
                    <p class="text-sm font-bold text-emerald-900">Pagamentos via PIX Automático</p>
                    <p class="text-xs text-emerald-700">Conciliação bancária em tempo real nas O.S.</p>
                  </div>
                </div>
                <div class="flex items-start gap-3 p-3 bg-amber-50 rounded-lg border border-amber-100">
                  <mat-icon class="text-amber-600 !text-[20px]">analytics</mat-icon>
                  <div>
                    <p class="text-sm font-bold text-amber-900">Relatórios com IA</p>
                    <p class="text-xs text-amber-700">Análise preditiva de lucros e sugestão de estoque.</p>
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
