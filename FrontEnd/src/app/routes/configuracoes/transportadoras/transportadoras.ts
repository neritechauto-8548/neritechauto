import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, finalize, Subject } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Transportadora, TransportadoraForm, TransportadoraService, TipoPessoa } from './transportadora.service';

@Component({
  selector: 'nt-transportadoras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transportadoras.html',
  styleUrls: ['./transportadoras.scss']
})
export class TransportadorasComponent implements OnInit {
  private readonly api = inject(TransportadoraService);
  private readonly http = inject(HttpClient);
  private readonly destroyRef = inject(DestroyRef);
  private readonly searchChanged = new Subject<string>();
  private requestNumber = 0;
  private cityRequest = 0;

  rows: Transportadora[] = [];
  total = 0;
  page = 0;
  readonly size = 20;
  busca = '';
  regiao = '';
  status: 'todas' | 'ativas' | 'inativas' = 'todas';
  loading = false;
  error = '';
  notice = '';
  drawer: 'cadastro' | 'filtro' | null = null;
  selected: Transportadora | null = null;
  saving = false;
  formError = '';
  form: TransportadoraForm = this.blank();
  cidades: string[] = [];
  buscandoCidades = false;
  cidadesErro = '';

  readonly estados = [
    ['AC','Acre'],['AL','Alagoas'],['AP','Amapá'],['AM','Amazonas'],
    ['BA','Bahia'],['CE','Ceará'],['DF','Distrito Federal'],['ES','Espírito Santo'],
    ['GO','Goiás'],['MA','Maranhão'],['MT','Mato Grosso'],['MS','Mato Grosso do Sul'],
    ['MG','Minas Gerais'],['PA','Pará'],['PB','Paraíba'],['PR','Paraná'],
    ['PE','Pernambuco'],['PI','Piauí'],['RJ','Rio de Janeiro'],['RN','Rio Grande do Norte'],
    ['RS','Rio Grande do Sul'],['RO','Rondônia'],['RR','Roraima'],
    ['SC','Santa Catarina'],['SP','São Paulo'],['SE','Sergipe'],['TO','Tocantins']
  ];

  ngOnInit(): void {
    this.searchChanged.pipe(debounceTime(280), distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)).subscribe(() => this.filtrar());
    this.listar();
  }

  private blank(): TransportadoraForm {
    return {
      tipoPessoa: 'FISICA', nomeFantasia: '', razaoSocial: '', cpf: '', cnpj: '',
      inscricaoEstadual: '', observacoes: '', cep: '', estado: '', cidade: '',
      logradouro: '', numero: '', bairro: '', complemento: '',
      modalidade: 'RODOVIARIO', ativo: true
    };
  }

  get isJuridica(): boolean { return this.form.tipoPessoa === 'JURIDICA'; }
  get documentoLimpo(): string {
    return this.isJuridica ? (this.form.cnpj ?? '').replace(/[^\da-z]/gi, '') :
      (this.form.cpf ?? '').replace(/\D/g, '');
  }
  get documentoValido(): boolean {
    return this.isJuridica ? this.validarCnpj(this.documentoLimpo) : this.validarCpf(this.documentoLimpo);
  }
  get formularioValido(): boolean {
    return this.documentoValido &&
      !!this.form.nomeFantasia?.trim() && this.form.nomeFantasia.trim().length <= 255 &&
      !!this.form.cep?.replace(/\D/g, '').match(/^\d{8}$/) &&
      !!this.form.estado?.trim() && !!this.form.cidade?.trim() &&
      !!this.form.logradouro?.trim() && !!this.form.numero?.trim() &&
      !!this.form.bairro?.trim() &&
      (!this.form.inscricaoEstadual || this.form.inscricaoEstadual.length <= 20) &&
      (!this.form.observacoes || this.form.observacoes.length <= 4000);
  }

  private validarCpf(cpf: string): boolean {
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
    for (let n = 9; n <= 10; n++) {
      let sum = 0;
      for (let i = 0; i < n; i++) sum += Number(cpf[i]) * (n + 1 - i);
      let digit = (sum * 10) % 11;
      if (digit === 10) digit = 0;
      if (digit !== Number(cpf[n])) return false;
    }
    return true;
  }

  private validarCnpj(cnpj: string): boolean {
    if (!/^\d{14}$/.test(cnpj) || /^(\d)\1+$/.test(cnpj)) return false;
    for (const digits of [12, 13]) {
      let sum = 0, weight = digits === 12 ? 5 : 6;
      for (let i = 0; i < digits; i++) {
        sum += Number(cnpj[i]) * weight;
        weight = weight === 2 ? 9 : weight - 1;
      }
      const remainder = sum % 11;
      if (Number(cnpj[digits]) !== (remainder < 2 ? 0 : 11 - remainder)) return false;
    }
    return true;
  }

  formatarDocumento(value: string | null | undefined, tipo: TipoPessoa): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, tipo === 'FISICA' ? 11 : 14);
    if (tipo === 'FISICA') {
      return digits.replace(/^(\d{3})(\d)/, '$1.$2')
        .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    }
    return digits.replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/\.(\d{3})(\d)/, '.$1/$2')
      .replace(/(\d{4})(\d{1,2})$/, '$1-$2');
  }

  documentoItem(item: Transportadora): string {
    return this.formatarDocumento(item.tipoPessoa === 'JURIDICA' ? item.cnpj : item.cpf, item.tipoPessoa);
  }
  alterarDocumento(value: string): void {
    const formatted = this.formatarDocumento(value, this.form.tipoPessoa);
    if (this.isJuridica) this.form.cnpj = formatted;
    else this.form.cpf = formatted;
  }
  alterarCep(value: string): void {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 8);
    this.form.cep = digits.length > 5 ? digits.slice(0, 5) + '-' + digits.slice(5) : digits;
  }
  mudarTipo(tipo: TipoPessoa): void {
    if (this.selected) return;
    this.form.tipoPessoa = tipo;
    this.form.cpf = '';
    this.form.cnpj = '';
  }
  regiaoItem(item: Transportadora): string {
    return [item.cidade, item.estado].filter(Boolean).join(' - ') || '—';
  }
  digitouBusca(): void { this.searchChanged.next(this.busca); }
  listar(): void {
    const request = ++this.requestNumber;
    const ativo = this.status === 'todas' ? undefined : this.status === 'ativas';
    this.loading = true;
    this.error = '';
    this.api.listar(this.page, this.size, this.busca, this.regiao, ativo)
      .pipe(finalize(() => { if (request === this.requestNumber) this.loading = false; }))
      .subscribe({
        next: result => {
          if (request !== this.requestNumber) return;
          this.rows = result.content ?? [];
          this.total = result.totalElements ?? 0;
        },
        error: err => {
          if (request !== this.requestNumber) return;
          this.rows = []; this.total = 0;
          this.error = this.describe(err);
        }
      });
  }
  filtrar(): void { this.page = 0; this.drawer = null; this.listar(); }
  resetarFiltros(): void {
    this.regiao = ''; this.status = 'todas'; this.drawer = null;
    this.filtrar();
  }
  pagina(delta: number): void {
    const next = this.page + delta;
    if (next < 0 || next * this.size >= this.total || this.loading) return;
    this.page = next; this.listar();
  }

  nova(): void {
    this.selected = null; this.form = this.blank();
    this.cidades = []; this.cidadesErro = '';
    this.formError = ''; this.notice = '';
    this.drawer = 'cadastro';
  }

  editar(item: Transportadora): void {
    this.api.obter(item.id).subscribe({
      next: loaded => {
        this.selected = loaded;
        this.form = {
          fornecedorId: loaded.fornecedorId,
          tipoPessoa: loaded.tipoPessoa,
          nomeFantasia: loaded.nomeFantasia,
          razaoSocial: loaded.razaoSocial ?? '',
          cpf: this.formatarDocumento(loaded.cpf, 'FISICA'),
          cnpj: this.formatarDocumento(loaded.cnpj, 'JURIDICA'),
          inscricaoEstadual: loaded.inscricaoEstadual ?? '',
          observacoes: loaded.observacoes ?? '',
          cep: loaded.cep ?? '', estado: loaded.estado ?? '', cidade: loaded.cidade ?? '',
          logradouro: loaded.logradouro ?? loaded.endereco ?? '',
          numero: loaded.numero ?? '', bairro: loaded.bairro ?? '',
          complemento: loaded.complemento ?? '',
          modalidade: loaded.modalidade ?? 'RODOVIARIO',
          ativo: loaded.ativo
        };
        this.formError = ''; this.drawer = 'cadastro';
        if (this.form.estado) this.carregarCidades(this.form.estado);
      },
      error: err => this.error = this.describe(err)
    });
  }

  mudarEstado(estado: string): void {
    this.form.estado = estado; this.form.cidade = '';
    this.carregarCidades(estado);
  }
  carregarCidades(uf: string): void {
    const request = ++this.cityRequest;
    this.cidades = []; this.cidadesErro = '';
    if (!this.estados.some(([sigla]) => sigla === uf)) return;
    this.buscandoCidades = true;
    this.http.get<Array<{ id: number; nome: string }>>(
      'https://servicodados.ibge.gov.br/api/v1/localidades/estados/' + encodeURIComponent(uf) + '/municipios'
    ).pipe(finalize(() => { if (request === this.cityRequest) this.buscandoCidades = false; }))
      .subscribe({
        next: items => {
          if (request !== this.cityRequest) return;
          this.cidades = items.map(x => x.nome).sort((a, b) => a.localeCompare(b, 'pt-BR'));
        },
        error: () => {
          if (request !== this.cityRequest) return;
          this.cidadesErro = 'Não foi possível carregar cidades. Digite a cidade para continuar.';
        }
      });
  }

  fechar(): void {
    if (this.saving) return;
    this.drawer = null; this.formError = '';
  }

  salvar(): void {
    if (this.saving) return;
    if (!this.formularioValido) {
      this.formError = 'Informe documento válido, nome, CEP, estado, cidade, logradouro, número e bairro.';
      return;
    }
    this.saving = true; this.formError = ''; this.notice = '';
    const payload: TransportadoraForm = {
      ...this.form,
      nomeFantasia: this.form.nomeFantasia.trim(),
      razaoSocial: this.form.razaoSocial?.trim() || this.form.nomeFantasia.trim(),
      cpf: this.isJuridica ? null : this.documentoLimpo,
      cnpj: this.isJuridica ? this.documentoLimpo : null,
      cep: (this.form.cep ?? '').replace(/\D/g, ''),
      estado: this.form.estado?.trim(),
      cidade: this.form.cidade?.trim(),
      logradouro: this.form.logradouro?.trim(),
      numero: this.form.numero?.trim(),
      bairro: this.form.bairro?.trim(),
      complemento: this.form.complemento?.trim(),
      inscricaoEstadual: this.form.inscricaoEstadual?.trim(),
      observacoes: this.form.observacoes?.trim(),
      modalidade: this.form.modalidade ?? 'RODOVIARIO'
    };
    const req = this.selected
      ? this.api.editar(this.selected.id, payload, this.selected.versao)
      : this.api.criar(payload);
    req.pipe(finalize(() => this.saving = false)).subscribe({
      next: () => {
        this.drawer = null; this.selected = null;
        this.notice = 'Transportadora salva com sucesso.';
        this.filtrar();
      },
      error: err => this.formError = this.describe(err)
    });
  }

  remover(item: Transportadora): void {
    if (!item.ativo || this.saving) return;
    if (!window.confirm('Desativar transportadora? Fornecedor e documentos anteriores serão preservados.')) return;
    this.saving = true; this.error = '';
    this.api.inativar(item.id).pipe(finalize(() => this.saving = false)).subscribe({
      next: () => {
        this.notice = 'Transportadora desativada. Vínculos e compras existentes foram preservados.';
        this.listar();
      },
      error: err => this.error = this.describe(err)
    });
  }

  private describe(err: any): string {
    if (err?.status === 403) return 'Acesso negado ou dados de outra empresa (403).';
    if (err?.status === 409) return 'Documento duplicado, vínculo existente ou cadastro alterado por outra sessão (409).';
    if (err?.status === 400 || err?.status === 422) return err.error?.message ?? 'Confira documento e endereço informados.';
    if (err?.status === 0) return 'Sem conexão com o servidor. Confira a rede e tente novamente.';
    if (err?.status === 404) return 'Transportadora não encontrada.';
    return 'Não foi possível concluir a operação. Tente novamente.';
  }
}
