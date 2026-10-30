import { TestBed } from '@angular/core/testing';
import { AuthService } from '@core';
import { NotificacaoService, NotificacaoSistemaResponse } from '@core/services/notificacao.service';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { NotificationButton } from './notification-button';

describe('NotificationButton global shell', () => {
  let user$: BehaviorSubject<{ id?: number }>;
  let service: jasmine.SpyObj<NotificacaoService>;
  let widget: NotificationButton;

  const notification = (): NotificacaoSistemaResponse => ({
    id: 8, empresaId: 1, usuarioDestinatarioId: 2,
    titulo: 'Ordem de serviço atualizada', mensagem: 'Acompanhe a oficina',
    lida: false, dataCadastro: '2026-10-11T10:00:00',
    tipoNotificacao: 'INFO', categoria: 'OFICINA', prioridade: 'MEDIA',
    exigeConfirmacao: false, confirmada: false,
    enviarEmail: false, enviarSms: false, enviarPush: false,
  });

  beforeEach(() => {
    user$ = new BehaviorSubject<{ id?: number }>({});
    service = jasmine.createSpyObj<NotificacaoService>('NotificacaoService', ['findMine', 'marcarComoLida']);
    service.findMine.and.returnValue(of({
      content: [notification()], totalElements: 1, totalPages: 1, size: 10, number: 0,
    }));
    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: { user: () => user$.asObservable() } },
        { provide: NotificacaoService, useValue: service },
      ],
    });
    widget = TestBed.runInInjectionContext(() => new NotificationButton());
  });

  afterEach(() => {
    widget.ngOnDestroy();
  });

  it('never queries the API in UX preview', () => {
    Object.defineProperty(widget, 'uxPreview', { value: true });
    widget.ngOnInit();
    user$.next({ id: 2 });
    expect(service.findMine).not.toHaveBeenCalled();
    expect(widget.notifications).toEqual([]);
  });

  it('fetches only the current user and avoids duplicate polling setup', () => {
    Object.defineProperty(widget, 'uxPreview', { value: false });
    widget.ngOnInit();
    user$.next({ id: 2 });
    expect(service.findMine).toHaveBeenCalledTimes(1);
    expect(widget.unreadCount).toBe(1);
    user$.next({ id: 2 });
    expect(service.findMine).toHaveBeenCalledTimes(1);
  });

  it('counts as read only after persisted server confirmation', () => {
    Object.defineProperty(widget, 'uxPreview', { value: false });
    service.marcarComoLida.and.returnValue(of({ ...notification(), lida: true }));
    widget.ngOnInit();
    user$.next({ id: 2 });
    widget.markAsRead(widget.notifications[0]);
    expect(service.marcarComoLida).toHaveBeenCalledWith(8);
    expect(widget.notifications[0].lida).toBeTrue();
    expect(widget.unreadCount).toBe(0);
  });

  it('shows a retryable error instead of pretending no notifications exist', () => {
    Object.defineProperty(widget, 'uxPreview', { value: false });
    service.findMine.and.returnValue(throwError(() => new Error('offline')));
    widget.ngOnInit();
    user$.next({ id: 2 });
    expect(widget.errorMessage).toContain('Não foi possível');
    expect(widget.notifications).toEqual([]);
  });
  it('filtra as abas de sistema e de agendamento com dados da API', () => {
    Object.defineProperty(widget, 'uxPreview', { value: false });
    service.findMine.and.returnValue(of({
      content: [notification(), { ...notification(), id: 9, categoria: 'AGENDAMENTO' }],
      totalElements: 2, totalPages: 1, size: 20, number: 0,
    }));
    widget.ngOnInit(); user$.next({ id: 2 });
    widget.category = 'system';
    expect(widget.filteredNotifications.map(item => item.id)).toEqual([8]);
    widget.category = 'schedule';
    expect(widget.filteredNotifications.map(item => item.id)).toEqual([9]);
  });

  it('abre configurações e permite alternar preferências e visualização sem fingir push', () => {
    widget.setPreference('sound', false);
    expect(widget.soundEnabled).toBeFalse();
    widget.setPreference('balloon', true);
    widget.showExample();
    expect(widget.exampleVisible).toBeTrue();
    widget.togglePanel();
    expect(widget.panelOpen).toBeTrue();
    widget.closePanel();
    expect(widget.panelOpen).toBeFalse();
  });

  it('anuncia apenas notificações novas após a primeira leitura, sem dados fictícios', () => {
    Object.defineProperty(widget, 'uxPreview', { value: false });
    widget.ngOnInit(); user$.next({ id: 2 });
    widget.setPreference('sound', false);
    expect(widget.exampleVisible).toBeFalse();
    service.findMine.and.returnValue(of({
      content: [notification(), { ...notification(), id: 11, titulo: 'Nova OS', mensagem: 'Atualização real', lida: false }],
      totalElements: 2, totalPages: 1, size: 20, number: 0,
    }));
    widget.loadNotifications();
    expect(widget.exampleVisible).toBeTrue();
    expect(widget.exampleTitle).toBe('Nova OS');
    expect(widget.exampleMessage).toBe('Atualização real');
  });

});
