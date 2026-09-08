import { Component, ViewEncapsulation } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { NeriTechIcon } from '@shared';

@Component({
  selector: 'app-auth-layout',
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [RouterOutlet, NeriTechIcon],
})
export class AuthLayout {}
