import { TestBed } from '@angular/core/testing';
import { DataViewState, DataViewStateKind } from './data-view-state';

describe('DataViewState', () => {
  beforeEach(() => TestBed.configureTestingModule({ imports: [DataViewState] }));

  function render(kind: DataViewStateKind, retryAvailable = false) {
    const fixture = TestBed.createComponent(DataViewState);
    fixture.componentInstance.kind = kind;
    fixture.componentInstance.retryAvailable = retryAvailable;
    fixture.detectChanges();
    const section = fixture.nativeElement.querySelector('section') as HTMLElement;
    return { fixture, section };
  }

  it('announces loading without suggesting a retry', () => {
    const { section } = render('loading', true);
    expect(section.getAttribute('role')).toBe('status');
    expect(section.getAttribute('aria-live')).toBe('polite');
    expect(section.getAttribute('aria-busy')).toBe('true');
    expect(section.querySelector('button')).toBeNull();
  });

  it('distinguishes empty content from an empty filtered result', () => {
    const empty = render('empty').section.textContent || '';
    const filtered = render('filtered-empty').section.textContent || '';
    expect(empty).toContain('Nenhum registro encontrado');
    expect(filtered).toContain('Nenhum resultado para os filtros');
  });

  it('announces connection and server errors as alerts', () => {
    for (const kind of ['offline', 'error'] as DataViewStateKind[]) {
      const { section } = render(kind);
      expect(section.getAttribute('role')).toBe('alert');
      expect(section.getAttribute('aria-live')).toBe('assertive');
      expect(section.getAttribute('aria-busy')).toBeNull();
    }
  });

  it('never provides retry for access denied', () => {
    const { section } = render('forbidden', true);
    expect(section.textContent).toContain('Acesso não autorizado');
    expect(section.querySelector('button')).toBeNull();
  });

  it('emits retry only on an explicit user click', () => {
    const { fixture, section } = render('error', true);
    const emit = spyOn(fixture.componentInstance.retry, 'emit');
    expect(emit).not.toHaveBeenCalled();
    (section.querySelector('button') as HTMLButtonElement).click();
    expect(emit).toHaveBeenCalledTimes(1);
  });
});
