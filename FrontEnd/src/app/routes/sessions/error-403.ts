import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ErrorCode } from '@shared/components/error-code/error-code';

@Component({
  selector: 'app-error-403',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <error-code
      code="403"
      eyebrow="Acesso protegido"
      title="Você não possui acesso a esta área"
      message="Seu perfil não tem a permissão necessária. Volte para a página inicial ou procure o administrador da sua oficina."
      icon="shield-check"
      primaryLabel="Voltar para Home"
    />
  `,
  imports: [ErrorCode],
})
export class Error403 {}
