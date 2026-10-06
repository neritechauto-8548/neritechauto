import { Component, EventEmitter, Input, Output, ViewEncapsulation, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';

import { Sidemenu } from '../sidemenu/sidemenu';
import { Branding } from '../widgets/branding';
import { SettingsService } from '@core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [CommonModule, MatTooltipModule, Branding, Sidemenu],
})
export class Sidebar {
  public readonly settings = inject(SettingsService);

  @Input() showToggle = true;
  @Input() showUser = true;
  @Input() showHeader = true;
  @Input() toggleChecked = false;

  @Output() toggleCollapsed = new EventEmitter<void>();
  @Output() closeSidenav = new EventEmitter<void>();
}
