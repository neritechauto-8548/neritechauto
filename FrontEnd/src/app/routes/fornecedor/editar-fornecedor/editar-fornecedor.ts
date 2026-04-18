import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageHeader } from '@shared';

@Component({
  selector: 'editar-fornecedor',
  standalone: true,
  templateUrl: './editar-fornecedor.html',
  styleUrls: ['./editar-fornecedor.scss'],
  imports: [CommonModule, PageHeader],
})
export class EditarFornecedor {}