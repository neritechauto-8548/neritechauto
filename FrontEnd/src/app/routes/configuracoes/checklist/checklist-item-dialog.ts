import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-checklist-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, MatIconModule],
  templateUrl: './checklist-item-dialog.html',
})
export class ChecklistItemDialog implements OnInit {
  descricao = '';
  isEdit = false;

  constructor(private ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  ngOnInit() {
    if (this.config?.data && this.config.data.descricao) {
      this.descricao = this.config.data.descricao;
      this.isEdit = true;
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const value = this.descricao.trim();
    if (value) {
      this.ref.close(value);
    }
  }
}
