import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { EMPTY } from 'rxjs';
import { MenuService } from '@core';
import { NavAccordion } from './nav-accordion';
import { NavAccordionItem } from './nav-accordion-item';

describe('Sidebar active branches', () => {
  let currentUrl = '/cliente/crm/comunicacao';
  let accordion: NavAccordion;

  beforeEach(() => {
    currentUrl = '/cliente/crm/comunicacao';
    TestBed.configureTestingModule({
      providers: [
        MenuService,
        { provide: Router, useValue: { events: EMPTY, get url() { return currentUrl; } } },
      ],
    });
    accordion = TestBed.runInInjectionContext(() => new NavAccordion());
  });

  it('expands the parent of a deep route even when its parent has no segment', () => {
    const clientes = { type: 'sub', activeWhen: ['/cliente/listar', '/cliente/crm/comunicacao'], expanded: false } as NavAccordionItem;
    const oficina = { type: 'sub', activeWhen: ['/os/listar'], expanded: false } as NavAccordionItem;
    accordion.addItem(clientes);
    accordion.addItem(oficina);
    accordion.checkOpenedItems();
    expect(clientes.expanded).toBeTrue();
    expect(oficina.expanded).toBeFalse();
  });

  it('closes an inactive branch after navigation', () => {
    const clientes = { type: 'sub', activeWhen: ['/cliente/listar', '/cliente/crm/comunicacao'], expanded: true } as NavAccordionItem;
    accordion.addItem(clientes);
    currentUrl = '/home?tab=gerencial';
    accordion.checkOpenedItems();
    expect(clientes.expanded).toBeFalse();
  });
});
