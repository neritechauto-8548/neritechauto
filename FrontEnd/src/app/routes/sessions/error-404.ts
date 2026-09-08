import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ErrorCode } from '@shared/components/error-code/error-code';

@Component({
  selector: 'app-error-404',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <error-code
      code="404"
      eyebrow="Página não encontrada"
      title="Este endereço não está disponível"
      message="O endereço pode ter mudado ou não fazer parte do seu ambiente. Use a navegação do sistema para continuar."
      icon="search"
      primaryLabel="Voltar para Home"
    />
  `,
  imports: [ErrorCode],
})
export class Error404 {}
