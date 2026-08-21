import {
  Component,
  Input,
  OnInit,
  ViewEncapsulation,
  booleanAttribute,
  inject,
} from '@angular/core';
import { Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { MenuService } from '@core';
import { Breadcrumb } from '../breadcrumb/breadcrumb';

@Component({
  selector: 'page-header',
  templateUrl: './page-header.html',
  styleUrl: './page-header.scss',
  host: {
    class: 'matero-page-header',
    '[class.matero-page-header--canonical]': 'canonical',
  },
  encapsulation: ViewEncapsulation.None,
  imports: [Breadcrumb, TranslateModule],
})
export class PageHeader implements OnInit {
  private readonly router = inject(Router);
  private readonly menu = inject(MenuService);

  @Input() title = '';
  @Input() subtitle = '';
  @Input() description = '';
  @Input() eyebrow = '';
  @Input() nav: string[] = [];
  @Input({ transform: booleanAttribute }) hideBreadcrumb = false;
  @Input({ transform: booleanAttribute }) canonical = false;

  ngOnInit() {
    if (this.title) {
      return;
    }

    const routes = this.router.url.slice(1).split('/');
    const menuLevel = this.menu.getLevel(routes);
    this.title = menuLevel[menuLevel.length - 1] || '';
  }

  get supportingText() {
    return this.description || this.subtitle;
  }
}
