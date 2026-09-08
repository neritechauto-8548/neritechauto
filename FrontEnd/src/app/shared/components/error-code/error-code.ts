import { DOCUMENT } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { NeriTechIcon, NeriTechIconName } from '../neritech-icon/neritech-icon';

@Component({
  selector: 'error-code',
  standalone: true,
  templateUrl: './error-code.html',
  styleUrl: './error-code.scss',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, NeriTechIcon],
})
export class ErrorCode {
  private readonly document = inject(DOCUMENT);

  @Input() code = '';
  @Input() eyebrow = 'Não foi possível continuar';
  @Input() title = '';
  @Input() message = '';
  @Input() icon: NeriTechIconName = 'alert-triangle';
  @Input() primaryLabel = 'Voltar para Home';
  @Input() primaryRoute = '/home/gerencial';
  @Input() secondaryLabel = '';
  @Input() secondaryRoute = '';
  @Input() retry = false;

  reloadPage() {
    this.document.defaultView?.location.reload();
  }
}
