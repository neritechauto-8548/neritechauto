import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { ErrorCode } from './error-code';

describe('ErrorCode', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ErrorCode],
    providers: [provideRouter([])],
  }));

  it('uses a semantic heading and real home navigation on 403/404', () => {
    for (const code of ['403', '404']) {
      const fixture = TestBed.createComponent(ErrorCode);
      fixture.componentInstance.code = code;
      fixture.componentInstance.title = code === '403' ? 'Acesso negado' : 'Página não encontrada';
      fixture.componentInstance.message = 'Mensagem em português.';
      fixture.detectChanges();
      const element: HTMLElement = fixture.nativeElement;
      expect(element.querySelector('h1')?.textContent).toContain(fixture.componentInstance.title);
      expect(element.querySelector('.matero-error-code')?.getAttribute('aria-hidden')).toBe('true');
      expect(element.querySelector('a')?.getAttribute('href')).toBe('/home');
      expect(element.textContent).toContain('Voltar ao início');
      fixture.destroy();
    }
  });
});
