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

  it('emits success only when the user confirms', () => {
    let result: boolean | undefined;
    service.confirm({ title: 'Confirmar', message: 'Deseja prosseguir?' })
      .subscribe(value => result = value);
    expect(result).toBeUndefined();
    component.onConfirm();
    expect(result).toBeTrue();
  });
});
