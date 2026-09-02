import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { ComentarioOrdemServico, EstadoDiarioOS } from './os-journal.models';
import { DiarioOrdemServicoService } from './os-journal.service';

@Component({
  selector: 'os-journal-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-journal-panel.html',
  styleUrl: './os-journal-panel.scss',
})
export class PainelDiarioOrdemServico implements OnChanges {
  private readonly servico = inject(DiarioOrdemServicoService);
  private readonly autenticacao = inject(AuthService);
  private readonly detectorMudancas = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  readonly limiteCaracteres = 2000;
  estado: EstadoDiarioOS = 'ocioso';
  comentarios: ComentarioOrdemServico[] = [];
  rascunho = '';
  mensagem = '';
  salvando = false;

  ngOnChanges(alteracoes: SimpleChanges): void {
    if (alteracoes['osId'] && Number.isInteger(this.osId) && this.osId > 0) {
      this.carregar();
    }
  }

  get podeVisualizar(): boolean {
    return this.temPermissao('OS_COMENTARIOS') || this.temPermissao('OS_COMENTARIOS_OUTROS');
  }

  get podeComentar(): boolean {
    return this.temPermissao('OS_COMENTARIOS');
  }

  get caracteresRestantes(): number {
    return this.limiteCaracteres - this.rascunho.length;
  }

  carregar(): void {
    if (!this.podeVisualizar) {
      this.estado = 'proibido';
      this.mensagem = 'Seu perfil não possui permissão para visualizar o diário desta Ordem de Serviço.';
      this.comentarios = [];
      return;
    }

    this.estado = 'carregando';
    this.mensagem = '';
    this.servico.listar(this.osId).subscribe({
      next: comentarios => {
        this.comentarios = comentarios ?? [];
        this.estado = 'pronto';
        this.detectorMudancas.markForCheck();
      },
      error: erro => {
        this.estado = erro?.status === 403 ? 'proibido' : 'erro';
        this.mensagem = erro?.status === 403
          ? 'Seu perfil não possui permissão para visualizar o diário desta Ordem de Serviço.'
          : 'Não foi possível carregar o diário operacional desta OS.';
        this.detectorMudancas.markForCheck();
      },
    });
  }

  registrarComentario(): void {
    const conteudo = this.rascunho.trim();
    if (!this.podeComentar || !conteudo || this.salvando || conteudo.length > this.limiteCaracteres) return;

    this.salvando = true;
    this.mensagem = '';
    this.servico.criar(this.osId, { conteudo }).subscribe({
      next: comentarioCriado => {
        this.comentarios = [comentarioCriado, ...this.comentarios];
        this.rascunho = '';
        this.salvando = false;
        this.estado = 'pronto';
        this.detectorMudancas.markForCheck();
      },
      error: erro => {
        this.salvando = false;
        this.mensagem = erro?.error?.message || erro?.message || 'Não foi possível registrar o comentário.';
        this.detectorMudancas.markForCheck();
      },
    });
  }

  identificarPorId(_: number, comentario: ComentarioOrdemServico): number {
    return comentario.id;
  }

  obterIniciais(nome?: string | null): string {
    const nomeNormalizado = nome?.trim();
    if (!nomeNormalizado) return 'U';
    return nomeNormalizado.split(/\s+/).slice(0, 2).map(parte => parte.charAt(0)).join('').toUpperCase();
  }

  private temPermissao(permissao: string): boolean {
    const usuario = this.autenticacao.snapshot();
    const permissoes = (usuario.permissions ?? []).map(valor => String(valor));
    const perfis = (usuario.roles ?? []).map(valor => String(valor));
    return permissoes.includes(permissao) || perfis.includes('ADMIN') || perfis.includes('ROLE_ADMIN');
  }
}
