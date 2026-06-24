import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[ntCnpjMask]',
  standalone: true,
})
export class CnpjMaskDirective {
  constructor(private el: ElementRef<HTMLInputElement>) {}

  @HostListener('input', ['$event'])
  onInput(event: any): void {
    const input = this.el.nativeElement;
    // Remove everything that is not alphanumeric
    let raw = input.value.replace(/[^A-Za-z0-9]/g, '').toUpperCase();

    // Limit to 14 characters
    if (raw.length > 14) {
      raw = raw.substring(0, 14);
    }

    // Apply mask: AA.AAA.AAA/AAAA-DD
    let formatted = raw;
    if (raw.length > 2) {
      formatted = raw.substring(0, 2) + '.' + raw.substring(2);
    }
    if (raw.length > 5) {
      formatted = formatted.substring(0, 6) + '.' + raw.substring(5);
    }
    if (raw.length > 8) {
      formatted = formatted.substring(0, 10) + '/' + raw.substring(8);
    }
    if (raw.length > 12) {
      formatted = formatted.substring(0, 16) + '-' + raw.substring(12);
    }

    input.value = formatted;

    // Dispatch input event for Angular forms to catch changes
    input.dispatchEvent(new Event('input', { bubbles: true }));
  }

  @HostListener('blur')
  onBlur(): void {
    this.el.nativeElement.value = this.el.nativeElement.value.toUpperCase();
  }
}
