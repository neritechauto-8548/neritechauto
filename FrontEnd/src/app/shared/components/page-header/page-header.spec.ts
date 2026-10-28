import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { PageHeader } from './page-header';

describe('PageHeader', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [PageHeader],
    providers: [provideRouter([]), provideTranslateService()],
  }));

  it('renders provided title, subtitle and accessible breadcrumb', () => {
    const fixture = TestBed.createComponent(PageHeader);
    fixture.componentInstance.title = 'Clientes';
    fixture.componentInstance.subtitle = 'Gestão de relacionamento';
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('h1')?.textContent).toContain('Clientes');
    expect(el.querySelector('.matero-page-subtitle')?.textContent).toContain('Gestão de relacionamento');
    expect(el.querySelector('nav[aria-label="breadcrumb"]')).not.toBeNull();
    expect(el.querySelector('a[href="/home"]')).not.toBeNull();
  });

  it('supports pages without breadcrumb without losing heading semantics', () => {
    const fixture = TestBed.createComponent(PageHeader);
    fixture.componentInstance.title = 'Configurações';
    fixture.componentInstance.hideBreadcrumb = true;
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('h1')?.textContent).toContain('Configurações');
    expect(el.querySelector('nav')).toBeNull();
  });
});
