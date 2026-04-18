import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmationService, ConfirmationConfig } from '../../services/confirmation.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [CommonModule, DialogModule, MatIconModule],
  template: `
    <p-dialog
      [(visible)]="visible"
      [modal]="true"
      [closable]="false"
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
              <mat-icon
                class="!text-[18px] !w-[18px] !h-[18px]"
                [ngClass]="{
                  'text-rose-600': config?.type === 'danger',
                  'text-amber-600': config?.type === 'warning',
                  'text-blue-600': config?.type === 'info' || !config?.type
                }"
              >
                {{ config?.icon || getDefaultIcon() }}
              </mat-icon>
            </div>
            <h3 class="text-base font-semibold text-slate-900">{{ config?.title }}</h3>
          </div>
          <p class="text-sm text-slate-600 leading-relaxed" [innerHTML]="config?.message"></p>
        </div>

        <!-- Actions -->
        <div class="bg-slate-50 px-5 py-3 flex gap-2 justify-end border-t border-slate-100">
          <button
            (click)="onCancel()"
            class="px-3 py-1.5 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 transition-colors"
          >
            {{ config?.cancelText || 'Cancelar' }}
          </button>
          <button
            (click)="onConfirm()"
            class="px-3 py-1.5 text-sm font-medium text-white rounded-md transition-colors shadow-sm"
            [ngClass]="{
              'bg-rose-600 hover:bg-rose-700': config?.type === 'danger',
              'bg-amber-600 hover:bg-amber-700': config?.type === 'warning',
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

  getDefaultIcon(): string {
    switch (this.config?.type) {
      case 'danger': return 'warning';
      case 'warning': return 'error_outline';
      case 'info': return 'info';
      default: return 'help_outline';
    }
  }
}
