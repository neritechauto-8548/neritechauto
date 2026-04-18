import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RouterModule, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { ClienteResponse } from '../../cliente/models/cliente.models';
import { ComunicacaoService, ComunicacaoEnviadaRequest } from '../comunicacao.service';

interface ClienteAniversariante extends ClienteResponse {
  nascimentoFormatado: string;
  selecionado: boolean;
  idadeACompletar: number;
  diaNascimento: number;
  isHoje: boolean;
  temEmail: boolean;
  temCelular: boolean;
}

@Component({
  selector: 'app-aniversario',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    ToastModule,
    RouterModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule
  ],
  providers: [MessageService, DatePipe],
  templateUrl: './aniversario.html',
  styleUrls: [],
})
export class AniversarioAgendamento implements OnInit {
  private clienteService = inject(ClientesService);
  private comunicacaoService = inject(ComunicacaoService);
  private messageService = inject(MessageService);
  private datePipe = inject(DatePipe);
  private router = inject(Router);

  meses = [
    { label: 'JANEIRO', value: 1 }, { label: 'FEVEREIRO', value: 2 }, { label: 'MARÇO', value: 3 },
    { label: 'ABRIL', value: 4 }, { label: 'MAIO', value: 5 }, { label: 'JUNHO', value: 6 },
    { label: 'JULHO', value: 7 }, { label: 'AGOSTO', value: 8 }, { label: 'SETEMBRO', value: 9 },
    { label: 'OUTUBRO', value: 10 }, { label: 'NOVEMBRO', value: 11 }, { label: 'DEZEMBRO', value: 12 }
  ];

  // Seta Mes atual por padrão
  mesSelecionado = new Date().getMonth() + 1;

  clientes: ClienteAniversariante[] = [];
  loading = false;
  submitLoad = false;

  // Diálogo de envio
  mostrarDialogo = false;
  modoEnvio: 'EMAIL' | 'WHATSAPP' | 'SMS' = 'WHATSAPP';
  mudarDataEnvio = false;
  dataReagendamento: string = '';
  assuntoEmail = 'Feliz aniversário! 🎉';

  modelosMensagens = [
    { nome: 'Desconto Aniversário', conteudo: 'Feliz aniversário! A equipe da AUTO CENTRO deseja um ótimo dia. Apresente esta mensagem e ganhe 15% de desconto no serviço!' },
    { nome: 'Simples', conteudo: 'Oi! Queremos lhe parabenizar pelo seu aniversário! Que seu dia seja repleto de alegria, saúde e sucesso. \n\nAbraços,\nSua Oficina' }
  ];
  modeloEmail = 'Desconto Aniversário';
  mensagemTexto = '';
  saldoSms = 0;
  creditosSmsRestantes = 0;

  get selecionados(): ClienteAniversariante[] {
    return this.clientes.filter(c => c.selecionado);
  }

  buscar(): void {
    this.loading = true;
    this.clienteService.list({}).subscribe({
      next: (res: any) => {
         const clis = res.content || res || [];

         // Fitrar localmente pelos aniversariantes do mês selecionado
         const aniversariantes = clis.filter((c: ClienteResponse) => {
            if (!c.dataNascimento) return false;
            // datanascimento format: YYYY-MM-DD
            const monthStr = c.dataNascimento.split('-')[1];
            return Number(monthStr) === this.mesSelecionado;
         });

         const hojeObj = new Date();
         const diaHoje = hojeObj.getDate();
         const mesHoje = hojeObj.getMonth() + 1;
         const anoHoje = hojeObj.getFullYear();

         let clientesMapped = aniversariantes.map((c: ClienteResponse) => {
            const dateParts = c.dataNascimento!.split('-'); // 'YYYY-MM-DD'
            const anoNasc = Number(dateParts[0]);
            const mesNasc = Number(dateParts[1]);
            const diaNasc = Number(dateParts[2]);

            const idade = anoHoje - anoNasc;
            const isHoje = diaNasc === diaHoje && mesNasc === this.mesSelecionado; // if selected month is current month and day matches

            const temEmail = !!c.email;
            const extraidos = c as any;
            const temCel = !!(this.extrairContato(extraidos, 'WHATSAPP') || this.extrairContato(extraidos, 'CELULAR'));

            return {
               ...c,
               // Fallbacks de Nomes
               nomeCompleto: c.nomeCompleto || c.razaoSocial || extraidos.nome || 'Sem Nome Cadastrado',
               nascimentoFormatado: this.datePipe.transform(c.dataNascimento, 'dd/MM/yyyy') || '',
               idadeACompletar: Math.max(0, idade),
               diaNascimento: diaNasc,
               isHoje: isHoje,
               temEmail: temEmail,
               temCelular: temCel,
               selecionado: isHoje // Auto-seleciona apenas se faz aniversário HOJE
            };
         });

         // Ordenar cronologicamente pelo dia do nascimento no mês
         clientesMapped.sort((a: ClienteAniversariante, b: ClienteAniversariante) => a.diaNascimento - b.diaNascimento);

         this.clientes = clientesMapped;

         this.loading = false;
      },
      error: () => {
         this.loading = false;
         this.messageService.add({severity:'error', summary: 'Erro', detail: 'Falha ao buscar tabela de clientes.'});
      }
    });
  }

  toggleAll(checked: boolean): void {
     this.clientes.forEach(c => c.selecionado = checked);
  }

  imprimir(): void {
    window.print();
  }

  verFicha(c: ClienteAniversariante): void {
     this.router.navigate(['/cliente/cadastro', { id: c.id }]);
  }

  enviarAlertaUnico(c: ClienteAniversariante): void {
     this.clientes.forEach(cliente => cliente.selecionado = false);
     c.selecionado = true;
     this.enviarAlerta();
  }

  enviarAlerta(): void {
    if (this.selecionados.length === 0) {
      this.messageService.add({severity:'warn', summary: 'Atenção', detail: 'Selecione pelo menos um cliente para enviar o alerta.'});
      return;
    }
    this.mostrarDialogo = true;
  }

  confirmarEnvio(): void {
    this.submitLoad = true;
    let countSuccess = 0;

    // Dispara via ComunicacaoService para cada cliente do laço do checkbox
    this.selecionados.forEach((cliente, index) => {

      const isEmail = this.modoEnvio === 'EMAIL';
      const contatoExtraido = isEmail ? (cliente.email || '') : (this.extrairContato(cliente, 'WHATSAPP') || this.extrairContato(cliente, 'CELULAR') || '');

      if (!contatoExtraido || contatoExtraido.trim().length === 0) {
          this.messageService.add({severity:'warn', summary: 'Contato Ausente', detail: `Ignorando envio para ${cliente.nomeCompleto}: Sem ${isEmail ? 'E-mail' : 'Celular'}.`});
          if (index === this.selecionados.length - 1) this.finalizarEnvio(countSuccess);
          return;
      }

      const payload: ComunicacaoEnviadaRequest = {
        empresaId: 1, // mock seguro ou pegar do store
        destinatarioTipo: 'CLIENTE',
        destinatarioId: cliente.id,
        destinatarioNome: cliente.nomeCompleto || '',
        tipoComunicacao: this.modoEnvio as string, // 'WHATSAPP' | 'EMAIL' | 'SMS'
        destinatarioContato: contatoExtraido,
        assunto: isEmail ? this.assuntoEmail : undefined,
        conteudo: this.mensagemTexto,
        status: this.mudarDataEnvio ? 'AGENDADA' : 'ENVIADA',
        agendadaPara: this.mudarDataEnvio && this.dataReagendamento ? this.dataReagendamento + ':00' : undefined // ISO com segundos forçados
      };

      this.comunicacaoService.enviarComunicacao(payload).subscribe({
        next: () => {
           countSuccess++;
           // Fecha dialog e da Toast se todos foram processados
           if (index === this.selecionados.length - 1) {
              this.finalizarEnvio(countSuccess);
           }
        },
        error: (err: any) => {
           console.error('Falha de envio para:', cliente.nomeCompleto, err);
           if (index === this.selecionados.length - 1) {
              this.finalizarEnvio(countSuccess);
           }
        }
      });
    });
  }

  private extrairContato(c: any, tipoBusca: string): string {
     // A entidade contatos é uma array vinda de listCliente, usamos um find basico pra tentar achar celular do whats
     if (c.contatos && Array.isArray(c.contatos)) {
        const d = c.contatos.find((x: any) => x.tipoContato === tipoBusca || x.tipoContato === 'CELULAR');
        return d ? d.valor : '';
     }
     return '';
  }

  private finalizarEnvio(sucessos: number) {
     this.submitLoad = false;
     this.mostrarDialogo = false;
     if (sucessos > 0) {
       this.messageService.add({severity:'success', summary: 'Sucesso', detail: `${sucessos} felicitações enviadas.`});
     } else {
       this.messageService.add({severity:'error', summary: 'Falha', detail: 'Houveram erros ao disparar a mensageria.'});
     }
  }

  ngOnInit(): void {
    this.onModeloMensagemChange(this.modeloEmail);
    this.buscar(); // Traz listagem ao carregar a página inicialmente
  }

  onModeloMensagemChange(nome: string): void {
    const m = this.modelosMensagens.find(x => x.nome === nome);
    this.mensagemTexto = m ? m.conteudo : '';
  }
}
