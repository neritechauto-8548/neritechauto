import { Component } from '@angular/core';
import { ErrorCode } from '@shared/components/error-code/error-code';

@Component({
  selector: 'app-error-404',
  template: `
    <error-code
      code="404"
      title="Página não encontrada"
      message="Verifique o endereço ou volte ao início."
    />
  `,
  imports: [ErrorCode],
})
export class Error404 {}
