import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, ConfirmationConfig } from '../../services/confirmation.service';
import { Subscription } from 'rxjs';
import { NeriTechIcon, NeriTechIconName } from '../neritech-icon/neritech-icon';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [CommonModule, DialogModule, NeriTechIcon],
  template: `
    <p-dialog
      [(visible)]="visible"
      [modal]="true"
      [closable]="true"
      [closeOnEscape]="true"
      [header]="config?.title || 'Confirmação'"
      (onHide)="onDialogHide()"
      [draggable]="false"
      [resizable]="false"
      styleClass="!p-0 !border-0 !shadow-2xl !rounded-lg !max-w-sm"
      contentStyleClass="!p-0"
      appendTo="body"
    >
      <div class="bg-white rounded-lg overflow-hidden">
        <!-- Header -->
        <div class="px-5 pt-5 pb-3">
          <div class="flex items-center gap-2.5 mb-2">
            <div
              class="w-8 h-8 rounded-full flex items-center justify-center"
              [ngClass]="{
                'bg-rose-100': config?.type === 'danger',
                'bg-amber-100': config?.type === 'warning',
                'bg-blue-100': config?.type === 'info' || !config?.type
              }"
            >
              <nt-icon
                [name]="getIconName()"
                [size]="18"
                class="!text-[18px] !w-[18px] !h-[18px]"
                [ngClass]="{
                  'text-rose-600': config?.type === 'danger',
                  'text-amber-600': config?.type === 'warning',
                  'text-blue-600': config?.type === 'info' || !config?.type
                }"
              />
            </div>
          </div>
          <p class="text-sm text-slate-600 leading-relaxed">{{ config?.message }}</p>
        </div>

        <!-- Actions -->
        <div class="bg-slate-50 px-5 py-3 flex gap-2 justify-end border-t border-slate-100">
          <button
            type="button"
            (click)="onCancel()"
            class="min-h-11 px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600 transition-colors"
          >
            {{ config?.cancelText || 'Cancelar' }}
          </button>
          <button
            type="button"
            (click)="onConfirm()"
            class="min-h-11 px-4 py-2 text-sm font-medium text-white rounded-md focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600 transition-colors shadow-sm"
            [ngClass]="{
              'bg-rose-600 hover:bg-rose-700': config?.type === 'danger',
              'bg-amber-700 hover:bg-amber-800': config?.type === 'warning',
              'bg-blue-600 hover:bg-blue-700': config?.type === 'info' || !config?.type
            }"
          >
            {{ config?.confirmText || 'Confirmar' }}
          </button>
        </div>
      </div>
    </p-dialog>
  `
})
export class ConfirmationDialogComponent implements OnInit, OnDestroy {
  visible = false;
  config: ConfirmationConfig | null = null;
  private subscription?: Subscription;

  constructor(private confirmationService: ConfirmationService) {}

  ngOnInit() {
    this.subscription = this.confirmationService.confirmation$.subscribe((config: ConfirmationConfig) => {
      this.config = config;
      this.visible = true;
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  onConfirm() {
    this.visible = false;
    this.confirmationService.sendResult(true);
  }

  onCancel() {
    this.visible = false;
    this.confirmationService.sendResult(false);
  }

  // Escape or the close button must never leave a decision unresolved.
  onDialogHide() {
    this.visible = false;
    this.confirmationService.sendResult(false);
  }

  getIconName(): NeriTechIconName {
    if (this.config?.icon === 'delete' || this.config?.icon === 'warning') return 'alert-circle';
    return this.config?.type === 'danger' || this.config?.type === 'warning' ? 'alert-circle' : 'info-circle';
  }
}
