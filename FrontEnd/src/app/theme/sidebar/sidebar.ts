import { Component, EventEmitter, Input, Output, ViewEncapsulation, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';

import { Sidemenu } from '../sidemenu/sidemenu';
import { Branding } from '../widgets/branding';
import { SettingsService } from '@core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    MatSlideToggleModule,
    MatIconModule,
    MatButtonModule,
    ToolbarModule,
    ButtonModule,
    MatTooltipModule,
    Branding,
    Sidemenu,
  ],
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
