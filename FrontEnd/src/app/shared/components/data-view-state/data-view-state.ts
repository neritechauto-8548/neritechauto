import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { NeriTechIcon, NeriTechIconName } from '../neritech-icon/neritech-icon';

export type DataViewStateKind = 'loading' | 'empty' | 'filtered-empty' | 'error' | 'offline' | 'forbidden';

/** Reusable accessible data state, without mock success or implicit retry. */
@Component({
  selector: 'nt-data-view-state',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NeriTechIcon],
  template: `
    <section class="nt-data-state" [class.nt-data-state-critical]="critical"
      [attr.role]="critical ? 'alert' : 'status'"
      [attr.aria-live]="critical ? 'assertive' : 'polite'"
      [attr.aria-busy]="kind === 'loading' ? 'true' : null">
      <div class="nt-data-state-icon">
        <nt-icon [name]="iconName" [size]="24" />
      </div>
      <h3 class="nt-data-state-title">{{ title || defaultTitle }}</h3>
      <p class="nt-data-state-description">{{ description || defaultDescription }}</p>
      @if (retryAvailable && kind !== 'loading' && kind !== 'forbidden') {
        <button type="button" class="nt-data-state-retry" (click)="retry.emit()">
          {{ retryLabel }}
        </button>
      }
    </section>
  `,
  styleUrl: './data-view-state.scss',
})
export class DataViewState {
  @Input() kind: DataViewStateKind = 'empty';
  @Input() title = '';
  @Input() description = '';
  @Input() retryAvailable = false;
  @Input() retryLabel = 'Tentar novamente';
  @Output() retry = new EventEmitter<void>();

  get critical(): boolean {
    return this.kind === 'error' || this.kind === 'offline' || this.kind === 'forbidden';
  }

  get iconName(): NeriTechIconName {
    switch (this.kind) {
      case 'loading': return 'loader';
      case 'empty': return 'file-search';
      case 'filtered-empty': return 'search';
      case 'offline': return 'wifi-off';
      case 'forbidden': return 'lock';
      default: return 'alert-circle';
    }
  }

  get defaultTitle(): string {
    switch (this.kind) {
      case 'loading': return 'Carregando informações';
      case 'empty': return 'Nenhum registro encontrado';
      case 'filtered-empty': return 'Nenhum resultado para os filtros';
      case 'offline': return 'Sem conexão';
      case 'forbidden': return 'Acesso não autorizado';
      default: return 'Não foi possível carregar os dados';
    }
  }

  get defaultDescription(): string {
    switch (this.kind) {
      case 'loading': return 'Aguarde enquanto buscamos as informações.';
      case 'empty': return 'Assim que existirem registros, eles aparecerão aqui.';
      case 'filtered-empty': return 'Experimente ajustar a busca ou limpar os filtros.';
      case 'offline': return 'Verifique sua conexão e tente novamente.';
      case 'forbidden': return 'Seu perfil não tem permissão para visualizar estes dados.';
      default: return 'Ocorreu um problema. Você pode tentar novamente.';
    }
  }
}
