import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { ConfirmationService } from '../../services/confirmation.service';

describe('ConfirmationDialogComponent', () => {
  let service: ConfirmationService;
  let component: ConfirmationDialogComponent;

  beforeEach(() => {
    service = new ConfirmationService();
    component = new ConfirmationDialogComponent(service);
    component.ngOnInit();
  });

  afterEach(() => component.ngOnDestroy());

  it('must not report success before explicit confirmation', () => {
    let result: boolean | undefined;
    service.confirm({ title: 'Excluir?', message: 'Essa ação é irreversível.', type: 'danger' })
      .subscribe(value => result = value);
    expect(component.visible).toBeTrue();
    expect(result).toBeUndefined();
    component.onCancel();
    expect(result).toBeFalse();
    expect(component.visible).toBeFalse();
  });

  it('cancels an earlier confirmation rather than resolving it with a later choice', () => {
    let first: boolean | undefined;
    let second: boolean | undefined;
    service.confirm({ title: 'Primeiro pedido', message: 'Excluir cliente?' })
      .subscribe(value => first = value);
    service.confirm({ title: 'Segundo pedido', message: 'Excluir veículo?' })
      .subscribe(value => second = value);

    expect(first).toBeFalse();
    expect(second).toBeUndefined();
    expect(component.config?.title).toBe('Segundo pedido');
    component.onConfirm();
    expect(first).toBeFalse();
    expect(second).toBeTrue();
  });

  it('replays the result when a consumer subscribes immediately after the decision', () => {
    const result$ = service.confirm({ title: 'Aviso', message: 'Prosseguir?' });
    component.onCancel();
    let value: boolean | undefined;
    result$.subscribe(result => value = result);
    expect(value).toBeFalse();
  });

  it('emits success only when the user confirms', () => {
    let result: boolean | undefined;
    service.confirm({ title: 'Confirmar', message: 'Deseja prosseguir?' })
      .subscribe(value => result = value);
    expect(result).toBeUndefined();
    component.onConfirm();
    expect(result).toBeTrue();
  });
});
