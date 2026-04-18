import { Component } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-notification',
  template: `
    <button mat-icon-button [matMenuTriggerFor]="menu" class="!text-slate-600 dark:!text-slate-300 hover:!text-slate-900 dark:hover:!text-white hover:!bg-slate-100 dark:hover:!bg-slate-800 !transition-colors !duration-200">
      <mat-icon matBadge="5" matBadgeColor="warn" aria-hidden="false">notifications</mat-icon>
    </button>

    <mat-menu #menu="matMenu">
      <mat-nav-list>
        @for (message of messages; track message) {
          <mat-list-item>
            <mat-icon class="m-x-16" matListItemIcon>info</mat-icon>
            <a matListItemTitle href="#">{{ message }}</a>
          </mat-list-item>
        }
      </mat-nav-list>
    </mat-menu>
  `,
  styles: `
    :host ::ng-deep .mat-badge-content {
      --mat-badge-background-color: #ef0000;
      --mat-badge-text-color: #fff;
    }
  `,
  imports: [MatBadgeModule, MatButtonModule, MatIconModule, MatListModule, MatMenuModule],
})
export class NotificationButton {
  messages = ['Server Error Reports 1', 'Server Error Reports 2', 'Server Error Reports 3'];
}
