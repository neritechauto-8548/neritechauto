import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ErrorCode } from '@shared/components/error-code/error-code';

@Component({
  selector: 'app-error-500',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <error-code
      code="500"
      eyebrow="Falha temporária"
      title="Não foi possível carregar esta página"
      message="O sistema encontrou uma falha inesperada. Tente novamente; se persistir, informe a operação realizada ao suporte."
      icon="alert-triangle"
      primaryLabel="Tentar novamente"
      [retry]="true"
      secondaryLabel="Voltar para Home"
      secondaryRoute="/home/gerencial"
    />
  `,
  imports: [ErrorCode],
})
export class Error500 {}
