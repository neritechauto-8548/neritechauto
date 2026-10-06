import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-aniversarios',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './aniversarios.html',
  styleUrl: './aniversarios.scss',
})
export class Aniversarios {}
