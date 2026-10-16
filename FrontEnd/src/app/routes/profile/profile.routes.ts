import { Routes } from '@angular/router';

import { ProfileLayout } from './layout/layout';
import { ProfileOverview } from './overview/overview';

export const routes: Routes = [
  {
    path: '',
    component: ProfileLayout,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      { path: 'overview', component: ProfileOverview },
      { path: 'settings', redirectTo: 'overview', pathMatch: 'full' },
    ],
  },
];
