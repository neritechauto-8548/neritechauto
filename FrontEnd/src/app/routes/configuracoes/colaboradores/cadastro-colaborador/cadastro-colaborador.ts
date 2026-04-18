import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { LocalStorageService } from '@shared/services/storage.service';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { DatePicker } from 'primeng/datepicker';
import { CheckboxModule } from 'primeng/checkbox';
import { PasswordModule } from 'primeng/password';
import { MultiSelectModule } from 'primeng/multiselect';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { FuncionarioService, FuncionarioRequest } from '../funcionario.service';
import { DepartamentoService } from '../../departamentos/departamento.service';
import { CargoService } from '../../cargos/cargo.service';
import { UsuarioService } from '../../usuario.service';
import { PermissaoService } from '../../permissoes/permissao.service';
import { ConfirmationService } from '@shared/services/confirmation.service';

@Component({
  selector: 'cadastro-colaborador',
  standalone: true,
  templateUrl: './cadastro-colaborador.html',
  styleUrls: ['./cadastro-colaborador.scss'],
  imports: [
    CommonModule, FormsModule, MatIconModule,
    InputTextModule, SelectModule, ButtonModule, TextareaModule, DatePicker, CheckboxModule,
    PasswordModule, MultiSelectModule, ToastModule
  ],
  providers: [MessageService]
})
export class CadastroColaborador implements OnInit {
  private readonly router   = inject(Router);
  private readonly route    = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly storage  = inject(LocalStorageService);
  private readonly messageService = inject(MessageService);
  private readonly funcionarioService  = inject(FuncionarioService);
  private readonly departamentoService = inject(DepartamentoService);
  private readonly cargoService        = inject(CargoService);
  private readonly usuarioService      = inject(UsuarioService);
  private readonly permissaoService    = inject(PermissaoService);
  private readonly confirmationService = inject(ConfirmationService);

  // ---- Acesso ao sistema ----
  temAcesso = false;
  usuarioId?: number;        // linked usuario id (loaded from funcionario.usuarioId)
  funcaoOptions: { label: string; value: number }[] = [];
  usuarioForm = {
    email: '',
    senha: '',
    funcoesIds: [] as number[],
    ativo: true,
    bloqueado: false,
  };

  activeTab = 'pessoais';
  navTabs = [
    { id: 'pessoais',    label: 'Dados Pessoais',    icon: 'person' },
    { id: 'vinculo',     label: 'Vínculo',            icon: 'badge' },
    { id: 'contato',     label: 'Contato',            icon: 'phone' },
    { id: 'documentos',  label: 'Documentos',         icon: 'description' },
    { id: 'bancario',    label: 'Dados Bancários',    icon: 'account_balance' },
    { id: 'usuario',     label: 'Acesso ao Sistema',  icon: 'lock' },
  ];

  isEditMode = false;
  funcionarioId?: number;
  loading = false;

  // ---- Select options ----
  sexoOptions = [
    { label: 'Masculino', value: 'M' },
    { label: 'Feminino',  value: 'F' },
    { label: 'Outro',     value: 'O' },
  ];
  estadoCivilOptions = [
    { label: 'Solteiro',       value: 'SOLTEIRO' },
    { label: 'Casado',         value: 'CASADO' },
    { label: 'Divorciado',     value: 'DIVORCIADO' },
    { label: 'Viúvo',          value: 'VIUVO' },
    { label: 'União Estável',  value: 'UNIAO_ESTAVEL' },
  ];
  escolaridadeOptions = [
    { label: 'Fund. Incompleto',  value: 'FUNDAMENTAL_INCOMPLETO' },
    { label: 'Fund. Completo',    value: 'FUNDAMENTAL_COMPLETO' },
    { label: 'Médio Incompleto',  value: 'MEDIO_INCOMPLETO' },
    { label: 'Médio Completo',    value: 'MEDIO_COMPLETO' },
    { label: 'Superior Incompleto', value: 'SUPERIOR_INCOMPLETO' },
    { label: 'Superior Completo', value: 'SUPERIOR_COMPLETO' },
    { label: 'Pós-graduação',     value: 'POS_GRADUACAO' },
    { label: 'Mestrado',          value: 'MESTRADO' },
    { label: 'Doutorado',         value: 'DOUTORADO' },
  ];
  tipoContratoOptions = [
    { label: 'CLT',          value: 'CLT' },
    { label: 'PJ',           value: 'PJ' },
    { label: 'Estagiário',   value: 'ESTAGIARIO' },
    { label: 'Terceirizado', value: 'TERCEIRIZADO' },
    { label: 'Temporário',   value: 'TEMPORARIO' },
    { label: 'Freelancer',   value: 'FREELANCER' },
  ];
  statusOptions = [
    { label: 'Ativo',      value: 'ATIVO' },
    { label: 'Inativo',    value: 'INATIVO' },
    { label: 'Afastado',   value: 'AFASTADO' },
    { label: 'Demitido',   value: 'DEMITIDO' },
    { label: 'Aposentado', value: 'APOSENTADO' },
  ];
  tipoContaOptions = [
    { label: 'Corrente', value: 'CORRENTE' },
    { label: 'Poupança', value: 'POUPANCA' },
  ];
  cnhCategoriaOptions = ['A', 'B', 'AB', 'C', 'D', 'E', 'ACC'];
  departamentoOptions: { label: string; value: number }[] = [];
  cargoOptions:        { label: string; value: number }[] = [];

  // ---- Form model (matches FuncionarioRequest keys exactly) ----
  form: Partial<FuncionarioRequest> & { dataNascimento?: Date | string; dataAdmissao?: Date | string; dataDemissao?: Date | string; cnhValidade?: Date | string } = {
    nomeCompleto: '',
    cpf: '',
    rg: '',
    matricula: '',
    sexo: undefined,
    estadoCivil: undefined,
    nacionalidade: 'Brasileira',
    naturalidade: '',
    nomeMae: '',
    nomePai: '',
    escolaridade: undefined,
    profissao: '',
    cargoId: undefined,
    departamentoId: undefined,
    status: 'ATIVO',
    tipoContrato: undefined,
    salarioBase: undefined,
    comissaoPercentual: 0,
    valeTransporte: 0,
    valeAlimentacao: 0,
    planoSaude: false,
    planoOdontologico: false,
    motivoInativacao: '',
    enderecoCompleto: '',
    telefonePrincipal: '',
    telefoneEmergencia: '',
    contatoEmergenciaNome: '',
    contatoEmergenciaParentesco: '',
    emailPessoal: '',
    pisPasep: '',
    tituloEleitor: '',
    carteiraTrabalho: '',
    certificadoReservista: '',
    cnhNumero: '',
    cnhCategoria: '',
    bancoCodigo: '',
    bancoAgencia: '',
    bancoConta: '',
    bancoTipoConta: undefined,
    observacoes: '',
  };

  ngOnInit() {
    this.loadOptions();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode    = true;
      this.funcionarioId = Number(id);
      this.loadData();
    }
  }

  loadOptions() {
    this.departamentoService.list().subscribe({
      next: (data: any) => {
        const dep = Array.isArray(data) ? data : (data.content ?? []);
        this.departamentoOptions = dep.map((d: any) => ({ label: d.nome, value: d.id }));
      },
    });
    this.cargoService.list().subscribe({
      next: (data: any) => {
        const c = Array.isArray(data) ? data : (data.content ?? []);
        this.cargoOptions = c.map((c: any) => ({ label: c.nome, value: c.id }));
      },
    });
    this.permissaoService.listFuncoes().subscribe({
      next: (funcoes: any[]) => {
        this.funcaoOptions = funcoes.map((f: any) => ({ label: f.nome, value: f.id }));
      },
    });
  }

  loadData() {
    if (!this.funcionarioId) return;
    this.funcionarioService.getById(this.funcionarioId).subscribe({
      next: (data) => {
        // carrega usuarioId do funcionario
        if (data.usuarioId) {
          this.usuarioId = data.usuarioId;
          this.temAcesso = true;
          this.usuarioService.get(data.usuarioId).subscribe({
            next: (u: any) => {
              this.usuarioForm.email     = u.email;
              this.usuarioForm.ativo     = u.ativo;
              this.usuarioForm.bloqueado = u.bloqueado;
            },
          });
        }
        this.form.nomeCompleto              = data.nomeCompleto;
        this.form.cpf                       = data.cpf;
        this.form.rg                        = data.rg;
        this.form.matricula                 = data.matricula;
        this.form.dataNascimento            = data.dataNascimento as any;
        this.form.sexo                      = data.sexo as any;
        this.form.estadoCivil               = data.estadoCivil as any;
        this.form.nacionalidade             = data.nacionalidade;
        this.form.naturalidade              = data.naturalidade;
        this.form.nomeMae                   = data.nomeMae;
        this.form.nomePai                   = data.nomePai;
        this.form.escolaridade              = data.escolaridade as any;
        this.form.profissao                 = data.profissao;
        this.form.cargoId                   = data.cargoId;
        this.form.departamentoId            = data.departamentoId;
        this.form.dataAdmissao              = data.dataAdmissao as any;
        this.form.dataDemissao              = data.dataDemissao as any;
        this.form.tipoContrato              = data.tipoContrato as any;
        this.form.status                    = data.status as any;
        this.form.salarioBase               = data.salarioBase as any;
        this.form.comissaoPercentual        = data.comissaoPercentual as any;
        this.form.valeTransporte            = data.valeTransporte as any;
        this.form.valeAlimentacao           = data.valeAlimentacao as any;
        this.form.planoSaude                = data.planoSaude;
        this.form.planoOdontologico         = data.planoOdontologico;
        this.form.motivoInativacao          = data.motivoInativacao;
        this.form.enderecoCompleto          = data.enderecoCompleto;
        this.form.telefonePrincipal         = data.telefonePrincipal;
        this.form.telefoneEmergencia        = data.telefoneEmergencia;
        this.form.contatoEmergenciaNome     = data.contatoEmergenciaNome;
        this.form.contatoEmergenciaParentesco = data.contatoEmergenciaParentesco;
        this.form.emailPessoal              = data.emailPessoal;
        this.form.bancoCodigo               = data.bancoCodigo;
        this.form.bancoAgencia              = data.bancoAgencia;
        this.form.bancoConta                = data.bancoConta;
        this.form.bancoTipoConta            = data.bancoTipoConta as any;
        this.form.pisPasep                  = data.pisPasep;
        this.form.tituloEleitor             = data.tituloEleitor;
        this.form.carteiraTrabalho          = data.carteiraTrabalho;
        this.form.certificadoReservista     = data.certificadoReservista;
        this.form.cnhNumero                 = data.cnhNumero;
        this.form.cnhCategoria              = data.cnhCategoria;
        this.form.cnhValidade               = data.cnhValidade as any;
        this.form.observacoes               = data.observacoes;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar funcionário' }),
    });
  }

  private formatDate(d: any): string | undefined {
    if (!d) return undefined;
    if (d instanceof Date) return d.toISOString().split('T')[0];
    return String(d);
  }

  private get tenantId(): number {
    const v = this.storage.has('tenantId') ? this.storage.get('tenantId') : '7';
    return Number(v) || 7;
  }

  salvar() {
    if (!this.form.nomeCompleto || !this.form.cpf || !this.form.matricula) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Nome, CPF e Matrícula são obrigatórios' });
      return;
    }
    if (!this.form.dataAdmissao) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Data de admissão é obrigatória' });
      return;
    }

    this.loading = true;
    const payload: FuncionarioRequest = {
      empresaId:                  this.tenantId,
      usuarioId:                  this.usuarioId,
      matricula:                  this.form.matricula!,
      nomeCompleto:               this.form.nomeCompleto!,
      cpf:                        this.form.cpf!,
      rg:                         this.form.rg,
      dataNascimento:             this.formatDate(this.form.dataNascimento),
      sexo:                       this.form.sexo as any,
      estadoCivil:                this.form.estadoCivil as any,
      nacionalidade:              this.form.nacionalidade,
      naturalidade:               this.form.naturalidade,
      nomeMae:                    this.form.nomeMae,
      nomePai:                    this.form.nomePai,
      escolaridade:               this.form.escolaridade as any,
      profissao:                  this.form.profissao,
      cargoId:                    this.form.cargoId,
      departamentoId:             this.form.departamentoId,
      dataAdmissao:               this.formatDate(this.form.dataAdmissao)!,
      dataDemissao:               this.formatDate(this.form.dataDemissao),
      tipoContrato:               this.form.tipoContrato as any,
      salarioBase:                this.form.salarioBase,
      comissaoPercentual:         this.form.comissaoPercentual,
      valeTransporte:             this.form.valeTransporte,
      valeAlimentacao:            this.form.valeAlimentacao,
      planoSaude:                 this.form.planoSaude,
      planoOdontologico:          this.form.planoOdontologico,
      status:                     (this.form.status ?? 'ATIVO') as any,
      motivoInativacao:           this.form.motivoInativacao,
      enderecoCompleto:           this.form.enderecoCompleto,
      telefonePrincipal:          this.form.telefonePrincipal,
      telefoneEmergencia:         this.form.telefoneEmergencia,
      contatoEmergenciaNome:      this.form.contatoEmergenciaNome,
      contatoEmergenciaParentesco: this.form.contatoEmergenciaParentesco,
      emailPessoal:               this.form.emailPessoal,
      bancoCodigo:                this.form.bancoCodigo,
      bancoAgencia:               this.form.bancoAgencia,
      bancoConta:                 this.form.bancoConta,
      bancoTipoConta:             this.form.bancoTipoConta as any,
      pisPasep:                   this.form.pisPasep,
      tituloEleitor:              this.form.tituloEleitor,
      carteiraTrabalho:           this.form.carteiraTrabalho,
      certificadoReservista:      this.form.certificadoReservista,
      cnhNumero:                  this.form.cnhNumero,
      cnhCategoria:               this.form.cnhCategoria,
      cnhValidade:                this.formatDate(this.form.cnhValidade),
      observacoes:                this.form.observacoes,
    };

    const op = this.isEditMode && this.funcionarioId
      ? this.funcionarioService.update(this.funcionarioId, payload)
      : this.funcionarioService.create(payload);

    op.subscribe({
      next: (funcionario: any) => {
        // Se tem acesso, cria/atualiza o usuario vinculado
        if (this.temAcesso && this.usuarioForm.email) {
          const usuReq = {
            nomeCompleto:  this.form.nomeCompleto || '',
            email:         this.usuarioForm.email,
            senha:         this.usuarioForm.senha || undefined,
            ativo:         this.usuarioForm.ativo,
            bloqueado:     this.usuarioForm.bloqueado,
            funcoesIds:    this.usuarioForm.funcoesIds,
          };
          const usuOp = this.usuarioId
            ? this.usuarioService.update(this.usuarioId, usuReq)
            : this.usuarioService.create(usuReq);
          usuOp.subscribe({
            next: (u: any) => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário e acesso salvos!' });
              this.loading = false;
              if (!this.isEditMode && funcionario?.id) {
                this.isEditMode = true;
                this.funcionarioId = funcionario.id;
                this.router.navigate(['/configuracoes/colaboradores/cadastro', funcionario.id], { replaceUrl: true });
              }
              if (u?.id) this.usuarioId = u.id;
            },
            error: (err: any) => {
              this.loading = false;
              this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Funcionário salvo, mas erro ao salvar acesso: ' + (err?.error?.message || '') });
            },
          });
        } else {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário salvo com sucesso!' });
          this.loading = false;
          if (!this.isEditMode && funcionario?.id) {
            this.isEditMode = true;
            this.funcionarioId = funcionario.id;
            this.router.navigate(['/configuracoes/colaboradores/cadastro', funcionario.id], { replaceUrl: true });
          }
        }
      },
      error: (err: any) => {
        this.loading = false;
        const msg = err?.error?.message || err?.error || 'Erro ao salvar funcionário';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      },
    });
  }

  excluir() {
    if (!this.funcionarioId) return;

    this.confirmationService.confirm({
      title: 'Excluir Colaborador',
      message: `Tem certeza que deseja excluir o colaborador <span class="font-semibold text-slate-900">${this.form.nomeCompleto || ''}</span>? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.loading = true;
        this.funcionarioService.delete(this.funcionarioId!).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Colaborador excluído com sucesso!' });
            this.router.navigate(['/configuracoes/colaboradores']);
          },
          error: (err: any) => {
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir colaborador: ' + (err?.error?.message || '') });
          },
        });
      }
    });
  }

  cancelar() { this.location.back(); }
}
