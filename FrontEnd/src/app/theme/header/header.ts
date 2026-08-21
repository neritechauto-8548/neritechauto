import { Component, EventEmitter, Input, Output, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';

import { AuthService, User } from '@core/authentication';
import { NotificationButton } from '../widgets/notification-button';
import { UserButton } from '../widgets/user-button';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.scss',
  host: {
    class: 'matero-header',
  },
  encapsulation: ViewEncapsulation.None,
  imports: [RouterLink, ToolbarModule, ButtonModule, MatTooltipModule, NotificationButton, UserButton],
})
export class Header {
  @Input() showToggle = true;
  @Input() showBranding = false;

  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() toggleSidenavNotice = new EventEmitter<void>();

  private readonly auth = inject(AuthService);

  companyLabel = 'Empresa atual';

  constructor() {
    this.auth
      .user()
      .pipe(takeUntilDestroyed())
      .subscribe(user => {
        this.companyLabel = this.resolveCompanyLabel(user);
      });
  }

  private resolveCompanyLabel(user: User): string {
    const candidate =
      user?.empresaNome ||
      user?.nomeEmpresa ||
      user?.empresa?.nomeFantasia ||
      user?.empresa?.razaoSocial ||
      user?.empresa?.nome;

    return candidate ? String(candidate) : 'Empresa atual';
  }
}
