import { BreakpointObserver, BreakpointState } from '@angular/cdk/layout';
import { TestBed } from '@angular/core/testing';
import { NavigationEnd, Router } from '@angular/router';
import { SettingsService } from '@core';
import { Subject } from 'rxjs';

import { AdminLayout } from './admin-layout';

const MOBILE = 'screen and (max-width: 767px)';
const TABLET = 'screen and (min-width: 768px) and (max-width: 1023px)';
const DESKTOP = 'screen and (min-width: 1024px)';

describe('AdminLayout responsive navigation', () => {
  let component: AdminLayout;
  let breakpoints$: Subject<BreakpointState>;
  let routerEvents$: Subject<NavigationEnd>;
  let settings: {
    options: any;
    notify: Subject<any>;
    setOptions: jasmine.Spy;
    setDirection: jasmine.Spy;
    setTheme: jasmine.Spy;
    getThemeColor: jasmine.Spy;
  };

  beforeEach(() => {
    breakpoints$ = new Subject<BreakpointState>();
    routerEvents$ = new Subject<NavigationEnd>();
    settings = {
      options: {
        navPos: 'side',
        headerPos: 'fixed',
        sidenavOpened: true,
        sidenavCollapsed: false,
        dir: 'ltr',
      },
      notify: new Subject<any>(),
      setOptions: jasmine.createSpy('setOptions'),
      setDirection: jasmine.createSpy('setDirection'),
      setTheme: jasmine.createSpy('setTheme'),
      getThemeColor: jasmine.createSpy('getThemeColor'),
    };

    TestBed.configureTestingModule({
      providers: [
        {
          provide: BreakpointObserver,
          useValue: { observe: () => breakpoints$.asObservable() },
        },
        {
          provide: Router,
          useValue: { events: routerEvents$.asObservable() },
        },
        { provide: SettingsService, useValue: settings },
      ],
    });

    component = TestBed.runInInjectionContext(() => new AdminLayout());
  });

  afterEach(() => component.ngOnDestroy());

  it('opens and closes the mobile drawer without overwriting the desktop preference', () => {
    breakpoints$.next({
      matches: true,
      breakpoints: { [MOBILE]: true, [TABLET]: false, [DESKTOP]: false },
    });

    component.onSidenavToggle();
    expect(component.mobileSidenavOpened).toBeTrue();
    expect(settings.options.sidenavOpened).toBeTrue();

    component.closeMobileSidenav();
    expect(component.mobileSidenavOpened).toBeFalse();
    expect(settings.setOptions).not.toHaveBeenCalled();
  });

  it('closes the mobile drawer after navigation', () => {
    breakpoints$.next({
      matches: true,
      breakpoints: { [MOBILE]: true, [TABLET]: false, [DESKTOP]: false },
    });
    component.onSidenavToggle();

    component.content = { scrollTo: jasmine.createSpy('scrollTo') } as any;
    routerEvents$.next(new NavigationEnd(1, '/clientes', '/clientes'));

    expect(component.mobileSidenavOpened).toBeFalse();
    expect(component.content.scrollTo).toHaveBeenCalledWith({ top: 0 });
  });

  it('uses compact navigation on tablet', () => {
    breakpoints$.next({
      matches: true,
      breakpoints: { [MOBILE]: false, [TABLET]: true, [DESKTOP]: false },
    });

    expect(component.isOver).toBeFalse();
    expect(settings.options.sidenavOpened).toBeTrue();
    expect(settings.options.sidenavCollapsed).toBeTrue();
  });
});
