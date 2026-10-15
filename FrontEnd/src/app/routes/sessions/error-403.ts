import { Component } from '@angular/core';
import { ErrorCode } from '@shared/components/error-code/error-code';

@Component({
  selector: 'app-error-403',
  template: `
    <error-code
      code="403"
      title="Acesso negado"
      message="Seu perfil não possui permissão para acessar esta página."
    />
  `,
  imports: [ErrorCode],
})
export class Error403 {}
