import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-incluir-xml',
  templateUrl: './incluir-xml.html',
  styleUrls: ['./incluir-xml.scss'],
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule],
})
export class IncluirXml {
  selectedFileName = 'Nenhum arquivo escolhido';
  selectedFile: File | null = null;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files[0];
    if (file) {
      this.selectedFile = file;
      this.selectedFileName = file.name;
    } else {
      this.selectedFile = null;
      this.selectedFileName = 'Nenhum arquivo escolhido';
    }
  }

  abrirSeletorArquivo() {
    if (this.fileInputRef?.nativeElement) {
      this.fileInputRef.nativeElement.click();
    }
  }

  enviarDados() {
    if (!this.selectedFile) return;
    // TODO: integrar com backend para envio do arquivo XML
    // Por enquanto, apenas exibe o nome do arquivo no console.
    console.log('Enviando XML:', this.selectedFile.name);
  }
}