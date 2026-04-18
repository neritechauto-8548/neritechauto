import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { TextareaModule } from 'primeng/textarea';

@Component({
  standalone: true,
  selector: 'app-nf-config',
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    SelectModule,
    CheckboxModule,
    TextareaModule,
  ],
  templateUrl: './nf-config.html',

})
export class NfConfigComponent {
  ambiente: 'producao' | 'homologacao' = 'producao';

  // Certificado
  certificadoArquivo?: File;
  certificadoSenha = '';
  certificadoValidade?: string;

  // Emitente
  emitenteRazao = '';
  emitenteCnpj = '';
  emitenteIe = '';
  emitenteCrt: 'Simples Nacional' | 'Simples Nacional – excesso sublimite' | 'Regime Normal' = 'Simples Nacional';
  emitenteCnae = '';
  emitenteCidade = '';
  emitenteUf = '';

  // Numeração
  docTipo: 'NFE' | 'NFCE' | 'NFSE' = 'NFE';
  numeracaoSerie = '';
  numeracaoProximo = '';
  numeracaoReiniciarAno = false;

  // Padrões fiscais
  cfopPadrao = '';
  naturezaOperacao = '';
  csosnPadrao = '';

  // NFSe
  nfseCodigoIbge = '';
  nfseRpsSerie = '';
  nfseRpsProximo = '';

  // DANFE/DANFCE
  danfeLayout: 'Retrato' | 'Paisagem' = 'Retrato';
  danfeLogo?: File;
  danfeMensagem = '';

  // Email
  smtpHost = '';
  smtpPorta = 587;
  smtpUsuario = '';
  smtpSenha = '';
  smtpRemetente = '';
  smtpTlsSsl = true;

  // Contingência
  contingenciaSvcan = false;
  contingenciaSvcrs = false;
  contingenciaJustificativa = '';

  // Dialogs
  sucessoVisible = false;
  testeCertificadoVisible = false;
  testeEmailVisible = false;

  onSelectCertificado(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.certificadoArquivo = input.files[0];
    }
  }

  onSelectLogo(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.danfeLogo = input.files[0];
    }
  }

  salvarConfiguracoes() {
    // Aqui integrar com API posteriormente; por enquanto apenas feedback visual
    this.sucessoVisible = true;
  }

  testarCertificado() {
    // Simula teste de certificado
    this.testeCertificadoVisible = true;
  }

  testarEmail() {
    // Simula teste de SMTP
    this.testeEmailVisible = true;
  }
}
