import { Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'relatorio-pedidos-fornecedor',
  standalone: true,
  templateUrl: './relatorio-pedidos.html',
  styleUrls: ['./relatorio-pedidos.scss'],
  imports: [CommonModule, RouterModule, ButtonModule],
})
export class RelatorioPedidosFornecedor {
  private readonly location = inject(Location);

  empresa = {
    nome: 'Oficina Exemplo',
    fantasia: 'AUTO CENTRO SUA OFICINA',
    telefone: '(999)999 0000',
    email: 'alexandre944@gmail.com',
    site: 'oficinaintegrada.com.br/oficinas/VQNW8IW0UBBRTFQ3VNHONQ7QBG2ALNP1B6V',
    endereco: 'AV ENDEREÇO COMPLETO, N 1000, BAIRRO, CIDADE - SP',
  };

  pedidos = [
    {
      pedido: 1,
      fornecedor: 'AUTO PEÇAS DE EXEMPLO',
      funcionario: 'MESTRE',
      previsao: '02/11/2025',
      status: 'Aguardando envio',
      descricao: 'rewwe',
    },
  ];

  total = 0;

  imprimir() {
    window.print();
  }

  voltar() {
    this.location.back();
  }
}