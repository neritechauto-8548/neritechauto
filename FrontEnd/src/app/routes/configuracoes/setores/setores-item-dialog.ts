import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { CheckboxModule } from 'primeng/checkbox';
import { ColorPicker } from 'primeng/colorpicker';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'setores-item-dialog',
  standalone: true,
  templateUrl: './setores-item-dialog.html',
  styleUrls: ['./setores-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, CheckboxModule],
})
export class SetoresItemDialog implements OnInit {
  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  form: any = {
    nome: '',
    ativo: true,
  };

  ngOnInit() {
    if (this.config.data) {
      this.form = {
        id: this.config.data.id,
        nome: this.config.data.nome,
        ativo: this.config.data.ativo !== false
      };
    }
  }

  cancelar() {
    this.ref.close(null);
  }

  adicionar() {
    this.ref.close(this.form);
  }
}
