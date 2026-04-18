import { Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'relatorio-fornecedor',
  standalone: true,
  templateUrl: './relatorio-fornecedor.html',
  styleUrls: ['./relatorio-fornecedor.scss'],
  imports: [CommonModule, RouterModule, ButtonModule],
})
export class RelatorioFornecedor {
  private readonly location = inject(Location);

  empresa = {
    nome: 'Oficina Exemplo',
    fantasia: 'AUTO CENTRO SUA OFICINA',
    telefone: '(999) 999 0000',
    email: 'alexandre944@gmail.com',
    site: 'oficinaintegrada.com.br/oficinas/VQNW8IW0UBBRTFQ3VNHONQ7QBG2ALNP1B6V',
    endereco: 'AV ENDEREÇO COMPLETO, N 1000, BAIRRO, CIDADE - SP',
  };

  fornecedores = [
    {
      codigo: 1,
      documento: '119787878787',
      nome: 'AUTO PEÇAS DE EXEMPLO',
      telefones: ['119787878787', '119787878787', '119787878787', '119787878787'],
    },
  ];

  imprimir() {
    window.print();
  }

  voltar() {
    this.location.back();
  }
}