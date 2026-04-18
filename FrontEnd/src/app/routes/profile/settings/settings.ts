import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';

import { ControlsOf, IProfile } from '@shared';

@Component({
  selector: 'app-profile-settings',
  templateUrl: './settings.html',
  styleUrl: './settings.scss',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    SelectModule,
  ],
})
export class ProfileSettings {
  private readonly fb = inject(FormBuilder);

  reactiveForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    gender: ['', [Validators.required]],
    city: ['', [Validators.required]],
    address: ['', [Validators.required]],
    company: ['', [Validators.required]],
    mobile: ['', [Validators.required]],
    tele: ['', [Validators.required]],
    website: ['', [Validators.required]],
    date: ['', [Validators.required]],
  });

  getErrorMessage(form: FormGroup<ControlsOf<IProfile>>) {
    return form.get('email')?.hasError('required')
      ? 'You must enter a value'
      : form.get('email')?.hasError('email')
        ? 'Not a valid email'
        : '';
  }
}
