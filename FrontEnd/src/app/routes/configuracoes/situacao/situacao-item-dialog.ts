import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ColorPicker } from 'primeng/colorpicker';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'situacao-item-dialog',
  standalone: true,
  templateUrl: './situacao-item-dialog.html',
  styleUrls: ['./situacao-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, ColorPicker, MatIconModule],
})
export class SituacaoItemDialog {
  isEdit = false;

  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {
    if (config?.data) {
      this.isEdit = !!config.data.id;
      this.form = {
        nmSituacao: config.data.nmSituacao ?? '',
        dsSituacao: config.data.dsSituacao ?? '',
        corSituacao: config.data.corSituacao ?? '#ff0000',
      };
    }
  }

  form = {
    nmSituacao: '',
    dsSituacao: '',
    corSituacao: '#2563EB',
  };

  cancelar() {
    this.ref.close(null);
  }

  salvar() {
    this.ref.close(this.form);
  }
}
