import { CommonModule } from '@angular/common';
import { Component, DestroyRef, ElementRef, ViewChild, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, finalize, firstValueFrom, Subject } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  FiltrosSeguradoras, Seguradora, SeguradoraForm, SeguradoraService
} from './seguradora.service';

type Drawer = 'cadastro' | 'filtro' | null;
type Situacao = '' | 'ativo' | 'inativo';

@Component({
  selector: 'nt-seguradoras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seguradoras.html',
  styleUrls: ['./seguradoras.scss']
})
export class SeguradorasComponent implements OnInit {
  private readonly api = inject(SeguradoraService);
  private readonly destroyed = inject(DestroyRef);
  private readonly buscaAlterada = new Subject<string>();
  @ViewChild('cnpjInput') cnpjInput?: ElementRef<HTMLInputElement>;

  items: Seguradora[] = [];
  page = 0;
  size = 20;
  total = 0;
  busca = '';
  loading = false;
  saving = false;
  exporting = false;
  error = '';
  formError = '';
  notice = '';
  drawer: Drawer = null;
  selected: Seguradora | null = null;
  form: SeguradoraForm = this.vazio();
  filtroCnpj = '';
  filtroNome = '';
  filtroFantasia = '';
  filtroSituacao: Situacao = '';
  private requestSeq = 0;

  ngOnInit(): void {
    this.buscaAlterada.pipe(debounceTime(280), distinctUntilChanged(),
      takeUntilDestroyed(this.destroyed)).subscribe(() => this.listar(true));
    this.listar();
  }

  private vazio(): SeguradoraForm {
    return {
      cnpj: '', razaoSocial: '', nomeFantasia: '', inscricaoEstadual: '',
      inscricaoMunicipal: '', email: '', observacoes: '', margemRefatoramento: null,
      site: '', retencaoIss: false, ativo: true
    };
  }

  get filtros(): FiltrosSeguradoras {
    return { cnpj: this.filtroCnpj, nome: this.filtroNome, nomeFantasia: this.filtroFantasia };
  }

  get cnpjValido(): boolean {
    return this.validarCnpj(this.form.cnpj);
  }

  get formularioValido(): boolean {
    const email = (this.form.email ?? '').trim();
    const site = (this.form.site ?? '').trim();
    const margem = this.form.margemRefatoramento;
    return this.cnpjValido && !!this.form.razaoSocial?.trim() &&
      this.form.razaoSocial.trim().length <= 255 &&
      (!email || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) &&
      (!site || /^https?:\/\/[^\s.]+(?:\.[^\s]+|:\d+)(?:\/\S*)?$/i.test(site)) &&
      (margem == null || (Number(margem) >= 0 && Number(margem) <= 100));
  }

  private validarCnpj(valor: string | null): boolean {
    const digits = (valor ?? '').replace(/\D/g, '');
    if (digits.length !== 14 || /^(\d)\1+$/.test(digits)) return false;
    for (const length of [12, 13]) {
      let sum = 0, weight = length === 12 ? 5 : 6;
      for (let i = 0; i < length; i++) {
        sum += Number(digits[i]) * weight;
        weight = weight === 2 ? 9 : weight - 1;
      }
      const remainder = sum % 11;
      if (Number(digits[length]) !== (remainder < 2 ? 0 : 11 - remainder)) return false;
    }
    return true;
  }

  formatarCnpj(value: string): string {
    const digits = (value || '').replace(/\D/g, '').slice(0, 14);
    if (digits.length <= 2) return digits;
    if (digits.length <= 5) return digits.slice(0, 2) + '.' + digits.slice(2);
    if (digits.length <= 8) return digits.slice(0, 2) + '.' + digits.slice(2, 5) + '.' + digits.slice(5);
    if (digits.length <= 12) return digits.slice(0, 2) + '.' + digits.slice(2, 5) + '.' + digits.slice(5, 8) + '/' + digits.slice(8);
    return digits.slice(0, 2) + '.' + digits.slice(2, 5) + '.' + digits.slice(5, 8) + '/' + digits.slice(8, 12) + '-' + digits.slice(12);
  }

  alterarCnpj(value: string): void { this.form.cnpj = this.formatarCnpj(value); }
  alterarFiltroCnpj(value: string): void { this.filtroCnpj = this.formatarCnpj(value); }
  buscaDigitada(): void { this.buscaAlterada.next(this.busca); }

  listar(reset = false): void {
    if (reset) this.page = 0;
    const seq = ++this.requestSeq;
    this.loading = true; this.error = '';
    const ativo = this.filtroSituacao === '' ? undefined : this.filtroSituacao === 'ativo';
    this.api.listar(this.page, this.size, this.busca, ativo, this.filtros)
      .pipe(finalize(() => { if (seq === this.requestSeq) this.loading = false; }))
      .subscribe({
        next: res => {
          if (seq !== this.requestSeq) return;
          this.items = res.content ?? [];
          this.total = res.totalElements ?? 0;
        },
        error: err => {
          if (seq !== this.requestSeq) return;
          this.items = []; this.total = 0; this.error = this.descreverErro(err);
        }
      });
  }

  mudarPagina(delta: number): void {
    const next = this.page + delta;
    if (next < 0 || next * this.size >= this.total || this.loading) return;
    this.page = next; this.listar();
  }

  novo(): void {
    this.selected = null; this.form = this.vazio();
    this.formError = ''; this.notice = ''; this.drawer = 'cadastro';
    queueMicrotask(() => this.cnpjInput?.nativeElement.focus());
  }

  editar(item: Seguradora): void {
    this.formError = '';
    this.api.obter(item.id).subscribe({
      next: value => {
        this.selected = value;
        this.form = {
          cnpj: this.formatarCnpj(value.cnpj),
          razaoSocial: value.razaoSocial,
          nomeFantasia: value.nomeFantasia ?? '',
          inscricaoEstadual: value.inscricaoEstadual ?? '',
          inscricaoMunicipal: value.inscricaoMunicipal ?? '',
          email: value.email ?? '',
          observacoes: value.observacoes ?? '',
          margemRefatoramento: value.margemRefatoramento,
          site: value.site ?? '', retencaoIss: !!value.retencaoIss, ativo: value.ativo
        };
        this.drawer = 'cadastro';
        queueMicrotask(() => this.cnpjInput?.nativeElement.focus());
      },
      error: err => this.error = this.descreverErro(err)
    });
  }

  abrirFiltro(): void { this.formError = ''; this.drawer = 'filtro'; }
  fechar(): void { if (!this.saving) { this.drawer = null; this.formError = ''; } }
  aplicarFiltro(): void { this.drawer = null; this.listar(true); }
  resetarFiltro(): void {
    this.filtroCnpj = ''; this.filtroNome = ''; this.filtroFantasia = ''; this.filtroSituacao = '';
    this.drawer = null; this.listar(true);
  }

  salvar(): void {
    if (this.saving) return;
    if (!this.formularioValido) { this.formError = 'Revise CNPJ, razão social, e-mail, site e margem.'; return; }
    this.saving = true; this.formError = ''; this.notice = '';
    const payload: SeguradoraForm = {
      ...this.form,
      cnpj: this.form.cnpj.replace(/\D/g, ''),
      razaoSocial: this.form.razaoSocial.trim(),
      nomeFantasia: this.form.nomeFantasia?.trim() ?? '',
      email: this.form.email?.trim() ?? '',
      site: this.form.site?.trim() ?? ''
    };
    const req = this.selected
      ? this.api.editar(this.selected.id, payload, this.selected.versao)
      : this.api.criar(payload);
    req.pipe(finalize(() => this.saving = false)).subscribe({
      next: () => {
        this.drawer = null; this.selected = null;
        this.notice = 'Seguradora salva com sucesso.';
        this.listar(true);
      },
      error: err => this.formError = this.descreverErro(err)
    });
  }

  async relatorio(): Promise<void> {
    if (this.exporting) return;
    this.exporting = true; this.error = '';
    try {
      const situacao = this.filtroSituacao === '' ? undefined : this.filtroSituacao === 'ativo';
      const registros: Seguradora[] = [];
      let page = 0;
      while (true) {
        const res = await firstValueFrom(this.api.listar(page, 100, this.busca, situacao, this.filtros));
        registros.push(...(res.content ?? []));
        page++;
        if (page >= res.totalPages) break;
      }
      const csvRow = (values: string[]) => values.map(v =>
        '"' + v.replace(/^[=+\-@\t\r]/, "'$&").replace(/"/g, '""') + '"').join(';');
      const rows = [
        csvRow(['Seguradora', 'CNPJ', 'E-mail', 'Website']),
        ...registros.map(s => csvRow([
          s.nomeFantasia || s.razaoSocial, s.cnpj ?? '', s.email ?? '', s.site ?? ''
        ]))
      ];
      const link = document.createElement('a');
      const url = URL.createObjectURL(new Blob(['\uFEFF' + rows.join('\r\n')], { type: 'text/csv;charset=utf-8' }));
      try { link.href = url; link.download = 'seguradoras-neritech.csv'; link.click(); }
      finally { setTimeout(() => URL.revokeObjectURL(url), 1000); }
    } catch (err) {
      this.error = this.descreverErro(err);
    } finally { this.exporting = false; }
  }

  private descreverErro(err: any): string {
    if (err?.status === 403) return 'Você não tem permissão para esta operação (403).';
    if (err?.status === 409) return 'CNPJ já cadastrado ou registro alterado por outro usuário (409).';
    if (err?.status === 422 || err?.status === 400) return err.error?.message ?? 'Dados inválidos. Revise os campos (422).';
    if (err?.status === 0) return 'Sem conexão com o servidor. Verifique a rede e tente novamente.';
    return 'Não foi possível concluir a operação. Tente novamente.';
  }
}
